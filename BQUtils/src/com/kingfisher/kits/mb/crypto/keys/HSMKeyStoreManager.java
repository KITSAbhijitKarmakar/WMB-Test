package com.kingfisher.kits.mb.crypto.keys;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import com.chrysalisits.crypto.LunaKey;
import com.chrysalisits.crypto.LunaPublicKey;
import com.chrysalisits.crypto.LunaTokenManager;

import com.kingfisher.kits.mb.crypto.interfaces.KITSKeyStore;

public class HSMKeyStoreManager implements KITSKeyStore {
	private LunaTokenManager hsm;
	
	/**
	 * @see com.kingfisher.kits.mb.crypto.interfaces.KITSKeyStore#initialise(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void initialise(String location, String username, String password) throws Exception {
		hsm = LunaTokenManager.getInstance();
		if(!hsm.isLoggedIn()){
			hsm.Login(location, password);
		}
		
		hsm.SetSecretKeysExtractable(true);
	}

	/**
	 * @see com.kingfisher.kits.mb.crypto.interfaces.KITSKeyStore#generatePublicKeyCertificate(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public Certificate generatePublicKeyCertificate(String certificateName,String certificateFilename, String privateKeyAlias,String privateKeyPassword, String signatureType,String signatureFilename) throws Exception {
		return null;
	}

	/**
	 * @see com.kingfisher.kits.mb.crypto.interfaces.KITSKeyStore#getKeyStore()
	 */
	public KeyStore getKeyStore() throws Exception{
		throw new Exception("Unable to Return KeyStore when using an HSM");
	}

	/**
	 * @see com.kingfisher.kits.mb.crypto.interfaces.KITSKeyStore#getKeyStoreType()
	 */
	public String getKeyStoreType() {
		return null;
	}
	
	public PrivateKey getPrivateKey(String privateKeyAlias, String privateKeyPassword) throws Exception {
		PrivateKey pk = (PrivateKey)LunaKey.LocateKeyByAlias(privateKeyAlias+".private");
		return pk;
	}

	public PublicKey getPublicKey(String publicKeyAlias) throws Exception {
		PublicKey pubKey = (PublicKey)LunaKey.LocateKeyByAlias(publicKeyAlias+".public");
		return pubKey;
	}

	public void setKeyStoreType(String type) {
	}
}
