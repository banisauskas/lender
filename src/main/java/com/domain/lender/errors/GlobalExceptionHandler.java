package com.domain.lender.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BadRequest400.class)
	public ResponseEntity<ErrorDto> handleBadRequest400(BadRequest400 exception) {
		var errorDto = new ErrorDto(exception.getMessage());
		return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NotFound404.class)
	public ResponseEntity<ErrorDto> handleBadRequest400(NotFound404 exception) {
		var errorDto = new ErrorDto(exception.getMessage());
		return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
	}
}