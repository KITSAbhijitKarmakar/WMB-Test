package com.kingfisher.kits.mb.crypto.wics;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.security.*;
import javax.crypto.*;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.util.Properties;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.Base64;

import com.kingfisher.kits.mb.crypto.httpssl.SSLConnection;
import com.kingfisher.kits.mb.crypto.interfaces.*;
import com.chrysalisits.crypto.LunaKey;
import com.chrysalisits.crypto.LunaTokenManager;
import com.bq.mb.utils.ConfigData;
import com.bq.mb.utils.DateTime;

public class StandaloneEPOSKeyGenerator implements KITSEncryptionConstants {
	private static LunaTokenManager hsm;
	private static Properties props;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			props = new java.util.Properties();
			props.load(new java.io.FileInputStream(System.getProperty("PROPS.FILE")));
			
			System.setProperty("javax.net.ssl.trustStore", props.getProperty("EPOS.SSLTRUSTSTORE"));
			
			if(args.length==0){
				System.out.println("No Arguments Specified - Generating a new Key To EPOS");
				genKey();
			}
			else if(args.length==1){
				System.out.println("Resending a Key to EPOS");
				resendKey(args[0]);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	private static void resendKey(String keyRef) throws Exception{
		byte[] hexKey = retrieveDBRecord(keyRef);
		byte[] key = new Base64().decode(hexKey);
		
		SecretKey secretKey = unwrapKey(key);
		key = secretKey.getEncoded();
		//String strKey = new String(new Hex().encode(key)).toUpperCase();
		
		//System.out.println("Key:" +strKey);
		DateTime dt = new DateTime();
		String startDate = dt.get("yyyy-MM-dd");
		String endDate = dt.addDays(dt.get("yyyy-MM-dd"), "yyyy-MM-dd", 365);
		String url = "https://"+props.getProperty("EPOS.HOSTNAME")+":9443/rjWebServices/KITSServiceServlet";
		System.out.println(url);
		SSLConnection sslConn = new SSLConnection();
		sslConn.sendKey(url, new String(new Hex().encode(secretKey.getEncoded())).toUpperCase(), keyRef, startDate, endDate);
	}
	
	private static void genKey() throws Exception{
		int validity = Integer.parseInt(props.getProperty("EPOS.KEY.VALIDITY"));
		SecretKey key = generateDESKey();
		DateTime dt = new DateTime();
		String alias = "RJ"+dt.get("ddMMMyyyy")+"@"+dt.get("HHmm");
		
		Base64 b64 = new Base64();
		Hex hex = new Hex();
	
		String startDate = dt.get("yyyy-MM-dd");
		String endDate = dt.addDays(dt.get("yyyy-MM-dd"), "yyyy-MM-dd", validity);
		String url = "https://"+props.getProperty("EPOS.HOSTNAME")+":9443/rjWebServices/KITSServiceServlet";

		SSLConnection sslConn = new SSLConnection();
		sslConn.sendKey(url, new String(hex.encode(key.getEncoded())).toUpperCase(), alias, startDate, endDate);
		insertDBRecord("EPOS", dt.get("yyyyMMdd"), alias, new String(b64.encode(wrapKey(key))), startDate, endDate);	
	}
	
	private static void insertDBRecord(String store, String bdd, String alias, String key, String start, String end) throws Exception{
		String sql = "insert into STORES_SESSIONKEYS (STORE_CODE, BUSINESS_DAY_DATE, SESSION_KEY, KEY_REFERENCE, START_DATE, END_DATE) values ('"+store+"',"+bdd+",'"+key+"','"+alias+"',to_date('"+start+"','yyyy-MM-dd'),to_date('"+end+"','yyyy-MM-dd'))";
		
		Class.forName(props.getProperty("IB.ORACLEDRIVER"));
		Connection conn = DriverManager.getConnection(props.getProperty("IB.ORACLEURL"),props.getProperty("IB.ORACLEUSER"), props.getProperty("IB.ORACLEPASSWORD"));
		Statement stmnt = conn.createStatement();
		stmnt.execute(sql);
		stmnt.execute("commit");
		
		System.out.println(sql);
	}
	
	private static byte[] retrieveDBRecord(String keyRef) throws Exception{
		String sql = "select SESSION_KEY from STORES_SESSIONKEYS where KEY_REFERENCE='"+keyRef+"'";
		
		Class.forName(props.getProperty("IB.ORACLEDRIVER"));
		Connection conn = DriverManager.getConnection(props.getProperty("IB.ORACLEURL"), props.getProperty("IB.ORACLEUSER"), props.getProperty("IB.ORACLEPASSWORD"));
		Statement stmnt = conn.createStatement();
		ResultSet rs = stmnt.executeQuery(sql);
		
		String key = null;
		while(rs.next())
			key = rs.getString("SESSION_KEY");
		
		if(key==null)
			throw new Exception("No Key Returned for Reference "+keyRef);
		else	
			System.out.println(key);
		
		return key.getBytes();
	}
	private static byte[] wrapKey(SecretKey key) throws Exception{
		PublicKey pk = (PublicKey)LunaKey.LocateKeyByAlias(props.getProperty("IB.HSM.PUBLICKEYALIAS"));
		
		if(pk==null) 
			throw new Exception("Public Key Is Null. Unable to Wrap Session Key. Ensure IB.HSM.PUBLICKEYALIAS is set");
		
		Cipher cipher = Cipher.getInstance(RSA_ENCRYPTION_ALG, DEFAULT_PROVIDER);
		cipher.init(Cipher.WRAP_MODE, pk);
		return cipher.wrap(key);
	}
	
	private static SecretKey unwrapKey(byte[] wrappedKey) throws Exception{
		hsm = LunaTokenManager.getInstance();
		hsm.Login(props.getProperty("IB.HSM.PARTITION"), props.getProperty("IB.HSM.PARTITION.PASSWORD"));
		hsm.SetSecretKeysExtractable(true);
		
		PrivateKey pk = (PrivateKey)LunaKey.LocateKeyByAlias(props.getProperty("IB.HSM.PRIVATEKEYALIAS"));
		if(pk==null)
			throw new Exception("Private Key Is Null. Unable to unwrap Session Key. Ensure IB.HSM.PRIVATEKEYALIAS is set.");
		
		Cipher cipher = Cipher.getInstance(RSA_ENCRYPTION_ALG, DEFAULT_PROVIDER);
		cipher.init(Cipher.UNWRAP_MODE, pk);
		return (SecretKey)cipher.unwrap(wrappedKey, "DESede", Cipher.SECRET_KEY);
	}
	
	private static SecretKey generateDESKey() throws Exception{
		hsm = LunaTokenManager.getInstance();
		hsm.Login(props.getProperty("IB.HSM.PARTITION"), props.getProperty("IB.HSM.PARTITION.PASSWORD"));

		hsm.SetSecretKeysExtractable(true);
		
		KeyGenerator keyGen = KeyGenerator.getInstance("DESede", DEFAULT_PROVIDER);
		keyGen.init(168);
		
		SecretKey key = keyGen.generateKey();
		
		return key;
	}
}
class KITSHostNameVerifier implements HostnameVerifier{

	public boolean verify(String hostname, SSLSession session) {
		System.out.println("Verifying Host: "+hostname);
		return true;
	}
}