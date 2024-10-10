<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<HTML>
 <HEAD>
  <TITLE> 시맨틱 태그 테스트 </TITLE>
  <link href="../css/style.css" type="text/css" rel="stylesheet">
  <script>
  
  // 정규식
  const email = /[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[0-9a-zA-Z]$/i;  
  
  // 버튼을 눌렀을 때 함수 작동
  function check() {
	  
	  // 유효성 검사하기
	  let fm = document.frm;  // 객체 참조 변수. getElement로 안가져와도 되는게 편리하네
	  
	  if (fm.memberid.value == "") {
		  alert("아이디를 입력해주세요");
		  fm.memberid.focus();
		  return;
	  } else if (fm.memberpwd.value == "") {
		  alert("비밀번호를 입력해주세요");
		  fm.memberpwd.focus();
		  return;
	  } else if (fm.memberpwd2.value == "") {
		  alert("비밀번호2를 입력해주세요");
		  fm.memberpwd2.focus();
		  return;
	  } else if (fm.memberpwd.value != fm.memberpwd2.value) {
		  alert("비밀번호가 일치하지 않습니다");
		  fm.memberpwd.value = "";
		  fm.memberpwd2.value = "";
		  fm.memberpwd.focus();
		  return;
	  } else if (fm.membername.value == "") {
		  alert("이름을 입력해주세요");
		  fm.membername.focus();
		  return;
	  } else if (fm.membermail.value == "") {
		  alert("이메일을 입력해주세요");
		  fm.membermail.focus();
		  return;
	  } else if (email.test(fm.membermail.value) == false) {
		  alert("이메일 형식이 올바르지 않습니다");
		  fm.membermail.value = "";
		  fm.membermail.focus();
		  return;
	  } else if (fm.memberphon.value == "") {
		  alert("연락처를 입력해주세요");
		  fm.memberphon.focus();
		  return;
	  } else if (fm.memberbirth.value == "") {
		  alert("생년월일을 입력해주세요");
		  fm.memberbirth.focus();
		  return;
	  } else if (hobbyCheck() == false) {
		  alert("취미를 한개 이상 선택해주세요");
		  return;
	  }
	  
	  let ans = confirm("저장하시겠습니까?");
	  
	  if (ans == true) {
		  
		  // alert("이동할 정보를 등록할 차례입니다")
		  // action="memberJoinAction.jsp" method="post"  html 홈태그 기능을 자바스크립트로 제어
		  
		  // http://localhost:8088/webPr/member/memberJoinAction.jsp
		  // fm.action="./memberJoinAction.jsp";  // 상대경로
		  fm.action="<%=request.getContextPath()%>/member/memberJoinAction.jsp";  <%-- 절대경로, <%=request.getContextPath()%>는 project(webapp) 위치를 가져옴 --%>
		  fm.method="post";
		  fm.submit();
	  }
	  
	  
	  return;  // 리턴에 값을 안쓰면 그냥 멈춤 종료
  }
  
  function hobbyCheck() {
	  let arr = document.frm.memberhobby;  // 문서객체 안에 폼객체 안에 input[type=check] 객체 선언
	  let flag = false;  // 체크유무 초기값 false 선언
	  
	  for (let i = 0; i < arr.length; i++) {  // 선택한 여러값을 반복해서 출력
		  if (arr[i].checked == true) {  // 하나라도 선택했다면 true 리턴
			  flag = true;
			  break;
		  }
	  }
	  
/* 	  if (!flag) {
		  alert("취미를 한개 이상 선택해주세요");
		  fm.memberhobby.focus();
		  return false;
	  } */
	  return flag;
  }
  </script>
 </HEAD>

 <BODY>
	<header><a href="./memberJoin.jsp" style="text-decoration:none;">회원가입 페이지</a></header>
	<nav> <a href="./memberLogin.jsp" style="text-decoration:none;">회원 로그인 가기</a></nav>
	<article>
		<form name="frm">
			<table>
				<tr>
					<th class="idcolor">아이디</th>
					<td><input type="text" name="memberid" style="width:200px" maxlength="30" value="" placeholder="아이디를 입력하세요"></td>
				</tr>
				<tr>
					<th class="idcolor">비밀번호</th>
					<td><input type="password" name="memberpwd" style="width:200px" maxlength="30"></td>
				</tr>
				<tr>
					<th>비밀번호확인</th>
					<td><input type="password" name="memberpwd2" style="width:200px" maxlength="30"></td>
				</tr>
				<tr>
					<th id="name">이름</th>
					<td><input type="text" name="membername" style="width:300px" maxlength="30"></td>
				</tr>
				<tr>
					<th>이메일</th>
					<td><input type="email" name="membermail" style="width:300px" maxlength="30"></td>
				</tr>
				<tr>
					<th>연락처</th>
					<td><input type="number" name="memberphon" style="width:300px" maxlength="30"></td>
				</tr>
				<tr>
					<th>주소</th>
					<td>
						<select name="memberaddress" style="width:100px;">
							<option value="서울">서울</option>
							<option value="대전" selected>대전</option>
							<option value="부산">부산</option>
							<option value="인천">인천</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>성별</th>
					<td>
						<input type="radio" name="membergender" id="radio1" value="M"><label for="radio1"> 남성</label>
						<input type="radio" name="membergender" id="radio2" value="F" checked><label for="radio2"> 여성</label>
					</td>
				</tr>
				<tr>
					<th>생년월일</th>
					<td><input type="number" name="memberbirth" style="width:100px" maxlength="8"> 예)20240101</td>
				</tr>
				<tr>
					<th>취미</th>
					<td>
						<input type="checkbox" name="memberhobby" id="check1" value="야구"><label for="check1"></label> 야구
						<input type="checkbox" name="memberhobby" id="check2" value="축구"><label for="check2"></label> 축구
						<input type="checkbox" name="memberhobby" id="check3" value="농구"><label for="check3"></label> 농구
					</td>
				</tr>
				<tr>
					<td colspan="2" style="text-align: center">
					<button type="button" onclick="check();">
						<img src="https://cdn.pixabay.com/photo/2024/03/12/09/28/ai-generated-8628373_1280.png" width="30px" height="20px">
					</button>
					
<!-- 						<input type="submit" name="btn" value="회원정보 저장하기">
						<input type="reset" name="btn" value="초기화"> -->
					</td>
				</tr>
			</table>
		</form>
	</article>
	<footer>
	made by jh.
	</footer>
 </BODY>
</HTML>
