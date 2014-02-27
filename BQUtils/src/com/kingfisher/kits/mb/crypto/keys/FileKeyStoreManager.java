package com.kingfisher.kits.mb.crypto.keys;
import com.bq.mb.utils.ConfigData;
import com.kingfisher.kits.mb.crypto.interfaces.*;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.Signature;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
public class FileKeyStoreManager implements KITSKeyStore
{
	private String keyStoreType = "JKS";
	private KeyStore keyStore;
	//
	private static final String KEYSTOREPATH="KeyStorePath";
	private static final String KEYSTOREPASSWORD="KeyStorePassword";
	
	public FileKeyStoreManager(Connection con) throws Exception{
		//Amended CHa 13/03/2008 - Read config details from IBConfig relationship
		//loadKeyStore("D:/temp/KeyStore.jks", "KITSPWD");
		loadKeyStore(new ConfigData(con).getConfigData(KEYSTOREPATH), new ConfigData(con).getConfigData(KEYSTOREPASSWORD));	
	}
	
	public KeyStore getKeyStore(){
		return keyStore;
	}
	
	/**
	 * 
	 * @param type
	 */
	public void setKeyStoreType(String type){
		keyStoreType = type;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getKeyStoreType(){
		return keyStoreType;
	}
	
	/**
	 * 
	 * @param keyStoreFilename
	 * @param keyStorePassword
	 * @throws Exception
	 */
	private void loadKeyStore(String keyStoreFilename, String keyStorePassword) throws Exception{
		keyStore = KeyStore.getInstance(keyStoreType);
		keyStore.load(new FileInputStream(keyStoreFilename), keyStorePassword.toCharArray());
	}
	
	/**
	 * 
	 * @param privateKeyAlias
	 * @param privateKeyPassword
	 * @return
	 * @throws Exception
	 */
	public PrivateKey getPrivateKey(String privateKeyAlias, String privateKeyPassword) throws Exception{
		if(keyStore==null)
			throw new Exception("KeyStore has not been loaded. Please call loadKeyStore(String, String) first.");
		
		return (PrivateKey)keyStore.getKey(privateKeyAlias, privateKeyPassword.toCharArray());
	}
	
	/**
	 * 
	 * @param publicKeyAlias
	 * @return
	 * @throws Exception
	 */
	public PublicKey getPublicKey(String publicKeyAlias) throws Exception{
		return keyStore.getCertificate(publicKeyAlias).getPublicKey();
	}
	
	/**
	 * @param certificateName
	 * @param certificateFilename
	 * @return
	 * @throws Exception
	 */
	public Certificate generatePublicKeyCertificate(String certificateName, String certificateFilename, String privateKeyAlias, String privateKeyPassword, String signatureType, String signatureFilename) throws Exception	{
		if(keyStore == null)
			throw new Exception("KeyStore has not been loaded. Please call loadKeyStore(String, String) first.");
		
		Certificate cert = keyStore.getCertificate(certificateName);
		
		FileOutputStream fos = new FileOutputStream(certificateFilename);
		fos.write(cert.getEncoded());
		fos.close();

		//Initialise a signature class
		Signature sig = Signature.getInstance(signatureType);
		sig.initSign((PrivateKey)keyStore.getKey(privateKeyAlias, privateKeyPassword.toCharArray()));

		//Write the Signature to the file specified.
		fos = new FileOutputStream(signatureFilename);
		fos.write(sig.sign());
		fos.close();
		
		return cert;
	}

	public void initialise(String location, String username, String password) throws Exception {
		// NA for this implemenation
	}
}
