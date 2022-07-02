package model;

public class MajorDTO {
	private int id;
	private String majorString;
	
	public boolean equals(Object o) {
		if(o instanceof MajorDTO) {
			return this.id==((MajorDTO)o).id;
		} else {
			return false;
		}
	}

	public int getId() {
		return id;
	}

	public String getMajorString() {
		return majorString;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setMajorString(String majorString) {
		this.majorString = majorString;
	}
	
}
