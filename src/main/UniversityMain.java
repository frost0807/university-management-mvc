package main;

import java.util.Scanner;

import connector.DBConnector;
import connector.MySqlConnector;
import viewer.UserViewer;

public class UniversityMain {

	public static void main(String[] args) {
		Scanner scanner=new Scanner(System.in);
		DBConnector connector=new MySqlConnector();
		UserViewer viewer=new UserViewer(scanner,connector);
		viewer.showIndex();
		
		scanner.close();
	}

}
