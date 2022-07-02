package controller;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connector.DBConnector;
import model.UserDTO;

public class UserController {
	private Connection conn;
	
	public UserController(DBConnector connector) {
		this.conn=connector.makeConnection();
	}
	
	public UserDTO logInValid(int userId, String password) {
		String query="SELECT * FROM `user` WHERE `id`=? AND `password`=?";
		
		try {
			PreparedStatement pstmt=conn.prepareStatement(query);
			
			pstmt.setInt(1, userId);
			pstmt.setString(2, convertToSha(password));
			
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()) {
				UserDTO u=new UserDTO();
				u.setId(rs.getInt("id"));
				u.setPassword(rs.getString("password"));
				u.setRank(rs.getInt("rank"));
				u.setName(rs.getString("name"));
				u.setYear(rs.getInt("year"));
				u.setMajor(rs.getInt("major"));
				u.setCreditLeft(rs.getInt("credit_left"));
				
				return u;
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		return null;
	}

	public String convertToSha(String password) {
		String converted=null;
		StringBuilder builder=null;
		
		try {
			MessageDigest md=MessageDigest.getInstance("SHA-256");
			byte[] hash = md.digest(password.getBytes("UTF-8"));
			
			builder=new StringBuilder();
			for(int i=0;i<hash.length;i++) {
				builder.append(String.format("%02x", 255&hash[i]));
			}
			converted=builder.toString();
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return converted;
	}
	
	public ArrayList<UserDTO> selectAll(){
		ArrayList<UserDTO> list=new ArrayList<>();
		
		String query="SELECT * FROM `user`";
		
		try {
			PreparedStatement pstmt=conn.prepareStatement(query);
			ResultSet rs=pstmt.executeQuery();
			
			while(rs.next()) {
				UserDTO u=new UserDTO();
				u.setId(rs.getInt("id"));
				u.setName(rs.getString("name"));
				u.setPassword(rs.getString("password"));
				u.setRank(rs.getInt("rank"));
				u.setYear(rs.getInt("year"));
				u.setCreditLeft(rs.getInt("credit_left"));
				
				list.add(u);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public UserDTO selectOne(int userId) {
		UserDTO u=null;
		String query="SELECT * FROM `user` WHERE `id`=?";
		
		try {
			PreparedStatement pstmt=conn.prepareStatement(query);
			
			pstmt.setInt(1, userId);
			
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()) {
				u=new UserDTO();
				u.setId(rs.getInt("id"));
				u.setRank(rs.getInt("rank"));
				u.setName(rs.getString("name"));
				u.setYear(rs.getInt("year"));
				u.setMajor(rs.getInt("major"));
				u.setCreditLeft(rs.getInt("credit_left"));
				
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		return u;
	}
	
	public void insert(UserDTO u) {
		String query="INSERT INTO `user` (`password`,`rank`,`name`,`year`,`major`,`credit_left`) VALUES(?,?,?,?,?,?)";
		try {
			PreparedStatement pstmt=conn.prepareStatement(query);
			pstmt.setString(1, u.getPassword());
			pstmt.setInt(2, u.getRank());
			pstmt.setString(3, u.getName());
			pstmt.setInt(4, u.getYear());
			pstmt.setInt(5, u.getMajor());
			pstmt.setInt(6, u.getCreditLeft());
			
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update(UserDTO logInInfo) {
		String query="UPDATE `user` SET `password`=? WHERE `id`=?";
		
		try {
			PreparedStatement pstmt=conn.prepareStatement(query);
			pstmt.setString(1, convertToSha(logInInfo.getPassword()));
			pstmt.setInt(2, logInInfo.getId());
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateByAdmin(UserDTO u) {
		String query="UPDATE `user` SET `password`=?,`name`=?,`year`=?,`major`=?,`credit_left`=? WHERE `id`=?";
		try {
			PreparedStatement pstmt=conn.prepareStatement(query);
			pstmt.setString(1, u.getPassword());
			pstmt.setString(2, u.getName());
			pstmt.setInt(3, u.getYear());
			pstmt.setInt(4, u.getMajor());
			pstmt.setInt(5, u.getCreditLeft());
			pstmt.setInt(6, u.getId());
			
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void delete(int userId) {
		String query="DELETE FROM `user` WHERE `id`=?";
		
		try {
			PreparedStatement pstmt=conn.prepareStatement(query);
			pstmt.setInt(1, userId);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
