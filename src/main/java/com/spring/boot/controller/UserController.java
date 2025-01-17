package com.spring.boot.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.spring.boot.entities.Stock;
import com.spring.boot.entities.User;
import com.spring.boot.helper.Message;
import com.spring.boot.service.Nifty50Service;
import com.spring.boot.service.StockService;
import com.spring.boot.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;



@RequestMapping("/user")
@Controller
public class UserController {

	@Autowired
	private Nifty50Service nifty50Service;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private StockService stockService;
	
	@ModelAttribute
	public void addCommonData(Model model, Principal principal)
	{
		String email = principal.getName();
		User user = userService.getUserByEmail(email);
		model.addAttribute("user", user);
	}
	
	@GetMapping("/profile")
	public String profile(Model model)
	{
		model.addAttribute("title", "User Profile");
		return "user/profile";
	}


	@PostMapping("/editUser")
	public String editUser(Model model)
	{
		model.addAttribute("title", "Update Profile");
		return "user/updateProfile";
	}
	
	@PostMapping("/processEditUser")
	public String processEditUser(@Valid @ModelAttribute("user") User user, BindingResult result, @RequestParam("contactImage") MultipartFile file, Model model, Principal principal, HttpSession session)
	{
		try
		{
			
			if(result.hasErrors())
			{
				model.addAttribute("user", user);
				return "user/updateProfile";
			}
			
			if(file.isEmpty())
			{
				user.setImageUrl(user.getImageUrl());
			}
			else
			{
				File saveFile = new ClassPathResource("static/img").getFile();
				
				String imgName = "";
				if (user.getImageUrl().equals("Default.png"))
				{
					System.out.println("Profile photo changed from default photo");
					String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
					imgName = "contact_"+ UUID.randomUUID().toString() + "_" + System.currentTimeMillis() + extension;
					user.setImageUrl(imgName);
					
				}
				else
				{
					System.out.println("Profile photo was changed");
					imgName = user.getImageUrl();
				}
				
				Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+imgName);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				
			}
	
			this.userService.addUser(user);
			session.setAttribute("message", new Message("Profile updated succesfully", "alert-success"));
			
			//return "normal/processContact";
			return "redirect:/user/profile";
		}
		catch (Exception e)
		{
			model.addAttribute("user", user);
			session.setAttribute("message", new Message(e.getMessage(), "alert-danger"));
			return "user/updateProfile";
			
		}
	}

	@GetMapping("/portfolio/{page}")
	public String contacts(@PathVariable("page") int page, Model model, Principal principal, HttpSession session)
	{
		if (session.getAttribute("message") != null)
		{
			model.addAttribute("message", session.getAttribute("message"));
			session.removeAttribute("message");
		}
		
		model.addAttribute("title", "Portfolio");
		
		String email = principal.getName();
		User user = userService.getUserByEmail(email);

		Pageable pageable = PageRequest.of(page, 5);
		Page<String> stockList = stockService.uniqueStockList(user.getId(), pageable);
		double portfolioInvestedAmount = 0;
		double portfolioCurrentAmount = 0;
		DecimalFormat df = new DecimalFormat("#.#");
		
		List<HashMap<String, String>> portfolioList = new ArrayList<>();
		for(String stock:stockList)
		{
			HashMap<String, String> portfolioMap = new HashMap<>();
			
			portfolioMap.put("stockName", stock);
			
			float averageStockPrice= 0;
			int stockCount = 0;
			double stockInvestedAmount = 0;
			double stockCurrentAmount = 0;
			float profitLoss = 0;
			
			float stockLTP = nifty50Service.getStockPrice(stock);
			
			ArrayList<Stock> stockHolding = stockService.getStockHolding(user.getId(), stock);
			for(Stock s : stockHolding)
			{
				stockCount += s.getQuantity();
				stockInvestedAmount += s.getStockPrice() * s.getQuantity();
				portfolioInvestedAmount += s.getStockPrice() * s.getQuantity();
				portfolioCurrentAmount += stockLTP * s.getQuantity();
			}
			stockCurrentAmount = stockLTP * stockCount;
			averageStockPrice = (float) (stockInvestedAmount/stockCount);
			profitLoss = (float) ((stockCurrentAmount - stockInvestedAmount)/stockInvestedAmount*100);
			

			portfolioMap.put("stockInvestedAmount", String.valueOf(df.format(stockInvestedAmount)));
			portfolioMap.put("stockCurrentAmount", String.valueOf(df.format(stockCurrentAmount)));
			portfolioMap.put("stockCount", String.valueOf(stockCount));
			portfolioMap.put("averageStockPrice", String.valueOf(df.format(averageStockPrice)));
			portfolioMap.put("profitLoss", String.valueOf(df.format(profitLoss)));
			portfolioMap.put("stockLTP", String.valueOf(stockLTP));
			
			portfolioList.add(portfolioMap);
		}
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", stockList.getTotalPages());

		model.addAttribute("portfolioList", portfolioList);
		
		return "user/portfolio";
	}
	
	@GetMapping("/addFund")
	public String addFund(HttpSession session)
	{
		if (session.getAttribute("message") != null)
		{
			session.removeAttribute("message");
		}
		return "user/addFund";
	}
	
	@PostMapping("/addFund")
	public String processAddedFund(@RequestParam("addFund") String addFund, @RequestParam("withdrawFund") String withdrawFund, Principal principal, HttpSession session)
	{
		if (session.getAttribute("message") != null)
		{
			session.removeAttribute("message");
		}
		try
		{
			String email = principal.getName();
			User user = userService.getUserByEmail(email);
			long availableFund = user.getAvalableBalance();
	
			if(!addFund.equals(""))
			{
				availableFund += Long.parseLong(addFund);
				session.setAttribute("message", new Message("Fund added successfully.", "alert-success"));
			}
			if(!withdrawFund.equals(""))
			{
				if(Long.parseLong(withdrawFund) > availableFund)
					throw new Exception("Cannot withdraw amount greater than available fund.");
				availableFund -= Long.parseLong(withdrawFund);
				session.setAttribute("message", new Message("Fund withdrawal request accessped", "alert-success"));
			}
			
			user.setAvalableBalance(availableFund);
			userService.addUser(user);
			return "user/addFund";
		}
		catch (Exception e) {
			session.setAttribute("message", new Message(e.getMessage(), "alert-danger"));
			return "user/addFund";
		}
	}
	
	@GetMapping("/buysell")
	public String buySell(Model model)
	{
		List<String> allStocks = nifty50Service.getAllStockName();
		
		model.addAttribute("allStocks", allStocks);
		return "user/buysell";
	}
	
	@PostMapping("/processBuySell")
	@ResponseBody
	public String processBuySell(@RequestParam("stockOptionList.value") String stockOptionList)
	{
		System.out.println(stockOptionList);
		return "DEMO";
	}
	
	
}
