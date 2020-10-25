package io.pismo.test.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Transaction POJO
 * @author danielbraga
 */
@Entity
public class Transaction implements Comparable<Transaction>{
	
	@JsonProperty(value = "transaction_id")
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@JsonProperty(value = "account_id")
	@NotNull(message = "account_id cannot be null")
	private Integer accountId;
	
	@JsonProperty(value = "operation_type_id")
	@NotNull(message = "operation_type_id cannot be null")
	private OperationType operationType;
	
	@JsonProperty(value = "amount")
	@NotNull(message = "amount cannot be null")
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
