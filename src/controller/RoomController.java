package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connector.DBConnector;

public class RoomController {
	private Connection conn;

	public RoomController(DBConnector connector) {
		this.conn=connector.makeConnection();
	}
	//room int값을 String 강의실이름으로 바꿔주는 메소드
	public String roomString(int room) {
		String query="SELECT *  FROM `room_tostring` WHERE `id`=?";
		String result=null;
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, room);
			
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()) {
				result=rs.getString("room_string");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
