package org.openframework.commons.rest.vo;

public class HistoryVO extends BaseVO {

	private int verNum;
	private Long parentId;
	private String action;

	public int getVerNum() {
		return verNum;
	}
	public void setVerNum(int verNum) {
		this.verNum = verNum;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}

}
