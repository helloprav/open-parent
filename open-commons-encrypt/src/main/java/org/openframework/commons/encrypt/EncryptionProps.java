package org.openframework.commons.encrypt;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;

//@Component
public class EncryptionProps {

	public EncryptionProps() {
		// no args constructor
	}

	@Inject
	private Properties properties;

	@PostConstruct
	public void init() {
		properties.put("cipherTransformation", cipherTransformation);
	}

	@Value("${cipherTransformation}")
	private String cipherTransformation;

	@Value("${encryptionDecryptionAlgorithm}")
	private String encryptionDecryptionAlgorithm;

	@Value("${iv}")
	private String iv;

	@Value("${shaAlgorithm}")
	private String shaAlgorithm;

	@Value("${secretKey}")
	private String secretKey;

	public String getCipherTransformation() {
		return cipherTransformation;
	}

	public void setCipherTransformation(String cipherTransformation) {
		this.cipherTransformation = cipherTransformation;
	}

	public String getEncryptionDecryptionAlgorithm() {
		return encryptionDecryptionAlgorithm;
	}

	public void setEncryptionDecryptionAlgorithm(String encryptionDecryptionAlgorithm) {
		this.encryptionDecryptionAlgorithm = encryptionDecryptionAlgorithm;
	}

	public String getIv() {
		return iv;
	}

	public void setIv(String iv) {
		this.iv = iv;
	}

	public String getShaAlgorithm() {
		return shaAlgorithm;
	}

	public void setShaAlgorithm(String shaAlgorithm) {
		this.shaAlgorithm = shaAlgorithm;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public Properties getProperties() {
		return this.properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
}
