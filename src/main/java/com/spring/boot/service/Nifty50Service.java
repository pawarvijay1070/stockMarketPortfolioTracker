package com.spring.boot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.boot.dao.Nifty50Dao;
import com.spring.boot.entities.Nifty50;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.SystemException;
import jakarta.transaction.Transaction;

@Service
public class Nifty50Service {

	@Autowired
	private Nifty50Dao nifty50Dao;
	
	public Nifty50 getByStockCode(String ticker)
	{
		return nifty50Dao.getByStockCode(ticker);
	}
	
	public Nifty50 save(Nifty50 nifty50)
	{
		return nifty50Dao.save(nifty50);
	}
	
	public float getStockPrice(String ticker)
	{
		return nifty50Dao.getStockPrice(ticker);
	}
	
	public List<String> getAllStockName()
	{
		return nifty50Dao.getAllStockName();
	}
}