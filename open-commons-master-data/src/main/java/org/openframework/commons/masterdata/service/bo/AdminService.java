package org.openframework.commons.masterdata.service.bo;

import java.util.List;

import org.openframework.commons.masterdata.vo.AdminStatusVO;

public interface AdminService {

	public boolean initAdminStatus();

	public boolean initMasterData();

	public List<AdminStatusVO> getStatus();
}
