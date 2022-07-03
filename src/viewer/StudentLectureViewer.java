package viewer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import connector.DBConnector;
import controller.LectureController;
import controller.MajorController;
import controller.RoomController;
import controller.StudentLectureController;
import controller.TimeController;
import controller.UserController;
import model.LectureDTO;
import model.StudentLectureDTO;
import model.UserDTO;
import util.ScUtil;

public class StudentLectureViewer {
	private Scanner scanner;
	private UserDTO logInInfo;
	private DBConnector connector;

	public StudentLectureViewer(Scanner scanner, DBConnector connector, UserDTO logInInfo) {
		this.logInInfo=logInInfo;
		this.scanner=scanner;
		this.connector=connector;
	}
	
	public void printMyAppliedList() {
		StudentLectureController sLectureController=new StudentLectureController(connector);
		TimeController timeController=new TimeController(connector);
		
		System.out.println("내가 신청한 과목들\n");
		System.out.println("수강신청코드\t강의명\t\t요일\t시작시간\t마치는시간\n");
		System.out.println("----------------------------------------------------------------------------");
		for(StudentLectureDTO s:sLectureController.selectAllByStudentId(logInInfo.getId())) {
			LectureController lectureController=new LectureController(connector);
			
			LectureDTO l=lectureController.selectOne(s.getLectureId());
			System.out.printf("%d\t%s\t\t%s\t%s\t%s\n",l.getId(),l.getLectureName(),
					l.getDay(),timeController.timeString(l.getStartTime()),
					timeController.timeString(l.getEndTime()));
		}
	}

