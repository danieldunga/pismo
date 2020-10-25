package io.pismo.test.repository;

import org.springframework.data.repository.CrudRepository;

import io.pismo.test.entity.Account;

public interface AccountRepository extends CrudRepository<Account, Integer> {
	Account findByDocumentNumber(Integer documentNumber);
}
