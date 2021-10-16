package org.openframework.commons.encrypt;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;

import org.openframework.commons.config.service.as.MessageResourceAS;
import org.openframework.commons.encrypt.constant.EncryptionConstants;
import org.openframework.commons.utils.FileFolderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EncryptionUtil {

	final Logger logger = LoggerFactory.getLogger(EncryptionUtil.class);

	@Inject
	Optional<MessageResourceAS> messageResourceAS;

	//@Inject
	//EncryptionProps encryptionProps;

	public static void main(String[] args) {
		EncryptionUtil encryptionUtil = new EncryptionUtil();
		encryptionUtil.encrypt("toBeEncrypt", FileFolderUtils.loadPropFromFile(getEncryptionFile()));
	}

	private static File getEncryptionFile() {

		return new File(Thread.currentThread().getContextClassLoader().getResource("dev/config/encryption-config.yml")
				.getFile());
	}

	public String encrypt(String toBeEncrypt) {

		Properties properties = getProperties();
		return encrypt(toBeEncrypt, properties);
	}

	public String encrypt(String toBeEncrypt, Properties properties) {

		String result = null;
		try {

			Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, properties);
			if (null == cipher) {
				return null;
			}

			byte[] encrypted = cipher.doFinal(toBeEncrypt.getBytes());
			result = new String(Base64.getEncoder().encode(encrypted), StandardCharsets.UTF_8);

		} catch (BadPaddingException | IllegalBlockSizeException ex) {
			logger.error(String.format("Error caught in encrypt(), for toBeEncrypt: %s ", toBeEncrypt), ex);
		}
		return result;
	}

	private Cipher getCipher(int mode, Properties properties) {

		Cipher cipher = null;
		try {
			String iv = properties.getProperty(EncryptionConstants.IV);
			String secretKey = properties.getProperty(EncryptionConstants.PROP_SECRET_KEY);
			IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
			SecretKeySpec secretKeySpec = new SecretKeySpec(generateKey(secretKey, properties),
					properties.getProperty(EncryptionConstants.ENCRYPTION_DECRYPTION_ALGORITHM));
			cipher = Cipher.getInstance(properties.getProperty(EncryptionConstants.CIPHER_TRANSFORMATION));

			cipher.init(mode, secretKeySpec, ivParameterSpec);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException
				| InvalidKeyException ex) {
			logger.error(String.format("Error in getCipher(), for mode: [%s] and [%s] properties: ", mode, properties),
					ex);
		}
		return cipher;
	}

	/**
	 * This method provides sha-256 hash key.
	 * 
	 * @param key
	 *            key to Digest
	 * @return hash key
	 * @throws GeneralSecurityException
	 *             General Security
	 * @throws UnsupportedEncodingException
	 *             wrong Encoding
	 */
	public byte[] generateKey(String key, Properties properties) {
		byte[] binary = null;
		try {
			binary = key.getBytes(StandardCharsets.UTF_8);
			MessageDigest sha = MessageDigest
					.getInstance(properties.getProperty(EncryptionConstants.SHA_ALGORITHM_256));
			binary = sha.digest(binary);
			binary = Arrays.copyOf(binary, EncryptionConstants.SHA_KEY_BIT);
		} catch (NoSuchAlgorithmException e) {
			logger.error(String.format("Error caught in generateKey(), for key: %s", key), e);
		}
		return binary;
	}

	public String decrypt(String encrypted, Properties properties) {

		String result = null;
		try {

			Cipher cipher = getCipher(Cipher.DECRYPT_MODE, properties);
			if (null == cipher) {
				return null;
			}

			byte[] cipherText = Base64.getDecoder().decode(encrypted.getBytes(StandardCharsets.UTF_8));

			result = new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
		} catch (BadPaddingException | IllegalBlockSizeException | IllegalArgumentException ex) {
			logger.error(String.format("Error caught in encrypt(), for toBeEncrypt: %s ", encrypted), ex);
		}
		return result;
	}

	public String decrypt(String encrypted) {

		Properties properties = getProperties();
		return decrypt(encrypted, properties);
	}

	private Properties getProperties() {

		Properties properties = null;
		if(messageResourceAS.isPresent()) {
			properties = messageResourceAS.get().getAppConfigsMap().get(EncryptionConstants.ENCRYPTION);
		} else {
			// provide default properties if not found in messageResourceAS
			properties = new Properties();
			//properties.putAll(this.encryptionProps.getProperties());
			//return this.encryptionProps.getProperties();
		}
		return properties;
	}
}
