package com.domain.lender.repositories;

import com.domain.lender.entities.ExtensionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtensionRepository extends CrudRepository<ExtensionEntity, Long> {
}