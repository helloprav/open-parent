package org.openframework.commons.enums;

public enum Action {

	ADD("ADD"), MOD("MOD"), DEL("DEL");

	private final String name;

	private Action(String value) {
		this.name = value;
	}

	public String value() {
		return this.name;
	}

	@Override
	public String toString() {
		return name;
	}
}
