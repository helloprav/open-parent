/**
 * 
 */
package org.openframework.commons.ofds.vo;

import org.openframework.commons.rest.vo.HistoryVO;

/**
 * @author Java Developer
 *
 */
public class GroupHistoryVO extends HistoryVO {

	private Long id;
	private String groupName;
	private String description;

	public GroupHistoryVO() {
		// no args constructor
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


}
