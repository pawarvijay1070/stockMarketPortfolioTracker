package com.spring.boot.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Nifty50 {

	private String stockName;
	
	@Id
	private String stockCode;
	private float stockPrice;
	
	
	public String getStockName() {
		return stockName;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	public String getStockCode() {
		return stockCode;
	}
	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}
	public float getStockPrice() {
		return stockPrice;
	}
	public void setStockPrice(float stockPrice) {
		this.stockPrice = stockPrice;
	}
	@Override
	public String toString() {
		return "Nifty50 [stockName=" + stockName + ", stockCode=" + stockCode + ", stockPrice=" + stockPrice + "]";
	}
	
	
}
