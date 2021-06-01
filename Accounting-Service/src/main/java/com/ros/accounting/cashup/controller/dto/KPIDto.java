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
public class KPIDto {
	private List<KpiCoverDto> kpiCovers;

	private List<ComplaintDto> complaints;

	private List<BreakDownDto> breakDownDetails;
}
