package com.kingfisher.kits.mb.crypto.wics;

import org.apache.commons.codec.binary.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.kingfisher.kits.mb.crypto.interfaces.*;
import com.kingfisher.kits.mb.crypto.keys.*;
import com.bq.mb.utils.ConfigData;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.util.Arrays;

/**
 * Class to be used from within WebSphere ICS to encrypt / decrypt data.<br>
 * 
 * Extends EPOSSessionKeyManager which provides EPOS related encryption
 * functions - create createRJEncryptionKey and getRJEncryptionKey<br>
 * 
 * The first transaction recieved from a store forces this class to load the
 * Symmetric / Session Key from the ODS. A different session key is used for
 * each store and each session key is held in memory (in a Hashtable). The keys
 * are stored in the ODS RSA Encrypted, This class decrypts each Key as it is
 * loaded from the ODS, The keys are stored in memory unencrypted.<br>
 * 
 * The End Of Day collaboration in the ICS is responsible for changing the
 * Session Keys at the end of each day.
 * 
 * @author Ross Stanton
 * 
 */
public class WICSEncryption extends WICSSessionKeyManager {

	// Apache Commons Codec BASE-64 Encoder / Decoder
	private static Base64 base64Codec = new Base64();
	// Apache Commons Codec HEX encoder / decoder
	private static Hex hexCodec = new Hex();
	// In Memory Hashtable to store cache of Session Keys, keyed by Store Code
	private static java.util.Hashtable keys = new java.util.Hashtable();
	// In Memory Hashtable of Ciphers, keyed by Algorithm.
	private java.util.Hashtable ciphers = new java.util.Hashtable();
	
	//SALT valriable to hold decrypted salt value(TCS - 10-SEP-2013)
	private static String SALT = "";
	
	/**
	 * Encryption method used for Store Code based encryption routines.<br>
	 * Encrypt data using an AES-256 Session Key, ECB and PKCS5Padding<br>
	 * Returns a BASE-64 Encoded String.<br>
	 * 
	 * @param input
	 * @param storeCode
	 * @param businessDayDate
	 * @param ibods
	 * @param ivdata
	 * @return
	 * @throws Exception
	 */
	public String encryptData(byte[] input, String storeCode, String businessDayDate, Connection ods,Connection repos, byte[] ivdata) throws Exception {
		SecretKey encKey = getEncryptionKeyForStore(storeCode, businessDayDate,	ods, repos);
		if(trace) trace("IN encryptData.. secretkey for store :"+storeCode+" for BDD: "+businessDayDate+ " for Sessionkey: "+encKey);
		Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, AES_ENCRYPTION_ALG, encKey, ivdata);

		byte[] encData = cipher.doFinal(input);

