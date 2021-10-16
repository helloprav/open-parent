package org.openframework.commons.ofds.service.adaptor;

import org.openframework.commons.rest.vo.BaseVO;
import org.openframework.commons.ofds.entity.BaseEntity;
import org.openframework.commons.ofds.entity.MainEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseAdaptor {

	/** Logger that is available to subclasses */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected void toVOCheckerMaker(BaseEntity entity, BaseVO baseVO) {
		if(null != entity.getCreatedBy()) {
			baseVO.setCreatedBy(entity.getCreatedBy().getUsername());
		}
		if(null != entity.getModifiedBy()) {
			baseVO.setModifiedBy(entity.getModifiedBy().getUsername());
		}
	}

	protected void toVOCreater(BaseEntity entity, BaseVO baseVO) {
		if(null != entity.getCreatedBy()) {
			baseVO.setCreatedBy(entity.getCreatedBy().getUsername());
		}
	}

	protected void toVOHistoryCreater(BaseEntity entity, BaseVO baseVO) {
		if(null != entity.getCreatedBy()) {
			baseVO.setCreatedBy(entity.getCreatedBy().getUsername());
		}
	}

	protected void toVOUpdater(MainEntity entity, BaseVO baseVO) {
		if(null != entity.getModifiedBy()) {
			//baseVO.setModifiedBy(entity.getModifiedBy().getUsername());
		}
	}

}
