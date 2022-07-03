package viewer;

import java.util.Scanner;

import connector.DBConnector;
import controller.MajorController;
import controller.UserController;
import model.UserDTO;
import util.ScUtil;

public class UserViewer {
	private Scanner scanner;
	private UserDTO logInInfo;
	private DBConnector connector;
	
	public UserViewer(Scanner scanner,DBConnector connector) {
		this.scanner=scanner;
		this.connector=connector;
	}
	
	public UserViewer(Scanner scanner, DBConnector connector, UserDTO logInInfo) {
		this.scanner=scanner;
		this.connector=connector;
		this.logInInfo=logInInfo;
	}

	public void showIndex() {
		while(true) {
			int userChoice=ScUtil.nextInt(scanner, 1, 2,"1.로그인 2.종료");
			if(userChoice==1) {
				logIn();
				if(logInInfo!=null) {
					System.out.println("로그인 되었습니다.");
					//계정등급별로 메뉴 나누기 1.학생 2.교수 3.교직원
					if(logInInfo.getRank()==1) {
						studentMenu();
					} else if(logInInfo.getRank()==2) {
						lecturerMenu();
					} else if(logInInfo.getRank()==3) {
						staffMenu();
					}
				}
			} else {
				System.out.println("사용해 주셔서 감사합니다. 종료합니다.");
				break;
			}
		}
	}

	private void studentMenu() {
		while(logInInfo!=null) {
			int userChoice=ScUtil.nextInt(scanner, 1, 5, "1.수강신청 2.시간표보기 3.내 성적보기 4.비밀번호 수정 5.로그아웃");
			
			if(userChoice==1) {
				LectureViewer viewer=new LectureViewer(scanner,connector,logInInfo);
				
				viewer.applyLecture();
			} else if(userChoice==2) {
				StudentLectureViewer viewer=new StudentLectureViewer(scanner,connector,logInInfo);
				
				viewer.printTimeTable();
			} else if(userChoice==3) {
				StudentLectureViewer viewer=new StudentLectureViewer(scanner,connector,logInInfo);
				
				viewer.printGrade();
			} else if(userChoice==4) {
				updatePassword();
			} else if(userChoice==5) {
				logInInfo=null;
			}
		}
	}
	
	private void lecturerMenu() {
		while(logInInfo!=null) {
			int userChoice=ScUtil.nextInt(scanner, 1, 4, "1.내 강의 보기 2.강의시간표 보기 3.비밀번호 수정 4.로그아웃");
			
			if(userChoice==1) {
				LectureViewer viewer=new LectureViewer(scanner,connector,logInInfo);
				
				viewer.printListByLecturer();
			} else if(userChoice==2) {
				LectureViewer viewer=new LectureViewer(scanner,connector,logInInfo);
				
				viewer.printTimetable();
			} else if(userChoice==3) {
				updatePassword();
			} else if(userChoice==4) {
				logInInfo=null;
			}
		}
	}

	private void staffMenu() {
		while(logInInfo!=null) {
			int userChoice=ScUtil.nextInt(scanner, 1, 3, "1.강의관리 2.사용자 계정관리 3.로그아웃");
			
			if(userChoice==1) {
				LectureViewer viewer=new LectureViewer(scanner,connector,logInInfo);
				
				viewer.lectureManagement();
			} else if(userChoice==2) {
				userManagement();
			} else if(userChoice==3) {
				logInInfo=null;
			}
		}
	}
	
	private void userManagement() {
		int userChoice=ScUtil.nextInt(scanner, 1, 3, "1.새로운계정 생성하기 2.계정정보 수정하기 3.뒤로가기");
		
		if(userChoice==1) {
			insertUser();
		} else if(userChoice==2) {
			printUserList();
		} else if(userChoice==3) {}
	}

	private void printUserList() {
		UserController controller=new UserController(connector);
		System.out.println("============================================================================");
		System.out.println("유저코드\t등급\t이름\t학년\t전공코드\t잔여학점\n");
		System.out.println("----------------------------------------------------------------------------");
		for(UserDTO u:controller.selectAll()) {
			System.out.printf("%d\t%d\t%s\t%d\t%d\t%d\n",u.getId(),u.getRank(),u.getName(),u.getYear(),u.getMajor(),u.getCreditLeft());
		}
		
		int userChoice=ScUtil.nextInt(scanner, "삭제하거나 수정할 유저코드를 입력해주시거나 0을 입력해 뒤로가기");
		while(userChoice!=0&&controller.selectOne(userChoice)==null) {
			System.out.println("잘못된 입력입니다.");
			userChoice=ScUtil.nextInt(scanner, "삭제하거나 수정할 유저코드를 입력해주시거나 0을 입력해 뒤로가기");
		}
		if(userChoice!=0) {
			int editChoice=ScUtil.nextInt(scanner, 1, 3, "1.삭제하기 2.수정하기 3.뒤로가기");
			if(editChoice==1) {
				deleteUser(userChoice);
			} else if(editChoice==2) {
				updateUser(userChoice);
			}
		}
	}

