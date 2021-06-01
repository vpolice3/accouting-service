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
public class PettyCashDto {
	
	private float amount;

	private List<DocumentDto> documents;
	
	private String pettyCashName;
}
