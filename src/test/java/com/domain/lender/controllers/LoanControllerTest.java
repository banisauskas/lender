package com.domain.lender.controllers;

import static org.hamcrest.collection.IsIterableWithSize.iterableWithSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.domain.lender.dtos.LoanDto;
import com.domain.lender.services.LoanService;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class LoanControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private LoanService loanService;

	@Test
	void getAllLoanIds() throws Exception {
		var loan = new LoanDto();
		loan.setName("John");
		loan.setAmount(new BigDecimal("999.99"));
		loan.setTerm(LocalDate.now().plusDays(100));

		loanService.createLoan(loan, "192.168.0.1");

		mvc.perform(
			get("/loans").contentType(MediaType.APPLICATION_JSON)
		)
		.andExpect(
				status().isOk()
		)
		.andExpect(
				content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
		)
		.andExpect(
				jsonPath("$", iterableWithSize(1))
		)
		.andExpect(
				jsonPath("$[0]", is(1))
		);
	}
}