package com.kingfisher.kits.mb.crypto.wics;
/**
* Class to be used from within WebSphere ICS to encrypt / decrypt data.<br>
* 
* Extends WICSEncryption which provides getCipher method and createIVData method.<br>
* This is a stand alone java class and will be executed to generate a encryption key
* and a IVData and use that key and IVData to generate encrypted SALT.
* All this information will be stored in MBCONFIG table.
*
* @author Asif Hossain(TCS 10-OCT-2013)
* 
*/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.PrivateKey;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.apache.commons.codec.binary.Base64;

import com.bq.mb.utils.ConfigData;
import com.kingfisher.kits.mb.crypto.interfaces.KITSEncryptionConstants;
import com.kingfisher.kits.mb.crypto.interfaces.KITSKeyStore;

public class SaltEncryptorUtility extends WICSEncryption implements
KITSEncryptionConstants {

	private static Properties props;

	public static void main(String[] args) throws IOException{

		SaltEncryptorUtility saltEncryptorUtility = new SaltEncryptorUtility();
		// method to generate session key for salt
		// method to encrypt salt
		
		String inputValue = null;
		
		if (args.length == 0) {
			System.out.println("No argument/values to be encrypted is specified in input.. ");
			
			System.out.println("Please provide SALT value to be encrypted");
			BufferedReader br =new BufferedReader(new InputStreamReader(System.in));
			inputValue = br.readLine();
		}	
		
		if(args.length>0 || inputValue!=null){
			
			Connection repos = saltEncryptorUtility.createDBConnection();
			
			if (repos==null){
				System.out.println("Connection to repos has not established successfully...");
				System.out.println("Terminating the execution...");
				System.exit(0);
			}
			
			String ptextSalt=null;
			if (args.length>0)
				ptextSalt = args[0];
			else if (inputValue!=null)
				ptextSalt = inputValue;
			
			System.out.println("Entering in method generateSaltEncryptionKey to generate key for encryption...");
			saltEncryptorUtility.encryptSalt(repos,
					saltEncryptorUtility.generateSaltEncryptionKey(repos), ptextSalt);
		}
	}
	/** This method generates a key for salt encryption and return the same key 
	 * in Sting format to be used in encryptSalt method
	 * 
	 * @param repos
	 * @return
	 */
	public String generateSaltEncryptionKey(Connection repos) {

		String encrptKey = null;
		SecretKey key = null;
		Base64 base64Codec = new Base64();

		ConfigData cd = null;

		try {
			cd = new ConfigData(repos);
			
			//Instantiate a KITS Key Store Class
			KITSKeyStore ksm = (KITSKeyStore)Class.forName(cd.getConfigData(KEYSTORECLASSNAME)).newInstance();
			
			//Initialise the connection to the HSM - parameters are the Partition, Username (blank), password
			ksm.initialise(cd.getConfigData(HSM_PARTITION_NAME), "", cd.getConfigData(HSM_PARTITION_PASSWORD));
			
			
			KeyGenerator keyGen = KeyGenerator.getInstance("AES",
					DEFAULT_PROVIDER);
			keyGen.init(256);
			key = keyGen.generateKey();

			// Create an RSA Cipher
			Cipher cipher = Cipher.getInstance(RSA_ENCRYPTION_ALG,
					DEFAULT_PROVIDER);

			// RSA Encrypt / 'wrap' the Session Key
			cipher.init(Cipher.WRAP_MODE,
					ksm.getPublicKey(cd.getConfigData(CERTIFICATE_ALIAS)));
			byte[] wrappedKey = cipher.wrap(key);

			// Base64 Encode the Encrypted Key
			wrappedKey = base64Codec.encode(wrappedKey);

			encrptKey = new String(wrappedKey);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return encrptKey;
	}
	
	/**
	 * 
	 * @param repos
	 * @param encKey
	 * @param salt
	 */
	void encryptSalt(Connection repos, String encKey, String salt) {
		
		System.out.println("Entering in method encryptSalt for encryption...");
		try {

			byte[] encodedKey = null;

			SecretKey key = null;

			ConfigData cd = new ConfigData(repos);
			
			// creating IVData for encryption
			Connection ods = null;
			byte[] ivData = createIVdata(ods, repos);
			
			System.out.println("Generating ivdata");
			
			encodedKey = new Base64().decode(encKey.getBytes());
			
			//Instantiate a KITS Key Store Class
			KITSKeyStore keyStore = (KITSKeyStore)Class.forName(cd.getConfigData(KEYSTORECLASSNAME)).newInstance();
			
			//Initialise the connection to the HSM - parameters are the Partition, Username (blank), password
			keyStore.initialise(cd.getConfigData(HSM_PARTITION_NAME), "", cd.getConfigData(HSM_PARTITION_PASSWORD));
			
			
			//PrivateKey privKey = keyStore.getPrivateKey("KITSRSA", "KITSPWD");
			PrivateKey privKey = keyStore.getPrivateKey(cd.getConfigData(CERTIFICATE_ALIAS), null);
		
			//Cipher cipher = Cipher.getInstance("RSA", "BC");
			Cipher cipher = Cipher.getInstance(RSA_ENCRYPTION_ALG, DEFAULT_PROVIDER);
			cipher.init(Cipher.UNWRAP_MODE, privKey);
			
			System.out.println("cipher init");
			
			key = (SecretKey)cipher.unwrap(encodedKey, "AES", Cipher.SECRET_KEY);
			
			System.out.println("Session Key UnWrapping");
			
			Cipher encryptedSalt = new WICSEncryption().getCipher(Cipher.ENCRYPT_MODE, AES_ENCRYPTION_ALG, key, ivData/*new Base64().decode(ivData)*/);
			
			byte[] encData = encryptedSalt.doFinal(salt.getBytes());
			
			String encryptedData = new String(new Base64().encode(encData));
			
			System.out.println("Generating encrypted data :"+encryptedData);
			
			Statement stmt = repos.createStatement();
			
			repos.setAutoCommit(false);
			
			updateDB(encKey,encryptedData,stmt,new String(new Base64().encode(ivData)));
			
			repos.commit();
			
			repos.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}

	/** If an entry for canVal exists then the value is updated else an entry is inserted
	 * 
	 * @param encKey
	 * @param encryptedData
	 * @param stmt
	 * @param ivdata
	 */
	static void updateDB(String encKey,String encryptedData,Statement stmt,String ivdata) {

		try {
			
			String sql = null;
			int rowsUpdated = 0;
			
			String canVal = "SALT_ENCRYPTION_KEY";
			
			//update encryption key in mbconfig
			sql = "update MBCONFIG SET CONFIGVALUE='"+encKey+"' where CANVAL='" + canVal + "'";
			
			rowsUpdated = stmt.executeUpdate(sql);
			
			System.out.println("SALT_ENCRYPTION_KEY updated "+rowsUpdated);
			
			if (rowsUpdated==0) {
				sql = "insert into MBCONFIG (CANVAL,CONFIGVALUE) values ('"
					+ canVal + "','" + encKey + "')";
				
				stmt.execute(sql);
			}
			
			
			canVal = "IVData";
			
			//update encryption key in mbconfig
			sql = "update MBCONFIG SET CONFIGVALUE='"+ivdata+"' where CANVAL='" + canVal + "'";
			
			rowsUpdated = stmt.executeUpdate(sql);
			
			System.out.println("IVData updated "+rowsUpdated);
			
			//storing ivdata in mbconfig
			if (rowsUpdated==0){
				sql = "insert into MBCONFIG (CANVAL,CONFIGVALUE) values ('"
					+ canVal + "','" + ivdata + "')";
			
				stmt.execute(sql);
			}
			
			//update encrypted value in mbconfig
			canVal = "ENCRYPTED_SALT";
			rowsUpdated = 0;
			
			sql = "update MBCONFIG SET CONFIGVALUE='"+encryptedData+"' where CANVAL='" + canVal + "'";
			
			rowsUpdated = stmt.executeUpdate(sql);
			
			System.out.println("ENCRYPTED_SALT updated "+rowsUpdated);
			
			if (rowsUpdated==0) {
				sql = "insert into MBCONFIG (CANVAL,CONFIGVALUE) values ('"
					+ canVal + "','" + encryptedData + "')";
				
				stmt.execute(sql);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * This method establishes the connection with repos and returns the connection
	 * to the calling function
	 * @return
	 */
	public Connection createDBConnection() {

		Connection repos = null;
		props = new java.util.Properties();

		try {
			props.load(new java.io.FileInputStream(System
					.getProperty("PROPS.FILE")));

		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			Class.forName(props.getProperty("IB.ORACLEDRIVER"));
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		try {
			repos = DriverManager.getConnection(
					props.getProperty("IB.ORACLEURL"),
					props.getProperty("IB.ORACLEUSER"),
					props.getProperty("IB.ORACLEPASSWORD"));
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		return repos;
	}

}
