package org.openframework.commons.ofds.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicUpdate;
import org.openframework.commons.constants.CommonConstants;


/**
 * The persistent class for the usergroups database table.
 * 
 */
@Entity
@DynamicUpdate
@Table(name="USER_GROUP")
//@NamedQuery(name="UserGroup.findAll", query="SELECT u FROM UserGroup u join fetch u.")
public class UserGroup extends MainEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//bi-directional many-to-one association to User
	//TODO: https://thoughts-on-java.org/common-hibernate-mistakes-cripple-performance/
	// TODO: @ManyToOne(fetch = FetchType.LAZY)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="USER_ID")
	private User user;

	//bi-directional many-to-one association to Group
	// TODO: @ManyToOne(fetch = FetchType.LAZY)
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="GROUP_ID")
	private Group group;

	public UserGroup() {
		// no argument constructor
		super();
	}

	public Group getGroup() {
		return this.group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toStringSub() {
		return groupString() + userString() + userString();
	}

	private String userString() {
		return null == user ? StringUtils.EMPTY : CommonConstants.STR_COMMA_SPACE + user.toString();
	}

	private String groupString() {
		return null == group ? StringUtils.EMPTY : CommonConstants.STR_COMMA_SPACE + group.toString();
	}
}
