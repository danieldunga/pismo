package io.pismo.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.pismo.test.entity.Account;
import io.pismo.test.entity.OperationType;
import io.pismo.test.entity.Transaction;
import io.pismo.test.exception.BusinessException;
import io.pismo.test.exception.NotFoundException;
import io.pismo.test.repository.TransactionRepository;
import io.pismo.test.util.DateUtil;

/**
 * TransactionService Service class
 * @author danielbraga
 */
@Service
public class TransactionService {

	@Autowired
	private TransactionRepository repository;
	
	@Autowired
	private AccountService accountService;
	
	public Transaction insertTransaction(Transaction transaction) throws BusinessException, NotFoundException {
		
		if (transaction.getOperationType() != OperationType.PAGAMENTO) {
			transaction.setAmount(transaction.getAmount().negate());
		}
		// Check if account exists
		Account account = accountService.get(transaction.getAccountId());
		transaction.setEventDate(DateUtil.getDate());
		
		accountService.availableCreditLimit(account, transaction.getAmount());
		
		return repository.save(transaction);
		
	}
}
