package viewer;

import java.util.ArrayList;
import java.util.Scanner;

import connector.DBConnector;
import controller.LectureController;
import controller.MajorController;
import controller.RoomController;
import controller.StudentLectureController;
import controller.TimeController;
import controller.UserController;
import model.LectureDTO;
import model.UserDTO;
import util.ScUtil;

public class LectureViewer {
	private Scanner scanner;
	private UserDTO logInInfo;
	private DBConnector connector;
	
	public LectureViewer(Scanner scanner,DBConnector connector, UserDTO logInInfo) {
		this.logInInfo=logInInfo;
		this.scanner=scanner;
		this.connector=connector;
	}
	
	public void applyLecture() {
		//UserDTO의 major, year, rank에 따라 해당되는 강의를 출력
		while(true) {
			//학생일 경우 현재 신청한 강의와 잔여학점 등을 출력
			printListByStudent();
			
			int userChoice=ScUtil.nextInt(scanner,1,4, "1.강의 신청하기 2.신청한 강의 삭제하기 3.강의 상세보기 4.뒤로가기");
			if(userChoice==1) {
				StudentLectureViewer viewer=new StudentLectureViewer(scanner,connector,logInInfo);
				
				viewer.applyByStudent();
			} else if(userChoice==2) {
				StudentLectureViewer viewer=new StudentLectureViewer(scanner,connector,logInInfo);
				
				viewer.deleteByStudent();
			} else if(userChoice==3) {
				printLectureDetails();
			} else if(userChoice==4) {
				System.out.println("메인화면으로 돌아갑니다.");
				break;
			}
		}
	}

	private void printListByStudent() {
		LectureController lectureController=new LectureController(connector);
		MajorController majorController=new MajorController(connector);
		StudentLectureViewer viewer=new StudentLectureViewer(scanner,connector,logInInfo);

		System.out.println("============================================================================");
		System.out.println("강의코드\t강의명\t\t전공\t학기\t학점\t신청가능인원수\n");
		System.out.println("----------------------------------------------------------------------------");
		for(LectureDTO l:lectureController.selectAll()) {
			System.out.printf("%d\t%s\t\t%s\t%d\t%d\t%d\n",l.getId(),l.getLectureName(),
					majorController.majorString(l.getMajor()),l.getSemester(),l.getCredit(),l.getSeatLeft());
		}
		System.out.println("============================================================================");
		viewer.printMyAppliedList();
		System.out.println("============================================================================");
	}

	private int printLectureDetails() {
		int lectureChoice=ScUtil.nextInt(scanner, "상세보기 할 강의코드를 입력해주시거나 0을입력해 뒤로가기");
		LectureController controller=new LectureController(connector);
		
		while(lectureChoice!=0&&controller.selectOne(lectureChoice)==null) {
			System.out.println("잘못된 입력입니다.");
			lectureChoice=ScUtil.nextInt(scanner, "상세보기 할 강의코드를 입력해주시거나 0을입력해 뒤로가기");
		}
		if(lectureChoice!=0) {
			printOne(lectureChoice);
			return lectureChoice;
		}
		return 0;
	}
	
	//해당 강의의 상세정보를 보여주는 메소드
	private void printOne(int lectureChoice) {
		LectureController lectureController=new LectureController(connector);
		UserController userController=new UserController(connector);
		MajorController majorController=new MajorController(connector);
		RoomController roomController=new RoomController(connector);
		TimeController timeController=new TimeController(connector);
		LectureDTO l=lectureController.selectOne(lectureChoice);

		System.out.println("============================================================================");
		System.out.printf("강의코드:%d\t담당교수님:%s\t강의명:%s\n",l.getId(),
				userController.selectOne(l.getLecturerId()).getName(),l.getLectureName());
		System.out.printf("전공:%s\t학점:%d\t학기:%d\t수강가능 학년:%s\n",
				majorController.majorString(l.getMajor()),l.getCredit(),l.getSemester(),l.getYear());
		System.out.printf("강의실:%s\t시작시간:%s\t마치는시간:%s\t강의요일:%s\n",
				roomController.roomString(l.getRoom()),timeController.timeString(l.getStartTime()),
				timeController.timeString(l.getEndTime()),l.getDay());
		System.out.printf("현재 수강신청 가능 인원:%d\n",l.getSeatLeft());
		System.out.println("============================================================================");
	}

