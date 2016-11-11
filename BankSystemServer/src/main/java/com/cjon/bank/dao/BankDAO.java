package com.cjon.bank.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.cjon.bank.dto.BankDTO;
import com.cjon.bank.dto.BankStatementDTO;

public class BankDAO {

	private Connection con;

	public BankDAO(Connection con) {
		this.con = con;
	}

	public ArrayList<BankDTO> selectAll() {

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<BankDTO> list = new ArrayList<BankDTO>();

		try {

			String sql = "select * from bank_member_tb";
			pstmt = con.prepareStatement(sql);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				BankDTO dto = new BankDTO();
				dto.setMemberId(rs.getString("member_id"));
				dto.setMemberName(rs.getString("member_name"));
				dto.setMemberAccount(rs.getString("member_account"));
				dto.setMemberBalance(rs.getInt("member_balance"));
				list.add(dto);
			}

		} catch (Exception e) {

		} finally {

			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;

	}

	public ArrayList<BankDTO> select(String memberId) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<BankDTO> list = new ArrayList<BankDTO>();

		try {

			String sql = "select * from bank_member_tb where member_id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, memberId);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				BankDTO dto = new BankDTO();
				dto.setMemberId(rs.getString("member_id"));
				dto.setMemberName(rs.getString("member_name"));
				dto.setMemberAccount(rs.getString("member_account"));
				dto.setMemberBalance(rs.getInt("member_balance"));
				list.add(dto);
			}

		} catch (Exception e) {

		} finally {

			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return list;
	}

	public boolean updateDeposit(String memberId, String memberBalance) {

		PreparedStatement pstmt = null;
		boolean result = false;

		try {

			String sql = "update bank_member_tb set member_balance = member_balance+? where member_id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(memberBalance));
			pstmt.setString(2, memberId);

			int count = pstmt.executeUpdate();

			if (count == 1) {
				result = true;
				
				String sql2 = "insert into bank_statement_tb(member_id, kind, money) "
						+ "value(?, ?, ?)";
				PreparedStatement pstmt2 = con.prepareStatement(sql2);
				pstmt2.setString(1, memberId);
				pstmt2.setString(2, "입금");
				pstmt2.setInt(3, Integer.parseInt(memberBalance));
				
				if(pstmt2.executeUpdate() == 1){
					System.out.println("state table insert success!");
				} else{
					System.out.println("state table insert failed!");
				}
				
				pstmt2.close();
				
			} else {
				result = false;
			}

		} catch (Exception e) {

		} finally {

			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return result;
	}

	public boolean updateWithdraw(String memberId, String memberBalance) {
		
		PreparedStatement pstmt = null;
		boolean result = false;
		
		try {
						
			String sql = "update bank_member_tb set member_balance = member_balance-? where member_id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(memberBalance));
			pstmt.setString(2, memberId);
			
			int count = pstmt.executeUpdate();
			
			if(count == 1){
				String sql1 = "select member_id, member_balance from bank_member_tb where member_id=?";
				PreparedStatement pstmt1 = con.prepareStatement(sql1);
				pstmt1.setString(1, memberId);	// IN Parameter 처리
				ResultSet rs = pstmt1.executeQuery();
				
				if(rs.next()){
					if(rs.getInt("member_balance") < 0){
						System.out.println("예금금액이 작아서 출금할 수 없습니다.");
						result = false;
					} else{
						result = true;
						
						String sql2 = "insert into bank_statement_tb(member_id, kind, money) "
								+ "value(?, ?, ?)";
						PreparedStatement pstmt2 = con.prepareStatement(sql2);
						pstmt2.setString(1, memberId);
						pstmt2.setString(2, "출금");
						pstmt2.setInt(3, Integer.parseInt(memberBalance));
						
						if(pstmt2.executeUpdate() == 1){
							System.out.println("state table insert2 success!");
						} else{
							System.out.println("state table insert2 failed!");
						}
						
						pstmt2.close();
					}
				}
				
				try{
					rs.close();
					pstmt1.close();
				} catch(Exception e){
					e.printStackTrace();
				}

			} else{
				result = false;
			}
			
		} catch(Exception e){
			
		} finally {
			
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return result;
	}

	public ArrayList<BankStatementDTO> selectStatement(String memberId) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<BankStatementDTO> list = new ArrayList<BankStatementDTO>();

		try {

			String sql = "select * from bank_statement_tb where member_id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, memberId);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				BankStatementDTO dto = new BankStatementDTO();
				dto.setMemberId(rs.getString("member_id"));
				dto.setKind(rs.getString("kind"));
				dto.setMoney(rs.getInt("money"));
				list.add(dto);
			}

		} catch (Exception e) {

		} finally {

			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return list;
	}

	public boolean insertTransfer(String sendId, String receiveId, String money) {
		PreparedStatement pstmt = null;
		boolean result = false;
		
		try {
						
			String sql = "update bank_member_tb set member_balance = member_balance-? where member_id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(money));
			pstmt.setString(2, sendId);
			
			int count = pstmt.executeUpdate();
			
			if(count == 1){
				String sql1 = "select member_id, member_balance from bank_member_tb where member_id=?";
				PreparedStatement pstmt1 = con.prepareStatement(sql1);
				pstmt1.setString(1, sendId);	// IN Parameter 처리
				ResultSet rs = pstmt1.executeQuery();
				
				if(rs.next()){
					if(rs.getInt("member_balance") < 0){
						System.out.println("예금금액이 작아서 출금할 수 없습니다.");
						result = false;
					} else{
						System.out.println("이체가능!");
						result = true;
						
						String sql2 = "update bank_member_tb set member_balance = member_balance+? where member_id = ?";
						PreparedStatement pstmt2 = con.prepareStatement(sql2);
						pstmt2.setInt(1, Integer.parseInt(money));
						pstmt2.setString(2, receiveId);

						int count2 = pstmt2.executeUpdate();

						if (count2 == 1) {
							result = true;
							
							String sql3 = "insert into bank_statement_tb(member_id, kind, money) "
									+ "value(?, ?, ?)";
							PreparedStatement pstmt3 = con.prepareStatement(sql3);
							pstmt3.setString(1, receiveId);
							pstmt3.setString(2, "이체_입금");
							pstmt3.setInt(3, Integer.parseInt(money));
							
							String sql4 = "insert into bank_statement_tb(member_id, kind, money) "
									+ "value(?, ?, ?)";
							PreparedStatement pstmt4 = con.prepareStatement(sql4);
							pstmt4.setString(1, sendId);
							pstmt4.setString(2, "이체_출금");
							pstmt4.setInt(3, Integer.parseInt(money));
							
							if(pstmt3.executeUpdate() == 1 && pstmt4.executeUpdate() == 1){
								System.out.println("state table transfer success!");
								
								String sqlTransfer = "insert into bank_transfer_history_tb (send_member_id, receive_member_id, transfer_money) "
									+ "value(?, ?, ?)";
								PreparedStatement pstmt5 = con.prepareStatement(sqlTransfer);
								pstmt5.setString(1, sendId);
								pstmt5.setString(2, receiveId);
								pstmt5.setInt(3, Integer.parseInt(money));
								
								
								if(pstmt5.executeUpdate() == 1){
									System.out.println("이체 히스토리 테이블 삽입 성공");
								} else {
									System.out.println("이체 히스토리 테이블 삽입 실패");
								}
								
								pstmt5.close();
							} else{
								System.out.println("state table transfer failed!");
							}
							
							pstmt4.close();
							pstmt3.close();
							pstmt2.close();
							
						} else {
							result = false;
						}


					}
				}
				
				try{
					rs.close();
					pstmt1.close();
				} catch(Exception e){
					e.printStackTrace();
				}

			} else{
				result = false;
			}
			
		} catch(Exception e){
			
		} finally {
			
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return result;
	}

}
