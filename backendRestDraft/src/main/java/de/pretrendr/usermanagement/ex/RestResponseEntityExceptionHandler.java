package de.pretrendr.usermanagement.ex;

import java.io.FileNotFoundException;
import java.text.MessageFormat;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { Exception.class, RuntimeException.class })
	protected ResponseEntity<Object> handleGeneralException(RuntimeException ex, WebRequest request) {
		String bodyOfResponse = "Something went horribly wrong.";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);
		logger.error(request, ex);
		return handleExceptionInternal(ex, bodyOfResponse, headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
	}

	@ExceptionHandler(value = { EntityNotFoundException.class })
	protected ResponseEntity<Object> handleEntityNotFound(RuntimeException ex, WebRequest request) {
		String bodyOfResponse = "The requested resource could not be found.";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);
		logger.error(MessageFormat.format("Request ({0}) could not be handled. EntityNotFoundException: {1}", request,
				ex.getMessage()));
		return handleExceptionInternal(ex, bodyOfResponse, headers, HttpStatus.NOT_FOUND, request);
	}

	@ExceptionHandler(value = { EntityExistsException.class })
	protected ResponseEntity<Object> handleEntityExists(RuntimeException ex, WebRequest request) {
		String bodyOfResponse = "Such an entity already exists.";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);
		logger.error(MessageFormat.format("Request ({0}) could not be handled. EntityExistsException: {1}", request,
				ex.getMessage()));
		return handleExceptionInternal(ex, bodyOfResponse, headers, HttpStatus.CONFLICT, request);
	}

	@ExceptionHandler(value = { EmailNotValidException.class })
	protected ResponseEntity<Object> handleEmailInvalid(RuntimeException ex, WebRequest request) {
		String bodyOfResponse = "The given email is not valid.";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);
		logger.error(MessageFormat.format("Request ({0}) could not be handled. EmailNotValidException: {1}", request,
				ex.getMessage()));
		return handleExceptionInternal(ex, bodyOfResponse, headers, HttpStatus.NOT_ACCEPTABLE, request);
	}
}