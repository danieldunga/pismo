package io.pismo.test.resource;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.pismo.test.entity.Account;
import io.pismo.test.entity.OperationType;
import io.pismo.test.entity.Transaction;
import io.pismo.test.service.AccountService;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/snippets")
class PismoResourceTest {

	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountService service;
    
	@Test
	void getAccountNotFound() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/accounts/9999")
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString("")))
		        .andExpect(MockMvcResultMatchers.status().isNotFound())
		        .andDo(MockMvcRestDocumentation.document("account"));
	}
	
	@Test
	void createAccount() throws Exception {
		
		Account createAccount = new Account();
		createAccount.setDocumentNumber(1);
		createAccount.setAvailableCreditLimit(new BigDecimal(100));
		
		mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString(createAccount)))
		        .andExpect(MockMvcResultMatchers.status().isCreated())
		        .andDo(MockMvcRestDocumentation.document("account"));
	}
	
	@Test
	void createAccountFailDocumentNumber() throws Exception {
		
		Account createAccount = new Account();
		createAccount.setDocumentNumber(10);
		createAccount.setAvailableCreditLimit(new BigDecimal(100));
		service.insert(createAccount);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString(createAccount)))
		        .andExpect(MockMvcResultMatchers.status().isBadRequest())
		        .andDo(MockMvcRestDocumentation.document("account"));
	}
	
	@Test
	void createAccountFail() throws Exception {
		
		Account account = new Account();
		account.setDocumentNumber(-6789);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString(account)))
		        .andExpect(MockMvcResultMatchers.status().isBadRequest())
		        .andDo(MockMvcRestDocumentation.document("account"));
	}
	
	@Test
	void getAccount() throws Exception {
		
		Account createAccount = new Account();
		createAccount.setDocumentNumber(2);
		createAccount.setAvailableCreditLimit(new BigDecimal(100));
		Account account = service.insert(createAccount);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/accounts/" + account.getId())
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString("")))
		        .andExpect(MockMvcResultMatchers.status().isOk())
		        .andDo(MockMvcRestDocumentation.document("account"));
	}
	
	@Test
	void createTransaction() throws Exception {
		
		Account createAccount = new Account();
		createAccount.setDocumentNumber(3);
		createAccount.setAvailableCreditLimit(new BigDecimal(100));
		Account account = service.insert(createAccount);
		
		Transaction transaction = new Transaction();
		transaction.setAccountId(account.getId());
		transaction.setOperationType(OperationType.SAQUE);
		transaction.setAmount(new BigDecimal(10));
		
		mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString(transaction)))
		        .andExpect(MockMvcResultMatchers.status().isCreated())
		        .andDo(MockMvcRestDocumentation.document("transaction"));
	}
	
	@Test
	void createTransactionFail() throws Exception {
		
		Account createAccount = new Account();
		createAccount.setDocumentNumber(4);
		createAccount.setAvailableCreditLimit(new BigDecimal(100));
		Account account = service.insert(createAccount);
		
		Transaction transaction = new Transaction();
		transaction.setAccountId(account.getId());
		transaction.setOperationType(null);
		transaction.setAmount(new BigDecimal(10));
		
		mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString(transaction)))
		        .andExpect(MockMvcResultMatchers.status().isBadRequest())
		        .andDo(MockMvcRestDocumentation.document("transaction"));
	}
	
	@Test
	void createTransactionFailAvailableCreditLimit() throws Exception {
		
		Account createAccount = new Account();
		createAccount.setDocumentNumber(7);
		createAccount.setAvailableCreditLimit(new BigDecimal(5));
		Account account = service.insert(createAccount);
		
		Transaction transaction = new Transaction();
		transaction.setAccountId(account.getId());
		transaction.setOperationType(OperationType.COMPRA_PARCELADA);
		transaction.setAmount(new BigDecimal(10));
		
		mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString(transaction)))
		        .andExpect(MockMvcResultMatchers.status().isBadRequest())
		        .andDo(MockMvcRestDocumentation.document("transaction"));
	}
	
	@Test
	void createTransactionFailAmountNegative() throws Exception {
		
		Account createAccount = new Account();
		createAccount.setDocumentNumber(8);
		createAccount.setAvailableCreditLimit(new BigDecimal(5));
		Account account = service.insert(createAccount);
		
		Transaction transaction = new Transaction();
		transaction.setAccountId(account.getId());
		transaction.setOperationType(OperationType.COMPRA_PARCELADA);
		transaction.setAmount(new BigDecimal(-10));
		
		mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString(transaction)))
		        .andExpect(MockMvcResultMatchers.status().isBadRequest())
		        .andDo(MockMvcRestDocumentation.document("transaction"));
	}
	
	@Test
	void getOperationType() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/operationType")
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString("")))
		        .andExpect(MockMvcResultMatchers.status().isOk())
		        .andDo(MockMvcRestDocumentation.document("operationType"));
	}
	
	@Test
	void getOperationTypeId() throws Exception {
		String jsonExpect = "{\"2\":\"COMPRA_PARCELADA\"}";
		mockMvc.perform(MockMvcRequestBuilders.get("/operationType/" + OperationType.COMPRA_PARCELADA.getId())
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString("")))
		        .andExpect(MockMvcResultMatchers.status().isOk())
		        .andExpect(MockMvcResultMatchers.content().json(jsonExpect, true))
		        .andDo(MockMvcRestDocumentation.document("operationType"));
	}
	
	@Test
	void getOperationTypeIdNotFound() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/operationType/9999")
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString("")))
		        .andExpect(MockMvcResultMatchers.status().isNotFound())
		        .andDo(MockMvcRestDocumentation.document("operationType"));
	}

}