	public void printListByLecturer() {
		LectureController lectureController=new LectureController(connector);
		RoomController roomController=new RoomController(connector);
		TimeController timeController=new TimeController(connector);

		System.out.println("============================================================================");
		System.out.printf("강의코드\t강의명\t\t\t강의실\t시작시간\t마치는시간\n");
		System.out.println("----------------------------------------------------------------------------");
		for(LectureDTO l:lectureController.selectAll())	{
			if(l.getLecturerId()==logInInfo.getId()) {
				System.out.printf("%d\t%s\t\t\t%s\t%s\t%s\n",l.getId(),l.getLectureName(),
						roomController.roomString(l.getRoom()),
						timeController.timeString(l.getStartTime()),
						timeController.timeString(l.getEndTime()));
			}
		}
		System.out.println("============================================================================");
		
		int lectureChoice=ScUtil.nextInt(scanner, "상세보기 할 강의번호를 입력하시거나 0을 입력해 뒤로가기");
		LectureDTO tempLecture=lectureController.selectOne(lectureChoice);
		//1.해당 강의가 존재하고 2.로그인한 강사의 강의가 아니면
		while(lectureChoice!=0&&(tempLecture==null||tempLecture.getLecturerId()!=logInInfo.getId())){
			System.out.println("잘못된 입력입니다.");
			lectureChoice=ScUtil.nextInt(scanner, "상세보기 할 강의번호를 입력하시거나 0을 입력해 뒤로가기");
		}
		if(lectureChoice!=0){
			printOne(lectureChoice);
			
			int userChoice=ScUtil.nextInt(scanner, 1, 3, "1.학생성적 입력 2.추가신청 관리 3.뒤로가기");
			if(userChoice==1) {				
				StudentLectureViewer viewer=new StudentLectureViewer(scanner,connector,logInInfo);
				
				viewer.scoreUpdate(lectureChoice);
			} else if(userChoice==2) {
				StudentLectureViewer viewer=new StudentLectureViewer(scanner,connector,logInInfo);
				
				viewer.additionalApply(lectureChoice);
			} else if(userChoice==3) {
				
			}
		}
	}
	
	public void printTimetable() {
		ArrayList<String> mon=new ArrayList<>();
		ArrayList<String> tue=new ArrayList<>();
		ArrayList<String> wed=new ArrayList<>();
		ArrayList<String> thu=new ArrayList<>();
		ArrayList<String> fri=new ArrayList<>();
		
		LectureController lectureController=new LectureController(connector);
		
		for(LectureDTO l:lectureController.selectAll()) {
			RoomController roomController=new RoomController(connector);
			TimeController timeController=new TimeController(connector);
			
			if(l.getLecturerId()==logInInfo.getId()) {
				String lectureName_room=l.getLectureName()+"_"+roomController.roomString(l.getRoom());
				String[] day=l.getDay().split(",");
				String startTime=timeController.timeString(l.getStartTime());
				String endTime=timeController.timeString(l.getEndTime());
				String result=lectureName_room+" "+startTime+"-"+endTime;
				
				for(int i=0;i<day.length;i++) {
					if(day[i].equals("mon")) {
						mon.add(result);
					} else if(day[i].equals("tue")) {
						tue.add(result);
					} else if(day[i].equals("wed")) {
						wed.add(result);
					} else if(day[i].equals("thu")) {
						thu.add(result);
					} else if(day[i].equals("fri")) {
						fri.add(result);
					}
				}
			}
		}

		System.out.println("============================================================================");
		System.out.print("[월요일]\n");
		printTimeTableByDay(mon);
		System.out.println("----------------------------------------------------------------------------");
		System.out.print("[화요일]\n");
		printTimeTableByDay(tue);
		System.out.println("----------------------------------------------------------------------------");
		System.out.print("[수요일]\n");
		printTimeTableByDay(wed);
		System.out.println("----------------------------------------------------------------------------");
		System.out.print("[목요일]\n");
		printTimeTableByDay(thu);
		System.out.println("----------------------------------------------------------------------------");
		System.out.print("[금요일]\n");
		printTimeTableByDay(fri);
		System.out.println("============================================================================");
	}
	
	private void printTimeTableByDay(ArrayList<String> list) {
		for(String s:list) {
			System.out.println(s);
		}
	}

	public void lectureManagement() {
		while(true) {
			int userChoice=ScUtil.nextInt(scanner, 1, 3, "1.새로운강의 추가하기 2.강의 목록보기 3.뒤로가기");
			
			if(userChoice==1) {
				insertLecture();
			} else if(userChoice==2) {
				printListByStaff();
			} else if(userChoice==3) {
				break;
			}
		}
	}

