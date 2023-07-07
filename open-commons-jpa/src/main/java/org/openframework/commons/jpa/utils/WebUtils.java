/**
 * 
 */
package org.openframework.commons.jpa.utils;

import java.util.Date;

import org.openframework.commons.jpa.entity.RecordInfo;
import org.openframework.commons.jpa.entity.User;

/**
 * @author praveen
 *
 */
public class WebUtils {

	public static RecordInfo populateCreateInfo(User loggedInUser, RecordInfo recordInfo ) {

		if (null == recordInfo) {
			recordInfo = new RecordInfo();
		}
		recordInfo.setCreatedBy(loggedInUser.getUsername());
		recordInfo.setCreatedDate(new Date());
		return recordInfo;
	}

	public static RecordInfo populateModifyInfo(User loggedInUser, RecordInfo recordInfo) {

		recordInfo.setModifiedBy(loggedInUser.getUsername());
		recordInfo.setModifiedDate(new Date());
		return recordInfo;
	}

}
