package com.domain.lender.services;

import com.domain.lender.dtos.ExtensionDto;
import com.domain.lender.entities.ExtensionEntity;
import com.domain.lender.entities.LoanEntity;
import com.domain.lender.errors.BadRequest400;
import com.domain.lender.errors.NotFound404;
import com.domain.lender.repositories.ExtensionRepository;
import com.domain.lender.repositories.LoanRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExtensionService {

	@Value("${lender.extension-length}")
	private int extensionLength;

	private final LoanRepository loanRepo;

	private final ExtensionRepository extensionRepo;

	public ExtensionService(LoanRepository loanRepo, ExtensionRepository extensionRepo) {
		this.loanRepo = loanRepo;
		this.extensionRepo = extensionRepo;
	}

	public ExtensionDto getExtension(Long loanId, Long extId) {
		LoanEntity entity = loanRepo.findById(loanId)
			.orElseThrow(() -> new NotFound404("Loan '" + loanId + "' not found."));

		return entity.getExtensions().stream()
			.filter(ext -> ext.getId().equals(extId))
			.map(this::toDto)
			.findFirst()
			.orElseThrow(() -> new NotFound404("Extension '" + extId + "' in loan '" + loanId + "'  not found."));
	}

	public List<ExtensionDto> getAllExtensions(Long loanId) {
		LoanEntity entity = loanRepo.findById(loanId)
			.orElseThrow(() -> new NotFound404("Loan '" + loanId + "' not found."));

		return entity.getExtensions().stream()
			.map(this::toDto)
			.collect(Collectors.toList());
	}

	@Transactional
	public ExtensionDto createExtension(Long loanId, ExtensionDto extension) {
		if (extension.getId() != null) {
			throw new BadRequest400("Extension 'id' must not be set.");
		}

		LoanEntity loan = loanRepo.findById(loanId)
			.orElseThrow(() -> new NotFound404("Loan '" + loanId + "' not found."));
		
		LocalDate newFinalTerm = calculateNewFinalTerm(loan);

		if (extension.getTerm() == null) {
			extension.setTerm(newFinalTerm);
		}
		else if (!extension.getTerm().equals(newFinalTerm)) {
			throw new BadRequest400("Incorrect extension 'term' value '" + extension.getTerm() + "' (expected '" + newFinalTerm + "').");
		}

		return toDto(extensionRepo.save(toEntity(extension, loan)));
	}

	private LocalDate calculateNewFinalTerm(LoanEntity loan) {
		LocalDate term = loan.getTerm();

		for (ExtensionEntity extension : loan.getExtensions()) {
			if (extension.getTerm().isAfter(term)) {
				term = extension.getTerm();
			}
		}

		return term.plusDays(extensionLength);
	}

	private ExtensionEntity toEntity(ExtensionDto dto, LoanEntity loan) {
		var entity = new ExtensionEntity();
		entity.setLoan(loan);
		entity.setTerm(dto.getTerm());
		return entity;
	}

	private ExtensionDto toDto(ExtensionEntity entity) {
		var dto = new ExtensionDto();
		dto.setId(entity.getId());
		dto.setTerm(entity.getTerm());
		return dto;
	}
}