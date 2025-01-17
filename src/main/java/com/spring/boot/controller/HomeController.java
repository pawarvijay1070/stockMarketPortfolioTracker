package com.spring.boot.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.boot.entities.Stock;
import com.spring.boot.entities.Transaction;
import com.spring.boot.entities.User;
import com.spring.boot.helper.Message;
import com.spring.boot.service.UserService;

import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Controller
public class HomeController {

	@Autowired
	private BCryptPasswordEncoder passwordEcoder;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/")
	public String home(Model model)
	{
		model.addAttribute("title", "Home - PortfolioTracker");
		return "home";
	}
	
	@GetMapping("/about")
	public String about(Model model)
	{
		model.addAttribute("title", "About - PortfolioTracker");
		return "about";
	}
	
	@GetMapping("/signin")
	public String login(Model model)
	{
		model.addAttribute("title", "Login - PortfolioTracker");
		return "login";
	}
	
	@GetMapping("/signup")
	public String signup(Model model, HttpSession session)
	{
		if (session.getAttribute("message") != null)
		{
			session.removeAttribute("message");
		}

		model.addAttribute("title", "Register - PortfolioTracker");
		model.addAttribute("user", new User());
		return "signup";
	}
	
	@GetMapping("doRegister")
	public String doRegisterGet()
	{
		return "redirect:/signup";
	}
	
	
	@PostMapping("/doRegister")
	public String doRegister(@Valid @ModelAttribute("user") User user, BindingResult result, @RequestParam(value="agreement", defaultValue = "false") boolean agreement, Model model, HttpSession session, HttpServletResponse response)
	{
		try
		{
			if(!agreement)
			{
				throw new Exception("Accept terms and condition");
			}
			
			if (result.hasErrors())
			{
				//System.out.println(result.getAllErrors());
				model.addAttribute("user", user);
				return "signup";
			}
			
			user.setEnabled(true);
			user.setRole("ROLE_USER");
			user.setImageUrl("Default.png");
			user.setPassword(passwordEcoder.encode(user.getPassword()));
			user.setAvalableBalance(0);
			userService.addUser(user);			
			
			
			model.addAttribute("user", new User());
			session.setAttribute("message", new Message("Registered succesfully", "alert-success"));
			
			//response.sendRedirect("signup");
			return "/signup";
		}
		catch (Exception e)
		{
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message(e.getMessage(), "alert-danger"));
			return "/signup";
		}
		

	}
}
