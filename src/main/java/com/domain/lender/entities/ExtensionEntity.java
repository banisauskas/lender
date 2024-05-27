package com.domain.lender.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "extension")
public class ExtensionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "loan_id", nullable = false)
	private LoanEntity loan;

	private LocalDate term;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LoanEntity getLoan() {
		return loan;
	}

	public void setLoan(LoanEntity loan) {
		this.loan = loan;
	}

	public LocalDate getTerm() {
		return term;
	}

	public void setTerm(LocalDate term) {
		this.term = term;
	}
}