package org.openframework.commons.ofds.service.bo;

import java.util.List;

import org.openframework.commons.ofds.vo.FunctionVO;


public interface FunctionService {

	List<FunctionVO> findFunctions(Boolean isValid);

}
