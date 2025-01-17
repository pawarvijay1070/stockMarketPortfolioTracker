package com.spring.boot.entities;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Stock {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String stockName;
	private String stockId;
	private float stockPrice;
	private int quantity;
	private Date purchaseDate;
	
	@ManyToOne
	private User user;

	
	
	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public String getStockName() {
		return stockName;
	}



	public void setStockName(String stockName) {
		this.stockName = stockName;
	}



	public String getStockId() {
		return stockId;
	}



	public void setStockId(String stockId) {
		this.stockId = stockId;
	}



	public float getStockPrice() {
		return stockPrice;
	}



	public void setStockPrice(float stockPrice) {
		this.stockPrice = stockPrice;
	}



	public int getQuantity() {
		return quantity;
	}



	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}



	public Date getPurchaseDate() {
		return purchaseDate;
	}



	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}



	public User getUser() {
		return user;
	}



	public void setUser(User user) {
		this.user = user;
	}



	@Override
	public String toString() {
		return "Stock [id=" + id + ", stockName=" + stockName + ", stockPrice=" + stockPrice + ", quantity=" + quantity
				+ ", purchaseDate=" + purchaseDate + ", user=" + user + "]";
	}
	
	
}
