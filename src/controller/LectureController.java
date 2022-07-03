package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connector.DBConnector;
import model.LectureDTO;

public class LectureController {
	private Connection conn;

	public LectureController(DBConnector connector) {
		this.conn=connector.makeConnection();
	}
	
	public LectureDTO selectOne(int lectureId) {
		LectureDTO l=null;
		
		String query="SELECT * FROM `lecture` WHERE `id`=?";
		
		try {
			PreparedStatement pstmt=conn.prepareStatement(query);
			pstmt.setInt(1, lectureId);
			
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()) {		
				l=new LectureDTO();
				
				l.setId(rs.getInt("id"));
				l.setLecturerId(rs.getInt("lecturer_id"));
				l.setSemester(rs.getInt("semester"));
				l.setMajor(rs.getInt("major"));
				l.setLectureName(rs.getString("lecture_name"));
				l.setSeatLeft(rs.getInt("seat_left"));
				l.setStartTime(rs.getInt("start_time"));
				l.setEndTime(rs.getInt("end_time"));
				l.setCredit(rs.getInt("credit"));
				l.setRoom(rs.getInt("room"));
				l.setYear(rs.getString("year"));
				l.setDay(rs.getString("day"));				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return l;
	}
	
	public ArrayList<LectureDTO> selectAll(){
		ArrayList<LectureDTO> list=new ArrayList<>();
		
		String query="SELECT * FROM `lecture`";

		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			ResultSet rs=pstmt.executeQuery();
			
			while(rs.next()) {
				LectureDTO l=new LectureDTO();
				l.setId(rs.getInt("id"));
				l.setLecturerId(rs.getInt("lecturer_id"));
				l.setSemester(rs.getInt("semester"));
				l.setMajor(rs.getInt("major"));
				l.setLectureName(rs.getString("lecture_name"));
				l.setSeatLeft(rs.getInt("seat_left"));
				l.setStartTime(rs.getInt("start_time"));
				l.setEndTime(rs.getInt("end_time"));
				l.setCredit(rs.getInt("credit"));
				l.setRoom(rs.getInt("room"));
				l.setYear(rs.getString("year"));
				l.setDay(rs.getString("day"));
				
				list.add(l);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public void insert(LectureDTO l) {
		String query="INSERT INTO `lecture`"
				+ "(`lecturer_id`,`semester`,`major`,`lecture_name`,`seat_left`,"
				+ "`start_time`,`end_time`,`credit`,`room`,`year`,`day`) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
		
		try {
			PreparedStatement pstmt=conn.prepareStatement(query);
			pstmt.setInt(1, l.getLecturerId());
			pstmt.setInt(2, l.getSemester());
			pstmt.setInt(3, l.getMajor());
			pstmt.setString(4, l.getLectureName());
			pstmt.setInt(5, l.getSeatLeft());
			pstmt.setInt(6, l.getStartTime());
			pstmt.setInt(7, l.getEndTime());
			pstmt.setInt(8, l.getCredit());
			pstmt.setInt(9, l.getRoom());
			pstmt.setString(10, l.getYear());
			pstmt.setString(11, l.getDay());
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void update(LectureDTO l) {
		String query="UPDATE `lecture` SET `lecturer_id`=?, `semester`=?, `major`=?,"
				+ "`lecture_name`=?, `seat_left`=?, `start_time`=?, `end_time`=?,"
				+ "`credit`=?, `room`=?, `year`=?, `day`=? WHERE `id`=?";
		
		try {
			PreparedStatement pstmt=conn.prepareStatement(query);
			pstmt.setInt(1, l.getLecturerId());
			pstmt.setInt(2, l.getSemester());
			pstmt.setInt(3, l.getMajor());
			pstmt.setString(4, l.getLectureName());
			pstmt.setInt(5, l.getSeatLeft());
			pstmt.setInt(6, l.getStartTime());
			pstmt.setInt(7, l.getEndTime());
			pstmt.setInt(8, l.getCredit());
			pstmt.setInt(9, l.getRoom());
			pstmt.setString(10, l.getYear());
			pstmt.setString(11, l.getDay());
			pstmt.setInt(12, l.getId());
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void delete(int lectureId) {
		String query="DELETE FROM `lecture` WHERE `id`=?";
		
		try {
			PreparedStatement pstmt=conn.prepareStatement(query);
			pstmt.setInt(1, lectureId);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
