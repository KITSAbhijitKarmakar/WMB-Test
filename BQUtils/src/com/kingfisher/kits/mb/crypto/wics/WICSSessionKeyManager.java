package com.kingfisher.kits.mb.crypto.wics;

//Java JCE classes
import java.security.PrivateKey;
//import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

//Crossworlds Classes
//import CxCommon.CwDBConnection.*;
//import CxCommon.CwDBConnection.CwDBConstants;
//import Server.RelationshipServices.*;
//import Collaboration.BusObj;

//Apache Codec Libraries
import org.apache.commons.codec.binary.*;

//KITS Interfaces
//import com.kingfisher.kits.mb.crypto.interfaces.KITSEncryptionConstants;
import com.kingfisher.kits.mb.crypto.interfaces.KITSKeyStore;

//IB Config Data
import com.bq.mb.utils.ConfigData;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
public class WICSSessionKeyManager extends EPOSSessionKeyManager{
	
	//Removed the static declaration of ConfigData at class level - TCS (30.07.2013)
	//public static ConfigData cd = null;
	
	public static synchronized SecretKey generateSessionKey(String storeCode, String businessDayDate, Connection ods, Connection repos) throws Exception {
		
		Base64 base64Codec = new Base64();
		ConfigData cd =null;
		SecretKey key=null;
		
		//select statemnt added to prevent duplicate session key in store_sessionkey table (TCS 10.09.2013)
		Statement st = ods.createStatement();
		String selectsql = "select SESSION_KEY from STORES_SESSIONKEYS where business_day_date="+businessDayDate+" and store_code='"+storeCode+"'";
		ResultSet rst = st.executeQuery(selectsql);
				
		byte[] encodedKey = new byte[0];
	try{
		while(rst.next()){
			String encKey = rst.getString("SESSION_KEY");
		
			//De-Base64 The Session Key
			encodedKey = base64Codec.decode(encKey.getBytes());
		}
		
	}
	catch (Exception e) {
		Exception excp =new Exception("Exception in method generate session keys while retrieving data from stores_sessionkeys");
		throw excp;
	}
		
		if (encodedKey.length==0){
		
		//CwDBConnection ods = (CwDBConnection)sessionKeyStore;
		//Connection ods = (Connection)sessionKeyStore;
		cd = new ConfigData(repos);
		//Instantiate a KITS Key Store Class
		KITSKeyStore ksm = (KITSKeyStore)Class.forName(cd.getConfigData(KEYSTORECLASSNAME)).newInstance();
		//Initialise the connection to the HSM - parameters are the Partition, Username (blank), password
		ksm.initialise(cd.getConfigData(HSM_PARTITION_NAME), "", cd.getConfigData(HSM_PARTITION_PASSWORD));
		
		KeyGenerator keyGen = KeyGenerator.getInstance("AES", DEFAULT_PROVIDER);
		keyGen.init(256);
		key = keyGen.generateKey();

		//Create an RSA Cipher
		//Cipher cipher = Cipher.getInstance("RSA", "BC");
		Cipher cipher = Cipher.getInstance(RSA_ENCRYPTION_ALG, DEFAULT_PROVIDER);
		
		//RSA Encrypt / 'wrap' the Session Key
		//cipher.init(Cipher.WRAP_MODE, ksm.getPublicKey("KITSRSA"));
		cipher.init(Cipher.WRAP_MODE, ksm.getPublicKey(cd.getConfigData(CERTIFICATE_ALIAS)));
		byte[] wrappedKey = cipher.wrap(key);
		
		//Base64 Encode the Encrypted Key
		wrappedKey = base64Codec.encode(wrappedKey);
			

		//Write New Session Key to DB.
		String sql = "insert into STORES_SESSIONKEYS (SESSION_KEY, STORE_CODE, BUSINESS_DAY_DATE, KEY_REFERENCE) values ('"+new String(wrappedKey)+"','"+storeCode+"',"+businessDayDate+",'"+storeCode+"-"+businessDayDate+"')";
		Statement stmt = ods.createStatement();
		stmt.execute(sql);
		stmt.execute("commit");
		
		
		}
		
		//if session key present in store_session key table then return the existing key to loadsessionkey method (TCS 10.09.2013)
		else{
			try{
			
			cd = new ConfigData(repos);
			System.out.println("Session Key Exists in the DB. UnWrapping");
			//Decrypt RSA Encoded Session Key
			KITSKeyStore keyStore = (KITSKeyStore)Class.forName(cd.getConfigData(KEYSTORECLASSNAME)).newInstance();
			//Initialise the connection to the HSM - parameters are the Partition, Username (blank), password
			keyStore.initialise(cd.getConfigData(HSM_PARTITION_NAME), "", cd.getConfigData(HSM_PARTITION_PASSWORD));
			
			//Ross Stanton, 23/9/2008 - changed for HSM
			//PrivateKey privKey = keyStore.getPrivateKey("KITSRSA", "KITSPWD");
			PrivateKey privKey = keyStore.getPrivateKey(cd.getConfigData(CERTIFICATE_ALIAS), null);
		
			//Cipher cipher = Cipher.getInstance("RSA", "BC");
			Cipher cipher = Cipher.getInstance(RSA_ENCRYPTION_ALG, DEFAULT_PROVIDER);
			cipher.init(Cipher.UNWRAP_MODE, privKey);
			key = (SecretKey)cipher.unwrap(encodedKey, "AES", Cipher.SECRET_KEY);
			
		}
		catch (Exception e) {
			Exception excp =new Exception("Exception in method generate session keys while creating key from retrieved database data");
			throw excp;
		}
		}
		//Free the DB Connection
		ods = null;
		repos = null;
		return key;
		
		
	}

		
		/**
	 * 
	 */
	public static SecretKey loadSessionKey(String storeCode, String businessDayDate, Connection ods, Connection repos) throws Exception {
		
		Base64 base64Codec = new Base64();
	
		
		//CwDBConnection ods = (CwDBConnection)sessionKeyStore;
		Statement stmt = ods.createStatement();
		
		//ConfigData declared at method level - TCS (30.07.2013)
		ConfigData cd = new ConfigData(repos);
		
		byte[] encodedKey = new byte[0];
		
		//Get the Base64 Encoded, RSA Secured, Session Key from the DB
		String sql = "select SESSION_KEY from STORES_SESSIONKEYS where business_day_date="+businessDayDate+" and store_code='"+storeCode+"'";
		ResultSet rst = stmt.executeQuery(sql);
		
		
		while(rst.next()){
			String encKey = rst.getString("SESSION_KEY");
			
			//De-Base64 The Session Key
			encodedKey = base64Codec.decode(encKey.getBytes());
		}
		
		SecretKey key = null;
		if(encodedKey.length==0){ //If encodedkey.length == 0 then the key isn't in the DB and we'll need to create a new one.
			System.out.println("Session Key Doesn't Exist in ODS for Store "+storeCode+" and BusinessDayDate "+businessDayDate+". Creating New Key.");
			key = generateSessionKey(storeCode, businessDayDate, ods, repos);
		}
		else{
			System.out.println("Session Key Exists in the DB. UnWrapping");
			//Decrypt RSA Encoded Session Key
			KITSKeyStore keyStore = (KITSKeyStore)Class.forName(cd.getConfigData(KEYSTORECLASSNAME)).newInstance();
			//Initialise the connection to the HSM - parameters are the Partition, Username (blank), password
			keyStore.initialise(cd.getConfigData(HSM_PARTITION_NAME), "", cd.getConfigData(HSM_PARTITION_PASSWORD));
			
			//Ross Stanton, 23/9/2008 - changed for HSM
			//PrivateKey privKey = keyStore.getPrivateKey("KITSRSA", "KITSPWD");
			PrivateKey privKey = keyStore.getPrivateKey(cd.getConfigData(CERTIFICATE_ALIAS), null);
		
			//Cipher cipher = Cipher.getInstance("RSA", "BC");
			Cipher cipher = Cipher.getInstance(RSA_ENCRYPTION_ALG, DEFAULT_PROVIDER);
			cipher.init(Cipher.UNWRAP_MODE, privKey);
			key = (SecretKey)cipher.unwrap(encodedKey, "AES", Cipher.SECRET_KEY);
			
		}		
		ods 	= null;
		repos 	= null;
		
		return key;
	}
}
