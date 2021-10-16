package org.openframework.commons.ofds.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the groups database table.
 * 
 */
@Entity
@Table(name="GROUP_HISTORY")
@NamedQuery(name="GroupHistory.findAll", query="SELECT g FROM Group g join fetch g.groupFunctions gf")
public class GroupHistory extends HistoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "GROUP_NAME", length = 50, nullable = false, unique = false)
	private String groupName;

	@Column(name = "DESCRIPTION", length = 500)
	private String description;

	public GroupHistory() {
		// no argument constructor
	}

	public String getGroupName() {
		return this.groupName;
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