<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>라디오 체크박스 객체 다루기</title>
</head>
<body>

<form>
	<input type="radio" name="city" value="서울" checked> 서울
	<input type="radio" name="city" value="부산"> 부산
	<input type="radio" name="city" value="춘천"> 춘천
	<input type="button" value="클릭" onclick="check();">
</form>

<script>
function check() {
	let korCity = document.getElementsByName("city");  // name으로 객체 찾기
	let alramCity = null;
	// 배열과 for문은 항상 붙어다닌다
	for(let i = 0; i < korCity.length; i++) {
		if(korCity[i].checked == true) {  // 각 값이 체크되었는지 물어봄
			alramCity = korCity[i];  // 체크되어 있으면 옮겨담는다
		}
	}
	
	if(alramCity == "") {
		alert("선택한 값이 없어요");
	} else {
		alert("선택한 도시는? " + alramCity.value);
	}
	
	return;
}
</script>
</body>
</html>