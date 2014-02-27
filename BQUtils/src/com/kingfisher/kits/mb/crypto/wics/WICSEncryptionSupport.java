package com.kingfisher.kits.mb.crypto.wics;
import com.kingfisher.kits.mb.crypto.interfaces.KITSEncryptionConstants;
import com.chrysalisits.crypto.LunaTokenManager;
import com.chrysalisits.crypto.LunaKey;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;

import java.security.*;
import java.security.interfaces.*;
import java.security.spec.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class WICSEncryptionSupport implements KITSEncryptionConstants
{
	private static LunaTokenManager hsm = null;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length<6){
			System.err.println("Too Few Parameters Supplied, Should be:\nWICSEncryptionSupport <Base64 Data> <Base64 Session Key> <Session Key Type 'AES' | 'DESede'> <private key alias> <hsm partition name> <hsm partition password>");
			System.exit(-1);
		}
		else{
			if(!(args[2].equalsIgnoreCase("DESede") || args[2].equalsIgnoreCase("AES"))){
				System.err.println("Session Key Algoritm must be either AES or DESede");
				System.exit(-1);
			}
			try{
				decrypt(args[0], args[1], args[2], args[3], args[4], args[5]);
			}
			catch(Exception e){
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}

	private static void decrypt(String data, String sessionKey, String sessionKeyType, String privateKeyAlias, String partitionName, String partitionPassword) throws Exception{
		login(partitionName, partitionPassword);
		Cipher cipher = Cipher.getInstance(RSA_ENCRYPTION_ALG, DEFAULT_PROVIDER);
		cipher.init(Cipher.UNWRAP_MODE, getPrivateKey(privateKeyAlias));
		SecretKey sk = (SecretKey)cipher.unwrap(new Base64().decode(sessionKey.getBytes()), sessionKeyType, Cipher.SECRET_KEY);

		byte[] encData = null;
		if(sessionKeyType.equalsIgnoreCase("AES")){
			cipher = Cipher.getInstance(AES_ENCRYPTION_ALG, DEFAULT_PROVIDER);
			IvParameterSpec ivSpec = new IvParameterSpec(new Base64().decode("5YI3TUDBMARMNYFFAXVKDW==".getBytes()));
			cipher.init(Cipher.DECRYPT_MODE, sk, ivSpec);
			encData = new Base64().decode(data.getBytes());
		}
		else{
			cipher = Cipher.getInstance("DESede/ECB/NoPadding", DEFAULT_PROVIDER);
			cipher.init(Cipher.DECRYPT_MODE, sk);
			encData = new Hex().decode(data.getBytes());
		}
		
		
		byte[] decrypted = cipher.doFinal(encData);
		System.out.println("Decrypted Data:\n'"+new String(decrypted)+"'");
	}
	
	private static void login(String partition, String password) throws Exception{
		hsm = LunaTokenManager.getInstance();
		if(!hsm.isLoggedIn()){
			hsm.Login(partition, password);
		}
		
		hsm.SetSecretKeysExtractable(true);
	}
	
	private static PrivateKey getPrivateKey(String keyName){
		return (PrivateKey)LunaKey.LocateKeyByAlias(keyName+".private");
	}
}
