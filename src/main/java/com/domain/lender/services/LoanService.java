package com.domain.lender.services;

import com.domain.lender.dtos.LoanDto;
import com.domain.lender.entities.ExtensionEntity;
import com.domain.lender.entities.LoanEntity;
import com.domain.lender.errors.BadRequest400;
import com.domain.lender.errors.NotFound404;
import com.domain.lender.repositories.LoanId;
import com.domain.lender.repositories.LoanRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LoanService {
	private static final int MAX_NAME_LENGTH = 100;

	@Value("${lender.min-amount}")
	private BigDecimal minAmount;

	@Value("${lender.max-amount}")
	private BigDecimal maxAmount;

	@Value("${lender.min-term}")
	private int minTerm;

	@Value("${lender.max-term}")
	private int maxTerm;

	@Value("${lender.max-loans-ip}")
	private int maxLoansIp;

	@Value("${lender.interest-rate}")
	private BigDecimal interestRate;

	@Value("${lender.extension-factor}")
	private BigDecimal extensionFactor;

	private LocalDate ipDay;

	private final Map<String, Long> ipLoans = new HashMap<>();

	private final LoanRepository repository;

	public LoanService(LoanRepository repository) {
		this.repository = repository;
	}

	public List<Long> getAllLoanIds() {
		return repository.findAllBy().stream()
			.map(LoanId::getId)
			.collect(Collectors.toList());
	}

	public LoanDto getLoan(Long id) {
		LoanEntity entity = repository.findById(id)
			.orElseThrow(() -> new NotFound404("Loan '" + id + "' not found."));

		LoanDto dto = toDto(entity);
		dto.setTerm(calculateFinalTerm(entity));
		dto.setInterestRate(calculateFinalInterestRate(entity.getExtensions().size()));
		return dto;
	}

	private LocalDate calculateFinalTerm(LoanEntity loan) {
		LocalDate term = loan.getTerm();

		for (ExtensionEntity extension : loan.getExtensions()) {
			if (extension.getTerm().isAfter(term)) {
				term = extension.getTerm();
			}
		}

		return term;
	}

	private BigDecimal calculateFinalInterestRate(int extensions) {
		if (extensions == 0) {
			return interestRate;
		}

		return interestRate.multiply(extensionFactor.pow(extensions));
	}

	public LoanDto createLoan(LoanDto loan, HttpServletRequest request) {
		LocalDate today = LocalDate.now();

		validateId(loan.getId());
		validateName(loan.getName());
		validateAmount(loan.getAmount());
		validateTerm(loan.getTerm(), today);
		validateIp(request.getRemoteAddr(), today);

		loan = toDto(repository.save(toEntity(loan)));
		loan.setInterestRate(interestRate);

		increaseIp(request.getRemoteAddr());
		return loan;
	}

	private void validateId(Long id) {
		if (id != null) {
			throw new BadRequest400("Loan 'id' must not be set.");
		}
	}

	private void validateName(String name) {
		if (name == null || name.isBlank()) {
			throw new BadRequest400("Loan 'name' '" + name + "' cannot be empty.");
		}

		if (name.length() > MAX_NAME_LENGTH) {
			throw new BadRequest400("Loan 'name' '" + name + "' cannot exceed '" + MAX_NAME_LENGTH + "' length.");
		}
	}

	private void validateAmount(BigDecimal amount) {
		if (amount == null) {
			throw new BadRequest400("Invalid loan 'amount' value '" + amount + "'.");
		}

		if (amount.compareTo(minAmount) < 0) {
			throw new BadRequest400("Loan 'amount' value '" + amount + "' too small.");
		}

		if (amount.compareTo(maxAmount) > 0) {
			throw new BadRequest400("Loan 'amount' value '" + amount + "' too large.");
		}

		if (amount.compareTo(maxAmount) == 0) {
			int hour = LocalDateTime.now().getHour(); // local (not UTC) hour in 0-23 range

			if (hour >= 0 && hour <= 6) {
				throw new BadRequest400("Max 'amount' value in 0-6 hrs range.");
			}
		}
	}

	private void validateTerm(LocalDate term, LocalDate today) {
		LocalDate dateMin = today.plusDays(minTerm);
		LocalDate dateMax = today.plusDays(maxTerm);

		if (term == null || term.isBefore(dateMin) || term.isAfter(dateMax)) {
			throw new BadRequest400("Invalid loan 'term' value '" + term + "'.");
		}
	}

	private synchronized void validateIp(String ip, LocalDate today) {
		if (ip == null) {
			throw new BadRequest400("Cannot determine client IP address.");
		}

		if (!today.equals(ipDay)) { // equals returns false when null
			ipDay = today;
			ipLoans.clear();
			return;
		}

		long loans = ipLoans.getOrDefault(ip, 0L);

		if (loans == maxLoansIp) {
			throw new BadRequest400("Max loans per IP per day has been reached.");
		}
	}

	private synchronized void increaseIp(String ip) {
		long loans = ipLoans.getOrDefault(ip, 0L);
		ipLoans.put(ip, loans + 1);
	}

	private LoanEntity toEntity(LoanDto dto) {
		var entity = new LoanEntity();
		entity.setName(dto.getName());
		entity.setAmount(dto.getAmount());
		entity.setTerm(dto.getTerm());
		return entity;
	}

	private LoanDto toDto(LoanEntity entity) {
		var dto = new LoanDto();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setAmount(entity.getAmount());
		dto.setTerm(entity.getTerm());
		return dto;
	}
}