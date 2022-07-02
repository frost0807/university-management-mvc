package model;

public class UserDTO {
	private int id;
	private String password;
	//계정등급 1.학생 2.강사 3.교직원
	private int rank;
	private String name;
	//몇학년인지 first,second,third,fourth
	private int year;
	//전공
	private int major;
	
	private int creditLeft;
	
	public boolean equals(Object o) {
		if(o instanceof UserDTO) {
			return this.id==((UserDTO)o).id;
		} else {
			return false;
		}
	}

	public int getId() {
		return id;
	}

	public String getPassword() {
		return password;
	}
	
	public int getRank() {
		return rank;
	}

	public String getName() {
		return name;
	}

	public int getYear() {
		return year;
	}

	public int getMajor() {
		return major;
	}
	
	public int getCreditLeft() {
		return creditLeft;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setPassword(String password) {
		this.password=password;
	}
	
	public void setRank(int rank) {
		this.rank = rank;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setMajor(int major) {
		this.major = major;
	}
	
	public void setCreditLeft(int creditLeft) {
		this.creditLeft=creditLeft;
	}
}
