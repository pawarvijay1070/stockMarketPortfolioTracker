package com.spring.boot.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.boot.entities.Stock;

@Repository
public interface StockDao extends JpaRepository<Stock, Integer>{

	
	@Query(value = "select DISTINCT stock_id From Stock Where user_id = :userId", nativeQuery = true)
	Page<String> uniqueStockList(@Param("userId") int userId, Pageable pageable);
	
	@Query(value = "select * From Stock Where user_id = :userId AND stock_id = :stock", nativeQuery = true)
	ArrayList<Stock> getStockHolding(@Param("userId") int userId, @Param("stock") String stock);
}
