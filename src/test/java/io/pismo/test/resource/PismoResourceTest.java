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
		
		Account account = new Account();
		account.setDocumentNumber(6789);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString(account)))
		        .andExpect(MockMvcResultMatchers.status().isCreated())
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
		Account account = new Account();
		account.setDocumentNumber(123);
		
		Account newAccount = service.insert(account);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/accounts/" + newAccount.getId())
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString("")))
		        .andExpect(MockMvcResultMatchers.status().isOk())
		        .andDo(MockMvcRestDocumentation.document("account"));
	}
	
	@Test
	void createTransaction() throws Exception {
		Account account = new Account();
		account.setDocumentNumber(123);
		Account newAccount = service.insert(account);
		
		Transaction transaction = new Transaction();
		transaction.setAccountId(newAccount.getId());
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
		Account account = new Account();
		account.setDocumentNumber(123);
		Account newAccount = service.insert(account);
		
		Transaction transaction = new Transaction();
		transaction.setAccountId(newAccount.getId());
		transaction.setOperationType(OperationType.SAQUE);
		transaction.setAmount(new BigDecimal(0));
		
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
