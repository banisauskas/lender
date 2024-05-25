package com.domain.lender.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "loan")
public class LoanEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loan_id_seq")
	private Long id;

	private String name;

	private LocalDate term;

	private BigDecimal amount;

	@OneToMany(mappedBy = "loan", cascade = CascadeType.ALL)
	private List<ExtensionEntity> extensions;

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

	public LocalDate getTerm() {
		return term;
	}

	public void setTerm(LocalDate term) {
		this.term = term;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public List<ExtensionEntity> getExtensions() {
		return extensions;
	}

	public void setExtensions(List<ExtensionEntity> extensions) {
		this.extensions = extensions;
	}
}