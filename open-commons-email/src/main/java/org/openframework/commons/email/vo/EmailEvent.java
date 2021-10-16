package org.openframework.commons.email.vo;

import java.util.Map;

public class EmailEvent {

	private String name;
	private Map<String, String> map;

	public EmailEvent(String name) {
		this.name = name;
	}

	public EmailEvent(String name, Map<String, String> map) {
		this.name = name;
		this.map = map;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the map
	 */
	public Map<String, String> getMap() {
		return map;
	}

	/**
	 * @param map the map to set
	 */
	public void setMap(Map<String, String> map) {
		this.map = map;
	}
}
