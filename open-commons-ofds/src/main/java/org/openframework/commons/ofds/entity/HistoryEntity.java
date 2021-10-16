package org.openframework.commons.ofds.entity;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import org.openframework.commons.enums.Action;


@MappedSuperclass
public class HistoryEntity extends BaseEntity {

	private int verNum;

	@Column(name = "parentId", nullable = false, unique = false)
	private Long parentId;

	@Enumerated(EnumType.STRING)
    private Action action;

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

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

}
