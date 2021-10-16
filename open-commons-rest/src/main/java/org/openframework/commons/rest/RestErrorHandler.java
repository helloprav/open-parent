/**
 * Copyright (c) 2017-present Laszlo Csontos All rights reserved.
 *
 * This file is part of springuni-particles.
 *
 * springuni-particles is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * springuni-particles is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with springuni-particles.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openframework.commons.rest;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;

import org.openframework.commons.domain.exceptions.EntityAlreadyExistsException;
import org.openframework.commons.domain.exceptions.EntityConflictsException;
import org.openframework.commons.domain.exceptions.EntityNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Created by lcsontos on 5/10/17.
 */
@RestControllerAdvice
public class RestErrorHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(EntityAlreadyExistsException.class)
	public ResponseEntity<Object> handleEntityExistsException(final EntityAlreadyExistsException ex) {
		return handleExceptionInternal(ex, BAD_REQUEST);
	}

	@ExceptionHandler(EntityConflictsException.class)
	public ResponseEntity<Object> handleEntityConflictsException(final EntityConflictsException ex) {
		return handleExceptionInternal(ex, CONFLICT);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<Object> handleEntityNotFoundException(final EntityNotFoundException ex) {
		return handleExceptionInternal(ex, NOT_FOUND);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleException(final Exception ex) {
		return handleExceptionInternal(ex, INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(UnsupportedOperationException.class)
	public ResponseEntity<Object> handleUnsupportedOperationException(final UnsupportedOperationException ex) {

		return handleExceptionInternal(ex, NOT_IMPLEMENTED);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		RestErrorResponse restErrorResponse = RestErrorResponse.of(status, ex);
		return super.handleExceptionInternal(ex, restErrorResponse, headers, status, request);
	}

	private ResponseEntity<Object> handleExceptionInternal(Exception ex, HttpStatus status) {
		return handleExceptionInternal(ex, null, null, status, null);
	}

}
