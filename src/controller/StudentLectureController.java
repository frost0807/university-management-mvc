package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connector.DBConnector;
import model.StudentLectureDTO;

public class StudentLectureController {
	private Connection conn;

	public StudentLectureController(DBConnector connector) {
		this.conn=connector.makeConnection();
	}
	
	public ArrayList<StudentLectureDTO> selectAllByStudentId(int studentId) {
		ArrayList<StudentLectureDTO> list=new ArrayList<>();
		String query="SELECT * FROM `student_lecture` WHERE `student_id`=?";
		try {
			PreparedStatement pstmt=conn.prepareStatement(query);
			pstmt.setInt(1, studentId);
			
			ResultSet rs=pstmt.executeQuery();
			
			while(rs.next()) {
				StudentLectureDTO s=new StudentLectureDTO();
				s.setId(rs.getInt("id"));
				s.setLectureId(rs.getInt("lecture_id"));
				s.setStudentId(rs.getInt("student_id"));
				s.setScore(rs.getDouble("score"));
				
				list.add(s);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public ArrayList<StudentLectureDTO> selectAllByLectureId(int lectureId){
		ArrayList<StudentLectureDTO> list=new ArrayList<>();
		String query="SELECT * FROM `student_lecture` WHERE `lecture_id`=?";
		
		try {
			PreparedStatement pstmt=conn.prepareStatement(query);
			pstmt.setInt(1, lectureId);
			
			ResultSet rs=pstmt.executeQuery();
			
			while(rs.next()) {
				StudentLectureDTO s=new StudentLectureDTO();
				s.setId(rs.getInt("id"));
				s.setLectureId(rs.getInt("lecture_id"));
				s.setStudentId(rs.getInt("student_id"));
				s.setScore(rs.getDouble("score"));
				
				list.add(s);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public StudentLectureDTO selectOneByTwoId(int studentId, int lectureId) {

		StudentLectureDTO s=null;
		
		String query="SELECT * FROM `lecture` WHERE `student_id`=? AND `lecture_id`=?";
		
		try {
			PreparedStatement pstmt=conn.prepareStatement(query);
			pstmt.setInt(1, studentId);
			pstmt.setInt(2, lectureId);
			
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()) {		
				s=new StudentLectureDTO();
				
				s.setId(rs.getInt("id"));
				s.setId(rs.getInt("student_id"));
				s.setId(rs.getInt("lecture_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return s;
	}
	
	public void insert(StudentLectureDTO s) {
		String query="INSERT INTO `student_lecture`(`student_id`,`lecture_id`) VALUES(?,?)";
		
		try {
			PreparedStatement pstmt=conn.prepareStatement(query);
			
			pstmt.setInt(1, s.getStudentId());
			pstmt.setInt(2, s.getLectureId());
			
			pstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void update(StudentLectureDTO s) {
		String query="UPDATE `student_lecture` SET `lecture_id`=?,`student_id`=?,"
				+ "`score`=? WHERE `id`=?";
		
		try {
			PreparedStatement pstmt=conn.prepareStatement(query);
			pstmt.setInt(1, s.getLectureId());
			pstmt.setInt(2, s.getStudentId());
			pstmt.setDouble(3, s.getScore());
			pstmt.setInt(4, s.getId());
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void delete(int userId,int lectureId) {
		String query="DELETE FROM `student_lecture` WHERE `student_id`=? AND `lecture_id`=?";
		
		try {
			PreparedStatement pstmt=conn.prepareStatement(query);
			
			pstmt.setInt(1, userId);
			pstmt.setInt(2, lectureId);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public double getAverageScore(int studentId) {
		double result;
		int lectureCount=0;
		double sum=0;
		for(StudentLectureDTO s:selectAllByStudentId(studentId)) {
			//double형은 오차가 발생할 수 있으므로 대소비교 안전하게하기
			if(s.getScore()>0.5) {
				lectureCount++;
				sum+=s.getScore();
			}
		}
		result=sum/lectureCount;
		
		return result;
	}
}
