package org.openframework.commons.encrypt.constant;

public class EncryptionConstants {

	private EncryptionConstants() {
		throw new IllegalStateException("Constant class");
	}

	public static final String IV = "iv";
	public static final String ENCRYPTION_DECRYPTION_ALGORITHM = "encryptionDecryptionAlgorithm";
	public static final String CIPHER_TRANSFORMATION = "cipherTransformation";
	public static final String SHA_ALGORITHM_256 = "shaAlgorithm";
	public static final int SHA_KEY_BIT = 16;

	public static final String PROP_SECRET_KEY = "secretKey";

	public static final String ENCRYPTION = "encryption-config";
}
