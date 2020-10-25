package io.pismo.test.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Transaction POJO
 * @author danielbraga
 */
public class Transaction implements Comparable<Transaction>{
	
	@JsonProperty(value = "transaction_id")
	private Integer id;
	
	@JsonProperty(value = "account_id")
	@NotNull(message = "account_id cannot be null")
	private Integer accountId;
	
	@JsonProperty(value = "operation_type_id")
	@NotNull(message = "operation_type_id cannot be null")
	private OperationType operationType;
	
	@JsonProperty(value = "amount")
	@NotNull(message = "amount cannot be null")
	@Min(value = (long)0.01, message = "amount must be greater than zero")
	private BigDecimal amount;
	
	private LocalDateTime eventDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public OperationType getOperationType() {
		return operationType;
	}

	public void setOperationType(OperationType operationType) {
		this.operationType = operationType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public LocalDateTime getEventDate() {
		return eventDate;
	}

	public void setEventDate(LocalDateTime eventDate) {
		this.eventDate = eventDate;
	}

	@Override
	public int compareTo(Transaction t) {
		if (getEventDate() == null || t.getEventDate() == null) {
			return 0;
		}
		return getEventDate().compareTo(t.getEventDate());
	}
	
	
}