	private void insertLecture() {
		UserController userController=new UserController(connector);
		LectureController lectureController=new LectureController(connector);
		MajorController majorController=new MajorController(connector);
		RoomController roomController=new RoomController(connector);
		LectureDTO tempLecture=new LectureDTO();
		
		tempLecture.setSemester(ScUtil.nextInt(scanner, 1, 2, "학기를 입력해주세요 1 혹은 2"));
		tempLecture.setLectureName(ScUtil.nextLine(scanner, "강의명을 입력해주세요"));
		tempLecture.setSeatLeft(ScUtil.nextInt(scanner, 1, 300, "최대 수강신청 가능 인원을 입력해주세요"));
		tempLecture.setStartTime(ScUtil.nextInt(scanner, 1, 21, "강의시작시각을 입력해주세요 1 - 09:00 ~ 21 - 19:00"));
		tempLecture.setEndTime(ScUtil.nextInt(scanner, 1, 21, "강의종료시각을 입력해주세요 1 - 09:00 ~ 21 - 19:00"));
		tempLecture.setCredit(ScUtil.nextInt(scanner, 1, 3, "해당 강의의 학점을 입력해주세요"));
		tempLecture.setYear(ScUtil.nextLine(scanner, "해당강의의 수강가능 학년을 입력해주세요 ex) 2학년,3학년이 신청가능하다면 2,3 입력"));
		tempLecture.setDay(ScUtil.nextLine(scanner, "해당 강의가 있는 요일을 입력해주세요 ex)월요일,수요일에 있는 수업이면 mon,wed 입력"));
		
		int majorChoice=ScUtil.nextInt(scanner, "해당 강의의 전공분야를 입력해주시거나 0을입력해 뒤로가기");
		//major테이블에 majorChoice를 id로 가진 row가 존재하지 않으면 null리턴
		String tempMajor=majorController.majorString(majorChoice);
		
		while(majorChoice!=0&&tempMajor==null) {
			System.out.println("잘못된 입력입니다.");
			majorChoice=ScUtil.nextInt(scanner, "해당 강의의 전공분야를 입력해주시거나 0을 입력해 뒤로가기");
			tempMajor=majorController.majorString(majorChoice);
		}
		if(majorChoice!=0) {
			tempLecture.setMajor(majorChoice);
			
			int roomChoice=ScUtil.nextInt(scanner, "해당 강의의 강의실을 입력해주시거나 0을 입력해 뒤로가기");
			//room테이블에 roomChoice를 id로 가진 row가 존재하지 않으면 null리턴
			String tempRoom=roomController.roomString(roomChoice);
			
			while(roomChoice!=0&&tempRoom==null) {
				System.out.println("잘못된 입력입니다.");
				roomChoice=ScUtil.nextInt(scanner, "해당 강의의 강의실을 입력해주시거나 0을 입력해 뒤로가기");
				tempRoom=roomController.roomString(roomChoice);
			}
			if(roomChoice!=0) {
				tempLecture.setRoom(roomChoice);
				
				int lecturerChoice=ScUtil.nextInt(scanner, "강의자의 유저코드를 입력해주시거나 0을 입력해 뒤로가기");
				//user테이블에 lecturerChoice를 id로 가진 row가 존재하지 않으면 null리턴
				UserDTO tempUser=userController.selectOne(lecturerChoice);
				//해당 유저가 존재하지 않거나, 존재하지만 강사등급이 아닐때
				while(lecturerChoice!=0&&(tempUser==null||tempUser.getRank()!=2)) {
					System.out.println("잘못된 입력입니다.");
					lecturerChoice=ScUtil.nextInt(scanner, "강의자의 유저코드를 입력해주시거나 0을 입력해 뒤로가기");
					tempUser=userController.selectOne(lecturerChoice);
				}
				if(lecturerChoice!=0) {
					tempLecture.setLecturerId(lecturerChoice);
					lectureController.insert(tempLecture);
					
					System.out.println("강의 추가가 완료되었습니다.");
				}
			}
		}
	}

	private void printListByStaff() {
		UserController userController=new UserController(connector);
		LectureController lectureController=new LectureController(connector);
		RoomController roomController=new RoomController(connector);
		MajorController majorController=new MajorController(connector);

		System.out.println("============================================================================");
		System.out.println("강의코드\t강의명\t\t전공\t\t강의실\t\t강사명\n");
		System.out.println("----------------------------------------------------------------------------");
		for(LectureDTO l:lectureController.selectAll()) {
			String tempMajor=majorController.majorString(l.getMajor());
			String tempRoom=roomController.roomString(l.getRoom());
			String tempLecturer=userController.selectOne(l.getLecturerId()).getName();
			
			System.out.printf("%d\t%8s\t%8s\t%s\t\t%s\n",l.getId(),l.getLectureName(),
					tempMajor,tempRoom,tempLecturer);
		}
		System.out.println("============================================================================");
		int lectureChoice=printLectureDetails();
		
		if(lectureChoice!=0) {
			int userChoice=ScUtil.nextInt(scanner, 1, 3, "1.해당강의 삭제하기 2.해당강의 수정하기 3.뒤로가기");
			if(userChoice==1) {
				deleteLecture(lectureChoice);
			} else if(userChoice==2) {
				updateLecture(lectureChoice);
			} else if(userChoice==3) {
				printListByStaff();
			}
		}
	}

