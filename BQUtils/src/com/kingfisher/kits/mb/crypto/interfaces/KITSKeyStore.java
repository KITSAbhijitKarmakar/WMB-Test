package com.kingfisher.kits.mb.crypto.interfaces;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

public interface KITSKeyStore {
	/** Connection Initialisation Parameters **/
	public void initialise(String location, String username, String password) throws Exception;
	/** Get Direct Access to the KeyStore Object **/
	public KeyStore getKeyStore() throws Exception;
	/** Set the KeyStore Type **/
	public void setKeyStoreType(String type);
	/** Return Key Store Type **/
	public String getKeyStoreType();
	/** Get Public Key **/
	public PublicKey getPublicKey(String publicKeyAlias) throws Exception;
	/** Get Private Key **/
	public PrivateKey getPrivateKey(String privateKeyAlias, String privateKeyPassword) throws Exception;
	/** Generate a public key certificate **/
	public Certificate generatePublicKeyCertificate(String certificateName, String certificateFilename, String privateKeyAlias, String privateKeyPassword, String signatureType, String signatureFilename) throws Exception;
}
