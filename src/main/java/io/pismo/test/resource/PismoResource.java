package io.pismo.test.resource;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.pismo.test.entity.Account;
import io.pismo.test.entity.OperationType;
import io.pismo.test.entity.Transaction;
import io.pismo.test.exception.BusinessException;
import io.pismo.test.exception.NotFoundException;
import io.pismo.test.service.AccountService;
import io.pismo.test.service.TransactionService;
import io.pismo.test.util.GenericResponse;
import io.pismo.test.util.ValidationResponse;

/**
 * REST Resources
 * @author danielbraga
 */
@RestController
public class PismoResource {
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private TransactionService transactionService;
	
	private static final Logger logger = LogManager.getLogger();
	
	@GetMapping("/accounts/{accountId}")
	public ResponseEntity<?> getAccount(@Valid @PathVariable Integer accountId) throws NotFoundException {
		Account account = accountService.get(accountId);
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(account);		
	}
	
	@PostMapping("/accounts")
	public ResponseEntity<?> postAccount(@Valid @RequestBody Account account) throws BusinessException {
		Account newAccount = accountService.insert(account);
		return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(newAccount);		
	}

	@PostMapping("/transactions")
	public ResponseEntity<?> postTransactions(@Valid @RequestBody Transaction transaction) throws BusinessException, NotFoundException {
		Transaction newTransaction = transactionService.insertTransaction(transaction);
		return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(newTransaction);
	}
	
	@GetMapping("/operationType")
	public ResponseEntity<?> getOperationType() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (OperationType type : OperationType.values()) { 
			map.put(type.getId(), type.name());
		}
		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(map);
	}
	
	@GetMapping("/operationType/{id}")
	public ResponseEntity<?> getOperationTypeId(@PathVariable Integer id) throws NotFoundException {
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (OperationType type : OperationType.values()) { 
			if (id == type.getId()) {				
				map.put(type.getId(), type.name());
			}
		}
		if (map.isEmpty()) {
			throw new NotFoundException("Operation type does not exists");
		}
		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(map);
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ValidationResponse handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		ValidationResponse response = new ValidationResponse();
	    ex.getBindingResult().getAllErrors().forEach((error) -> {
	        response.add(error.getDefaultMessage());
	    });
	    return response;
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NotFoundException.class)
	public GenericResponse handleNotFoundException(NotFoundException e) {
		logger.info(e.getMessage());
		return new GenericResponse(e.getMessage());
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BusinessException.class)
	public GenericResponse handleBusinessException(BusinessException e) {
		logger.info(e.getMessage());
		return new GenericResponse(e.getMessage());
	}
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ValidationResponse handleExceptions(Exception e) {
		logger.error(e);
		ValidationResponse response = new ValidationResponse();
		response.add(e.getMessage());
	    return response;
	}
}
