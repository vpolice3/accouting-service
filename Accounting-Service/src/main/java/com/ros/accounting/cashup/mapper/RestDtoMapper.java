package com.ros.accounting.cashup.mapper;

import java.util.List;
import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import com.ros.accounting.cashup.controller.dto.BankingInfoDto;
import com.ros.accounting.cashup.controller.dto.CashUpDto;
import com.ros.accounting.cashup.controller.dto.PDQSystemDto;
import com.ros.accounting.cashup.controller.dto.PettyCashDto;
import com.ros.accounting.cashup.controller.dto.ThirdPartyInfoDto;
import com.ros.accounting.cashup.controller.dto.TillSystemDto;
import com.ros.accounting.cashup.model.BankingInfo;
import com.ros.accounting.cashup.model.CashUp;
import com.ros.accounting.cashup.model.PDQSystem;
import com.ros.accounting.cashup.model.PettyCash;
import com.ros.accounting.cashup.model.ThirdPartyInfo;
import com.ros.accounting.cashup.model.TillSystem;

/**
 * 
 * This interface represents the converting entities and dtos
 *
 */
@Mapper
@Component
public interface RestDtoMapper {

	RestDtoMapper mapper = Mappers.getMapper(RestDtoMapper.class);

	@Mapping(target = "note.reason", source = "reason")
	@Mapping(target = "note.reasonAddedBy", source = "reasonAddedBy")
	CashUp convertToCashUpEntity(CashUpDto dto);

	@Mapping(source = "note.reason", target = "reason")
	@Mapping(source = "note.reasonAddedBy", target = "reasonAddedBy")
	CashUpDto convertToCashUpDto(CashUp entity);

	@Mapping(source = "note.reason", target = "reason")
	@Mapping(source = "note.reasonAddedBy", target = "reasonAddedBy")
	void updateCashUp(CashUp entity, @MappingTarget CashUpDto dto);

	void updateCashUp(CashUpDto dto , @MappingTarget CashUp cashUp);

	@Mapping(source = "pettyCashName", target = "pettyCashMaster.name")
	PettyCash convertToPettyCashEntity(PettyCashDto dto);

	@Mapping(source = "name", target = "tillMaster.name")
	TillSystem convertToTillCashEntity(TillSystemDto dto);

	@Mapping(source = "name", target = "pdqMachineMaster.name")
	@Mapping(source = "cardName", target = "pdqCardMaster.name")
	PDQSystem convertToPdqSystem(PDQSystemDto dto);

	@Mapping(source = "name", target = "thirdPartyInfoMaster.name")
	ThirdPartyInfo convertToThirdPartyInfo(ThirdPartyInfoDto dto);

	@Mapping(target = "pettyCashName", source = "pettyCashMaster.name")
	PettyCashDto convertToPettyCashDto(PettyCash entity);

	@Mapping(target = "name", source = "tillMaster.name")
	TillSystemDto convertToTillSystemDto(TillSystem entity);

	@Mapping(target = "name", source = "pdqMachineMaster.name")
	@Mapping(target = "cardName", source = "pdqCardMaster.name")
	PDQSystemDto convertToPdqSystemDto(PDQSystem entity);

	@Mapping(target = "name", source = "thirdPartyInfoMaster.name")
	ThirdPartyInfoDto convertToThirdPartyInfoDto(ThirdPartyInfo entity);

	BankingInfo convertToBankingEntity(BankingInfoDto dto);

	void updateBanking(BankingInfoDto bankingInfoDto, @MappingTarget BankingInfo bankingInfo);

	List<BankingInfoDto> convertToBankingDto(List<BankingInfo> list);

	BankingInfoDto convertToBankingDto(BankingInfo bankingInfo);
	
	BankingInfoDto convertToBankingDto(Optional<BankingInfo> bankingInfo);
	
	CashUpDto convertToCashUpDto(Optional<CashUp> cashUp);

	void getBanking(List<BankingInfoDto> bankingInfoDto, @MappingTarget List<BankingInfo> bankingInfo);

}
