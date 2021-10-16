/**
 * 
 */
package org.openframework.commons.ofds.controller;

import org.openframework.commons.rest.controller.BaseController;
import org.openframework.commons.config.service.I18nService;
import org.openframework.commons.ofds.props.MasterDataProps;
import org.openframework.commons.ofds.props.OfdsSecurityProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author praveenm
 *
 */
@RequestMapping("/api")
public class OfdsBaseController extends BaseController {

	/** Logger that is available to subclasses */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private I18nService i18nService;

	@Autowired
	private OfdsSecurityProps encryptionProps;

	@Autowired
	private MasterDataProps masterDataProps;

	public OfdsSecurityProps getEncryptionProps() {
		return encryptionProps;
	}

	public MasterDataProps getMasterDataProps() {
		return masterDataProps;
	}

	public I18nService getI18nService() {
		return i18nService;
	}

}
