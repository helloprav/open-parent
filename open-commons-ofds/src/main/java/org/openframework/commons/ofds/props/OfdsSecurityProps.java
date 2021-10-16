package org.openframework.commons.ofds.props;

import java.util.List;
import java.util.Properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ofds.security")
public class OfdsSecurityProps implements Props {

	private Properties encryptionProps;
	private List<String> unprotectedUrls;
	private List<String> unprotectedUris;

	@Override
	public String toString() {

		return "EncryptionProps[encrypt=" + this.encryptionProps + "]";
	}

	public Properties getEncryptionProps() {
		return encryptionProps;
	}

	public void setEncryptionProps(Properties encryptionProps) {
		this.encryptionProps = encryptionProps;
	}

	public List<String> getUnprotectedUrls() {
		return unprotectedUrls;
	}

	public void setUnprotectedUrls(List<String> unprotectedUrls) {
		this.unprotectedUrls = unprotectedUrls;
	}

	public List<String> getUnprotectedUris() {
		return unprotectedUris;
	}

	public void setUnprotectedUris(List<String> unprotectedUris) {
		this.unprotectedUris = unprotectedUris;
	}
}
