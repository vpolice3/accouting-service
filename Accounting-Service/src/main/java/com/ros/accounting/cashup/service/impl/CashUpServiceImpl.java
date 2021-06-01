package com.ros.accounting.cashup.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.ros.accounting.cashup.controller.dto.BankingInfoDto;
import com.ros.accounting.cashup.controller.dto.CashUpDto;
import com.ros.accounting.cashup.controller.dto.PDQSystemDto;
import com.ros.accounting.cashup.controller.dto.TillSystemDto;
import com.ros.accounting.cashup.controller.dto.PettyCashDto;
import com.ros.accounting.cashup.controller.dto.ThirdPartyInfoDto;
import com.ros.accounting.cashup.exceptions.BankNotFoundException;
import com.ros.accounting.cashup.exceptions.cashUpNotFoundException;
import com.ros.accounting.cashup.mapper.RestDtoMapper;
import com.ros.accounting.cashup.model.BankingInfo;
import com.ros.accounting.cashup.model.CashUp;
import com.ros.accounting.cashup.model.CashUpInfoMode;
import com.ros.accounting.cashup.model.PDQSystem;
import com.ros.accounting.cashup.model.CashUpStatus;
import com.ros.accounting.cashup.model.CashUpTimeIndicator;
import com.ros.accounting.cashup.model.TillSystem;
import com.ros.accounting.cashup.model.PettyCash;
import com.ros.accounting.cashup.model.ThirdPartyInfo;
import com.ros.accounting.cashup.repository.BankingInfoRepository;
import com.ros.accounting.cashup.repository.CashUpRepository;
import com.ros.accounting.cashup.service.CashUpService;
import com.ros.accounting.log.RosLogDebug;
import com.ros.accounting.util.Properties;
import com.thoughtworks.xstream.mapper.Mapper;

@Service
public class CashUpServiceImpl implements CashUpService {

	@Autowired
	private CashUpRepository cashUpRepo;

	@Autowired
	private BankingInfoRepository bankingInfoRepo;

	@Autowired
	private RestDtoMapper restDtoMapper;

	/**
	 * Adding cashup to the database
	 */
	@Override
	@RosLogDebug
	public CashUpDto addNewCashUp(CashUpDto cashUp) throws cashUpNotFoundException {
		List<CashUp> cashUps = checkCashUpByDate(cashUp.getCashUpDate());
		int amCount = 0, pmCount = 0;
		for (CashUp data : cashUps) {
			if (data.getCashUpTimeIndicator().equals(CashUpTimeIndicator.AM)) {
				amCount++;
			} else if (data.getCashUpTimeIndicator().equals(CashUpTimeIndicator.PM)) {
				pmCount++;
			}
		}
		if (amCount < 1 && cashUp.getCashUpTimeIndicator().equals(CashUpTimeIndicator.AM)
				|| pmCount < 1 && cashUp.getCashUpTimeIndicator().equals(CashUpTimeIndicator.PM)) {
			if (cashUps.size() < 2) {
				return saveCashUpData(cashUp);
			} else
				throw new cashUpNotFoundException(Properties.cashUpCreationNotPossible);
		} else
			throw new cashUpNotFoundException(Properties.cashUpCreationNotPossible);
	}

	private CashUpDto saveCashUpData(CashUpDto cashUp) {
		CashUp cashUpEntity = restDtoMapper.convertToCashUpEntity(cashUp);
		cashUpEntity.addMetaData();
		updateManualEntry(cashUpEntity);
//		updateEntityData(cashUp, cashUpEntity);
		return restDtoMapper.convertToCashUpDto(cashUpRepo.save(cashUpEntity));
	}

	private void updateManualEntry(CashUp cashUpEntity) {
		cashUpEntity.getSales().forEach(sales -> {
			sales.setCashUpInfoMode(CashUpInfoMode.MANUAL);
		});
		cashUpEntity.getCashnPdq().setCashUpInfoMode(CashUpInfoMode.MANUAL);
		cashUpEntity.getThirdPartyInfo().forEach(info -> {
			info.setCashUpInfoMode(CashUpInfoMode.MANUAL);
		});
	}

