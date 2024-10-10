<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>  <!-- Connection, PreparedStatement 사용하기 위해 import -->
<%!

// 어디서나 접근 가능 public, 리턴 타입은 숫자형 int = 메소드 타입과 같음

public int memberInsert(Connection conn, String memberId, String memberPw, String memberName, String memberGender, String memberBirth, 
			String memberAddress, String memberPhon, String memberMail, String memberInHobby) {

	int value = 0;  // 메소드 지역변수 결과값을 담는다
	String sql = "";
	PreparedStatement pstmt = null;  // 쿼리 구문 클래스 선djs
	try {
		sql = "insert into " +
				"member(memberid,memberpwd,membername,membergender,memberbirth,memberaddr,memberphone,memberemail,memberhobby) " + 
				"values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
		 pstmt = conn.prepareStatement(sql);
		 
		 // 문자형 메소드 사용, 숫자형은 setInt(번호, 값);
		 pstmt.setString(1, memberId);
		 pstmt.setString(2, memberPw); 
		 pstmt.setString(3, memberName);
		 pstmt.setString(4, memberGender);
		 pstmt.setString(5, memberBirth);
		 pstmt.setString(6, memberAddress);
		 pstmt.setString(7, memberPhon);
		 pstmt.setString(8, memberMail);
		 pstmt.setString(9, memberInHobby);
		 value = pstmt.executeUpdate();  // 구문객체 실행하면 성공시 1, 실패시 0 리턴
		
	} catch(Exception e) {
		e.printStackTrace();
		
	} finally {  // try를 했던 catch를 했던 꼭 실행해야 하는 영역
		// 객체 사라지게하고
		// DB 연결 끊기
		try {
			pstmt.close();
			conn.close();			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	return value;
}
 
%>