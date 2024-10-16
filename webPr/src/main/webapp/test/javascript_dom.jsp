<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>자바스크립트로 DOM객체 제어하기</title>
</head>
<body>
<p id="firstP" style="color: yellow; background: green;" onclick="this.style.color='red';">
	<span id="mySpan" style="color: blue;">안녕</span>하세요
</p>

<input type="button" value="스타일바꾸기" onclick="change();">
<input type="button" value="글자내용 바꾸기" onclick="textChange();"><br>

<script>
	let p = document.getElementById("firstP");  // 속성값 id로 객체를 찾는다.
	document.write("객체의 아이디는? " + p.id + "<br>");
	document.write("객체의 태그는? " + p.tagName + "<br>");
	document.write("객체가 담고있는 내용은? " + p.innerHTML + "<br>");
	document.write("객체의 스타일은? " + p.style.color + "<br>");
	document.write("객체의 이벤트 리스너(감지기)는? " + p.onclick + "<br>");
	document.write("객체의 너비는? " + p.offsetWidth + "<br>");
	document.write("객체의 높이는? " + p.offsetHeight + "<br>");
	
	function change() {
		let span = document.getElementById("mySpan");
		span.style.color = "#FF6600";
		span.style.fontSize = "30px";
		span.style.border = "3px dotted skyblue";
		return;
	}
	
	function textChange() {
		let myP = document.getElementById("firstP");
		myP.innerHTML = "무궁화꽃이 <span style='color: red;'>피었습니다.</span>";
		return;
	}	
	
	
	function fruit(){
	   //let sp = document.getElementsByTagName("span");   //여러개의 span태그  컬렉션 집합객체변수
	   let cls = document.getElementsByClassName("a");    //클래스속성(프로퍼티)으로 컬렉션 객체를 찾는다
	   
	    for(let i=0;i<cls.length;i++){
	       let entityCls = cls[i];
	       entityCls.style.color="red";
	       
	    }
	   
	   return;   
	}


/* 	document.open();
	document.write("<html><title>새화면</title><body>새로 열립니다.</body></html>");
	document.close(); */


	function winOpen(){
	   
	   window.open("<%=request.getContextPath()%>/member/memberJoin.jsp","winName","width=800,height=600");
	   
	   
	   return;
	}
	</script>

	저는 빨간 <span class="a">사과</span>를 좋아해서
	아침마다 한 개씩 먹고 있어요. 운동할 때는 중간 중간에
	<span class="a">바나나</span>를 먹지요. 탄수화물 섭취가 빨라
	힘이 납니다. 또한 달콤한 향기를 품은 <span class="a">체리</span>와
	여름 냄새 물씬 나는 <span class="a">자두</span>를 좋아합니다.
	<br>
	 아침에는 <span class="b">우유</span>도 같이 먹고 있어요

	<br>
	위 문장에서 과일이름은 빨간색상으로 변경하세요
	<button type="button" onclick="fruit();">과일 이름바꾸기</button>


	새창열기 연습
	<button type="button" onclick="winOpen();">새창열기</button>


</body>
</html>