package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connector.DBConnector;

public class MajorController {
	private Connection conn;

	public MajorController(DBConnector connector) {
		this.conn=connector.makeConnection();
	}
	//major int값을 String 전공문자열로 바꿔주는 메소드
	public String majorString(int major) {
		String query="SELECT *  FROM `major_tostring` WHERE `id`=?";
		String result=null;
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, major);
			
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()) {
				result=rs.getString("major_string");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
