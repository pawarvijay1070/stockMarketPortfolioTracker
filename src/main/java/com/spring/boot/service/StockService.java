package com.spring.boot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.spring.boot.dao.StockDao;
import com.spring.boot.entities.Stock;

@Service
public class StockService {

	@Autowired
	private StockDao stockDao;
	
	
	public Page<String> uniqueStockList(int userId, Pageable pageable)
	{
		return stockDao.uniqueStockList(userId, pageable);
	}
	
	public ArrayList<Stock> getStockHolding(int userId, String stock)
	{
		return stockDao.getStockHolding(userId, stock);
	}
}
