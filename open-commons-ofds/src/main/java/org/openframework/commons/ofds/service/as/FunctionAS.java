package org.openframework.commons.ofds.service.as;

import java.util.List;

import org.openframework.commons.jpa.entity.Function;


public interface FunctionAS {

	List<Function> findFunctions(Boolean isValid);

}
