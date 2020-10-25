package io.pismo.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.pismo.test.entity.Account;
import io.pismo.test.exception.BusinessException;
import io.pismo.test.exception.NotFoundException;
import io.pismo.test.repository.PismoRepository;

/**
 * Account Service class
 * @author danielbraga
 */
@Service
public class AccountService {
	
	@Autowired
	private PismoRepository repository;
	
	
	public Account get(Integer accountId) throws NotFoundException {
		Account account = repository.findAccount(accountId);
		if (account == null) {
			throw new NotFoundException("Account not found");
		}
		return account;
	}
	
	public Account insert(Account account) throws BusinessException {
		if (account.getId() != null) {
			if (repository.findAccount(account.getId()) != null) {
				throw new BusinessException("Account already exists");
			}
		}
		Account existAccount = repository.findAccountByDocumentNumber(account.getDocumentNumber());
		if (existAccount != null) {
			throw new BusinessException("Account already exists for this document. Accound ID: " + existAccount.getId());
		}
		return repository.insert(account);
	}
	
	public Account update(Account account) throws NotFoundException {
		// check for account
		this.get(account.getId());
		
		return repository.update(account);
	}
}
