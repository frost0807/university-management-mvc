package model;

public class RoomDTO {
	//뒷3자리는 강의실번호,앞자리는 건물번호
	private int id;
	private String roomString;
	
	public boolean equals(Object o) {
		if(o instanceof RoomDTO) {
			return this.id==((RoomDTO)o).id;
		} else {
			return false;
		}
	}

	public int getId() {
		return id;
	}

	public String getRoomString() {
		return roomString;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setRoomString(String roomString) {
		this.roomString = roomString;
	}
	
}