		return new String(base64Codec.encode(encData));
	}

	/**
	 * Primary decryption method for use within the IB. Looks up the relevant
	 * session key using the Store Code and Key Reference provided.
	 * CwDBConnection object required for the DB connection.
	 * 
	 * @param input A Byte array containing encrypted data.
	 * @param storeCode The session key related to this store code will be loaded.
	 * @param keyRef Will be a (business day date) or (epos key ref when decrypting data recieved from EPOS)
	 * @param ibods
	 * @param ivdata
	 * @return Returns a String object containing the decrypted data.
	 * @throws Exception
	 */
	public String decryptData(byte[] input, String storeCode, String keyRef, Connection ods,Connection repos, byte[] ivdata) throws Exception {
		Cipher cipher = null;

		if (!storeCode.equalsIgnoreCase("EPOS")){
			if(trace) trace("Decrypting Non EPOS Data (SAP / IB)");
			SecretKey encKey = getEncryptionKeyForStore(storeCode, keyRef, ods, repos);
			cipher = getCipher(Cipher.DECRYPT_MODE, AES_ENCRYPTION_ALG, encKey, ivdata);
		}
		else{
			if(trace){ 
				trace("Decrypting EPOS Data. Key Reference: "+keyRef+", Data Length: "+input.length);
			}
			SecretKey encKey = getRJEncryptionKey(keyRef, ods, repos);
			cipher = getCipher(Cipher.DECRYPT_MODE, DES_ENCRYPTION_ALG, encKey, ivdata);
		}

		byte[] rawData = cipher.doFinal(input);

		return new String(rawData);
	}

	/**
	 * 
	 * @param cipherMode
	 * @param algorithm
	 * @param key
	 * @param ivdata
	 * @return
	 * @throws Exception
	 */
	public Cipher getCipher(int cipherMode, String algorithm, SecretKey key, byte[] ivdata) throws Exception{
		Cipher cipher = null;
		
		if(ciphers.containsKey(algorithm)){
			if(trace)
				trace("Cipher Already Created - Returning From Memory");
			cipher = (Cipher)ciphers.get(algorithm);
		}
		else{
			if(trace)
				trace("Cipher For Algorithm "+algorithm+" not created. Creating and Storing");
			
			if(algorithm.equals(DES_ENCRYPTION_ALG))
				cipher = Cipher.getInstance(algorithm);
			else
				cipher = Cipher.getInstance(algorithm, DEFAULT_PROVIDER);
			
			ciphers.put(algorithm, cipher);
		}
		
		if(ivdata!=null){
			if(trace) trace("Initialising Cipher - IVData - Algorithm: "+algorithm);
			
			IvParameterSpec ivSpec = new IvParameterSpec(ivdata);
			cipher.init(cipherMode, key, ivSpec);
		}
		else{
			//Luna doesn't support DESede/ECB/PKCS5Padding (specifically, the padding), so we default it back to the Sun / IBM JCE to decrypt EPOS data.]
			if(trace) trace("Initialising Cipher - No IVData - Algorithm "+algorithm);
	
			cipher.init(cipherMode, key);
		}
		
		return cipher;
	}
	/**
	 * 
	 * @param storeCode
	 * @param businessDayDate
	 * @param ibods
	 * @return
	 * @throws Exception
	 */
	public static SecretKey getEncryptionKeyForStore(String storeCode, String businessDayDate, Connection ods, Connection repos) throws Exception {
		try {
			SecretKey key = null;
		
			String hashKey = storeCode+businessDayDate;
			
			if (!keys.containsKey(hashKey)) {
				if(trace) trace("Session Key For Store '" + storeCode+ "' and '" + businessDayDate+ "' Not Loaded. Loading.....");

				// Load the Session Key Manager - this will pull the Session Key from the IB ODS.
				key = loadSessionKey(storeCode, businessDayDate, ods, repos);

				// Put the session key into the in memory hashtable.
				keys.put(hashKey, key);

				if(trace) trace("Session Key Loaded for Store '" + storeCode	+ "'..... In Memory Session Key Store Now Contains "+ keys.size() + " keys.");
			} 
			else{
				if(trace) trace("Returning Session Key from Memory");
				key = (SecretKey) keys.get(hashKey);
			}

			return key;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 
	 * @param accountId
	 * @param startPos
	 * @param endPos
	 * @param maskChar
	 * @return
	 * @throws Exception
	 */
	public static String getMaskedValue(String accountId, int startPos,int endPos, String maskChar) throws Exception {
		try {
			String maskedAccountId = null;
			if ( (startPos > accountId.length()) || (endPos > accountId.length()) ) {
				throw new Exception(
						"Start or End position is greater than the length passed in");
			} else {
				maskedAccountId = accountId.substring(0, startPos);
				int count = 0;
				while (count < endPos - startPos) {
					maskedAccountId = maskedAccountId + maskChar;
					count++;
				}
				maskedAccountId = maskedAccountId + accountId.substring(endPos, accountId.length());
			}

			return maskedAccountId;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Hashes a given value using SHA-1 hashing, adds a pre-defined SALT to the
	 * value being hashed.
	 * 
	 * @param value The Value to be hashed, normally a card number
	 * @return Returns a SHA-1 hash of the value passed.
	 * @throws Exception
	 */
	public static String getHashedValue(String value,Connection repos) throws Exception {
		try {
			//Strip all spaces....
			
			value = value.trim();
			while(value.indexOf(" ")!=-1){
				value = value.substring(0, value.indexOf(" ")) + value.substring(value.indexOf(" ")+1, value.length());
			}
			
			if (SALT.equals("")|| SALT==null){
				SALT = decryptSalt(repos);
			}
				
			// Prepend the SALT to the card number
			value = SALT + value;
			
			byte[] ba = value.getBytes();
			MessageDigest md = MessageDigest.getInstance("SHA-1");

			md.update(ba);
			ba = md.digest();
			
			//Ross Stanton, 5/11/2008 - Returning HEX string as UPPER CASE as agreed with Malcolm Hotson
			return new String(hexCodec.encode(ba)).toUpperCase();
		} catch (Exception e) {
			throw new Exception("Exception occured Whilst Hashing. Error was "
					+ e.toString());
		}
	}

	/**
	 * Returns ivData value
	 * 
	 * @return ivData
	 * @throws Exception
	 */
	public static byte[] createIVdata(Connection ods, Connection repos) throws Exception {
	try {
			//ConfigData declared at method level - TCS (30.07.2013)
			ConfigData cd = new ConfigData(repos);
			
			//Instantiate a KITS Key Store Class
			KITSKeyStore ksm = (KITSKeyStore)Class.forName(cd.getConfigData(KEYSTORECLASSNAME)).newInstance();
			
			//Initialise the connection to the HSM - parameters are the Partition, Username (blank), password
			ksm.initialise(cd.getConfigData(HSM_PARTITION_NAME), "", cd.getConfigData(HSM_PARTITION_PASSWORD));
			
			byte[] ivData = new byte[16];
			SecureRandom random = SecureRandom.getInstance("LunaRNG", "LunaJCAProvider");
			
			random.nextBytes(ivData);
			return ivData;
		} catch (Exception e) {
			throw new Exception(
					"Exception Caught whilst creating IV Data. Exception was: "
							+ e.toString());
		}
	}

	/**
	 * Wraps a Session key using a specified encryption algorithm so that key
	 * can be passed to a third party. The Algorithm used is normally RSA.
	 * 
	 * @param pkey The Public Key to be used in the Wrapping process.
	 * @param alg The Algortihm used to initialise the Cipher. Must be a JCE supported value.
	 * @param key The Encryption Key to be wrapped.
	 * @return Returns a BASE-64 encoded version of the RSA Encoded Session Key.
	 * @throws Exception
	 */
	public static String wrapKey(PublicKey pkey, String alg, Key key)
			throws Exception {
		try {
			Cipher cipher = Cipher.getInstance(alg, DEFAULT_PROVIDER);
			cipher.init(Cipher.WRAP_MODE, pkey);// initialise cipher with public
												// key
			byte[] wrappedKey = cipher.wrap(key);// wrap key with session key
			return new String(new Base64().encode(wrappedKey));

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 
	 * @param storeCode
	 * @param bdd
	 * @param ibods
	 * @return
	 * @throws Exception
	 */
	public static String createKeyCheckVal(String storeCode, String bdd, Connection ods, Connection repos) throws Exception {
		try {
			// Create a 16 digit array filled with 0
			byte[] ba = new byte[16];
			byte fillByte = 0;
			Arrays.fill(ba, fillByte);

			// Get the session key for the store
			SecretKey storeKey = getEncryptionKeyForStore(storeCode, bdd, ods, repos);

			// Encrypt the 16 zeros with the store session key
			Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding", DEFAULT_PROVIDER);//differs from other ciphers as required by Solve
			cipher.init(Cipher.ENCRYPT_MODE, storeKey);
			byte[] encData = cipher.doFinal(ba);

			// Hash the encrypted zeros using SHA256
			MessageDigest md = MessageDigest.getInstance("SHA256", DEFAULT_JCA_PROVIDER);
			md.update(encData);
			encData = md.digest();

			// Get first 16 bytes
			java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(encData);
			encData = new byte[16];
			bis.read(encData);
			bis.close();
			bis = null;

			// Return them base64 encoded
			return new String(new Base64().encode(encData));
		} catch (Exception e) {
			throw e;
		}
	}
	
	/** Method added to decrypt the encrypted salt with decryption key ,stored in MBCONFIG 
	 * @author Asif Hossain TCS(10-OCT-2013)
	 * @param repos
	 * @return
	 * @throws Exception 
	 */
	public static String decryptSalt(Connection repos) throws Exception{
		
		String encKey = null;
		String encSalt = null;
		String saltValue = null;
		String ivdata = null;
		
		byte[] encodedKey = new byte[0];
		SecretKey key = null;
		
		ConfigData cd = new ConfigData(repos);
		try {
			ResultSet rs = null;
			
			String sql = "SELECT * FROM MBCONFIG WHERE CANVAL" + " IN(?,?,?)";
			
			PreparedStatement pst = repos.prepareStatement(sql);
			
			String canVal = "SALT_ENCRYPTION_KEY";
			pst.setString(1, canVal);
			
			canVal = "ENCRYPTED_SALT";
			pst.setString(2, canVal);
			
			canVal = "IVData";
			pst.setString(3, canVal);
			
			rs = pst.executeQuery();
			
			while (rs.next()){
				
				if ("SALT_ENCRYPTION_KEY".equals(rs.getString("CANVAL"))){
					encKey = rs.getString("CONFIGVALUE");
					encodedKey = new Base64().decode(encKey.getBytes());
				}
				
				else if ("ENCRYPTED_SALT".equals(rs.getString("CANVAL"))){
					encSalt = rs.getString("CONFIGVALUE");
				} 
				
				else if ("IVData".equals(rs.getString("CANVAL"))){
					ivdata = rs.getString("CONFIGVALUE");
				} 
				
			}
			
			rs.close();
			pst.close();
			
			//end ivdata
			KITSKeyStore keyStore = (KITSKeyStore)Class.forName(cd.getConfigData(KEYSTORECLASSNAME)).newInstance();
			
			keyStore.initialise(cd.getConfigData(HSM_PARTITION_NAME), "",cd.getConfigData(HSM_PARTITION_PASSWORD));
			PrivateKey privKey = keyStore.getPrivateKey("IB"/*CERTIFICATE_ALIAS*/,null);

			
			Cipher cipher = Cipher.getInstance(RSA_ENCRYPTION_ALG, DEFAULT_PROVIDER);
			cipher.init(Cipher.UNWRAP_MODE, privKey);
			key = (SecretKey)cipher.unwrap(encodedKey, "AES"/*"DESede"*/, Cipher.SECRET_KEY); 
			
			cipher = new WICSEncryption().getCipher(Cipher.DECRYPT_MODE, AES_ENCRYPTION_ALG, key,new Base64().decode(ivdata.getBytes()));
			byte[] rawData = cipher.doFinal(new Base64().decode(encSalt.getBytes()));
			
			saltValue = new String(rawData);
			
		}
		catch (Exception e) {
			throw new Exception("Exception in decryptSalt method: "+e);
		}
		return saltValue;
	}
}
