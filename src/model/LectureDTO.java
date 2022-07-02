package model;

public class LectureDTO {
	private int id;
	//강의자 id
	private int lecturerId;
	//1학기 or 2학기
	private int semester;
	//전공 마지막 1자리는 각 단과대학에 속해있는 전공 앞 1~2자리는 단과대학코드 -> DB
	private int major;
	private String lectureName;
	//남은 수강신청 가능 인원수
	private int seatLeft;
	//시작시간 1이 09:00 2가 09:30 -> DB
	private int startTime;
	private int endTime;
	//학점
	private int credit;
	//강의실 마지막 2자리는 각 건물에 속해있는 강의실번호, 앞 1~2자리는 건물코드 -> DB
	private int room;
	//학년 형식은 1학년과 2학년이 모두 들을 수 있는 강의의 경우 "1,2"로 표기
	//1,2,3,4
	private String year;
	//요일 형식은 월요일과 수요일에 강의가 있는 경우 "mon,wed"로 표기
	//mon,tue,wed,thr,fri
	private String day;
	
	public boolean equals(Object o) {
		if(o instanceof LectureDTO) {
			return this.id==((LectureDTO)o).id;
		} else {
			return false;
		}
	}

	public int getId() {
		return id;
	}

	public int getLecturerId() {
		return lecturerId;
	}

	public int getSemester() {
		return semester;
	}

	public int getMajor() {
		return major;
	}

	public String getLectureName() {
		return lectureName;
	}

	public int getSeatLeft() {
		return seatLeft;
	}

	public int getStartTime() {
		return startTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public int getCredit() {
		return credit;
	}

	public int getRoom() {
		return room;
	}
	
	public String getYear() {
		return year;
	}
	
	public String getDay() {
		return day;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public void setLecturerId(int lecturerId) {
		this.lecturerId = lecturerId;
	}

	public void setSemester(int semester) {
		this.semester = semester;
	}

	public void setMajor(int major) {
		this.major = major;
	}

	public void setLectureName(String lectureName) {
		this.lectureName = lectureName;
	}

	public void setSeatLeft(int seatLeft) {
		this.seatLeft = seatLeft;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}
	
	public void setRoom(int room) {
		this.room=room;
	}
	
	public void setYear(String year) {
		this.year=year;
	}
	
	public void setDay(String day) {
		this.day=day;
	}
}
