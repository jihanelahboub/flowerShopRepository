package com.everis.flowershop.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.everis.flowershop.repository.crypt.BCrypt;
import com.everis.flowershop.service.AccountService;
import com.everis.flowershop.service.dto.AccountDTO;

@Controller
public class AccountController {

	@Autowired
	AccountService accountService;
	
	@RequestMapping("/account")
	private String account() {
		return "account";
	}
	
	@GetMapping("/sign")
	private String signup(ModelMap modelMap) {
		modelMap.put("account", new AccountDTO());
		
		return "signup";
	}
	
	@PostMapping("/signup")
	private String signup(@ModelAttribute("accountDTO") AccountDTO accountDTO) {
		accountDTO.setPassword(BCrypt.hashpw(accountDTO.getPassword(), BCrypt.gensalt()));
		accountService.create(accountDTO);
		System.out.println("account created :"+accountService.create(accountDTO));
		return "account";
	}
	
	@RequestMapping("/login")
	private String login(ModelMap modelMap, HttpServletRequest request, HttpSession session) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		AccountDTO accountDTO = accountService.login(username, password);
		if (accountDTO == null) {
			modelMap.put("error", "Invalid Account");
			return "account";
		}else {
			session.setAttribute("username", username);
			return "cart";
		}
		
		
	}
}
