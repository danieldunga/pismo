package io.pismo.test.repository;

import org.springframework.data.repository.CrudRepository;

import io.pismo.test.entity.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

}
