package org.openframework.commons.ofds.entity;

import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.StringUtils;

@MappedSuperclass
public class MainEntity extends BaseEntity {

	@Override
	public String toString() {
		return this.getClass().getSimpleName()+"[id="+this.getId()+toStringSub()+"]";
	}

	@Override
	public String toStringSub() {
		return StringUtils.EMPTY;
	}
}
