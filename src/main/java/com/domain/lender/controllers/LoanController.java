package com.domain.lender.controllers;

import com.domain.lender.dtos.LoanDto;
import com.domain.lender.services.LoanService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loans")
public class LoanController {
	private final LoanService service;

	public LoanController(LoanService service) {
		this.service = service;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public LoanDto createLoan(@RequestBody LoanDto loan, HttpServletRequest request) {
		return service.createLoan(loan, request);
	}

	@GetMapping
	public List<Long> getAllLoanIds() {
		return service.getAllLoanIds();
	}

	@GetMapping("/{id}")
	public LoanDto getLoan(@PathVariable("id") Long id) {
		return service.getLoan(id);
	}
}