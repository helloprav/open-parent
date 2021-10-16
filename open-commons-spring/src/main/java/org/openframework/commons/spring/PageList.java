package org.openframework.commons.spring;

import java.util.List;

public class PageList<T> {

	private List<T> data;
	private Pagination pagination;

	public PageList() {
		// no arg constructor
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

}
