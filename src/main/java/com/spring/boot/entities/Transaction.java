package com.spring.boot.entities;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String transactionName;
	private int quantity;
	private String transactionType;
	private double price;
	private Date transactionDate;
	
	@ManyToOne
	private User user;
	
}
