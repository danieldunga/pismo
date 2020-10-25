package io.pismo.test.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import io.pismo.test.entity.Account;

/**
 * Repository 
 * @author danielbraga
 */
@Component
public class PismoRepository {
	
	private static final Map<Integer, Account> DATABASE = new HashMap<Integer, Account>();
	private static int idx = 1;
	
	
	public Account findAccount(Integer accountId) {
		return DATABASE.get(accountId);
	}
	
	public Account findAccountByDocumentNumber(Integer documentNumber) {
		Account account = null;
		for (Map.Entry<Integer, Account> entry : DATABASE.entrySet()) {
			Account accountMap = entry.getValue();
			if (accountMap.getDocumentNumber().equals(documentNumber)) {
	        	account = accountMap;
	        	break;
	        }
		}
		return account;
	}
	
	public Account insert(Account account) {
		if (account.getId() == null) {
			account.setId(idx++);
		}
		DATABASE.put(account.getId(), account);
		return account;
	}
	
	public Account update(Account account) {
		DATABASE.put(account.getId(), account);
		return account;
	}
	
	public void delete(Account account) {
		DATABASE.remove(account.getId());
	}
	

}
