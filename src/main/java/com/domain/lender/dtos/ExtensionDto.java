package com.domain.lender.dtos;

import java.time.LocalDate;

public class ExtensionDto {

	/** Null in request, not null in response */
	private Long id;

	/** Null or not null in request. Not null ir response. */
	private LocalDate term;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getTerm() {
		return term;
	}

	public void setTerm(LocalDate term) {
		this.term = term;
	}
}