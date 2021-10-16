package org.openframework.commons.ofds.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;


/**
 * The persistent class for the groups database table.
 * 
 */
@Entity
@DynamicUpdate
@Table(name="GROUPE")
@NamedQuery(name="Group.findAll", query="SELECT g FROM Group g join fetch g.groupFunctions gf")
public class Group extends MainEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "GROUP_NAME", length = 50, nullable = false, unique = true)
	private String groupName;

	@Column(name = "DESCRIPTION", length = 500)
	private String description;

	@OneToMany(mappedBy="group", fetch=FetchType.LAZY)
	private List<GroupFunction> groupFunctions;

	//bi-directional many-to-one association to UserGroup
	@OneToMany(mappedBy="group")
	private List<UserGroup> userGroups;

	public Group() {
		// no argument constructor
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public List<GroupFunction> getGroupFunctions() {
		return groupFunctions;
	}

	public void setGroupFunctions(List<GroupFunction> groupFunctions) {
		this.groupFunctions = groupFunctions;
	}

	public List<UserGroup> getUserGroups() {
		return this.userGroups;
	}

	public void setUserGroups(List<UserGroup> userGroups) {
		this.userGroups = userGroups;
	}

	/**
	public UserGroup addUsergroup(UserGroup usergroup) {
		getUserGroups().add(usergroup);
		usergroup.setGroup(this);

		return usergroup;
	}

	public UserGroup removeUsergroup(UserGroup usergroup) {
		getUserGroups().remove(usergroup);
		usergroup.setGroup(null);

		return usergroup;
	}	*/

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}