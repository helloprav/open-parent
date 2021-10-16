package org.openframework.commons.ofds.service.adaptor;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.openframework.commons.ofds.entity.Function;
import org.openframework.commons.ofds.vo.FunctionVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class FunctionAdaptor extends BaseAdaptor {

	public List<FunctionVO> toFunctionVO(List<Function> functionList) {

		List<FunctionVO> functionVOList = new ArrayList<>();
		ListIterator<Function> listIterator = functionList.listIterator();
		while (listIterator.hasNext()) {
			Function function = listIterator.next();
			FunctionVO productCategoryVO = toVO(function);
			functionVOList.add(productCategoryVO);
		}
		return functionVOList;
	}

	public FunctionVO toVO(Function function) {
		FunctionVO functionVO = new FunctionVO();
		BeanUtils.copyProperties(function, functionVO);
		toVOCheckerMaker(function, functionVO);
		return functionVO;
	}

}
