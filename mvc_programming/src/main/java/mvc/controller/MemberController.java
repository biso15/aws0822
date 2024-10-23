package mvc.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mvc.dao.MemberDao;
import mvc.vo.MemberVo;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet("/MemberController")  // 서블릿 : 자바로 만든 웹페이지(접속주소 : /MemberController)
public class MemberController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    private String location;  // 멤버변수(전역) 초기화 => 이동할 페이지
    
	public MemberController(String location) {
		this.location = location;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {  // 우선순위

		// response.getWriter().append("Served at: ").append(request.getContextPath());
		// 넘어온 모든 값은 여기에서 처리해서 분기한다 - controller 역할
		// System.out.println("값이 넘어오나요?");
		
		// jsp에서 보낸 전체 주소를 추출
		/*
		 * String uri = request.getRequestURI(); System.out.println("uri : " + uri); //
		 * /mvc_programming/member/memberJoinAction.aws
		 * 
		 * String[] location = uri.split("/");
		 */
		
		String paramMethod = "";  // 전송방식이 sendRedirect면 S, forward 방식이면 F
		String url = "";
		
		if (location.equals("memberJoinAction.aws")) {  // location의 값이 memberJoinAction.aws이면
			
			String memberId = request.getParameter("memberid");
			String memberPw = request.getParameter("memberpwd");
			String memberName = request.getParameter("membername");
			String memberMail = request.getParameter("membermail");
			String memberPhon = request.getParameter("memberphon");
			String memberAddress = request.getParameter("memberaddress");
			String memberGender = request.getParameter("membergender");
			String memberBirth = request.getParameter("membernirth");
						
			String[] memberHobby = request.getParameterValues("memberhobby");  // checkbox는 여러개의 값을 가지고 있으므로 배열로 사용해야 함
			String memberInHobby = "";
			for(int i = 0; i<memberHobby.length; i++) {
				memberInHobby = memberInHobby + memberHobby[i] + ", ";
			}
			
			MemberDao md = new MemberDao();
			int value = md.memberInsert(
				memberId,
				memberPw,
				memberName,
				memberGender,
				memberBirth,
				memberAddress,
				memberPhon,
				memberMail,
				memberInHobby
			 );
			 
			 String pageUrl = "";
			 String msg = "";
			 HttpSession session = request.getSession();  // 세션객체 활용
			 
			 if (value == 1) {
				 
				 msg = "회원가입되었습니다.";
				 session.setAttribute("msg", msg);
				 
				 url = "/";
				 
			 } else {
				 msg = "회원가입 오류 발생하였습니다.";
				 session.setAttribute("msg", msg);
				 
				 url = "/member/memberJoin.jsp";
			 }

			 paramMethod = "S";  // sendRedirect : (처리가 끝나면)새롭게 다른쪽으로 가라고 지시
			 
			 // System.out.println("msg는 ? " + msg);
			 
		} else if (location.equals("memberJoin.aws")) {
			// System.out.println("들어왔나?");
			
			url = "/member/memberJoin.jsp";
			paramMethod = "F";  // forward : 내부안에서 넘겨서 토스하겠다는 뜻(화면이동)
			
		} else if (location.equals("memberLogin.aws")) {
			// System.out.println("들어왔나? 로그인");
			

			url = "/member/memberLogin.jsp";
			paramMethod = "F";
			
		} else if (location.equals("memberLoginAction.aws")) {
			// System.out.println("memberLoginAction 들어왔나?");
			
			String memberId = request.getParameter("memberid");
			String memberPwd = request.getParameter("memberpwd");
			
			MemberDao md = new MemberDao();
			MemberVo mv = md.memberLoginCheck(memberId, memberPwd);
			
			System.out.println("mv객체가 생겼나요? " + mv);
			
			if (mv == null) {
				url = "/member/memberLogin.aws";
				paramMethod = "S";  // 해당 주소로 다시 가세요
				
			} else {
				// 해당되는 로그인 사용자가 있으면 세션에 회원정보 담아서 메인으로 가라
				
				String mid = mv.getMemberid();  // 아이디 꺼내기
				int midx = mv.getMidx();  // 회원번호 꺼내기
				String memberName = mv.getMembername();  // 이름 꺼내기

				HttpSession session = request.getSession();
				session.setAttribute("mid", mid);
				session.setAttribute("midx", midx);
				session.setAttribute("memberName", memberName);

				url = "/";
				paramMethod = "S";  // 로그인 되었으면 메인으로 가세요
			}
			
		} else if (location.equals("memberLogout.aws")) {
			// System.out.println("memberLogout");
			
			// 세션 삭제			
			HttpSession session = request.getSession();
			session.removeAttribute("mid");  // 세션 초기화
			session.removeAttribute("midx");
			session.removeAttribute("memberName");
			session.invalidate();  // 세션 끊기
			
			url = "/";
			paramMethod = "S";
			
		} else if (location.equals("memberList.aws")) {
			// System.out.println("memberList.aws");
			
			// 1. 메소드 불러서 처리하는 코드를 만들어야한다
			MemberDao md = new MemberDao();
			ArrayList<MemberVo> alist = md.memberSelectAll();
			request.setAttribute("alist", alist);
			
			// 2. 보여줄 페이지를 forward 방식으로 보여준다. 공유의 특성을 가지고 있다
			url = "/member/memberList.jsp";
			paramMethod = "F";
			
		} else if (location.equals("memberIdCheck.aws")) {
			System.out.println("memberIdCheck.aws");
			
			String memberId = request.getParameter("memberId");
			
			MemberDao md = new MemberDao();
			int cnt = md.memberIdCheck(memberId);
			System.out.println("cnt: " + cnt);
			
			PrintWriter out = response.getWriter();
			out.println("{\"cnt\":\""+cnt+"\"}");  // json 파일을 넘긴다. 작은따옴표는 json 파일 형식에 맞지 않기 때문에 \"를 사용
		}

		if (paramMethod.equals("F")) {
			RequestDispatcher rd = request.getRequestDispatcher(url);
			rd.forward(request, response);
		} else if(paramMethod.equals("S")) {
			response.sendRedirect(request.getContextPath() + url);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);  // 결국 doGet에서 모두 처리하게 됨
	}

}
