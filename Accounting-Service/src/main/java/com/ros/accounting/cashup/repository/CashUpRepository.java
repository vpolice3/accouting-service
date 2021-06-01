package com.ros.accounting.cashup.repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ros.accounting.cashup.model.CashUp;
import com.ros.accounting.cashup.model.CashUpStatus;

@Repository
public interface CashUpRepository extends JpaRepository<CashUp, UUID> {
	@Query(value = "select c from CashUp c where c.cashUpStatus =:status and"
			+ " c.cashUpDate between :fromDate and :toDate")
	List<CashUp> findCashUpSheets(@Param("status") CashUpStatus status, @Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate);

	@Query(value = "select c from CashUp c where c.cashUpDate =:cashUpDate")
	List<CashUp> checkCashUpByDate(@Param("cashUpDate") Date cashUpDate);
	
	
}
