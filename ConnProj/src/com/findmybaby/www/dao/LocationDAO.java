package com.findmybaby.www.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.findmybaby.www.dto.LocationDTO;

public class LocationDAO {

	String sql;
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;
	final String TABLE_LOCATION = "location";
//	String url="jdbc:mysql://localhost:3306/findmybaby";
	String url="jdbc:mysql://gitfastlane.cafe24.com/gitfastlane";
	String username="gitfastlane";
//	String username="root";
	String password="Narcissus93!";
//	String password="1234";
	
	String driver = "com.mysql.cj.jdbc.Driver";
//	String url="jdbc:mysql://localhost:3306/findmybaby?characterEncoding=utf-8";
//	String username="root";
//	String password="mysql";
	
	public LocationDAO() {
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
	
	public void insertLocation(String id) {
		conn = getConnection();
		sql = "insert into "+TABLE_LOCATION+" values(?,0,0,0)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			int rs = pstmt.executeUpdate();
			if(rs<=0)System.out.println("insertLocation Error");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close();
		}
	}
	
	public Boolean updateLocation(LocationDTO dto) {
		conn = getConnection();
		int result = 0;
		Boolean falge = false;
		sql = "update "+TABLE_LOCATION+" set l_latitude=?,l_longitude=?,l_heading=?  where l_id=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getL_latitude());
			pstmt.setString(2, dto.getL_longitude());
			pstmt.setString(3, dto.getL_heading());
			pstmt.setString(4, dto.getL_id());
			result = pstmt.executeUpdate();
			if(result>0) {
				System.out.println("updateLocation 실행");
				falge = true;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close();
			return falge;
		}
	}
	
	public LocationDTO searchLocation(LocationDTO dto) {
		conn = getConnection();
		sql = "select * from "+TABLE_LOCATION+" where l_id=?";
		LocationDTO resultDto = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getL_id());
			rs = pstmt.executeQuery();
			if(rs.next()) {
				resultDto = new LocationDTO();
				System.out.println("resultDTo 존재");
				resultDto.setL_latitude(rs.getString("l_latitude"));
				resultDto.setL_longitude(rs.getString("l_longitude"));
				resultDto.setL_heading(rs.getString("l_heading"));
				System.out.println(rs.getString("l_latitude"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
			return resultDto;
		}
	}
	
	
	
	
	public void close() {
		if(rs!=null)try {rs.close();} catch (SQLException e) {}
		if(pstmt!=null)try {pstmt.close();} catch (SQLException e) {}
		if(conn!=null)try {conn.close();} catch (SQLException e) {}
	}
}
