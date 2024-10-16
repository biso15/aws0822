<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Bom객체로 타이머 사용하기</title>
</head>
<body>

<img id="img" src="../image/strawberry.png" onmouseover="startTimer(5000);" onmouseout="cancleTimer()">

<button onclick="window.open('./javascript_size.jsp', 'test', 'width=300, height=300')">버튼 크기 조절 테스트</button> 
<script>
let timerId = null;

function startTimer(time) {
	timerId = setTimeout("load('http://naver.com')", time);
}

function load(url) {
	window.document.location.href = url;  // 이동한다
}

function cancleTimer() {
	clearTimeout(timerId);  // 타이머를 취소한다
}
</script>
</body>
</html>