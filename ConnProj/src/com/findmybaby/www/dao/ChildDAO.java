package com.findmybaby.www.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.findmybaby.www.dto.ChildDTO;

public class ChildDAO {
	String sql;
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;
	final String TABLE_CHILD = "child";
	
	//String url="jdbc:mysql://gitfastlane.cafe24.com/gitfastlane";
	//String username="gitfastlane";
	//String password="Narcissus93!";
	
	String driver = "com.mysql.cj.jdbc.Driver";
	String url="jdbc:mysql://localhost:3306/findmybaby?characterEncoding=utf-8";
	String username="root";
	String password="1234";
	public ChildDAO() {
		//Context init;
		try {
			//init = new InitialContext();
			//DataSource ds = (DataSource)init.lookup("java:comp/env/findmybaby");
			//conn = ds.getConnection();
			Class.forName(driver);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Connection getConnectioin() {
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
	
	//추가 파트
	public ArrayList<ChildDTO> selectAll(){
		conn = getConnectioin();
		ArrayList<ChildDTO> list = new ArrayList<>();
		sql = "select * from "+TABLE_CHILD;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				ChildDTO dto = new ChildDTO();
				dto.setC_id(rs.getString("c_id"));
				dto.setC_name(rs.getString("c_name"));
				dto.setC_phone(rs.getString("c_phone"));
				list.add(dto);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close();
		}
		return list;
	}
	
	public Boolean insertChild(ChildDTO dto) {
		conn = getConnectioin();
		int result = 0;
		Boolean falge = false;
		sql = "insert into "+TABLE_CHILD+" values(?,?,?,?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getC_id());
			pstmt.setString(2, dto.getC_pw());
			pstmt.setString(3, dto.getC_phone());
			pstmt.setString(4, dto.getC_name());
			result = pstmt.executeUpdate();
			if(result>0) falge = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close();
			return falge;
		}
	}
	
	public ChildDTO loginChild(ChildDTO dto) {
		conn = getConnectioin();
		ChildDTO res = null;
		sql = "select * from "+TABLE_CHILD+" where c_id = ? and c_pw = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getC_id());
			pstmt.setString(2, dto.getC_pw());
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				res = new ChildDTO();
				res.setC_id(rs.getString("c_id"));
				res.setC_pw(rs.getString("c_pw"));
				res.setC_phone(rs.getString("c_phone"));
				res.setC_name(rs.getString("c_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		return  res;
	}
	
	public String searchID(ChildDTO dto) {
		conn = getConnectioin();
		String result = null;
		sql = "select * from "+TABLE_CHILD+" where c_phone = ? and c_name = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getC_phone());
			pstmt.setString(2, dto.getC_name());
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getString("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		return  result;
	}
	
	public String searchPw(ChildDTO dto) { 
		conn = getConnectioin();
		String result = null;
		sql = "select * from "+TABLE_CHILD+" where c_id = ? and c_phone = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getC_id());
			pstmt.setString(2, dto.getC_phone());
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getString("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		return  result;
	}
	
	public Boolean changPw(ChildDTO dto) {
		conn = getConnectioin();
		Boolean result = false;
		sql =  "update "+TABLE_CHILD+" set c_pw =? where c_id = ? ";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getC_pw());
			pstmt.setString(2, dto.getC_id());
			int repstmt = pstmt.executeUpdate();
			if(repstmt != -1) {
				System.out.println("ChangePw 접근완료");
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		return  result;
	}
	public Boolean updatePw(ChildDTO dto) {
		conn = getConnectioin();
		Boolean result = false;
		sql =  "update "+TABLE_CHILD+" set c_pw =? where c_id = ? ";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getC_pw());
			pstmt.setString(2, dto.getC_id());
			int repstmt = pstmt.executeUpdate();
			if(repstmt != -1) {
				System.out.println("ChangePw 접근완료");
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		return  result;
	}
	public Boolean updateName(ChildDTO dto) {
		conn = getConnectioin();
		Boolean result = false;
		sql =  "update "+TABLE_CHILD+" set c_name =? where c_id = ? ";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getC_name());
			pstmt.setString(2, dto.getC_id());
			int repstmt = pstmt.executeUpdate();
			if(repstmt != -1) {
				System.out.println("ChangePw 접근완료");
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		return  result;
	}
	public Boolean updatePhone(ChildDTO dto) {
		conn = getConnectioin();
		Boolean result = false;
		sql =  "update "+TABLE_CHILD+" set c_phone =? where c_id = ? ";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getC_phone());
			pstmt.setString(2, dto.getC_id());
			int repstmt = pstmt.executeUpdate();
			if(repstmt != -1) {
				System.out.println("ChangePw 접근완료");
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		return  result;
	}
	public String checkId(ChildDTO dto) {
		conn = getConnectioin();
		String result = null;
		sql = "select * from parent p,child c where p.p_id =? or c.c_id=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getC_id());
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getString("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		return  result;
	}
	
	//기존 파트
	public String selectChildById(String id) {
		conn = getConnectioin();
		sql = "select c_id, c_phone, c_name from "+TABLE_CHILD+" where c_id=?";
		String result = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getString("c_id")+",";
				result += rs.getString("c_phone")+",";
				result += rs.getString("c_name");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close();
		}
		return result;
	}
	
	public String selectListChildName(ArrayList<String> list) {
		String result = "";
		for(int i=0;i<list.size();i++) {
			conn = getConnectioin();
			sql = "select c_id, c_name from "+TABLE_CHILD+" where c_id=?";
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, list.get(i));
				rs = pstmt.executeQuery();
				if(rs.next()) {
					result += rs.getString("c_id")+",";
					result += rs.getString("c_name");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(i<list.size()-1) {
				result += ",";
			}
		}
		return result;
	}
	
	public void close() {
		if(rs!=null)try {rs.close();} catch (SQLException e) {}
		if(pstmt!=null)try {pstmt.close();} catch (SQLException e) {}
		if(conn!=null)try {conn.close();} catch (SQLException e) {}
	}
}
