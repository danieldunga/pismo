package io.pismo.test.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Accounts POJO
 * @author danielbraga
 */
@JsonPropertyOrder(alphabetic=true)
@Entity
public class Account {
	
	@JsonProperty(value = "account_id")
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@JsonProperty(value = "document_number")
	@DecimalMin(value = "1", message = "document_number must be greater than zero")
	@NotNull(message = "document_number cannot be null")
	private Integer documentNumber;
	
	@JsonInclude(Include.NON_EMPTY)
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "accountId")
	private List<Transaction> transactions = new ArrayList<Transaction>();
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(Integer documentNumber) {
		this.documentNumber = documentNumber;
	}

	public List<Transaction> getTransactions() {
		Collections.reverse(transactions);
		return transactions;
	}

	public void setTransaction(Transaction transaction) {
		transactions.add(transaction);
	}
	
	
}
