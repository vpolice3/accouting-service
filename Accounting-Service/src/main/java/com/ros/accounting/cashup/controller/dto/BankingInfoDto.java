/**
 * 
 */
package com.ros.accounting.cashup.controller.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author debad
 *
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankingInfoDto {

	private UUID id;

	private Date bankingDate;

	private int giroSlipNumber;

	private float bankingTotal;

	private float bankedTotal;

	private String reason;

	private List<Date> cashUpDates;

	private String sealedBy;

}
