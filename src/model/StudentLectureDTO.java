package model;

public class StudentLectureDTO {
	private int id;
	private int lectureId;
	private int studentId;
	//성적 4.5, 4.0, 3.5, 3.0, 2.5, 2.0, 1.5, 1.0, 0.0 까지 있고 0.0(F) 학점계산할때 빠짐
	private double score;
	
	public boolean equals(Object o) {
		if(o instanceof StudentLectureDTO) {
			return this.id==((StudentLectureDTO)o).id;
		} else {
			return false;
		}
	}

	public int getId() {
		return id;
	}

	public int getLectureId() {
		return lectureId;
	}

	public int getStudentId() {
		return studentId;
	}

	public double getScore() {
		return score;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setLectureId(int lectureId) {
		this.lectureId = lectureId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public void setScore(double score) {
		this.score = score;
	}
	
}
