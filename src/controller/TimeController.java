package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connector.DBConnector;

public class TimeController {
	private Connection conn;

	public TimeController(DBConnector connector) {
		this.conn=connector.makeConnection();
	}
	
	public String timeString(int time) {
		String query="SELECT *  FROM `time_tostring` WHERE `id`=?";
		String result=null;

		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, time);
			
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()) {
				result=rs.getString("time_string");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
