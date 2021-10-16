package org.openframework.commons.ofds.service.as.impl;

import java.util.List;

import javax.inject.Inject;

import org.openframework.commons.ofds.entity.Function;
import org.openframework.commons.ofds.service.as.FunctionAS;
import org.openframework.commons.ofds.service.repository.FunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class FunctionASImpl extends BaseASImpl implements FunctionAS {

	@Inject
	private FunctionRepository functionRepository;

	@Override
	public List<Function> findFunctions(Boolean isValid) {
		if(null == isValid) {
			return functionRepository.findAll();
		} else {
			return functionRepository.findByIsValid(isValid);
		}
	}

}
