package com.ros.accounting.cashup.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WageAdvanceDto {
	private float amount;

	private long employeeId;
}