	private List<CashUp> checkCashUpByDate(Date cashUpDate) {
		return cashUpRepo.checkCashUpByDate(cashUpDate);
	}

//	private void updateEntityData(CashUpDto cashUp, CashUp cashUpEntity) {
//		for (PettyCashDto pettyCashDto : cashUp.getCashnPdq().getPettyCashs()) {
//			PettyCash pettyCash = restDtoMapper.convertToPettyCashEntity(pettyCashDto);
//			pettyCash.getPettyCashMaster().setName(pettyCashDto.getPettyCashName());
//		}
//		for (TillSystemDto tillDto : cashUp.getCashnPdq().getTillSystems()) {
//			TillSystem till = restDtoMapper.convertToTillCashEntity(tillDto);
//			till.getTillMaster().setName(tillDto.getName());
//		}
//		for (PDQSystemDto pdqDto : cashUp.getCashnPdq().getPdqSystems()) {
//			PDQSystem pdq = restDtoMapper.convertToPdqSystem(pdqDto);
//			pdq.getPdqMachineMaster().setName(pdqDto.getName());
//			pdq.getPdqCardMaster().setName(pdqDto.getCardName());
//		}
//		for (ThirdPartyInfoDto thirdPartyInfoDto : cashUp.getThirdPartyInfo()) {
//			ThirdPartyInfo thirdParty = restDtoMapper.convertToThirdPartyInfo(thirdPartyInfoDto);
//			thirdParty.getThirdPartyInfoMaster().setName(thirdPartyInfoDto.getName());
//		}
//	}

	@Override
	@RosLogDebug
	public CashUpDto getCashUpById(UUID id) throws cashUpNotFoundException {
		Optional<CashUp> cashUpFromDB = cashUpRepo.findById(id);
//		CashUp cashUp = cashUpFromDB.orElseThrow(() -> new cashUpNotFoundException(Properties.cashUpNotFound)); 
		if (cashUpFromDB.isPresent()) {
			restDtoMapper.convertToCashUpDto(cashUpFromDB);
			return restDtoMapper.convertToCashUpDto(cashUpRepo.getOne(id));

		} else
			throw new cashUpNotFoundException(Properties.cashUpNotFound);

	}

//	@Override
//	@RosLogDebug
//	@Transactional
//	public Object editCashUp(CashUpDto dto) throws cashUpNotFoundException {
//		CashUp cashUp = cashUpRepo.getOne(dto.getId());
////		CashUp cashUp = cashUpFromDB.orElseThrow(() -> new cashUpNotFoundException(Properties.cashUpNotFound));
//		if (cashUp.getCashUpStatus() != CashUpStatus.PUBLISHED || cashUp.getCashUpStatus() != CashUpStatus.BANKED) {
//			restDtoMapper.updateCashUp(dto);
////			CashUp cashUpEntity = restDtoMapper.updateCashUp(dto);
//			cashUp.editMetaData();
//			System.out.println(cashUp.toString());
////			updateEntityData(dto, cashUp);
//			return cashUpRepo.saveAndFlush(cashUp);
//		}
//		return Properties.cashUpEditedException;
//	}

	@Override
	@RosLogDebug
	public CashUpDto editCashUp(CashUpDto dto) throws cashUpNotFoundException {
		CashUp cashUp = cashUpRepo.getOne(dto.getId());

		if (cashUp.getCashUpStatus() == CashUpStatus.DRAFT) {

			restDtoMapper.updateCashUp(dto, cashUp);

			return restDtoMapper.convertToCashUpDto(cashUpRepo.save(cashUp));
		} else
			throw new cashUpNotFoundException(Properties.cashUpCreationNotPossible);
	}

