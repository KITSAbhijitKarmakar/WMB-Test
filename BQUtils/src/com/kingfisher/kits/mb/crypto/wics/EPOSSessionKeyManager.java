package com.kingfisher.kits.mb.crypto.wics;
import java.security.PrivateKey;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
//import CxCommon.CwDBConnection.CwDBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.chrysalisits.crypto.LunaAPI;
import com.bq.mb.utils.ConfigData;
import com.kingfisher.kits.mb.crypto.interfaces.*;

public class EPOSSessionKeyManager implements KITSEncryptionConstants {
	
	protected static java.util.Hashtable eposKeys = new java.util.Hashtable();
	protected static boolean trace = Boolean.valueOf(System.getProperty("PCI.TRACE")).booleanValue();
	
	//Added by Malay-TCS for loading SecurityProvider before Cipher instantiation happens
	public EPOSSessionKeyManager(){
			Security.addProvider(new com.chrysalisits.cryptox.LunaJCEProvider());
			Security.addProvider(new com.chrysalisits.crypto.LunaJCAProvider());
	}
	/**
	 * Creates Triple DES key for RJ stored in IB Key database wrapped using RSA encryption
	 * and encoded in base64
	 * @param keyRef
	 * @param businessDayDate
	 * @param ibods connection object.
	 * @param startDate Date the EPOS Key is valid from - yyyyMMdd format
	 * @param endDate Date the EPOS key is valid to - yyyyMMdd format.
	 * @return 
	 * @throws Exception
	 */
	public static byte[] createRJEncryptionKey(String keyRef, String businessDayDate, Connection ods,Connection repos, String startDate, String endDate) throws Exception{
		try {
			//Instantiate a KITS Key Store Class
			ConfigData cd = new ConfigData(repos);
			KITSKeyStore ksm = (KITSKeyStore)Class.forName(cd.getConfigData(KEYSTORECLASSNAME)).newInstance();
			ksm.initialise(cd.getConfigData(HSM_PARTITION_NAME), "", cd.getConfigData(HSM_PARTITION_PASSWORD));
			
			String storeCode = "EPOS";
			Base64 base64Codec = new Base64();
			
			//generate a triple DES key
			KeyGenerator keyGen = KeyGenerator.getInstance("DESede", DEFAULT_PROVIDER);
						
			keyGen.init(168);
			SecretKey key = keyGen.generateKey();

			//Create an RSA Cipher
			Cipher cipher = Cipher.getInstance(RSA_ENCRYPTION_ALG, DEFAULT_PROVIDER);
			
			//RSA Encrypt / 'wrap' the Session Key
			cipher.init(Cipher.WRAP_MODE, ksm.getPublicKey(cd.getConfigData(CERTIFICATE_ALIAS)));
			byte[] wrappedKey = cipher.wrap(key);
			
			//Base64 Encode the Encrypted Key
			wrappedKey = base64Codec.encode(wrappedKey);
			
			//Write New RJ Symmetric Key to DB.
			String sql = "insert into STORES_SESSIONKEYS (SESSION_KEY, STORE_CODE, BUSINESS_DAY_DATE, KEY_REFERENCE, START_DATE, END_DATE) values ('"+new String(wrappedKey)+"','"+storeCode+"',"+businessDayDate+",'"+keyRef+"',to_date('"+startDate+"','yyyy-MM-dd'),to_date('"+endDate+"','yyyy-MM-dd'))";
			Statement stmt = ods.createStatement();
			stmt.executeQuery(sql);
			stmt.executeQuery("commit");
			
			//Free the DB Connection
			ods = null;
			byte[] hexKey = new Hex().encode(key.getEncoded());
			stmt.close();
			return hexKey;
		}
		catch(Exception e){
			throw e;
			
		}
	}
	
	/**
	 * Returns the RJ DES Key encoded in base64
	 * @param keyRef
	 * @param ibods
	 * @param storeCode
	 * @return 
	 * @throws Exception
	 */
	protected static SecretKey getRJEncryptionKey(String keyRef, Connection ods, Connection repos) throws Exception{
		try {
			SecretKey key = null;
			ResultSet rst=null;
			Statement stmt = ods.createStatement();
			if(!eposKeys.containsKey(keyRef)){
				if(trace) trace("Loading EPOS Key ("+keyRef+") from DB");
				Base64 base64Codec = new Base64();

				byte[] encodedKey = new byte[0];
				
				//Get the Base64 Encoded, RSA Secured, Session Key from the DB
				String sql = "select SESSION_KEY from STORES_SESSIONKEYS where store_code='EPOS' and key_reference='"+keyRef+"'";
				if(trace) trace(sql);
				rst = stmt.executeQuery(sql);
				
				while(rst.next()){
					String encKey = rst.getString("SESSION_KEY");
				
					//De-Base64 The Session Key
					encodedKey = base64Codec.decode(encKey.getBytes());
				}
				
				if(encodedKey.length==0){ //If encodedkey.length == 0 then the key isn't in the DB and we'll need to create a new one.
					throw new Exception("EPOS Key Ref "+keyRef+" Doesn't Exist");
				}
				else{
					if(trace) trace("Got Key From DB. Unwrapping to form DESede Secret Key");
					//Decrypt RSA Encoded Session Key
					ConfigData cd = new ConfigData(repos);
					KITSKeyStore keyStore = (KITSKeyStore)Class.forName(cd.getConfigData(KEYSTORECLASSNAME)).newInstance();
					
					keyStore.initialise(cd.getConfigData(HSM_PARTITION_NAME), "", cd.getConfigData(HSM_PARTITION_PASSWORD));
					PrivateKey privKey = keyStore.getPrivateKey(cd.getConfigData(CERTIFICATE_ALIAS),null);

					if(trace) trace("Got Private Key. Is Null?"+(privKey==null));
					
					cd = null;
					
					Cipher cipher = Cipher.getInstance(RSA_ENCRYPTION_ALG, DEFAULT_PROVIDER);
					cipher.init(Cipher.UNWRAP_MODE, privKey);
					key = (SecretKey)cipher.unwrap(encodedKey, "DESede", Cipher.SECRET_KEY);

					if(trace) trace("Secret Key Unwrapped. Key Class: "+key.getClass().getName()+". Is Null? "+(key==null));
					//Update in memory Key Cache
					eposKeys.put(keyRef, key);
				}
				
				//Free the DB Connection
				ods = null;
			}
			else{
				if(trace) trace("Returning EPOS Key ("+keyRef+") From Memory");
				key = (SecretKey)eposKeys.get(keyRef);
			}
			
			if(trace) trace("Returning EPOS Key. In Memory Key Count: "+eposKeys.size());
			
			return key;
		}
		catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	
	protected static void trace(String message){
		try{
			String datetime = new com.bq.mb.utils.DateTime().get("yyyyMMdd HH:mm:ss.SSS");
			System.out.println(datetime+", "+message);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
