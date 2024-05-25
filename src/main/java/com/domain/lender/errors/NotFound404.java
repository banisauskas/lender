package com.domain.lender.errors;

public class NotFound404 extends RuntimeException {
	public NotFound404(String error) {
		super(error);
	}
}