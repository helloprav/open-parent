package org.openframework.commons.ofds.vo;

import org.openframework.commons.rest.vo.MainVO;

public class FunctionVO extends MainVO {

	private Long id;
	private String name;

	public FunctionVO() {
		// no args constructor
	}

	public FunctionVO(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "FunctionVO [id=" + id + ", name=" + name + "]";
	}
}
