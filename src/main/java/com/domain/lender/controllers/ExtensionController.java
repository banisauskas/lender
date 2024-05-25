package com.domain.lender.controllers;

import com.domain.lender.dtos.ExtensionDto;
import com.domain.lender.services.ExtensionService;
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
@RequestMapping("/loans/{loanId}/extensions")
public class ExtensionController {
	private final ExtensionService service;

	public ExtensionController(ExtensionService service) {
		this.service = service;
	}

	@GetMapping("/{extId}")
	public ExtensionDto getExtension(@PathVariable Long loanId, @PathVariable Long extId) {
		return service.getExtension(loanId, extId);
	}

	@GetMapping
	public List<ExtensionDto> getAllExtensions(@PathVariable Long loanId) {
		return service.getAllExtensions(loanId);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ExtensionDto createExtension(@PathVariable Long loanId, @RequestBody ExtensionDto extension) {
		return service.createExtension(loanId, extension);
	}
}
