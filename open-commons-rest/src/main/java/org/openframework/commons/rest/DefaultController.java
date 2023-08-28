package org.openframework.commons.rest;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by lcsontos on 5/10/17. This DefaultController is not OK if any non
 * spring controller url is requested, because this DefaultController blocks the
 * non spring controller url and creates an error response. For example, swagger
 * integration. Swagger has the URL:
 * http://localhost:8080/<contextPath>/api/swagger-ui.html. This is blocked by
 * this DefaultController, so removing it.
 */
//@Controller
public class DefaultController {

	@GetMapping
	public ResponseEntity<RestErrorResponse> handleUnmappedRequest(final HttpServletRequest request) {
		return ResponseEntity.status(NOT_FOUND).body(RestErrorResponse.of(NOT_FOUND));
	}

}
