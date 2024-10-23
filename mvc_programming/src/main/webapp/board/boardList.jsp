<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="mvc.vo.*" %>
    
<%

ArrayList<BoardVo> alist = (ArrayList<BoardVo>)request.getAttribute("alist");  // Object 타입이므로 ArrayList<BoardVo> 타입으로 형변환 한다.
// System.out.println("alist ==> " + alist);

PageMaker pm = (PageMaker)request.getAttribute("pm");

String isBack = (String)request.getAttribute("isBack");

%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글목록</title>
<link href="../css/style2.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script>

window.onpageshow = function(event){   // onpageshow는 page 호출되면 캐시든 아니든 무조건 호출된다.
	
	// 뒤로가기로 접속한 경우
    if (event.persisted || (window.performance && window.performance.navigation.type == 2)){
        // 사파리 or 안드로이드에서 뒤로가기로 넘어온 경우 캐시를 이용해 화면을 보여주는데, 
        // 이때 사파리의 경우 event.persisted 가 ture다. 
        // 그외 브라우저(크롬 등)에서는 || 뒤에 있는 조건으로 뒤로가기인지 체크가 가능하다!
       
        const urlParams = new URL(location.href).searchParams;
		const page = urlParams.get('page');
				
    	$.ajax({
			type: "post",
			url: "<%=request.getContextPath()%>/board/boardList.aws",
			dataType: "json",
			data: {"isBack": "true", "page": page},
			success: function(result) {
				
                for (let i = 0; i < result.length; i++) {
                	$(".viewcnt").eq(i).text(result[i]["viewcnt" + i]);
					$(".recom").eq(i).text(result[i]["recom" + i]);
                }					
				
				/*
				let viewcntIdx = 0;
				let recomIdx = 0;
			
				for(let i = 0; i < Object.keys(result).length; i++) {
									
					if (i % 2 == 0) {
						$(".viewcnt").eq(viewcntIdx).text(result[i]);
				        viewcntIdx++;
					} else {
						$(".recom").eq(recomIdx).text(result[i]);
						recomIdx++;
					}
				}
				*/
				
			},
			error: function(xhr, status, error) {  // 결과가 실패했을 때 받는 영역
 			    console.log("Error Status: " + status);
			    console.log("Error Detail: " + error);
			    console.log("Response: " + xhr.responseText);
			}
		}); 
    }
	
};
</script>
</head>
<body>
<header>
	<h2 class="mainTitle">글목록</h2>
	<form class="search">
		<select>
			<option>제목</option>
			<option>작성자</option>
		</select>
		<input type="text">
		<button class="btn">검색</button>
	</form>
</header>

<section>	
	<table class="listTable">
		<tr>
			<th id="recom">No</th>
			<th>제목</th>
			<th>작성자</th>
			<th>조회</th>
			<th>추천</th>
			<th>날짜</th>
		</tr>
			
		
		<% for(BoardVo bv : alist) { %>
		<tr>
			<td><%= bv.getBidx() %></td>  <%-- <% out.println(bv.getBidx()); %> 이것과 같다. --%>
			<td class="title"><a href="<%=request.getContextPath()%>/board/boardContents.aws?bidx=<%=bv.getBidx()%>"><%= bv.getSubject() %></a></td>
			<td><%= bv.getWriter() %></td>
			<td class="viewcnt"><% if(isBack == null) out.println(bv.getViewcnt()); %></td>
			<td class="recom"><% if(isBack == null) out.println(bv.getRecom()); %></td>
			<td><%= bv.getWriteday() %></td>
		</tr>
		<% } %>
		
	</table>
	
	<div class="btnBox">
		<a class="btn aBtn" href="<%=request.getContextPath()%>/board/boardWrite.aws">글쓰기</a>
	</div>
	
	<div class="page">
		<ul>
		<% if(pm.isPrev() == true) { %>
			<li><a href="<%=request.getContextPath()%>/board/boardList.aws?page=<%=pm.getStartPage() - 1%>">◀</a></li>
		<% } %>
		
		<% for(int i = pm.getStartPage(); i <= pm.getEndPage(); i++) { %>
			<li <% if(i == pm.getCri().getPage()) { %>class="on"<% } %>><a href="<%= request.getContextPath() %>/board/boardList.aws?page=<%=i%>"><%= i %></a></li>
		<% } %>
		
		<% if(pm.isNext() == true && pm.getEndPage() > 0) { %>  <!-- && pm.getEndPage() > 0 : 게시물이 0개일 경우 endPage가 0이 됨. 이때는 버튼이 없어야 함 -->
			<li><a href="<%=request.getContextPath()%>/board/boardList.aws?page=<%=pm.getEndPage() + 1%>">▶</a></li>
		<% } %>
		</ul>
	</div>
</section>

</body>
</html>