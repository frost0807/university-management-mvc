package util;

import java.util.Scanner;

public class ScUtil {
	public static double nextDouble(Scanner sc,double min,double max,String msg) {
		System.out.println(msg);
		System.out.print(">");
		double result=sc.nextDouble();
		while(result<min||result>max) {
			System.out.println("잘못된 범위의 값입니다. 다시 입력해주세요");
			result=sc.nextDouble();
		}
		sc.nextLine();
		return result;
	}
	public static double nextDouble(Scanner sc,String msg) {
		System.out.println(msg);
		System.out.print(">");
		double result=sc.nextDouble();
		sc.nextLine();
		return result;
	}
	public static int nextInt(Scanner sc,int min,int max,String msg) {
		System.out.println(msg);
		System.out.print(">");
		int result=sc.nextInt();
		while(result<min||result>max) {
			System.out.println("잘못된 범위의 값입니다. 다시 입력해주세요");
			result=sc.nextInt();
		}
		sc.nextLine();
		return result;
	}
	public static int nextInt(Scanner sc,int min,int max) {
		System.out.print(">");
		int result=sc.nextInt();
		while(result<min||result>max) {
			System.out.println("잘못된 범위의 값입니다. 다시 입력해주세요");
			result=sc.nextInt();
		}
		sc.nextLine();
		return result;
	}

	public static int nextInt(Scanner sc,String msg) {	
		System.out.println(msg);
		System.out.print(">");
		int result=sc.nextInt();
		sc.nextLine();
		return result;
	}
	public static int nextInt(Scanner sc) {		
		System.out.print(">");
		int result=sc.nextInt();
		sc.nextLine();
		return result;
	}

	public static String nextLine(Scanner sc,String msg) {
		System.out.println(msg);
		System.out.println(">");
		String result=sc.nextLine();
		return result;
	}
	public static String nextLine(Scanner sc) {
		System.out.println(">");
		String result=sc.nextLine();
		return result;
	}

	public static String nextLine(Scanner sc,int length,String msg) {
		System.out.println(msg);
		System.out.println(">");
		String result=sc.nextLine();
		if(result.length()!=length) {
			System.out.println("길이가 잘못되었습니다. 다시 입력해주세요");
			result=sc.nextLine();
		}
		return result;
	}
	public static String nextLine(Scanner sc,int length) {
		System.out.println(">");
		String result=sc.nextLine();
		if(result.length()!=length) {
			System.out.println("길이가 잘못되었습니다. 다시 입력해주세요");
			result=sc.nextLine();
		}
		return result;
	}
}
