package com.domain.lender.repositories;

import com.domain.lender.entities.LoanEntity;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends CrudRepository<LoanEntity, Long> {

	List<LoanId> findAllBy();

	long countByCreatedIpAndCreatedDate(String createdIp, LocalDate createdDate);
}