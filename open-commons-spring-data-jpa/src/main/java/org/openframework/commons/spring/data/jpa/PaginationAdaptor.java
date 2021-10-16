package org.openframework.commons.spring.data.jpa;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openframework.commons.domain.exceptions.ApplicationValidationException;
import org.openframework.commons.spring.Pagination;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class PaginationAdaptor {

	public static final String SORT_ORDER_ASC = "asc";
	public static final String SORT_ORDER_DESC = "desc";
	public static final String SORT_ORDER_SEPARATOR = "::";

	private PaginationAdaptor() {
		throw new IllegalArgumentException("Utility Class");
	}

	/**
	 * Converts the paginationVO from request to spring data jpa specific
	 * <code>Pageable</code> for the database query.
	 * 
	 * @param pagination user requested pagination details
	 * @return Spring Data JPA specific Pageable object
	 */
	public static Pageable fromPaginationVO(Pagination pagination) {

		Pageable paging = null;
		// no pagination i.e. pageSize < 0
		if (pagination.getPage() < 1) {
			pagination.setPage(1);
		}
		if (pagination.getLimit() < 1) {
			// no pagination required here. Now check sorting
			pagination.setLimit(Short.MAX_VALUE);
			pagination.setPage(1);
		} else if (pagination.getLimit() == 0) {
			throw new ApplicationValidationException("pageSize can't be zero");
		}

		if (null == pagination.getSort()) {
			// pagination without sorting
			paging = PageRequest.of(pagination.getPage()-1, pagination.getLimit());
		} else {
			// pagination with sorting
			if (StringUtils.isBlank(pagination.getSortOrder())) {
				Sort groupSort = calculateGroupSort(pagination.getSort());
				if (null == groupSort) {
					paging = PageRequest.of(pagination.getPage()-1, pagination.getLimit());
					// log this using warn/info logger
				} else {
					paging = PageRequest.of(pagination.getPage()-1, pagination.getLimit(), groupSort);
				}
			} else {
				// else calculate Direction from the requested sortOrder
				Direction direction = getSortDirection(pagination.getSortOrder());
				String[] columns = pagination.getSort().toArray(new String[0]);
				paging = PageRequest.of(pagination.getPage()-1, pagination.getLimit(), direction, columns);
			}
		}
		return paging;
	}

	/**
	 * Calculates groupSort based on the given list of sortColumn::sortDirection
	 * 
	 * @param sortColumns list of String in format
	 *                    (sortColumnNames::sortColumnDirection)
	 * @return final Sort object.
	 */
	public static Sort calculateGroupSort(List<String> sortColumns) {

		Sort groupSort = null;
		// if no sortOrder present, check sortOrder for each column
		for (String sortColumn : sortColumns) {
			String[] sortColumnArray = sortColumn.split(SORT_ORDER_SEPARATOR);
			Sort sortOnThisColumn = null;
			if (sortColumnArray.length == 1) {
				sortOnThisColumn = Sort.by(sortColumnArray[0]);
			} else if (sortColumnArray.length > 1) {
				Direction sortInThisDirection = getSortDirection(sortColumnArray[1]);
				sortOnThisColumn = Sort.by(sortInThisDirection, sortColumnArray[0]);
			}
			if (null == sortOnThisColumn) {
				continue;
			}
			if (null == groupSort) {
				groupSort = sortOnThisColumn;
			} else {
				groupSort = groupSort.and(sortOnThisColumn);
			}
		}
		return groupSort;
	}

	/**
	 * Returns Direction from the given string.
	 * 
	 * @param sortingOrder the string value for direction
	 * @return Direction
	 */
	private static Direction getSortDirection(String sortingOrder) {

		Direction direction;
		if (null != sortingOrder && SORT_ORDER_DESC.equalsIgnoreCase(sortingOrder)) {
			direction = Direction.DESC;
		} else {
			direction = Direction.ASC;
		}
		return direction;
	}

	/**
	 * Converts Spring Data JPA Specific <code>Pageable</code> object into
	 * <code>Pagination</code> object
	 * 
	 * @param pageable spring data jpa specific object for pagination
	 * @param size     of the items in response
	 * @return PaginationVO object
	 */
	public static Pagination toPaginationVO(Pageable pageable, long total, int size) {

		Pagination pagination = new Pagination();
		pagination.setPage(pageable.getPageNumber()+1);
		pagination.setLimit(pageable.getPageSize());
		pagination.setTotal(total);
		pagination.setSize(size);
		return pagination;
	}
}
