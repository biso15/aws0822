<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>String객체 메소드 사용해보기</title>
</head>
<body>
	<script>
		let str = new String("dream come true");  // String 클래스 생성자에 인자값 담아서 호출
		
		// indexOf
		let position = str.indexOf("come");
		document.write("come 자리 위치는? " + position + "<br>");
		
		let position2 = str.indexOf("hello");
		document.write("hello 자리 위치는? " + position2 + "<br>");
		
		if (position2 == -1) {
			document.write("해당하는 단어는 포함되어 있지 않습니다" + "<br>");
		} else {
			document.write("해당하는 단어는 " + position2 + "번 자리에 있습니다.<br>");
		}

		let str3 = prompt("글자를 입력하세요", "");
		let position3 = str3.indexOf("good");  // good이라는 단어가 있는지 없는지 체크한다

		if (position3 == -1) {
			document.write("해당하는 단어는 포함되어 있지 않습니다" + "<br>");
		} else {
			document.write("해당하는 단어는 " + position3 + "번 자리에 있습니다.<br>");
		}
		
		// substr 함수 : 글자 자르기
 		let str4 = "무궁화꽃이 피었습니다";
		let value = str4.substr(3, 5);  // 3번자리부터 5개(공백포함)
		document.write("값은? " + value + "<br>");		

		let value2 = str4.substring(3, 5);  // 3번 다음부터 5번까지(공백포함)
		document.write("값은? " + value2 + "<br>");
		
		let s = " 안녕   ";
		let ss = s.trim();
		document.write("값2는? " + ss + "<br>");
	</script>
</body>
</html>