	@Override
	@RosLogDebug
	public String deleteCashUp(UUID id) throws cashUpNotFoundException {
		Optional<CashUp> cashUpFromDB = cashUpRepo.findById(id);
		if (cashUpFromDB.get().getCashUpStatus() != CashUpStatus.PUBLISHED) {
			CashUp cashUp = cashUpFromDB.orElseThrow(() -> new cashUpNotFoundException(Properties.cashUpNotFound));
			cashUpRepo.delete(cashUp);
			return Properties.cashUpDeleted;
		} else
			return Properties.cashUpDeleteException;
	}

	@Override
	@RosLogDebug
	public List<?> getCashUpSheets(CashUpStatus status, Date fromDate, Date toDate) throws cashUpNotFoundException {
		List<CashUp> cashUpFromDB = new ArrayList<>();
		List<CashUpDto> cashUpDtos = new ArrayList<>();
		List<BankingInfo> cashBankingInfos = new ArrayList<>();
		List<BankingInfoDto> bankingDtos = new ArrayList<>();
		List<Date> cashUpDates = new ArrayList<>();
		if (status.equals(CashUpStatus.DRAFT)) {
			cashUpFromDB = cashUpRepo.findCashUpSheets(status, fromDate, toDate);
			cashUpFromDB.forEach(data -> {
				cashUpDtos.add(restDtoMapper.convertToCashUpDto(data));
			});
			return cashUpDtos;
		} else if (status.equals(CashUpStatus.PUBLISHED)) {
			cashUpFromDB = cashUpRepo.findCashUpSheets(status, fromDate, toDate);
			cashUpDtos.addAll(checkPendingDeposits(cashUpFromDB, fromDate, toDate));
			return cashUpDtos;
		} else if (status.equals(CashUpStatus.BANKED)) {
			cashUpFromDB = cashUpRepo.findCashUpSheets(status, fromDate, toDate);
			cashBankingInfos.addAll(getBankingData(cashUpFromDB));
			cashBankingInfos.forEach(data -> {
				data.getCashUps().forEach(cashUp -> {
					cashUpDates.add(cashUp.getCashUpDate());
				});
				bankingDtos.add(restDtoMapper.convertToBankingDto(data));
			});
			for (BankingInfoDto bankingInfoDto : bankingDtos) {
				bankingInfoDto.setCashUpDates(cashUpDates);
			}
			return bankingDtos;
		} else
			throw new cashUpNotFoundException(Properties.cashUpNotFound);
	}

	private List<BankingInfo> getBankingData(List<CashUp> cashUpFromDB) {
		List<BankingInfo> bankingInfo = new ArrayList<>();
		for (CashUp cashUp : cashUpFromDB) {
			bankingInfo.add(cashUp.getBankingInfo());
		}
		return bankingInfo;
	}

	private List<CashUpDto> checkPendingDeposits(List<CashUp> cashUpFromDB, Date fromDate, Date toDate) {
		float tillAmount = 0;
		List<CashUpDto> cashUpDtos = new ArrayList<>();
		for (CashUp data : cashUpFromDB) {
			for (TillSystem till : data.getCashnPdq().getTillSystems()) {
				tillAmount += till.getAmount();
				if (tillAmount != 0) {
					cashUpDtos.add(restDtoMapper.convertToCashUpDto(data));
				} else
					cashUpDtos.add(restDtoMapper.convertToCashUpDto(data));
			}
//			if (data.getCashUpDate() == fromDate || data.getCashUpDate() == toDate) {
//				cashUps.add(data);
//			}

		}
//		for (CashUp cashUp : cashUpFromDB) {
//			if (cashUp.getCashUpDate() == fromDate || cashUp.getCashUpDate() == toDate) {
//				cashUps.add(cashUp);
//			}
//		}
//		for (CashUp data : cashUps) {
//			if (data.getCashUpDate() == fromDate) {
//				count++;
//			} else if (count <= 2) {
//				
//			}
//		}
		return cashUpDtos;
	}

