<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<HTML>
 <HEAD>
  <TITLE> 로그인 페이지 </TITLE>
  <style>
	header { 
		width: 100%;
		height: 50px;
		text-align: center;
		--background-color: yellow; 
	}
	section {
		text-align: center;
		height: 200px;
		--background-color: olivedrab;
		border: 10px solid burlywood;
	}
	.id { float: left; width: 50%; height: 40px; margin-top: 30px; }
	.pw { float: left; width: 50%; height: 40px; margin-top: 30px; }
	.btn { clear: both; text-align: center; height: 40px; }
	input { margin-left: 20px; }
	footer {
		width: 100%;
		height: 150px;
		text-align: center;
		/* background-color: plum; */
		margin-top: 20px;
	}
  </style>
 </HEAD>

 <BODY>
	<header>로그인 페이지</header>
	<section>
		<form name="frm" action="./test0920_result.html" method="post">

			<p class="id">아이디 <input type="text" name="memberId" style="width:200px" maxlength="30" value=""></p>
			
			<p class="pw">비밀번호 <input type="password" name="memberPw" style="width:200px" maxlength="30"></p>
			
			<p class="btn"><input type="submit" name="btn" value="로그인하기"></p>

			<p><a href="#">아이디 찾기</a> / <a href="#">비밀번호 찾기</a>

		</form>
	</section>
	<footer>
	made by jh.
	</footer>	  
 </BODY>
</HTML>
