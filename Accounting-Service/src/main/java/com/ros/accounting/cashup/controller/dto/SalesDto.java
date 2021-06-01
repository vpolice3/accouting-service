package com.ros.accounting.cashup.controller.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalesDto {
	private float foodPayment;

	private float drinksPayment;

	private float takeAwayPayment;

	private float otherPayment;

	private float serviceCharges;

	private float creditCardTip;

	private List<TaxInfoDto> taxInfo;
}
