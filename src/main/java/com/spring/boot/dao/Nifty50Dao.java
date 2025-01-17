package com.spring.boot.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.spring.boot.entities.Nifty50;

public interface Nifty50Dao extends JpaRepository<Nifty50, String>{
	

	@Query("SELECT n FROM Nifty50 n WHERE n.stockCode = :stockCode")
    Nifty50 getByStockCode(@Param("stockCode") String stockCode);

	@Query(value="select stock_price from nifty50 where stock_code = :ticker", nativeQuery = true)
	float getStockPrice(@Param("ticker") String ticker);
	
	@Query(value="select stock_name from nifty50", nativeQuery = true)
	List<String> getAllStockName();
}
