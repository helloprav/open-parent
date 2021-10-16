package org.openframework.commons.masterdata.service.bo.impl;

import java.util.List;

import org.openframework.commons.masterdata.service.adaptor.AdminAdapter;
import org.openframework.commons.masterdata.service.as.AdminAS;
import org.openframework.commons.masterdata.service.bo.AdminService;
import org.openframework.commons.masterdata.vo.AdminStatusVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackForClassName = "Exception", isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public class AdminServiceImpl implements AdminService {

	public static final String MASTER_DATA_ADMIN_STATUS = "adminStatus";

	/** Logger that is available to subclasses */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private AdminAS adminAS;

	@Autowired
	private AdminAdapter adminAdapter;

	@Override
	public boolean initAdminStatus() {

		logger.debug("initAdminStatus");
		return adminAS.createAdminStatus();
	}

	@Override
	public boolean initMasterData() {

    	logger.debug("initMasterData");
		return adminAS.createMasterData();
	}

	@Override
	public List<AdminStatusVO> getStatus() {

		return adminAdapter.toVO(adminAS.getStatus());
	}

}
