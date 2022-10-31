package com.findmybaby.www.dao;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.findmybaby.www.dto.ParentDTO;

public class ParentDAO {
	String sql;
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;
	final String TABLE_PARENT = "parent";
		
	//         DB 종류  ://  url / 데이터베이스명
//	String url="jdbc:mysql://gitfastlane.cafe24.com/gitfastlane";
//	String username="gitfastlane";
//	String password="Narcissus93!";
	
	String driver = "com.mysql.cj.jdbc.Driver";
	String url="jdbc:mysql://localhost:3306/findmybaby?characterEncoding=utf-8";
	String username="root";
	String password="1234";
	
	public ParentDAO() {
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
	
	private Connection getConnection() {
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
	
	//추가 파트
	public ArrayList<ParentDTO> selectAll(){
		conn = getConnection();
		ArrayList<ParentDTO> list = new ArrayList<>();
		sql = "select * from "+TABLE_PARENT;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				ParentDTO dto = new ParentDTO();
				dto.setP_id(rs.getString("p_id"));
				dto.setP_name(rs.getString("p_name"));
				dto.setP_phone(rs.getString("p_name"));
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
	
	public Boolean insertParent(ParentDTO dto) {
		conn = getConnection();
		int result = 0;
		Boolean falge = false;
		sql = "insert into "+TABLE_PARENT+" values(?,?,?,?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getP_id());
			pstmt.setString(2, dto.getP_pw());
			pstmt.setString(3, dto.getP_phone());
			pstmt.setString(4, dto.getP_name());
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
	
	public ParentDTO loginParent(ParentDTO dto) {
		conn = getConnection();
		ParentDTO res = null;
		sql = "select * from "+TABLE_PARENT+" where p_id = ? and p_pw = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getP_id());
			pstmt.setString(2, dto.getP_pw());
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				res = new ParentDTO();
				res.setP_id(rs.getString("p_id"));
				res.setP_pw(rs.getString("p_pw"));
				res.setP_phone(rs.getString("p_phone"));
				res.setP_name(rs.getString("p_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		return  res;
	}
	
	public String searchID(ParentDTO dto) {
		conn = getConnection();
		String result = null;
		sql = "select * from parent where p_phone = ? and p_name = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getP_phone());
			pstmt.setString(2, dto.getP_name());
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getString("p_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		return  result;
	}
	
	public String searchPw(ParentDTO dto) {
		conn = getConnection();
		String result = null;
		sql = "select * from parent where p_id = ? and p_phone = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getP_id());
			pstmt.setString(2, dto.getP_phone());
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getString("p_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		return  result;
	}
	
	public Boolean changPw(ParentDTO dto) {
		conn = getConnection();
		Boolean result = false;
		sql =  "update "+TABLE_PARENT+" set p_pw =? where p_id = ? ";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getP_pw());
			pstmt.setString(2, dto.getP_id());
			int repstmt = pstmt.executeUpdate();
			if(repstmt != -1) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		return  result;
	}
	public String checkId(ParentDTO dto) {
		conn = getConnection();
		String result = null;
		sql = "select * from parent p,child c where p.p_id =? or c.c_id=?";
		//SELECT * FROM EMP, DEPT WHERE EMP.DEPTNO = DEPT.DEPTNO
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getP_id());
			pstmt.setString(2, dto.getP_id());
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getString("p_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		return  result;
	}
	public Boolean updatePw(ParentDTO dto) {
		conn = getConnection();
		Boolean result = false;
		sql =  "update "+TABLE_PARENT+" set p_pw =? where p_id = ? ";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getP_pw());
			pstmt.setString(2, dto.getP_id());
			int repstmt = pstmt.executeUpdate();
			if(repstmt != -1) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		return  result;
	}
	public Boolean updateName(ParentDTO dto) {
		conn = getConnection();
		Boolean result = false;
		sql =  "update "+TABLE_PARENT+" set p_name =? where p_id = ? ";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getP_name());
			pstmt.setString(2, dto.getP_id());
			int repstmt = pstmt.executeUpdate();
			if(repstmt != -1) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		return  result;
	}
	public Boolean updatePhone(ParentDTO dto) {
		conn = getConnection();
		Boolean result = false;
		sql =  "update "+TABLE_PARENT+" set p_phone =? where p_id = ? ";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getP_phone());
			pstmt.setString(2, dto.getP_id());
			int repstmt = pstmt.executeUpdate();
			if(repstmt != -1) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		return  result;
	}
	
	//기존 파트	
	public String selectParentList(ArrayList<String> list) {
		String result = "";
		for(int i=0;i<list.size();i++) {
			String sql = "select p_id, p_name from "+TABLE_PARENT+" where p_id=?";
			conn = getConnection();
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, list.get(i));
				rs = pstmt.executeQuery();
				if(rs.next()) {
					result += rs.getString("p_id")+",";
					result += rs.getString("p_name");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				close();
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