	/*
	 * Deposit services
	 */
	@Override
	public BankingInfoDto CreateBanking(BankingInfoDto bankingDto) throws cashUpNotFoundException {
		BankingInfo bankingEntity = restDtoMapper.convertToBankingEntity(bankingDto);
		List<Date> cashUpDates = new ArrayList<>();
		bankingEntity.addMetaData();
		BankingInfo bankingInfo = updateBankingInfo(bankingDto, bankingEntity);
		for (CashUp date : bankingInfo.getCashUps()) {
			cashUpDates.add(date.getCashUpDate());
		}
		BankingInfoDto bankingInfoDto = restDtoMapper.convertToBankingDto(bankingInfoRepo.save(bankingInfo));
		bankingInfoDto.setCashUpDates(cashUpDates);
		return bankingInfoDto;
	}

	private BankingInfo updateBankingInfo(BankingInfoDto banking, BankingInfo bankingEntity)
			throws cashUpNotFoundException {
		List<CashUp> cashUps = new ArrayList<>();
		banking.getCashUpDates().forEach(date -> {
			cashUps.addAll(cashUpRepo.checkCashUpByDate(date));
		});
		if (cashUps.size() != 0) {
			for (CashUp cashUp : cashUps) {
				if (cashUp.getCashUpStatus() == CashUpStatus.DRAFT || cashUp.getCashUpStatus() == CashUpStatus.BANKED)
					throw new cashUpNotFoundException(Properties.bankingNotCreated);
				else if (cashUp.getCashUpStatus() == CashUpStatus.PUBLISHED) {
					cashUp.setCashUpStatus(CashUpStatus.BANKED);
					cashUp.setBankingInfo(bankingEntity);
					cashUpRepo.save(cashUp);
				}
			}
			bankingEntity.setCashUps(cashUps);
			return bankingEntity;
		} else
			throw new cashUpNotFoundException(Properties.bankingNotCreated);
	}

	@Override
	public String DeleteBankingById(UUID id) {
		Optional<BankingInfo> banking = bankingInfoRepo.findById(id);
		if (banking.isPresent()) {
			bankingInfoRepo.deleteById(id);
			return Properties.bankingDeleted;
		} else
			return Properties.notFindTheRequestedBank;

	}

	@Override
	public BankingInfoDto getBankById(UUID id) throws cashUpNotFoundException {
		Optional<BankingInfo> bankingInfo = bankingInfoRepo.findById(id);

		if (bankingInfo.isPresent()) {
			restDtoMapper.convertToBankingDto(bankingInfo);
			return restDtoMapper.convertToBankingDto(bankingInfoRepo.getOne(id));

		} else
			throw new cashUpNotFoundException(Properties.bankingNotFound);
	}

	@Override
	@RosLogDebug
	public BankingInfoDto updateBanking(BankingInfoDto dto) throws cashUpNotFoundException {
		BankingInfo bankingInfo = bankingInfoRepo.getOne(dto.getId());
		if (bankingInfo != null) {

			restDtoMapper.updateBanking(dto, bankingInfo);

			return restDtoMapper.convertToBankingDto(bankingInfoRepo.save(bankingInfo));
		} else
			throw new cashUpNotFoundException(Properties.bankingNotCreated);
	}

	@Override
	@RosLogDebug
	public List<BankingInfoDto> getAllBanks() throws cashUpNotFoundException {
		List<BankingInfo> bankingInfos = bankingInfoRepo.findAll();
		List<BankingInfoDto> bankingInfoDtos = new ArrayList<>();

		if (bankingInfos.size() != 0) {
			restDtoMapper.getBanking(bankingInfoDtos, bankingInfos);

			return restDtoMapper.convertToBankingDto(bankingInfoRepo.findAll());
		} else
			throw new cashUpNotFoundException(Properties.bankingNotFound);

	}

	@Override
	public List<CashUpDto> getAllCashUpSheets() throws cashUpNotFoundException {
		List<CashUp> cashUps = cashUpRepo.findAll();
		List<CashUpDto> cashUpDtos = new ArrayList<>();
		if (cashUps.size() != 0) {
			cashUps.forEach(data -> {
				cashUpDtos.add(restDtoMapper.convertToCashUpDto(data));
			});
			return cashUpDtos;
		} else
			throw new cashUpNotFoundException(Properties.cashUpNotFound);
	}
}