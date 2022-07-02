package model;

public class TimeDTO {
	private int id;
	private String timeString;
	
	public boolean equals(Object o) {
		if(o instanceof TimeDTO) {
			return this.id==((TimeDTO)o).id;
		} else {
			return false;
		}
	}

	public int getId() {
		return id;
	}

	public String getTimeString() {
		return timeString;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTimeString(String timeString) {
		this.timeString = timeString;
	}
	
}
