package io.pismo.test.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.pismo.test.entity.Account;
import io.pismo.test.exception.BusinessException;
import io.pismo.test.exception.NotFoundException;
import io.pismo.test.repository.AccountRepository;

/**
 * Account Service class
 * @author danielbraga
 */
@Service
public class AccountService {
	
	@Autowired
	private AccountRepository accountRepository;
	
	
	public Account get(Integer accountId) throws NotFoundException {
		Optional<Account>  account = accountRepository.findById(accountId);
		if (account.isEmpty()) {
			throw new NotFoundException("Account not found");
		}
		return account.orElseThrow();
	}
	
	public Account insert(Account account) throws BusinessException {
		if (account.getId() != null) {
			if (accountRepository.findById(account.getId()) != null) {
				throw new BusinessException("Account already exists");
			}
		}
		
		Account existAccount = accountRepository.findByDocumentNumber(account.getDocumentNumber());
		if (existAccount != null) {
			throw new BusinessException("Account already exists for this document. Accound ID: " + existAccount.getId());
		}
		return accountRepository.save(account);
	}
	
	public void availableCreditLimit(Account account, BigDecimal amount) throws BusinessException {
		BigDecimal newValue = account.getAvailableCreditLimit().add(amount);
		if (newValue.compareTo(BigDecimal.ZERO) < 0) {
			throw new BusinessException("Available Credit Limit exceeded - Limit: " + account.getAvailableCreditLimit());
		}
		account.setAvailableCreditLimit(newValue);
		accountRepository.save(account);
	}
	
}
