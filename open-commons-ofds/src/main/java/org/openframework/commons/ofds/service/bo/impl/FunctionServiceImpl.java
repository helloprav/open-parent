/**
 * 
 */
package org.openframework.commons.ofds.service.bo.impl;

import java.util.List;

import javax.inject.Inject;

import org.openframework.commons.ofds.service.adaptor.FunctionAdaptor;
import org.openframework.commons.ofds.service.as.FunctionAS;
import org.openframework.commons.ofds.service.bo.FunctionService;
import org.openframework.commons.ofds.vo.FunctionVO;
import org.springframework.stereotype.Service;

/**
 * @author Java Developer
 *
 */
@Service
public class FunctionServiceImpl extends BaseServiceImpl implements FunctionService {

	@Inject
	private FunctionAS functionAS;

	@Inject
	private FunctionAdaptor functionAdaptor;

	@Override
	public List<FunctionVO> findFunctions(Boolean isValid) {
		return functionAdaptor.toFunctionVO(functionAS.findFunctions(isValid));
	}

}
