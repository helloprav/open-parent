package org.openframework.commons.masterdata.service.as.impl;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.openframework.commons.config.service.as.MessageResourceAS;
import org.openframework.commons.masterdata.entity.AdminStatus;
import org.openframework.commons.masterdata.service.as.AdminAS;
import org.openframework.commons.masterdata.service.repository.AdminStatusRepository;
import org.openframework.commons.masterdata.vo.MasterData;
import org.openframework.commons.masterdata.vo.MasterData.QueryData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AdminASImpl implements AdminAS {

	/** Logger that is available to subclasses */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@PersistenceContext
	protected EntityManager em;

	@Inject
	private MasterData masterData;

	@Inject
	MessageResourceAS messageResourceAS;

	@Inject
	private AdminStatusRepository adminStatusRepository;

	@Override
	public boolean createAdminStatus() {

    	logger.debug("createAdminStatus");
    	return createData(masterData.getAppInstallQueryList());
	}

	@Override
	public boolean createMasterData() {

    	logger.debug("createMasterData");
    	return createData(masterData.getAppStartQueryList());
	}

	public boolean createData(List<QueryData> queryList) {

		boolean status = true;
    	logger.debug("createData");
		for (QueryData queryData : queryList) {

	    	logger.debug("queryData: {}", queryData);
			if (null == queryData.getCondition()) {
				alwaysExcuteSqls(queryData);
			} else if (queryData.getCondition()) {
				excuteIfCountZero(queryData);
			} else if (!queryData.getCondition()) {
				// condition is set as false, so no need to execute any query
			} else {
				logger.info("Unknown value found for query.name [{}] = {} ", queryData.getName(),
						queryData.getCondition());
			}
		}
		return status;
	}

	private void excuteIfCountZero(QueryData queryData) {

		int readCount = -1;
		String readSql = queryData.getRead();
		String readCountList = em.createNativeQuery(readSql).getResultList().toString();
		String cnt = readCountList.substring(1, readCountList.length() - 1);
		if (cnt.length() > 0) {
			readCount = Integer.parseInt(cnt);
		}

		if (readCount == 0) {
			List<String> sqls = queryData.getSqls();
			for (String sql : sqls) {
				int count = em.createNativeQuery(sql).executeUpdate();
				logger.info("executed query [{}], count: [{}] ", sql, count);
			}
		} else {
			logger.info("executed select query [{}], count is zero", readSql);
		}
	}

	private void alwaysExcuteSqls(QueryData queryData) {

		logger.info("executing all query as condition is null");
		List<String> sqls = queryData.getSqls();
		for (String sql : sqls) {
			int count = em.createNativeQuery(sql).executeUpdate();
			logger.info("executed query [{}], count: [{}] ", sql, count);
		}
	}

	@Override
	public List<AdminStatus> getStatus() {

		return adminStatusRepository.findAll();
	}

}
