package io.pismo.test.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.pismo.test.entity.Account;
import io.pismo.test.entity.Transaction;
import io.pismo.test.exception.BusinessException;
import io.pismo.test.exception.NotFoundException;
import io.pismo.test.util.DateUtil;

/**
 * TransactionService Service class
 * @author danielbraga
 */
@Service
public class TransactionService {

	@Autowired
	private AccountService accountService;
	
	private static int transactionId = 1;
	
	public Transaction insertTransaction(Transaction transaction) throws BusinessException, NotFoundException {
		
		if (transaction.getAmount().equals(BigDecimal.ZERO)) {
			throw new BusinessException("amount cannot be zero");
		}
		Account account = accountService.get(transaction.getAccountId());
		transaction.setEventDate(DateUtil.getDate());
		transaction.setId(transactionId++);
		
		account.setTransaction(transaction);
		
		accountService.update(account);
		
		return transaction;
	}
}
