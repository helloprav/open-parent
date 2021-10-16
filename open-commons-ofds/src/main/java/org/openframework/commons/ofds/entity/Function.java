package org.openframework.commons.ofds.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

/**
 * The persistent class for the groups database table.
 * 
 */
@Entity
@DynamicUpdate
@Table(name = "FUNCTIONS")
@NamedQuery(name = "Function.findAll", query = "SELECT g FROM Function g")
public class Function extends MainEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "FUNCTION_NAME", length = 50, nullable = false, unique = true)
	private String name;

	@OneToMany(mappedBy = "function")
	private List<GroupFunction> groupFunctions;

	public List<GroupFunction> getGroupFunctions() {
		return groupFunctions;
	}

	public void setGroupFunctions(List<GroupFunction> groupFunctions) {
		this.groupFunctions = groupFunctions;
	}

	public String getName() {
		return name;
	}

	public void setName(String functionName) {
		this.name = functionName;
	}

	public Function() {
		// no argument constructor
	}

}