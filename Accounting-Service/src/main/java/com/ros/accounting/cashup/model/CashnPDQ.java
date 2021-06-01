package com.ros.accounting.cashup.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * This entity represents for the cash & PDQ
 *
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CashnPDQ implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "cash_n_pdq_id",length = 16)
	private UUID id;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "cash_n_pdq_id")
	private List<PettyCash> pettyCashs;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "cash_n_pdq_id")
	private List<TillSystem> tillSystems;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "cash_n_pdq_id")
	private List<PDQSystem> pdqSystems;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "cash_n_pdq_id")
	private List<WageAdvance> wageAdvances;
	
	@Enumerated(EnumType.STRING)
	private CashUpInfoMode cashUpInfoMode;
}