	public void applyByStudent() {
		LectureController lectureController=new LectureController(connector);
		StudentLectureController sLectureController=new StudentLectureController(connector);
		int lectureChoice=ScUtil.nextInt(scanner, "신청할 강의코드를 입력하시거나 0을 입력해 뒤로가기");
		
		//강의가 존재하지 않거나 이미 등록한 강의일때
		//수강신청코드를 입력해 진행할수도 있지만 신청자의 수강신청정보가 맞는지 확인하는 작업이 추가되어 번거로워지므로...
		//lectureController.selectOne(lectureChoice))는 lectureId로 강의정보를 가져옴
		//studentLectureController.duplicateCheck(logInInfo.getId(), lectureChoice)는 userId와 lectureId로 수강신청정보를 가져옴
		while(lectureChoice!=0&&(lectureController.selectOne(lectureChoice))==null||
				sLectureController.selectOneByTwoId(logInInfo.getId(), lectureChoice)!=null) {
			System.out.println("잘못된 입력입니다.");
			lectureChoice=ScUtil.nextInt(scanner, "신청할 강의코드를 입력하시거나 0을 입력해 뒤로가기");
		}
		if(lectureChoice!=0) {
			LectureDTO tempLecture=lectureController.selectOne(lectureChoice);
			
			//1.남은 좌석수가 존재하고 2.잔여학점이 신청하려는 강의의 학점이상이고, 3.시간표 중복이 아니며, 4.학년 조건이 맞으면
			if(tempLecture.getSeatLeft()>0&&logInInfo.getCreditLeft()>=tempLecture.getCredit()
					&&validTime(lectureChoice)==true&&validYear(lectureChoice)==true) {
				StudentLectureDTO s=new StudentLectureDTO();
				
				s.setStudentId(logInInfo.getId());
				s.setLectureId(lectureChoice);
				
				sLectureController.insert(s);
				//현재 남은 좌석수에서 1자리 빼고 update
				tempLecture.setSeatLeft(tempLecture.getSeatLeft()-1);
				lectureController.update(tempLecture);
				System.out.println("강의 신청이 완료되었습니다.");
			} else {
				if(tempLecture.getSeatLeft()<=0) {
					System.out.println("수강신청 가능인원이 가득찼습니다.");
				} else if(logInInfo.getCreditLeft()<tempLecture.getCredit()) {
					System.out.println("신청 가능한 학점을 초과하였습니다.");
				} else if(validTime(lectureChoice)==false) {
					System.out.println("시간표가 중복입니다.");
				} else if(validYear(lectureChoice)==false) {
					System.out.println("학년 조건이 맞지 않습니다.");
				}
			}
		}
	}
	//로그인한 사용자가 본인의시간표 중복 체크하는 메소드
	private boolean validTime(int lectureId) {
		boolean result=true;
		LectureController lectureController=new LectureController(connector);
		StudentLectureController sLectureController=new StudentLectureController(connector);
		//신청하려는 강의DTO
		LectureDTO tempLecture=lectureController.selectOne(lectureId);
		//신청하려는 강의의 요일을 담은 배열
		String[] tempDay=tempLecture.getDay().split(",");

		for(StudentLectureDTO l:sLectureController.selectAllByStudentId(logInInfo.getId())) {
			//이미 신청한 강의중 하나의 강의DTO
			LectureDTO myLecture=lectureController.selectOne(l.getLectureId());
			//이미 신청한 강의중 하나의 요일을 담은 배열
			String[] myDay=myLecture.getDay().split(",");
			
			for(int i=0;i<tempDay.length;i++) {
				if(Arrays.binarySearch(myDay, tempDay[i])>0) {
					if(tempLecture.getStartTime()<myLecture.getEndTime()
							&&tempLecture.getEndTime()>myLecture.getStartTime()) {
						result=false;
					}
				}
			}
		}
		
		return result;
	}
	//다른 사용자의 시간표 중복을 체크하는 메소
	private boolean validTime(UserDTO student,int lectureId) {
		boolean result=true;
		LectureController lectureController=new LectureController(connector);
		StudentLectureController sLectureController=new StudentLectureController(connector);
		//신청하려는 강의DTO
		LectureDTO tempLecture=lectureController.selectOne(lectureId);
		//신청하려는 강의의 요일을 담은 배열
		String[] tempDay=tempLecture.getDay().split(",");

		for(StudentLectureDTO l:sLectureController.selectAllByStudentId(student.getId())) {
			//이미 신청한 강의중 하나의 강의DTO
			LectureDTO myLecture=lectureController.selectOne(l.getLectureId());
			//이미 신청한 강의중 하나의 요일을 담은 배열
			String[] myDay=myLecture.getDay().split(",");
			
			for(int i=0;i<tempDay.length;i++) {
				if(Arrays.binarySearch(tempDay, myDay[i])>0) {
					if(tempLecture.getStartTime()<myLecture.getEndTime()
							&&tempLecture.getEndTime()>myLecture.getStartTime()) {
						result=false;
					}
				}
			}
		}
		
		return result;
	}
	
	private boolean validYear(int lectureId) {
		boolean result=true;
		LectureController lectureController=new LectureController(connector);
		//신청하려는 강의DTO
		LectureDTO tempLecture=lectureController.selectOne(lectureId);
		//신청하려는 강의의 학년을 담은 배열
		String[] tempYear=tempLecture.getYear().split(",");
		
		if(Arrays.binarySearch(tempYear, String.valueOf(logInInfo.getYear()))<0) {
			result=false;
		}
		
		return result;
	}

	public void deleteByStudent() {
		LectureController lectureController=new LectureController(connector);
		StudentLectureController sLectureController=new StudentLectureController(connector);
		
		int deleteChoice=ScUtil.nextInt(scanner, "삭제할 강의코드를 입력하시거나 0을 입력해 뒤로가기");
		
		//강의가 존재하지 않거나 신청하지않은 강의일때
		//lectureController.selectOne(lectureChoice))는 lectureId로 강의정보를 가져옴
		//studentLectureController.duplicateCheck(logInInfo.getId(), lectureChoice)는 userId와 lectureId로 수강신청정보를 가져옴
		while(deleteChoice!=0&&(lectureController.selectOne(deleteChoice)==null||
				sLectureController.selectOneByTwoId(logInInfo.getId(), deleteChoice)==null)) {
			System.out.println("잘못된 입력입니다.");
			deleteChoice=ScUtil.nextInt(scanner, "삭제할 강의코드를 입력하시거나 0을 입력해 뒤로가기");
		}
		if(deleteChoice!=0) {
			LectureDTO tempLecture=lectureController.selectOne(deleteChoice);
			sLectureController.delete(logInInfo.getId(), deleteChoice);
			
			tempLecture.setSeatLeft(tempLecture.getSeatLeft()-1);
			lectureController.update(tempLecture);
			System.out.println("삭제가 완료되었습니다.");
		}
	}

