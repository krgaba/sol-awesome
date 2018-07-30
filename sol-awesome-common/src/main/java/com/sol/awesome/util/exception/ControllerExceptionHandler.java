package com.sol.awesome.util.exception;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.core.NestedRuntimeException;
import org.springframework.dao.CleanupFailureDataAccessException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.dao.UncategorizedDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler({ DataAccessResourceFailureException.class, CleanupFailureDataAccessException.class,
			OptimisticLockingFailureException.class, DeadlockLoserDataAccessException.class,
			UncategorizedDataAccessException.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public Map<String, Object> handleInternalServerException(final HttpServletRequest request, final DataAccessException ex) {
		return handleThrowable(request, ex, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@ExceptionHandler(InvalidDataAccessResourceUsageException.class)
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	@ResponseBody
	public Map<String, Object> handleUnprocessableEntityException(final HttpServletRequest request, final DataAccessException ex) {
		return handleThrowable(request, ex, HttpStatus.UNPROCESSABLE_ENTITY);

	}

	@ExceptionHandler({IllegalArgumentException.class, InvalidDataAccessApiUsageException.class, DataRetrievalFailureException.class,
			DataIntegrityViolationException.class, DataAccessException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Map<String, Object> handleSQLException(final HttpServletRequest request, final DataAccessException ex) {
		return handleThrowable(request, ex, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler({ MethodArgumentNotValidException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Map<String, Object> handleMethodArgumentNotValidException(final HttpServletRequest request,
			final MethodArgumentNotValidException ex) {
		return handleThrowable(request, ex, HttpStatus.BAD_REQUEST, ex.getBindingResult().getFieldErrors());

	}

	private Map<String, Object> handleThrowable(final HttpServletRequest request, final MethodArgumentNotValidException ex,
			final HttpStatus status, final List<FieldError> fieldErrors) {
		final Map<String, Object> response = handleThrowable(request, status);

		if (fieldErrors != null) {
			final List<String> errors = fieldErrors.stream()
					.map(fe -> String.format("Error in the object '%s' field '%s' %s; rejected value: %s",
							fe.getObjectName(), fe.getField(), fe.getDefaultMessage(), fe.getRejectedValue()))
					.collect(Collectors.toList());
			if (CollectionUtils.isNotEmpty(errors)) {
				response.put("fieldErrors", errors);
			}

		}

		response.put("exception", ex.getClass());
		response.put("exceptionMessage", ex.getMessage());
		return response;
	}


	private Map<String, Object> handleThrowable(final HttpServletRequest request, final Throwable th, final HttpStatus status) {

		final Map<String, Object> response = handleThrowable(request, status);

		response.put("exception", th.getClass());
		response.put("exceptionMessage", th.getMessage());
		if (NestedRuntimeException.class.isAssignableFrom(th.getClass())) {
			addSpecificCause((NestedRuntimeException) th, response);
		}

		return response;

	}

	private Map<String, Object> handleThrowable(final HttpServletRequest request, final HttpStatus status) {
		final Map<String, Object> response = new LinkedHashMap<>();

		response.put("status", status.value());
		response.put("error", status.getReasonPhrase());
		response.put("message", message(request));

		return response;
	}

	private void addSpecificCause(final NestedRuntimeException source, final Map<String, Object> response) {
		response.put("mostSpecificCause", source.getMostSpecificCause().getClass());
		response.put("mostSpecificCauseMessage", source.getMostSpecificCause().getMessage());
	}

	private String message(final HttpServletRequest request) {
		final Map<String, String> requestParams = Optional.ofNullable(request.getParameterMap())
				.map(p -> p.entrySet().stream()
						.collect(Collectors.toMap(e -> e.getKey(), e -> Arrays.deepToString(e.getValue()))))
				.orElse(Collections.emptyMap());
		return String.format("Could not process %s to URL %s with parameters %s from %s", request.getMethod(),
				request.getRequestURL(), requestParams, request.getRemoteHost());
	}

}
