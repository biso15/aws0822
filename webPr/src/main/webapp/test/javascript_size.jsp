<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>윈도우 사이즈 크기 조절</title>

<script>
function startScroll(interval) {
	setInterval("autoScroll()", interval);
}

function autoScroll() {
	window.scrollBy(0, 10);  // 10픽셀씩 이동
}
</script>
</head>
<body onload="startScroll(1000);">
<h3>윈도우 위치와 크기조절</h3>
<button onclick="window.moveBy(-10, 0)">left</button>
<button onclick="window.moveBy(10, 0)">right</button>
<button onclick="window.moveBy(0, 10)">down</button>
<button onclick="window.moveBy(0, -10)">up</button>

<br><br>

무<br>
궁<br>
화<br>
꽃<br>
이<br>
피<br>
었<br>
습<br>
니<br>
다<br><br>
무<br>
궁<br>
화<br>
꽃<br>
이<br>
피<br>
었<br>
습<br>
니<br>
다<br><br>
무<br>
궁<br>
화<br>
꽃<br>
이<br>
피<br>
었<br>
습<br>
니<br>
다<br><br>
무<br>
궁<br>
화<br>
꽃<br>
이<br>
피<br>
었<br>
습<br>
니<br>
다<br><br>
무<br>
궁<br>
화<br>
꽃<br>
이<br>
피<br>
었<br>
습<br>
니<br>
다<br><br>

<a href="./javascript_history.jsp">이동</a>
</body>
</html>