package com.ros.accounting.cashup.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
//import com.ros.accounting.reconciliation.model.ReconciliationLog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Mohith This class represents the main entity of cashup
 *
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CashUp extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "cash_up_id", length = 16)
	private UUID id;

	@Temporal(value = TemporalType.DATE)
	private Date cashUpDate;
	@Enumerated(EnumType.STRING)
	private CashUpTimeIndicator cashUpTimeIndicator;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cashup_Note_id")
	private CashUpNote note;

	@Enumerated(value = EnumType.STRING)
	private CashUpStatus cashUpStatus;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "cash_up_id")
	private List<Sales> sales;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cash_n_pdq_id")
	private CashnPDQ cashnPdq;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "cash_up_id")
	private List<ThirdPartyInfo> thirdPartyInfo;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "kpi_id")
	private KPI kpi;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "safe_summary_id")
	private SafeSummary safeSummary;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "banking_info_id")
	private BankingInfo bankingInfo;

	private UUID restaurantId;
	
//	@OneToOne(cascade = CascadeType.ALL)
//	@JoinColumn(name = "reconciliation_id")
//	private ReconciliationLog reconciliationLog;

}
