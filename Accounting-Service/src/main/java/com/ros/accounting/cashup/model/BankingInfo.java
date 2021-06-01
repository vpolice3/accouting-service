package com.ros.accounting.cashup.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankingInfo extends BaseEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "banking_info_id")
	private UUID id;
	
	@Temporal(TemporalType.DATE)
	@NotNull(message = "Date is mandatory")
	private Date bankingDate;
	
	@Column
	private int giroSlipNumber;
	
	@Column
	private float bankingTotal;
	
	@Column
	private float bankedTotal;
	
	private String reason;
	
	@Column(nullable = false)
	private String sealedBy;
	
	private long employeeId;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "bankingInfo")
	private List<CashUp> cashUps;
}
