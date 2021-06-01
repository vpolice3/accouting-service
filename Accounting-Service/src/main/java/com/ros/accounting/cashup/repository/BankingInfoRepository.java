/**
 * 
 */
package com.ros.accounting.cashup.repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ros.accounting.cashup.model.BankingInfo;
import com.ros.accounting.cashup.model.CashUp;
import com.ros.accounting.cashup.model.CashUpStatus;

/**
 * @author debad
 *
 */
@Repository
public interface BankingInfoRepository extends JpaRepository<BankingInfo, UUID>{
	
	@Query(value = "select b.bankingTotal,b.bankedTotal from BankingInfo b")
	List<BankingInfo> findVarience();
}
