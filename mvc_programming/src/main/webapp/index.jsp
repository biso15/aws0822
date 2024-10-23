<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     
<%
/*
System.out.println("안녕하세요");

out.println("웹페이지에서 안녕하세요");
*/

String msg = "";
if (session.getAttribute(msg) != null) {
	msg = (String)session.getAttribute(msg);
}
session.setAttribute("msg", "");  // setAttribute를 빈 값으로 변경

// 로그인
int midx = 0;
String memberId = "";
String memberName = "";
String alt = "";
String logMsg = "";
if (session.getAttribute("midx") != null) {  // 로그인이 되었으면
	midx = (int)session.getAttribute("midx");
	memberId = (String)session.getAttribute("mid");
	memberName = (String)session.getAttribute("memberName");
	
	alt = memberName+"님 로그인되었습니다.";
	logMsg = "<a href='"+request.getContextPath()+"/member/memberLogout.aws'>로그아웃</a>";
	
} else {
	alt = "로그인 하세요.";
	logMsg = "로그인";
}

%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="./css/style.css" type="text/css" rel="stylesheet">
</head>
<body>
	<%= alt %>
	<%= logMsg %>
	<div class="main">환영합니다. 메인페이지입니다.</div>
	<div><a href="<%=request.getContextPath()%>/member/memberJoin.aws">회원가입 페이지 가기</a></div>  <!-- request.getContextPath() : 프로젝트 이름 -->
	<div><a href="<%=request.getContextPath()%>/member/memberLogin.aws">회원로그인 하기</a></div>
	<div><a href="<%=request.getContextPath()%>/member/memberList.aws">회원목록 가기</a></div>
	<div><a href="<%=request.getContextPath()%>/board/boardList.aws">게시판 가기</a></div>
</body>

<script>
<% if (!msg.equals("")) { %>
	alert(<%= msg %>);
<% } %>
</script>
</html>