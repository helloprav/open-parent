package org.openframework.commons.spring;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class Pagination {

	private int page;			// default value 0
	private int limit = 5;		// default value 10
	private int size;
	private long total;
	@JsonInclude(Include.NON_NULL) // Jackson 2 or higher
	private List<String> sort;
	@JsonInclude(Include.NON_NULL) // Jackson 2 or higher
	private String sortOrder;
	@JsonInclude(Include.NON_NULL) // Jackson 2 or higher
	private List<String> filter;

	public Pagination() {
		// no arg constructor
	}

	public Pagination(int page, int limit) {
		super();
		this.page = page;
		this.limit = limit;
	}

	public Pagination(int pageNo, int limit, String sortOrder, List<String> sortColumns) {
		super();
		this.page = pageNo;
		this.limit = limit;
		this.sortOrder = sortOrder;
		this.sort = sortColumns;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<String> getSort() {
		return sort;
	}

	public void setSort(List<String> sort) {
		this.sort = sort;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public List<String> getFilter() {
		return filter;
	}

	public void setFilter(List<String> filter) {
		this.filter = filter;
	}


}
