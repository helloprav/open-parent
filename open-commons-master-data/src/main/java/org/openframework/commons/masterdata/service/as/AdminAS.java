package org.openframework.commons.masterdata.service.as;

import java.util.List;

import org.openframework.commons.masterdata.entity.AdminStatus;


public interface AdminAS {

	public boolean createAdminStatus();

	public boolean createMasterData();

	public List<AdminStatus> getStatus();
}
