package org.openframework.commons.email.vo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="mail-template")
@XmlAccessorType(XmlAccessType.FIELD)
public class EmailVO implements Cloneable {

	/**
	 * Email Address that appears in TO list of mail.
	 */
	@XmlList
	@XmlElement(name="to")
	private List<String> to;

	/**
	 * Email Address that appears in CC list of mail.
	 */
	private String[] cc;

	/**
	 * Email Address that appears in BCC list of mail.
	 */
	private String[] bcc;

	/**
	 * Indicated if this email template is enabled or not.
	 */
	private boolean isEnabled;

	/**
	 * Email address of the sender. It is not supported as of now.
	 */
	private String fromAddress;

	/**
	 * Mail message text.
	 */
	private String body;

	/**
	 * Subject of the mail.
	 */
	private String subject;

	/**
	 * Type of mail.
	 */
	private String mailType;

	public List<String> getTo() {
		return to;
	}

	public void setTo(List<String> to) {
		this.to = to;
	}

	public void addTo(String toStr) {
		if(null == this.to) {
			this.to = new ArrayList<String>();
		}
		this.to.add(toStr);
	}

	public void addTo(String... toArray) {
		for(String toStr: toArray) {
			this.addTo(toStr);
		}
	}

	public String[] getCc() {
		return cc;
	}

	public void setCc(String[] cc) {
		this.cc = cc;
	}

	public String[] getBcc() {
		return bcc;
	}

	public void setBcc(String[] bcc) {
		this.bcc = bcc;
	}

	/**
	 * @return the isEnabled
	 */
	public boolean isEnabled() {
		return isEnabled;
	}

	/**
	 * @param isEnabled the isEnabled to set
	 */
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMailType() {
		return mailType;
	}

	public void setMailType(String mailType) {
		this.mailType = mailType;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public String toString() {

		return "EmailVO [fromAddress=" + fromAddress + ", to=\" + to + \", subject=" + subject + ", body=" + body + "]";
	}
}
