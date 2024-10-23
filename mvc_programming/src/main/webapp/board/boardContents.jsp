<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="mvc.vo.BoardVo"%>
    
<%
	BoardVo bv = (BoardVo)request.getAttribute("bv");  // 강제형변환. 양쪽 형을 맞춰준다
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글내용</title>
<link href="../css/style2.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script> 

// 페이지 강제 새로고침
window.addEventListener("pageshow", function(event) {
    if (event.persisted) {  // 페이지가 캐시에서 불러와졌을 때
        window.location.reload();  // 페이지 강제 새로고침
    }
});

// 유효성 검사하기
function check() {
	  
	  let fm = document.frm;
	  
	  if (fm.content.value == "") {
		  alert("내용을 입력해주세요");
		  fm.content.focus();
		  return;
	  }
	  
	  let ans = confirm("저장하시겠습니까?");
	  
	  if (ans == true) {
		  fm.action="./detail.html";
		  fm.method="post";
		  fm.submit();
	  }	  
	  
	  return;
}

// 추천수 업데이트
$(document).ready(function() {
	$("#btn").click(function() {
		// alert("추천버튼 클릭");
		
		$.ajax({
			type: "get",  // bidx를 보내야 함
			url: "<%=request.getContextPath()%>/board/boardRecom.aws?bidx=<%=bv.getBidx()%>",
			dataType: "json",
			// data: {"bidx": bidx},  // get 방식으로 parameter로 넘긴다
			success: function(result) {
				// alert("전송성공");
				
				var str = "추천(" + result.recom + ")";
				$("#btn").val(str);
			},
			error: function(xhr, status, error) {  // 결과가 실패했을 때 받는 영역
				// alert("전송실패");
			}
		});
	 })
})
</script>
</head>
<body>
<header>
	<h2 class="mainTitle">글내용</h2>
</header>

<article class="detailContents">
	<div class="detailTitle">
		<h2 class="contentTitle"><%=bv.getSubject()%> (조회수:<%=bv.getViewcnt()%>)</h2>		
		<input type="button" id="btn" value="추천(<%=bv.getRecom()%>)" class="btn">
	</div>
	<p class="write"><%=bv.getWriter()%> (<%=bv.getWriteday()%>)</p>
	<div class="content">
		<%=bv.getContents()%>
	</div>
	<% if(bv.getFilename() != null) { %>
	<a href="#" class="fileDown">
		<img src="<%=bv.getFilename()%>">첨부파일입니다.
	</a>
	<% } %>
</article>
	
<div class="btnBox">
	<a class="btn aBtn" href="<%=request.getContextPath()%>/board/boardModify.aws?bidx=<%=bv.getBidx()%>">수정</a>
	<a class="btn aBtn" href="./delete.html">삭제</a>
	<a class="btn aBtn" href="./comment.html">답변</a>
	<a class="btn aBtn" href="./list.html">목록</a>
</div>

<article class="commentContents">
	<form name="frm">
		<p class="commentWriter">admin</p>	
		<input type="text" name="content">
		<button type="button" class="replyBtn" onclick="check();">댓글쓰기</button>
	</form>
	
	
	<table class="replyTable">
		<tr>
			<th>번호</th>
			<th>작성자</th>
			<th>내용</th>
			<th>날짜</th>
			<th>DEL</th>
		</tr>
		<tr>
			<td>1</td>
			<td>홍길동</td>
			<td class="content">댓글입니다</td>
			<td>2024-10-18</td>
			<td>sss</td>
		</tr>
	</table>
</article>

</body>
</html>