	public void printTimeTable() {
		ArrayList<String> mon=new ArrayList<>();
		ArrayList<String> tue=new ArrayList<>();
		ArrayList<String> wed=new ArrayList<>();
		ArrayList<String> thu=new ArrayList<>();
		ArrayList<String> fri=new ArrayList<>();
		
		StudentLectureController sLecturecontroller=new StudentLectureController(connector);
		
		for(StudentLectureDTO s:sLecturecontroller.selectAllByStudentId(logInInfo.getId())) {
			LectureController lectureController=new LectureController(connector);
			RoomController roomController=new RoomController(connector);
			TimeController timeController=new TimeController(connector);
			LectureDTO l=lectureController.selectOne(s.getLectureId());
			
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

	public void printGrade() {
		StudentLectureController sLectureController=new StudentLectureController(connector);
		LectureController lectureController=new LectureController(connector);
		
		for(StudentLectureDTO s:sLectureController.selectAllByStudentId(logInInfo.getId())) {
			String lectureName=lectureController.selectOne(s.getLectureId()).getLectureName();
			
			System.out.printf("과목명:%s 학점:%f\n",lectureName,s.getScore());
		}
		System.out.println("학점 평균:"+sLectureController.getAverageScore(logInInfo.getId()));
	}

	public void scoreUpdate(int lectureId) {
		UserController userController=new UserController(connector);
		StudentLectureController sLectureController=new StudentLectureController(connector);
		printStudentList(lectureId);
		
		int studentChoice=ScUtil.nextInt(scanner, "성적을 입력&수정할 학생의 코드를 입력해주시거나 0을 입력해 뒤로가기");
		//성적편집할 학생DTO
		UserDTO tempUser=userController.selectOne(studentChoice);
		//학생의 수강정보DTO
		StudentLectureDTO tempsLecture=sLectureController.selectOneByTwoId(studentChoice, lectureId);
		
		//1.존재하는 학생 코드인지 2.해당강의를 듣는 학생인지 
		while(studentChoice!=0&&(tempUser==null||tempsLecture==null)) {
			System.out.println("잘못된 입력입니다.");
			studentChoice=ScUtil.nextInt(scanner, "성적을 입력하거나 수정할 학생의 코드를 입력해주시거나 0을 입력해 뒤로가기");
			tempUser=userController.selectOne(studentChoice);
			tempsLecture=sLectureController.selectOneByTwoId(studentChoice, lectureId);
		}
		if(studentChoice!=0) {
			double tempScore=gradeStringToDouble(ScUtil.nextLine(scanner,
					"학점을 입력해주세요 A+, A0, B+, B0, C+, C0, D+, D0, F\nQ를 입력하면 뒤로가기\n"));
			//조건에 맞지않는 값을 입력해 switch문의 default작동
			//실수형 변수의 미세한 오차를 고려해 조건식 값 넉넉하게 선택
			while(tempScore<-1.5) {
				System.out.println("잘못된 입력입니다.");
				tempScore=gradeStringToDouble(ScUtil.nextLine(scanner,
						"학점을 입력해주세요 A+, A0, B+, B0, C+, C0, D+, D0, F\nQ를 입력하면 뒤로가기\n"));
			}
			//Q가 아닌값을 입력해 점수가 양수일때
			if(tempScore>-0.5) {
				tempsLecture.setScore(tempScore);
				sLectureController.update(tempsLecture);
				System.out.println("점수가 갱신되었습니다.");
				
			}
		}
	}
	
	private void printStudentList(int lectureId) {
		StudentLectureController sLecturecontroller=new StudentLectureController(connector);
		MajorController majorController=new MajorController(connector);
		UserController userController=new UserController(connector);
		//해당 강의의 수강정보들을 가져와 학생 리스트 출력
		System.out.println("============================================================================");
		System.out.printf("학생코드\t이름\t학년\t전공\t학점\n");
		System.out.println("----------------------------------------------------------------------------");
		for(StudentLectureDTO s:sLecturecontroller.selectAllByLectureId(lectureId)) {
			UserDTO temp=userController.selectOne(s.getStudentId());
			System.out.printf("%d\t%s\t%d\t%s\t%.1f\n",s.getStudentId(),temp.getName(),
					temp.getYear(),majorController.majorString(temp.getMajor()),s.getScore());
		}
		System.out.println("============================================================================");
	}
	
	private double gradeStringToDouble(String tempGrade) {
		double tempScore;
		
		switch(tempGrade) {
			case "A+":tempScore=4.5;
				break;
			case "A0":tempScore=4.0;
				break;
			case "B+":tempScore=3.5;
				break;
			case "B0":tempScore=3.0;
				break;
			case "C+":tempScore=2.5;
				break;
			case "C0":tempScore=2.0;
				break;
			case "D+":tempScore=1.5;
				break;
			case "D0":tempScore=1.0;
				break;
			case "F":tempScore=0.0;
				break;
			case "Q":tempScore=-1.0;
				break;
			default:tempScore=-2.0;
				break;
		}
		
		return tempScore;
	}

	public void additionalApply(int lectureChoice) {
		UserController userController=new UserController(connector);
		LectureController lectureController=new LectureController(connector);
		StudentLectureController sLectureController=new StudentLectureController(connector);
		int studentChoice=ScUtil.nextInt(scanner, "추가수강신청 받을 학생의 코드를 입력해주시거나 0을 입력해 뒤로가기");
		//추가신청 받을 학생DTO
		UserDTO tempUser=userController.selectOne(studentChoice);
		//추가신청 받을 lecture
		LectureDTO tempLecture=lectureController.selectOne(lectureChoice);
		//추가신청 학생의 이 강의에 대한 DTO
		StudentLectureDTO tempsLecture=sLectureController.selectOneByTwoId(studentChoice, lectureChoice);
		
		//존재하는 학생코드인지
		while(studentChoice!=0&&tempUser==null) {
			System.out.println("존재하지 않는 학생코드입니다.");
			studentChoice=ScUtil.nextInt(scanner, "추가수강신청 받을 학생의 코드를 입력해주시거나 0을 입력해 뒤로가기");
			tempUser=userController.selectOne(studentChoice);
			tempLecture=lectureController.selectOne(lectureChoice);
			tempsLecture=sLectureController.selectOneByTwoId(studentChoice, lectureChoice);
		}
		if(studentChoice!=0) {
			//1.학생의 시간표가 겹치지 않는지 2.잔여학점이 충분한지 3.이미 등록한 강의는 아닌지
			if(tempsLecture==null&&tempUser.getCreditLeft()>=tempLecture.getCredit()
					&&validTime(tempUser,lectureChoice)==true) {
				//새로운 수강정보 생성 후 insert
				tempsLecture=new StudentLectureDTO();
				tempsLecture.setStudentId(studentChoice);
				tempsLecture.setLectureId(lectureChoice);
				sLectureController.insert(tempsLecture);
				//잔여좌석 -1한 후 update
				tempLecture.setSeatLeft(tempLecture.getSeatLeft()-1);
				lectureController.update(tempLecture);
				System.out.println("추가수강신청 등록이 완료되었습니다.");
			} else if(tempsLecture!=null) {
				System.out.println("이미 해당강의를 신청하셨습니다.");
			} else if(tempUser.getCreditLeft()<tempLecture.getCredit()) {
				System.out.println("신청하려는 강의의 학점이 잔여학점을 초과합니다.");
			} else if(validTime(tempUser,lectureChoice)==false) {
				System.out.println("시간표가 중복입니다.");
			} else {
				System.out.println("비정상적인 값입니다.");
			}
		}
		
	}
}
