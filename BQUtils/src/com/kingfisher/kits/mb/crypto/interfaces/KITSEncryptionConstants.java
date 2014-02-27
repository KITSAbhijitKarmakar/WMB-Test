package com.kingfisher.kits.mb.crypto.interfaces;

public interface KITSEncryptionConstants {
	/** Pointer to the IBConfig Relationship Entry that holds the Key Store Manager Class Name **/
	//public static final String KEYSTORECLASSNAME="PCI-KeyStoreClassName";
	public static final String KEYSTORECLASSNAME="PCI-KeyStoreClassName_New";
	public static final String CERTIFICATE_ALIAS="PCI-CertificateAlias";
	public static final String HSM_PARTITION_NAME="PCI-HSMPartitionName";
	public static final String HSM_PARTITION_PASSWORD="PCI-HSMPassword";
	
	/** Default Encryption Algorithm **/
	public static final String RSA_ENCRYPTION_ALG = "RSA/ECB/OAEPWithSHA1AndMGF1Padding";
	public static final String AES_ENCRYPTION_ALG = "AES/CBC/PKCS5Padding";
	public static final String DES_ENCRYPTION_ALG = "DESede/ECB/PKCS5Padding";

	/** Default Cipher **/
	public static final String SYMMETRIC_CIPHER = "AES";
	/** Random Number Generator Algorithm **/
	//public static final String RND_ALGORITHM = "SHA1PRNG";
	//public static final String RND_ALGORITHM = "IBMSecureRandom";
	public static final String RND_ALGORITHM = "LunaRNG";
	/** Default Encryption Provider **/
	public static final String DEFAULT_PROVIDER = "LunaJCEProvider";
	public static final String DEFAULT_JCA_PROVIDER = "LunaJCAProvider";
	
	/** Contants to be returned to SAP in the event of an encryption failure **/
	public static final int ENCRYPTION_SUCCESS = 0;
	public static final int KEY_LOAD_FAILURE = 1;
	public static final int CIPHER_INIT_FAILURE = 2;
	public static final int ENCRYPTION_FAILURE = 3;
	public static final int DECRYPTION_FAILURE = 4;
	public static final int HASHING_FAILURE = 5;
	public static final int MASKING_FAILURE = 6;
	public static final int MAPPING_FAILURE = 7;
	public static final int IVDATA_CREATION_FAILURE = 8;
			
}
