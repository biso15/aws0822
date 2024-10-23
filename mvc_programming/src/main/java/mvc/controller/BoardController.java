package mvc.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mvc.dao.BoardDao;
import mvc.vo.BoardVo;
import mvc.vo.Criteria;
import mvc.vo.PageMaker;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

@WebServlet("/BoardController")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
    private String location;  // 멤버변수(전역) 초기화 => 이동할 페이지
    
	public BoardController(String location) { 
		this.location = location;
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String paramMethod = "";  // 전송방식이 sendRedirect면 S, forward 방식이면 F
		String url = "";
		
		if (location.equals("boardList.aws")) {  // 가상경로

			String page = request.getParameter("page");  // parameter는 모두 문자형
			
			if (page == null || page == "")  page = "1";
			int pageInt = Integer.parseInt(page);  // 문자를 숫자로 변경한다
			
			Criteria cri = new Criteria();
			cri.setPage(pageInt);
			
			PageMaker pm = new PageMaker();
			pm.setCri(cri);  // <-- PageMaker에 Criteria 담아서 가지고 다닌다
			
			BoardDao bd = new BoardDao();

			// 페이징 처리하기 위한 전체 데이터 갯수 가져오기
			int boardCnt = bd.boardTotalCount();
			
			pm.setTotalCount(boardCnt);  // <-- PageMaker에 전체게시물수를 담아서 페이지 계산
			
			ArrayList<BoardVo> alist = bd.boardSelectAll(cri);
			
			request.setAttribute("alist", alist);  // 화면까지 가지고 가기위해 request 객체에 담는다
			request.setAttribute("pm", pm);  // forward 방식으로 넘기기 때문에 공유가 가능하다

			// 뒤로가기로 접속했는지 확인
			String isBack = (String)request.getParameter("isBack");
			request.setAttribute("isBack", isBack);
			
			if (isBack == null) {  // 링크로 접속한 경우
				paramMethod = "F";
				url = "/board/boardList.jsp";  // 실제 내부경로
			
			} else {  // 뒤로가기로 접속한 경우
				
				int maxLength = alist.size();

				PrintWriter out = response.getWriter();
				
				StringBuilder print = new StringBuilder();
				print.append("["); // 배열 시작

				for (int i = 0; i < maxLength; i++) {
				   
				    print.append("{");  // 객체 시작
				    
				    print.append("\"viewcnt").append(i).append("\":\"").append(alist.get(i).getViewcnt()).append("\",");
				    print.append("\"recom").append(i).append("\":\"").append(alist.get(i).getRecom()).append("\"");
				    
				    print.append("}"); // 객체 끝
				    
				    // 마지막 객체가 아닐 경우 쉼표 추가
				    if (i < maxLength - 1) {
				        print.append(",");
				    }
				}

				// 배열 끝
				print.append("]");

				// 응답 설정
				response.setContentType("application/json");
				out.print(print.toString());
				out.flush();
				
				 /*
				 int maxLength = alist.size() * 2;
				 int alistIdx = 0;					

				 PrintWriter out = response.getWriter(); 
				 
				 String print = "{";
				 
				 for (int i = 0; i < maxLength - 2; i++) {
					 // print += "\"viewcnt" + i + "\" : \"" + alist.get(i).getViewcnt() + "\",";
					 // print += "\"recom" + i + "\" : \"" + alist.get(i).getRecom() + "\",";
					 print += "\"" + i + "\" : \"" + alist.get(alistIdx).getViewcnt() + "\",";
					 i++;
					 print += "\"" + i + "\" : \"" + alist.get(alistIdx).getRecom() + "\",";
					 alistIdx++;
				 }
				 
				 print += "\"" + Integer.toString(maxLength - 2) + "\" : \"" + alist.get(alist.size()-1).getViewcnt() + "\","; 
				 print += "\"" + Integer.toString(maxLength - 1) + "\" : \"" + alist.get(alist.size()-1).getRecom() + "\"";
				 
				 print += "}";
				 
				 out.println(print);
				 */
				 
			}
		
		} else if(location.equals("boardWrite.aws")) {

			paramMethod = "F";  // 포워드 방식은 내부에서 공유하는것이기 때문에 내부에서 활동한다(request.getContextPath() 생략 가능)
			url = "/board/boardWrite.jsp";
			
		} else if(location.equals("boardWriteAction.aws")) {
			System.out.println("boardWriteAction.aws");
			
			// 저장될 위치
			String savePath = "C:\\Users\\admin\\git\\aws0822\\mvc_programming\\src\\main\\webapp\\image";
			int sizeLimit = 15 * 1024 * 1024;  // 15MB
			String dataType = "UTF-8";
			DefaultFileRenamePolicy policy = new DefaultFileRenamePolicy();
			
			
			
			// MultipartRequest multi = new MultipartRequest(request, savePath, sizeLimit, dataType, policy);
			
			
			
			
			
			
			
			
			// 1. 파라미터값을 넘겨받는다.
			String subject = request.getParameter("subject");
			String contents = request.getParameter("contents");
			String writer = request.getParameter("writer");
			String password = request.getParameter("password");
			
			HttpSession session = request.getSession();  // 세션 객체를 불러와서
			int midx = Integer.parseInt(session.getAttribute("midx").toString());  // 로그인할 때 담았던 세션변수 midx값을 꺼낸다
			
			BoardVo bv = new BoardVo();
			bv.setSubject(subject);
			bv.setContents(contents);
			bv.setWriter(writer);
			bv.setPassword(password);
			bv.setMidx(midx);
			
			// 2. DB 처리한다.
			BoardDao bd = new BoardDao();
			int value = bd.boardInsert(bv);

			// 3. 처리 후 이동한다. sendRedirect
			if (value == 2) {  // 입력성공				
				paramMethod = "S";
				url = "/board/boardList.aws";
				
			} else {  // 실패했으면
				paramMethod = "S";
				url ="/board/boardWrite.aws";
				
			}
			
		} else if(location.equals("boardContents.aws")) {
			System.out.println("boardContents.aws");
			
			// 1. 넘어온 값 받기
			String bidx = request.getParameter("bidx");
			System.out.println("bidx--> " + bidx);
			int bidxInt = Integer.parseInt(bidx);
			
			// 2. 처리하기
			BoardDao bd = new BoardDao();  // 객체 생성하고
			int value = bd.boardViewCntUpdatet(bidxInt);  // 조회수 +1 업데이트 하기
			
			if (value == 1) {  // 조회수 업데이트 성공
				BoardVo bv = bd.boardSelectOne(bidxInt);  // 생성한 메소드 호출(해당되는 bidx의 게시물 데이터 가져옴)
				request.setAttribute("bv", bv);  // 포워드 방식이라 같은 영역안에 있어서 공유해서 jsp 페이지에 꺼내쓸 수 있다

				// 3. 이동해서 화면 보여주기
				paramMethod = "F";  // 화면을 보여주기 위해서 같은 영역안에 jsp 페이지를 보여준다
				url ="/board/boardContents.jsp";
				
			} else {  // 조회수 업데이트 실패

				paramMethod = "F";
				url ="/board/boardList.jsp";
			}
			
		} else if(location.equals("boardModify.aws")) {
			System.out.println("boardModify.aws");
			
			String bidx = request.getParameter("bidx");
			int bidxInt = Integer.parseInt(bidx);
			BoardDao bd = new BoardDao();
			BoardVo bv = bd.boardSelectOne(bidxInt);
			
			request.setAttribute("bv", bv);
			
			paramMethod = "F";
			url ="/board/boardModify.jsp";
			
		} else if(location.equals("boardModifyAction.aws")) {
			System.out.println("boardModifyAction.aws");
			
			String subject = request.getParameter("subject");
			String contents = request.getParameter("contents");
			String writer = request.getParameter("writer");
			String password = request.getParameter("password");
			String bidx = request.getParameter("bidx");
			int bidxInt = Integer.parseInt(bidx);  // 숫자형변환
			
			BoardDao bd = new BoardDao();
			BoardVo bv = bd.boardSelectOne(bidxInt);  // 원본 게시글 내용이 담긴 bv
			
			paramMethod = "S";

			// 비밀번호 체크
			if(password.equals(bv.getPassword())) {
				
				// 비밀번호가 일치하면	
				BoardDao bd2 = new BoardDao();
				BoardVo bv2 = new BoardVo();  // 수정할 게시글을 담을 bv
				bv2.setSubject(subject);
				bv2.setContents(contents);
				bv2.setWriter(writer);
				bv2.setPassword(password);
				bv2.setBidx(bidxInt);
				
				int value = bd2.boardUpdate(bv2);
				
				if(value == 1) {
					request.setAttribute("bv", bv);	
					url ="/board/boardContents.aws?bidx="+bidx;	
					
				} else {
					url ="/board/boardModify.aws?bidx="+bidx;
				}
				
			} else {
				
				// 비밀번호가 일치하지 않으면
				url ="/board/boardModify.aws?bidx="+bidx;
				
//				 페이지 이동 전 alert 띄우기
//				 response.setContentType("text/html; charset=UTF-8");  // 응답 콘텐츠 타입 설정
//	             PrintWriter out = response.getWriter();  // PrintWriter 객체 가져오기
//	            
//	             out.println("<script>");
//	             out.println("alert('비밀번호가 다릅니다.');");
//	             out.println("location.href='" + request.getContextPath() + "/board/boardModify.aws?bidx=" + bidx + "';");
//	             out.println("</script>");
//	             out.flush();
			}
			
		} else if (location.equals("boardRecom.aws")) {
			String bidx = request.getParameter("bidx");
			int bidxInt = Integer.parseInt(bidx);  // 숫자형변환
			
			BoardDao bd = new BoardDao();  // 객체 생성하고
			int recom = bd.boardRecomUpdate(bidxInt);  // 추천수 +1 업데이트

			PrintWriter out = response.getWriter();
			out.println("{\"recom\":\""+recom+"\"}");
			
		} 

		if (paramMethod.equals("F")) {
			RequestDispatcher rd = request.getRequestDispatcher(url);
			rd.forward(request, response);
		} else if(paramMethod.equals("S")) {
			response.sendRedirect(request.getContextPath() + url);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
