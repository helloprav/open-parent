package org.openframework.commons.ofds.vo;

public class GroupFunctionsVO {

	private FunctionVO functions;

	public FunctionVO getFunctions() {
		return functions;
	}

	public void setFunctions(FunctionVO functions) {
		this.functions = functions;
	}

	@Override
	public String toString() {
		return "GroupFunctionsVO.functions=" + functions + "]";
	}
}
