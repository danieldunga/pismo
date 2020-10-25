package io.pismo.test.resource;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.pismo.test.entity.Account;
import io.pismo.test.entity.OperationType;
import io.pismo.test.entity.Transaction;
import io.pismo.test.service.AccountService;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class PismoResourceTest {

	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountService service;
    
    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders
        		.webAppContextSetup(webApplicationContext)
        		.apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
        		.build();
    }
    
    @Test
	public void indexExample() throws Exception {
	    this.mockMvc.perform(MockMvcRequestBuilders.get("/")).andExpect(MockMvcResultMatchers.status().isOk())
	      .andDo(document("index-example", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), links(linkWithRel("crud").description("The CRUD resource")), responseFields(subsectionWithPath("_links").description("Links to other resources")),
	                responseHeaders(headerWithName("Content-Type").description("The Content-Type of the payload, e.g. `application/hal+json`"))));
	}
    
	@Test
	void getAccountNotFound() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/crud/accounts/9999")
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString("")))
		        .andExpect(MockMvcResultMatchers.status().isNotFound())
		        .andDo(MockMvcRestDocumentation.document("account"));
	}
	
	@Test
	void createAccount() throws Exception {
		
		Account createAccount = new Account();
		createAccount.setDocumentNumber(1);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/crud/accounts")
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString(createAccount)))
		        .andExpect(MockMvcResultMatchers.status().isCreated())
		        .andDo(MockMvcRestDocumentation.document("account"));
	}
	
	@Test
	void createAccountFailDocumentNumber() throws Exception {
		
		Account createAccount = new Account();
		createAccount.setDocumentNumber(10);
		service.insert(createAccount);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/crud/accounts")
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString(createAccount)))
		        .andExpect(MockMvcResultMatchers.status().isBadRequest())
		        .andDo(MockMvcRestDocumentation.document("account"));
	}
	
	@Test
	void createAccountFail() throws Exception {
		
		Account account = new Account();
		account.setDocumentNumber(-6789);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/crud/accounts")
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString(account)))
		        .andExpect(MockMvcResultMatchers.status().isBadRequest())
		        .andDo(MockMvcRestDocumentation.document("account"));
	}
	
	@Test
	void getAccount() throws Exception {
		
		Account createAccount = new Account();
		createAccount.setDocumentNumber(2);
		Account account = service.insert(createAccount);
		
		mockMvc.perform(RestDocumentationRequestBuilders.get("/crud/accounts/{accountId}",account.getId())
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString("")))
		        .andExpect(MockMvcResultMatchers.status().isOk())
		        .andDo(document("crud-get-example", 
		        	      pathParameters(
		        	    	        parameterWithName("accountId").description("The id of the input to delete")
		        	    	      )));
	}
	
	@Test
	void createTransaction() throws Exception {
		
		Account createAccount = new Account();
		createAccount.setDocumentNumber(3);
		Account account = service.insert(createAccount);
		
		Transaction transaction = new Transaction();
		transaction.setAccountId(account.getId());
		transaction.setOperationType(OperationType.SAQUE);
		transaction.setAmount(new BigDecimal(10));
		
		mockMvc.perform(MockMvcRequestBuilders.post("/crud/transactions")
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString(transaction)))
		        .andExpect(MockMvcResultMatchers.status().isCreated())
		        .andDo(MockMvcRestDocumentation.document("transaction"));
	}
	
	@Test
	void createTransactionFail() throws Exception {
		
		Account createAccount = new Account();
		createAccount.setDocumentNumber(4);
		Account account = service.insert(createAccount);
		
		Transaction transaction = new Transaction();
		transaction.setAccountId(account.getId());
		transaction.setOperationType(null);
		transaction.setAmount(new BigDecimal(10));
		
		mockMvc.perform(MockMvcRequestBuilders.post("/crud/transactions")
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString(transaction)))
		        .andExpect(MockMvcResultMatchers.status().isBadRequest())
		        .andDo(MockMvcRestDocumentation.document("transaction"));
	}
	
	@Test
	void getOperationType() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/crud/operationType")
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString("")))
		        .andExpect(MockMvcResultMatchers.status().isOk())
		        .andDo(MockMvcRestDocumentation.document("operationType"));
	}
	
	@Test
	void getOperationTypeId() throws Exception {
		String jsonExpect = "{\"2\":\"COMPRA_PARCELADA\"}";
		mockMvc.perform(MockMvcRequestBuilders.get("/crud/operationType/" + OperationType.COMPRA_PARCELADA.getId())
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString("")))
		        .andExpect(MockMvcResultMatchers.status().isOk())
		        .andExpect(MockMvcResultMatchers.content().json(jsonExpect, true))
		        .andDo(MockMvcRestDocumentation.document("operationType"));
	}
	
	@Test
	void getOperationTypeIdNotFound() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/crud/operationType/9999")
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString("")))
		        .andExpect(MockMvcResultMatchers.status().isNotFound())
		        .andDo(MockMvcRestDocumentation.document("operationType"));
	}
	
	

}
