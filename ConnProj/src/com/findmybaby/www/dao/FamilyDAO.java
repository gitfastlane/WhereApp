package com.findmybaby.www.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.findmybaby.www.dto.ChildDTO;
import com.findmybaby.www.dto.FamilyDTO;
import com.findmybaby.www.dto.ParentDTO;

public class FamilyDAO {
	String sql;
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;
	final String TABLE_FAMILY = "family";
	
//	String url="jdbc:mysql://gitfastlane.cafe24.com/gitfastlane";
// username="gitfastlane";
//	String password="Narcissus93!";
	
	String driver = "com.mysql.cj.jdbc.Driver";
	String url="jdbc:mysql://localhost:3306/findmybaby?characterEncoding=utf-8";
	String username="root";
	String password="1234";
	
	public FamilyDAO() {
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
	
	public String insertChildByParent(String parentId,String childId) {
		String result = null;
		if(!checkFamily(parentId, childId)) {
			conn = getConnection();
			sql = "insert into "+TABLE_FAMILY+" values(?,?,0)";
			int rslt = 0;
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, parentId);
				pstmt.setString(2, childId);
				pstmt.executeUpdate();
				result = "true";
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				close();
			}
		}else {
			result = "false";
		}
		return result;
	}
	private boolean checkFamily(String parentId,String childId) {
		conn = getConnection();
		sql = "select f_child from "+TABLE_FAMILY+" where f_parent=? and f_child=?";
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, parentId);
			pstmt.setString(2, childId);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				if(childId.equals(rs.getString("f_child"))) {
					flag = true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close();
		}
		return flag;
	}
	
	public ArrayList<String> selectListMyChild(String id) {
		ArrayList<String> list = new ArrayList<>();
		conn = getConnection();
		sql = "select f_child from "+TABLE_FAMILY+" where f_parent=? and f_confirm=1";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				list.add(rs.getString("f_child"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close();
		}
		return list;
	}
	
	public boolean deleteMyChild(FamilyDTO dto) {
		boolean flag = false;
		conn = getConnection();
		sql = "delete from "+TABLE_FAMILY+" where f_parent=? and f_child=? and f_confirm=1";
		int res = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getF_parent());
			pstmt.setString(2, dto.getF_child());
			res = pstmt.executeUpdate();
			if(res<=0) {
				System.out.println("deleteMyChild Error");
			}else {
				flag = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close();
		}
		return flag;
	}
	
	public ArrayList<String> showInviteParentList(String childId){
		ArrayList<String> list = new ArrayList<>();
		sql = "select f_parent from "+TABLE_FAMILY+" where f_child=? and f_confirm=0";
		conn = getConnection();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, childId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				list.add(rs.getString("f_parent"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close();
		}
		return list;
	}
	
	public void confirmParent(FamilyDTO dto) {
		sql = "update "+TABLE_FAMILY+" set f_confirm=1 where f_parent=? and f_child=?";
		conn = getConnection();
		int rst = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getF_parent());
			pstmt.setString(2, dto.getF_child());
			rst = pstmt.executeUpdate();
			if(rst<=0) System.out.println("confirmParent Error"); 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close();
		}
	}
	
	public ArrayList<String> selectMyParentList(FamilyDTO dto) {
		ArrayList<String> list = new ArrayList<>();
		sql = "select f_parent from "+TABLE_FAMILY+" where f_child=? and f_confirm=1";
		conn = getConnection();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getF_child());
			rs = pstmt.executeQuery();
			while(rs.next()) {
				list.add(rs.getString("f_parent"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close();
		}
		return list;
	}
	
	public void close() {
		if(rs!=null)try {rs.close();} catch (SQLException e) {}
		if(pstmt!=null)try {pstmt.close();} catch (SQLException e) {}
		if(conn!=null)try {conn.close();} catch (SQLException e) {}
	}
}
