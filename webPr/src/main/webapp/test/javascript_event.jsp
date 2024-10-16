<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>이벤트 연습하기</title>
</head>
<body onload="alert('모든 객체가 생성된후에 로딩됩니다.');">

<script>
	window.onload = function() {
		//alert("모든 객체가 생성된후에 로딩됩니다2");
	}
	
	function calculate() {

		let exp = document.getElementById("exp");  // exp input 객체 찾기
		let result = document.getElementById("result");  // result input 객체 찾기
		
		result.value = eval(exp.value);  // 잘못 연산하는 경우가 있어서 지양
		
		return;
	}
	
	function aaa() {
		alert("오른쪽 버튼은 사용금지입니다.");
		return false;
	};
	document.oncontextmenu = aaa;
	
	function changeImage() {
		let sel = document.getElementById("sel");  // select 객체 찾기
		let img = document.getElementById("myImg");  // 이미지 객체 찾기
		
		img.onload = function() {  // 이미지가 로딩이 되면 익명함수 동작
			let mySpan = document.getElementById("mySpan");
			mySpan.innerHTML = img.width + " X " + img.height;
		}

		let index = sel.selectedIndex;
		img.src = sel.options[index].value;
	}
	
	let isAlertShown = false;  // alert 중복 방지를 위한 변수
	function chk() {
		let nm = document.getElementById("nm");
		if(nm.value == "" && !isAlertShown) {
			isAlertShown = true;  // alert를 실행하기 전에 true로 설정
			alert("아무것도 입력하지 않았음. 포커스로 다시 입력하게 하겠음");
			nm.focus();
			
 			setTimeout(function() {
	          isAlertShown = false;
	        }, 0);
			return;
		}
		
		return;
	}
</script>

<form>
	<input type="text" id="exp" value="">
	<br>
	<input type="text" id="result">
	<br>
	<input type="button" value="계산하기" onclick="calculate();">
	
	<hr>
	
	<p>마우스 오른쪽버튼 클릭하기 해보는 테스트</p>
	
</form>

<form>
	<select id="sel" onchange="changeImage();">
		<option value="../image/apple.png">사과</option>
		<option value="../image/banana.png">바나나</option>
		<option value="../image/strawberry.png">딸기</option>
	</select>	
</form>
<span id="mySpan">이미지 크기</span>
<p><img id="myImg" src="../image/apple.png" title="사과"></p>

<input type="text" id="nm" onblur="chk()">
</body>
</html>