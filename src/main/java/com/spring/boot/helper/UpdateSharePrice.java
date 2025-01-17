package com.spring.boot.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.spring.boot.entities.Nifty50;
import com.spring.boot.service.Nifty50Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@Component
public class UpdateSharePrice {

	@Autowired
	private Nifty50Service nifty50Service;

    @Scheduled(fixedRate = 60000) // Schedule every 5 seconds
    public void updateNifty50() 
    {
    	String[] nifty50array = {"ADANIENT", "ADANIPORTS", "APOLLOHOSP", "ASIANPAINT", "AXISBANK", "BAJAJ-AUTO", "BAJAJFINSV", "BAJFINANCE", "BEL", "BHARTIARTL", "BPCL", "BRITANNIA", "CIPLA", "COALINDIA", "DRREDDY", "EICHERMOT", "GRASIM", "HCLTECH", "HDFCBANK", "HDFCLIFE", "HEROMOTOCO", "HINDALCO", "HINDUNILVR", "ICICIBANK", "INDUSINDBK", "INFY", "ITC", "JSWSTEEL", "KOTAKBANK", "LT", "M&M", "MARUTI", "NESTLEIND", "NTPC", "ONGC", "POWERGRID", "RELIANCE", "SBILIFE", "SBIN", "SHRIRAMFIN", "SUNPHARMA", "TATACONSUM", "TATAMOTORS", "TATASTEEL", "TCS", "TECHM", "TITAN", "TRENT", "ULTRACEMCO", "WIPRO"};
    	List<String> nifty50 = Arrays.asList(nifty50array);
        for(String nifty : nifty50)
        {
        	String url = "https://finance.yahoo.com/quote/"+nifty+".NS/";
        	Thread thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
		        	getPrice(url, nifty);
				}
			});
        	thread.start();
        }
    }
    
    public void getPrice(String url, String ticker)
    {
        try 
        {
            Document doc = Jsoup.connect(url).get();
            Elements sections = doc.select("section[data-testid='quote-price']");
            
            for(Element section : sections)
            {
            	Element priceDiv = section.selectFirst("div").selectFirst("section").selectFirst("div").selectFirst("div");
            	String strPrice = priceDiv.text().split(" ")[0].replace(",", "");
            	float price = Float.parseFloat(strPrice);
            	updatePriceInDatabase(ticker, price);
            }

        }
        catch (IOException e) 
        {
        	System.out.println(e.getMessage());
        }
    	
    }
    
    public synchronized void updatePriceInDatabase(String ticker, float price)
    {
    	//System.out.println(ticker + " -> "+ price);
    	Nifty50 nifty50 = nifty50Service.getByStockCode(ticker);
    	
    	if(nifty50 != null)
    	{
	    	nifty50.setStockPrice(price);
	    	Nifty50 newNifty50 = nifty50Service.save(nifty50);
    	}
    	
    }
}