	private void updateLecture(int lectureChoice) {
		UserController userController=new UserController(connector);
		LectureController lectureController=new LectureController(connector);
		MajorController majorController=new MajorController(connector);
		RoomController roomController=new RoomController(connector);
		LectureDTO tempLecture=lectureController.selectOne(lectureChoice);
		
		tempLecture.setSemester(ScUtil.nextInt(scanner, 1, 2, "새로운 학기를 입력해주세요 1 혹은 2"));
		tempLecture.setLectureName(ScUtil.nextLine(scanner, "새로운 강의명을 입력해주세요"));
		tempLecture.setSeatLeft(ScUtil.nextInt(scanner, 1, 300, "새로운 최대 수강신청 가능 인원을 입력해주세요"));
		tempLecture.setStartTime(ScUtil.nextInt(scanner, 1, 21, "새로운 강의시작시각을 입력해주세요 1 - 09:00 ~ 21 - 19:00"));
		tempLecture.setEndTime(ScUtil.nextInt(scanner, 1, 21, "새로운 강의종료시각을 입력해주세요 1 - 09:00 ~ 21 - 19:00"));
		tempLecture.setCredit(ScUtil.nextInt(scanner, 1, 3, "새로운 해당 강의의 학점을 입력해주세요"));
		tempLecture.setYear(ScUtil.nextLine(scanner, "새로운 해당강의의 수강가능 학년을 입력해주세요 ex) 2학년,3학년이 신청가능하다면 2,3 입력"));
		tempLecture.setDay(ScUtil.nextLine(scanner, "새로운 해당 강의가 있는 요일을 입력해주세요 ex)월요일,수요일에 있는 수업이면 mon,wed 입력"));
		
		int majorChoice=ScUtil.nextInt(scanner, "새로운 해당 강의의 전공분야를 입력해주시거나 0을입력해 뒤로가기");
		//major테이블에 majorChoice를 id로 가진 row가 존재하지 않으면 null리턴
		String tempMajor=majorController.majorString(majorChoice);
		
		while(majorChoice!=0&&tempMajor==null) {
			System.out.println("잘못된 입력입니다.");
			majorChoice=ScUtil.nextInt(scanner, "새로운 해당 강의의 전공분야를 입력해주시거나 0을 입력해 뒤로가기");
			tempMajor=majorController.majorString(majorChoice);
		}
		if(majorChoice!=0) {
			tempLecture.setMajor(majorChoice);
			
			int roomChoice=ScUtil.nextInt(scanner, "새로운 해당 강의의 강의실을 입력해주시거나 0을 입력해 뒤로가기");
			//room테이블에 roomChoice를 id로 가진 row가 존재하지 않으면 null리턴
			String tempRoom=roomController.roomString(roomChoice);
			
			while(roomChoice!=0&&tempRoom==null) {
				System.out.println("잘못된 입력입니다.");
				roomChoice=ScUtil.nextInt(scanner, "새로운 해당 강의의 강의실을 입력해주시거나 0을 입력해 뒤로가기");
				tempRoom=roomController.roomString(roomChoice);
			}
			if(roomChoice!=0) {
				tempLecture.setRoom(roomChoice);
				
				int lecturerChoice=ScUtil.nextInt(scanner, "새로운 강의자의 유저코드를 입력해주시거나 0을 입력해 뒤로가기");
				//user테이블에 lecturerChoice를 id로 가진 row가 존재하지 않으면 null리턴
				UserDTO tempUser=userController.selectOne(lecturerChoice);
				//해당 유저가 존재하지 않거나, 존재하지만 강사등급이 아닐때
				while(lecturerChoice!=0&&(tempUser==null||tempUser.getRank()!=2)) {
					System.out.println("잘못된 입력입니다.");
					lecturerChoice=ScUtil.nextInt(scanner, "새로운 강의자의 유저코드를 입력해주시거나 0을 입력해 뒤로가기");
					tempUser=userController.selectOne(lecturerChoice);
				}
				if(lecturerChoice!=0) {
					tempLecture.setLecturerId(lecturerChoice);
					lectureController.update(tempLecture);
					
					System.out.println("강의정보 수정이 완료되었습니다.");
					printOne(lectureChoice);
				}
			}
		}
	}

	private void deleteLecture(int lectureId) {
		String yOrN=ScUtil.nextLine(scanner, "정말로 삭제하시겠습니까? Y/N");
		if(yOrN.equalsIgnoreCase("y")) {
			LectureController controller=new LectureController(connector);
			
			controller.delete(lectureId);
			System.out.println("삭제가 완료되었습니다.");
		}
	}
}
