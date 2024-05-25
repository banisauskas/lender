package com.domain.lender.errors;

public class BadRequest400 extends RuntimeException {
	public BadRequest400(String error) {
		super(error);
	}
}