package com.cjon.bank.service;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.ui.Model;

import com.cjon.bank.dao.BankDAO;

public class BankTransferService implements BankService {

	@Override
	public void execute(Model model) {
		HttpServletRequest request = (HttpServletRequest) model.asMap().get("request");
		String sendId = request.getParameter("sendId");
		String receiveId = request.getParameter("receiveId");
		String money = request.getParameter("money");
		
		DataSource dataSource = (DataSource) model.asMap().get("dataSource");
		
		Connection con = null;
		try {
			
			con = dataSource.getConnection();
			con.setAutoCommit(false); // transaction 시작
			
			BankDAO dao = new BankDAO(con);
			boolean result = dao.insertTransfer(sendId, receiveId, money);
		
			if(result){
				con.commit();
			} else {
				con.rollback();
			}

			model.addAttribute("RESULT", result);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try{
				con.close();
			}catch(Exception e){
				
			}
		}

	}

}