	private void deleteUser(int userId) {
		String yOrN=ScUtil.nextLine(scanner, "정말로 삭제하시겠습니까? Y/N");
		
		if(yOrN.equalsIgnoreCase("y")) {
			UserController controller=new UserController(connector);
			controller.delete(userId);
			
			System.out.println("삭제가 완료되었습니다.");
			printUserList();
		}
	}
	
	private void updateUser(int userId) {
		UserController userController=new UserController(connector);
		MajorController majorController=new MajorController(connector);
		UserDTO tempUser=userController.selectOne(userId);

		tempUser.setName(ScUtil.nextLine(scanner, "새로운 이름을 입력해주세요"));
		tempUser.setRank(ScUtil.nextInt(scanner, 1, 3, "새로운 등급을 입력해주세요"));
		if(tempUser.getRank()==1) {
			tempUser.setYear(ScUtil.nextInt(scanner, 1, 4, "새로운 학년을 입력해주세요"));
			tempUser.setCreditLeft(ScUtil.nextInt(scanner, 1, 30, "최대 신청가능 학점을 입력해주세요"));
			
			int tempMajor=ScUtil.nextInt(scanner, "새로운 전공코드를 입력해주시거나 0을 입력해 뒤로가기");
			while(tempMajor!=0&&majorController.majorString(tempMajor)==null) {
				System.out.println("잘못된 입력입니다.");
				tempMajor=ScUtil.nextInt(scanner, "새로운 전공코드를 입력해주시거나 0을 입력해 뒤로가기");
			}
			if(tempMajor!=0) {
				tempUser.setMajor(tempMajor);
				
				userController.updateByAdmin(tempUser);
				System.out.println("회원정보 수정이 완료되었습니다.");
				printUserList();
			}
		}
		userManagement();
	}

	private void insertUser() {
		UserDTO tempUser=new UserDTO();
		tempUser.setName(ScUtil.nextLine(scanner, "이름을 입력해주세요"));
		tempUser.setPassword(ScUtil.nextLine(scanner, "비밀번호를 입력해주세요"));
		tempUser.setRank(ScUtil.nextInt(scanner, 1, 3, "등급을 입력해주세요"));
		
		if(tempUser.getRank()==1) {
			tempUser.setYear(ScUtil.nextInt(scanner, 1, 4, "학년을 입력해주세요"));
			tempUser.setCreditLeft(ScUtil.nextInt(scanner, 1, 30, "최대 신청가능 학점을 입력해주세요"));
			MajorController majorController=new MajorController(connector);
			int majorChoice=ScUtil.nextInt(scanner, "전공코드를 입력해주시거나 0을 입력해 뒤로가기");
			String tempMajor=majorController.majorString(majorChoice);
			//전공코드가 존재하지 않으면
			while(majorChoice!=0&&tempMajor==null) {
				System.out.println("잘못된 입력입니다.");
				
				majorChoice=ScUtil.nextInt(scanner, "전공코드를 입력해주세요 0을 입력해 뒤로가기");
				tempMajor=majorController.majorString(majorChoice);
			}
			if(majorChoice!=0) {
				UserController userController=new UserController(connector);
				tempUser.setMajor(majorChoice);
				
				userController.insert(tempUser);
				System.out.println("사용자 등록이 완료되었습니다.");
			}
		} else {
			UserController userController=new UserController(connector);
			
			userController.insert(tempUser);
			System.out.println("사용자등록이 완료되었습니다.");
		}
	}

	private void logIn() {
		int tempId=ScUtil.nextInt(scanner, "학번 혹은 교수코드를 입력해주세요");
		String tempPassword=ScUtil.nextLine(scanner, "비밀번호를 입력해주세요");
		
		UserController controller=new UserController(connector);
		
		logInInfo=controller.logInValid(tempId, tempPassword);
		
		while(logInInfo==null) {
			System.out.println("잘못 입력하셨습니다.");
			String userChoice=ScUtil.nextLine(scanner, "다시 입력하시겠습니까? Y/N");
			
			if(userChoice.equalsIgnoreCase("y")) {
				tempId=ScUtil.nextInt(scanner, "학번 혹은 교수코드를 입력해주세요");
				tempPassword=ScUtil.nextLine(scanner, "비밀번호를 입력해주세요");
				
				logInInfo=controller.logInValid(tempId, tempPassword);
			} else {
				break;
			}
		}
	}
	
	private void updatePassword() {
		UserController controller=new UserController(connector);
		
		String tempPassword=ScUtil.nextLine(scanner, "새로 사용할 비밀번호를 입력해주세요");
		String oldPassword=ScUtil.nextLine(scanner, "기존 비밀번호를 입력해주세요");
		if(logInInfo.getPassword().equals(controller.convertToSha(oldPassword))) {
			logInInfo.setPassword(controller.convertToSha(tempPassword));
			
			controller.update(logInInfo);
			System.out.println("비밀번호가 수정되었습니다.");
		}
	}
}
