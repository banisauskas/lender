package com.domain.lender.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LoanDto {
	private Long id;
	private String name;
	private BigDecimal amount;
	private BigDecimal interestRate;
	private LocalDate term;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}

	public LocalDate getTerm() {
		return term;
	}

	public void setTerm(LocalDate term) {
		this.term = term;
	}
}