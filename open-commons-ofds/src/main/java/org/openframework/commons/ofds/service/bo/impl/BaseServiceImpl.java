/**
 * 
 */
package org.openframework.commons.ofds.service.bo.impl;

import java.util.Date;

import javax.inject.Inject;

import org.openframework.commons.ofds.entity.BaseEntity;
import org.openframework.commons.ofds.service.as.UserAS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

/**
 * @author Java Developer
 *
 */
public class BaseServiceImpl {

	/** Logger that is available to subclasses in same package*/
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Inject
	private UserAS userAS;

	@Inject
	private ApplicationEventPublisher publisher;

	/**
	 * @return the publisher
	 */
	public ApplicationEventPublisher getPublisher() {
		return publisher;
	}

	/**
	 * @param publisher the publisher to set
	 */
	public void setPublisher(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}

	protected void setCommonProperties(BaseEntity entity, Long loggedInUserId, boolean isCreate) {

		if(null == entity.getIsValid()) {
			entity.setIsValid(true);
		}

		if(isCreate) {
			// set createdBy and createdDate
			entity.setCreatedBy(userAS.findUserById(loggedInUserId));
			entity.setCreatedDate(new Date());
			entity.setId(null);
		} else {
			// set modifiedBy and modifiedDate
			entity.setModifiedBy(userAS.findUserById(loggedInUserId));
			entity.setModifiedDate(new Date());
		}
	}

}
