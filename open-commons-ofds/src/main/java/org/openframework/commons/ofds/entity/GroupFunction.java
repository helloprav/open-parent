package org.openframework.commons.ofds.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicUpdate;
import org.openframework.commons.constants.CommonConstants;

/**
 * The persistent class for the groups database table.
 * 
 */
@Entity
@DynamicUpdate
@Table(name = "GROUP_FUNCTION")
@NamedQuery(name = "GroupFunction.findAll", query = "SELECT g FROM GroupFunction g")
public class GroupFunction extends MainEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//bi-directional many-to-one association to Function
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "FUNCTION_ID")
	private Function function;

	//bi-directional many-to-one association to Group
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "GROUP_ID")
	private Group group;

	public GroupFunction() {
		// no argument constructor
		super();
	}

	public Group getGroup() {
		return this.group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Function getFunction() {
		return function;
	}

	public void setFunction(Function functions) {
		this.function = functions;
	}

	@Override
	public String toStringSub() {
		return groupString() + functionString();
	}

	private String functionString() {
		return null == function ? StringUtils.EMPTY : CommonConstants.STR_COMMA_SPACE + function.toString();
	}

	private String groupString() {
		return null == group ? StringUtils.EMPTY : CommonConstants.STR_COMMA_SPACE + group.toString();
	}

}
