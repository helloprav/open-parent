package org.openframework.commons.ofds.service.bo;

import java.util.Map;

import org.openframework.commons.ofds.vo.UserCredentialsVO;


public interface AuthenticationService {

	Map<String, Object> authenticateUser(UserCredentialsVO userVO);

}
