package mvc.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/FrontController")
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String uri = request.getRequestURI();  // 전체 주소 가져오기
		
		String[] entity = uri.split("/");  // split으로 주소 자르기
		
		if (entity[1].equals("member")) {  // 2번째방의 값이 member면
			MemberController mc = new MemberController(entity[2]);
			mc.doGet(request, response);
			
		} else if (entity[1].equals("board")) {
			BoardController bc = new BoardController(entity[2]);
			bc.doGet(request, response);
			
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
