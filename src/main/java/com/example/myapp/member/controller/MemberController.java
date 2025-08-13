package com.example.myapp.member.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.myapp.member.MemberValidator;
import com.example.myapp.member.model.Member;
import com.example.myapp.member.service.IMemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class MemberController {
private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	IMemberService memberService;
	
	@Autowired
	MemberValidator memberValidator;
	
	@InitBinder
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(memberValidator);
	}
	
	@GetMapping("/member/insert")
	public String inserMember(HttpSession session, Model model) {
		String csrfToken = UUID.randomUUID().toString();
		session.setAttribute("csrfToken", csrfToken);
		logger.info("/member/insert, GET", csrfToken);
		model.addAttribute("member", new Member());
		return "member/form";
	}
	
	@PostMapping("/member/insert")
	public String insertMember(@Validated Member member, BindingResult result, String csrfToken, HttpSession session, Model model) {
		if(csrfToken==null || "".equals(csrfToken)){
			throw new RuntimeException("csrf 토큰이 없다.");
		}else if(!csrfToken.equals(session.getAttribute("csrfToken"))) {
			throw new RuntimeException("잘못된 접근이 감지되었습니다");
			
		}try {
			if(!member.getPassword().equals(member.getPassword2())) {
				model.addAttribute("member",member);
				model.addAttribute("member","MEMBER_PW_RE");
				return "member/form";
			}
			memberService.insertMember(member);
		}catch(DuplicateKeyException e){
			member.setUserid(null);
			model.addAttribute("member",member);
			model.addAttribute("message", "ID_ALREADY_EXIST");
			
		}
		session.invalidate();
		return "home";
	}
	
	@GetMapping("/member/login")
	public String login(){
		return "member/login";
	}
	
	@PostMapping("/member/login")
	public String login(String userid, String password, HttpSession session, Model model) {
		Member member = memberService.selectMember(userid);
		if(member !=null) {
			logger.info(member.toString());
			String dbPassword = member.getPassword();
			if(dbPassword.equals(password)){
				session.setMaxInactiveInterval(600);
				session.setAttribute("userid", userid);
				session.setAttribute("name", member.getName());
				session.setAttribute("email", member.getEmail());
			}else {
				session.invalidate();
				model.addAttribute("message","WRONG_PASSWORD");
			}
		}else {
			session.invalidate();
			model.addAttribute("message","USER_NOT_FOUND");
		}
		return "member/login";
	}
	
	@GetMapping("/member/logout")
	public String logout(HttpSession session, HttpServletRequest request) {
		session.invalidate();
		return "home";
	}
	
	@GetMapping("/member/update")
	public String upateMember(HttpSession session, Model model) {
		String userid = (String)session.getAttribute("userid");
		if(userid != null && !userid.equals("")) {
			Member member = memberService.selectMember(userid);
			model.addAttribute("member",member);
			model.addAttribute("message", "UPDATE_USER_INFO");
			return "member/update";
		}else {
			model.addAttribute("message","NOT_LOGIN_USER");
			return "member/login";
		}
	}
	
	@PostMapping("/member/update")
	public String updateMember(@Validated Member member, BindingResult result, HttpSession session, Model model) {
		if(result.hasErrors()) {
			model.addAttribute("member",member);
			return "member/update";
		}
		try {
			memberService.updateMember(member);
			model.addAttribute("message", "UPDATED_MEMBER_INFO");
			model.addAttribute("member",member);
			session.setAttribute("email",member.getEmail());
			return "member/login";
		}catch(Exception e) {
			model.addAttribute("message",e.getMessage());
			e.printStackTrace();
			return "member/error";
		}
	}
	
	@GetMapping("/member/delete")
	public String deleteMember(HttpSession session, Model model) {
		String userid= (String)session.getAttribute("userid");
		if(userid != null && !userid.equals("")) {
			Member member = memberService.selectMember(userid);
			model.addAttribute("member", member);
			model.addAttribute("message","MEMBER_PW_RE");
			return "member/delete";
		}else {
			model.addAttribute("message","NOT_LOGIN_USER");
			return "member/login";
		}
	}
	
	@PostMapping("/member/delete")
	public String deleteMember(String password, HttpSession session, Model model) {
		try {
			Member member = new Member();
			member.setUserid((String)session.getAttribute("userid"));
			String dbpw = memberService.getPassword(member.getUserid());
			if(password != null && password.equals(dbpw)) {
				member.setPassword(password);
				memberService.deleteMember(member);
				session.invalidate();
				return "/member/login";
			}else {
				model.addAttribute("message","WRONG_PASSWORD");
				return "member/delete";
			}
		}catch(Exception e) {
			model.addAttribute("message","DELETE_FAIL");
			e.printStackTrace();
			return "member/delete";
		}
	}
}
