package com.cjon.bank.controller;

import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cjon.bank.dto.BankDTO;
import com.cjon.bank.dto.BankStatementDTO;
import com.cjon.bank.service.BankCheckMemberService;
import com.cjon.bank.service.BankDepositService;
import com.cjon.bank.service.BankSearchMemberService;
import com.cjon.bank.service.BankSelectAllMemberService;
import com.cjon.bank.service.BankService;
import com.cjon.bank.service.BankTransferService;
import com.cjon.bank.service.BankWithdrawService;

@Controller
public class BankController {

	private BankService service;
	private DataSource dataSource;
	
	@Autowired
	public void setDataSorce(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	// 이 프로젝트는 view로 JSP를 이용하지 않는다.
	// 만약 JSP를 이용한다면, String을 return 해야하고,
	// JSON을 결과값으로 사용한다면, void를 반환한다.
	// 또한, 클라이언트로부터 callback값을 받아야 한다.
	@RequestMapping(value = "/selectAllMember", method = RequestMethod.GET)
	public void selectAllMember(HttpServletRequest request, HttpServletResponse response, Model model) {
		String callback = request.getParameter("callback");

		// 로직처리
		service = new BankSelectAllMemberService();
		model.addAttribute("dataSource", dataSource);
		service.execute(model);

		// 결과처리: model에서 결과를 꺼내 사용.
		
		ArrayList<BankDTO> list = (ArrayList<BankDTO>) model.asMap().get("RESULT");

		String json = null;
		ObjectMapper om = new ObjectMapper();
		try {
			json = om.defaultPrettyPrintingWriter().writeValueAsString(list);

			response.setContentType("text/plain; charset=utf8");
			response.getWriter().println(callback + "(" + json + ")");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/searchMember")
	public void searchMember(HttpServletRequest request, HttpServletResponse response, Model model){
		String callback = request.getParameter("callback");
		String memberId = request.getParameter("memberId");
		
		service = new BankSearchMemberService();
		model.addAttribute("dataSource", dataSource);
		model.addAttribute("memberId", memberId);
		service.execute(model);
		
		ArrayList<BankDTO> list = (ArrayList<BankDTO>) model.asMap().get("RESULT");

		String json = null;
		ObjectMapper om = new ObjectMapper();
		try {
			json = om.defaultPrettyPrintingWriter().writeValueAsString(list);

			response.setContentType("text/plain; charset=utf8");
			response.getWriter().println(callback + "(" + json + ")");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	@RequestMapping("/deposit")
	public void deposit(HttpServletRequest request, HttpServletResponse response, Model model){
		String callback = request.getParameter("callback");
		
		service = new BankDepositService();
		model.addAttribute("dataSource", dataSource);
		model.addAttribute("request", request);
		service.execute(model);
	
		boolean result = (Boolean) model.asMap().get("RESULT");
	
		try {
			response.setContentType("text/plain; charset=utf8");
			PrintWriter out = response.getWriter();
			out.println(callback + "(" + result + ")");
			out.flush();
			out.close();
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@RequestMapping("/withdraw")
	public void withdraw(HttpServletRequest request, HttpServletResponse response, Model model){
		String callback = request.getParameter("callback");
		
		service = new BankWithdrawService();
		model.addAttribute("dataSource", dataSource);
		model.addAttribute("request", request);
		service.execute(model);
		
		boolean result = (Boolean) model.asMap().get("RESULT");
		
		try {
			response.setContentType("text/plain; charset=utf8");
			PrintWriter out = response.getWriter();
			out.println(callback + "(" + result + ")");
			out.flush();
			out.close();
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/transfer")
	public void transfer(HttpServletRequest request, HttpServletResponse response, Model model){
		String callback = request.getParameter("callback");
		
		service = new BankTransferService();
		model.addAttribute("dataSource", dataSource);
		model.addAttribute("request", request);
		service.execute(model);
		
		boolean result = (Boolean) model.asMap().get("RESULT");
		
		try {
			response.setContentType("text/plain; charset=utf8");
			PrintWriter out = response.getWriter();
			out.println(callback + "(" + result + ")");
			out.flush();
			out.close();
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/checkMember")
	public void checkMember(HttpServletRequest request, HttpServletResponse response, Model model){
		String callback = request.getParameter("callback");
		String memberId = request.getParameter("memberId");
		
		service = new BankCheckMemberService();
		model.addAttribute("dataSource", dataSource);
		model.addAttribute("memberId", memberId);
		service.execute(model);
		
		ArrayList<BankStatementDTO> list = (ArrayList<BankStatementDTO>) model.asMap().get("RESULT");

		String json = null;
		ObjectMapper om = new ObjectMapper();
		try {
			json = om.defaultPrettyPrintingWriter().writeValueAsString(list);

			response.setContentType("text/plain; charset=utf8");
			response.getWriter().println(callback + "(" + json + ")");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
