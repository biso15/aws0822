<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file = "/common/dbconn.jsp" %>
<%@ include file = "/common/function.jsp" %>
<jsp:useBean id = "mv" class="Vo.MemberVo" scope = "page" />
<!--
scope는 4가지가 있음
1. page : 페이지내에서만
2. request : 전송하는 범위까지
3. session : 서버에서 끝날때까지(로그아웃)
4. application : 프로그램이 살아있을때까지
-->

<jsp:setProperty name = "mv" property = "*" />  <!-- 아래 .getParameter 대체.-->
    
<%

/* 	String memberId = request.getParameter("memberId");
	System.out.println("memberId값은?" + memberId);
	System.out.println("<br>");
	String memberPw = request.getParameter("memberPw");
	System.out.println("memberPw값은?" + memberPw);
	System.out.println("<br>");
	String memberPw2 = request.getParameter("memberPw2");
	System.out.println("memberPw2값은?" + memberPw2);
	System.out.println("<br>");
	String memberName = request.getParameter("memberName");
	System.out.println("memberName값은?" + memberName);
	System.out.println("<br>");
	String memberMail = request.getParameter("memberMail");
	System.out.println("memberMail값은?" + memberMail);
	System.out.println("<br>");
	String memberPhon = request.getParameter("memberPhon");
	System.out.println("memberPhon값은?" + memberPhon);
	System.out.println("<br>");
	String memberAddress = request.getParameter("memberAddress");
	System.out.println("memberAddress값은?" + memberAddress);
	System.out.println("<br>");
	String memberGender = request.getParameter("memberGender");
	System.out.println("memberGender값은?" + memberGender);
	System.out.println("<br>");
	String memberBirth = request.getParameter("memberBirth");
	System.out.println("memberBirth값은?" + memberBirth);
	System.out.println("<br>"); 
	
	String[] memberHobby = request.getParameterValues("memberHobby");  // checkbox는 여러개의 값을 가지고 있으므로 배열로 사용해야 함
	System.out.println("memberHobby값은?");
	String memberInHobby = "";
	for(int i = 0; i<memberHobby.length; i++) {
	memberInHobby = memberInHobby + memberHobby[i] + ", ";
	   System.out.println((i+1) + ". " + memberHobby[i]);
	}
	System.out.println(memberInHobby); */

  
// 1. jsp 프로그래밍(날코딩 날코딩방법부터 -> 함수화 -> 객체화 방식)
// 2. java/jsp 프로그래밍(model1, model2 MVC방식으로 진화되는 방법)
// 3. spring 프레임워크로 프로그래밍 하는 방법

 
/*
// 1. createStatement
// conn 객체 안에는 많은 메소드가 있는데, 일단 createStatement 메소드를 사용해서 쿼리 작성 -> 작은따옴표(')를 사용하므로 sql인젝션 공격에 취약함

String sql = "insert into " +
			"member(memberid,memberpwd,membername,membergender,memberbirth,memberaddr,memberphone,memberemail,memberhobby) " + 
			"values('" + memberId + "', '" + memberPw + "', '" + memberName + "', '" + memberGender + "', '" + memberBirth + "', '" + memberAddress + "', '" + memberPhon + "', '" + memberMail + "', '" + memberInHobby + "')";
Statement stmt = conn.createStatement();  // 쿼리 구문을 동작시키는 클래스 Statement
int value = stmt.executeUpdate(sql);  // value가 0이면 미입력, 1이면 입력됨
*/

/*

// 2. prepareStatement

String sql = "insert into " +
	"member(memberid,memberpwd,membername,membergender,memberbirth,memberaddr,memberphone,memberemail,memberhobby) " + 
	"values(?, ?, ?, ?, ?, ?, ?, ?, ?)";

PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setString(1, memberId);
pstmt.setString(2, memberPw);
pstmt.setString(3, memberName);
pstmt.setString(4, memberGender);
pstmt.setString(5, memberBirth);
pstmt.setString(6, memberAddress);
pstmt.setString(7, memberPhon);
pstmt.setString(8, memberMail);
pstmt.setString(9, memberInHobby);
int value = pstmt.executeUpdate();

-> function.jsp에서 메서드로 만듦
*/
 

 // 매개변수에 인자값 대입해서 함수 호출
/*  int value = memberInsert(conn, memberId, memberPw, memberName, memberGender, memberBirth, memberAddress, memberPhon, memberMail, memberInHobby); */
	String[] memberhobby = request.getParameterValues("memberhobby");
	String memberInhobby = "";
	for(int i = 0; i<memberhobby.length; i++) {
	memberInhobby = memberInhobby + memberhobby[i] + ", ";
	   System.out.println((i+1) + ". " + memberhobby[i]);
	}
	
 int value = memberInsert(
		 conn,
		 mv.getMemberid(),
		 mv.getMemberpwd(),
		 mv.getMembername(),
		 mv.getMembergender(),
		 mv.getMemberbirth(),
		 mv.getMemberaddr(),
		 mv.getMemberphone(),
		 mv.getMemberemail(),
		 memberInhobby
 );  /*  Vo 메서드 값을 사용해서 호출 */
 
 // value값이 1이면 입력성공, 0이면 입력실패
 // 1이면 성공했기 때문에 다른 페이지로 이동시키고 0이면 다시 회원가입 입력 페이지로 간다
 
 String pageUrl = "";
 String msg = "";
 if (value == 1) {
	 
	 msg = "회원가입되었습니다.";
	 pageUrl = request.getContextPath() + "/";  // index.jsp파일은 web.xml 웹 설정파일에 기본등록되어있어서 생략 가능
	 // request.getContextPath() : 프로젝트 이름
	 // response.sendRedirect(pageUrl);  // 전송방식 sendRedirect는 요청받으면 다시 그쪽으로 가라고 지시하는 방법. 지시 후 실행코드가 있으면 실행이 되서 javascript로 페이지 이동시킴
	 
 } else {
	 msg = "회원가입 오류 발생하였습니다.";
	 pageUrl = request.getContextPath() + "/member/memberJoin.jsp";
	 // response.sendRedirect(pageUrl);
 }

 // 페이지 이동방식
 // sendRedirect : pageUrl로 다시 돌아가세요(나를 다시 거쳐서 감)
 // forward : pageUrl로 바로 이동시킴(나를 안거침)
 %>
 
 <script>
 alert("<%= msg %>");
 // document 객체 안에 location 객체 안에 주소 속성에 담아서 자바스크립트로 페이지를 이동시킨다. 
 document.location.href="<%= pageUrl %>";
 </script>