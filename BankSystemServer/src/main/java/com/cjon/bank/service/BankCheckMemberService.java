package com.cjon.bank.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.springframework.ui.Model;

import com.cjon.bank.dao.BankDAO;
import com.cjon.bank.dto.BankStatementDTO;

public class BankCheckMemberService implements BankService {

	@Override
	public void execute(Model model) {
		String memberId = (String) model.asMap().get("memberId");
		
		DataSource dataSource = (DataSource) model.asMap().get("dataSource");
		Connection con;
		try {
			
			con = dataSource.getConnection();
			con.setAutoCommit(false); // transaction 시작

			BankDAO dao = new BankDAO(con);
			ArrayList<BankStatementDTO> list = dao.selectStatement(memberId);
			if (list != null) {
				con.commit();
			} else {
				con.rollback();
			}

			model.addAttribute("RESULT", list);
			con.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
