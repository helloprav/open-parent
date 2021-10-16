package org.openframework.commons.encrypt;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HashingUtils {

	private static final Logger logger = LoggerFactory.getLogger(HashingUtils.class);
	private static String hashingAlgorithm = "SHA-1";

	public static String getHashingAlgorithm() {
		return hashingAlgorithm;
	}

	public static void setHashingAlgorithm(String hashingAlgorithm) {
		HashingUtils.hashingAlgorithm = hashingAlgorithm;
	}

	private HashingUtils() {
	}

	public static String sha1(String message) throws NoSuchAlgorithmException, UnsupportedEncodingException {

		MessageDigest md = MessageDigest.getInstance(getHashingAlgorithm());
		md.update(message.getBytes("iso-8859-1"), 0, message.length());
		byte[] sha1hash = md.digest();
		return convToHex(sha1hash);
	}

	static String convToHex(byte[] data) {

		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < data.length; i++) {
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int twoHalfs = 0;
			do {
				if ((0 <= halfbyte) && (halfbyte <= 9))
					buf.append((char) ('0' + halfbyte));
				else
					buf.append((char) ('a' + (halfbyte - 10)));
				halfbyte = data[i] & 0x0F;
			} while (twoHalfs++ < 1);
		}
		return buf.toString();
	}

	public static String generateRandomPassword() {

		logger.debug("Entered generatePassword()");
		return UUID.randomUUID().toString();
	}

	public static void main(String[] args) {

		char[] array = new char[] {'p','a','s','s','w','o','r','d'};
		String plainMessage = String.valueOf(array);//
		System.out.println("plainMessage: "+plainMessage);

		try {
			for(int i=1; i<=1; i++)
				System.out.println(sha1(plainMessage));
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
