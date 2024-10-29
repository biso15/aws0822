package mvc.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mvc.dao.BoardDao;
import mvc.dao.CommentDao;
import mvc.vo.CommentVo;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.ArrayList;

@WebServlet("/CommentController")
public class CommentController extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
    private String location;
    
	public CommentController(String location) { 
		this.location = location;
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				
		if (location.equals("commentList.aws")) {
			System.out.println("commentList.aws");

			// json 파일 한글 깨짐 방지
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			
			String bidx = request.getParameter("bidx");
			
			CommentDao cd= new CommentDao();			
			ArrayList<CommentVo> alist = cd.commentSelectAll(Integer.parseInt(bidx));
			
			int cidx = 0;
			String cwriter = "";
			String ccontents = "";
			String writeday = "";
			String delyn = "";
			int midx = 0;
			
			String str = "";
			
			for(int i = 0; i < alist.size(); i++) {
				cidx = alist.get(i).getCidx();
				cwriter = alist.get(i).getCwriter();
				ccontents = alist.get(i).getCcontents();
				writeday = alist.get(i).getWriteday();
				delyn = alist.get(i).getDelyn();
				midx = alist.get(i).getMidx();
				
				str = str + "{"
						  + "\"cidx\": \""+cidx+"\","
						  + "\"cwriter\": \""+cwriter+"\","
						  + "\"ccontents\": \""+ccontents+"\","
						  + "\"writeday\": \""+writeday+"\","
						  + "\"delyn\": \""+delyn+"\","
						  + "\"midx\": \""+midx+"\""
						  + "}";
				
				if (i < alist.size() - 1) {
					str = str + ", ";  // 형식 잘 맞추자
			    }
			}
			PrintWriter out = response.getWriter();
			out.println("["+str+"]");
			
		
		} else if(location.equals("commentWriteAction.aws")) {
			System.out.println("commentWriteAction.aws");

			// 1. 파라미터값을 넘겨받는다.
			String cwriter = request.getParameter("cwriter");
			String ccontents = request.getParameter("ccontents");
			String bidx = request.getParameter("bidx");
			String midx = request.getParameter("midx");
			
			// ip주소 추출
			String ip = "";
			
			try {
				ip = getUserIp(request);
				System.out.println("ip : " + ip);
				String serverIp = InetAddress.getLocalHost().getHostAddress();
				System.out.println("serverIp : " + serverIp);
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			CommentVo cv = new CommentVo();
			cv.setCwriter(cwriter);
			cv.setCcontents(ccontents);
			cv.setBidx(Integer.parseInt(bidx));
			cv.setMidx(Integer.parseInt(midx));
			cv.setCip(ip);
			
			// 2. DB 처리한다.
			CommentDao cd = new CommentDao();
			int value = cd.commentInsert(cv);
			
			PrintWriter out = response.getWriter();
			out.println("{\"value\":\""+value+"\"}");
		
		} else if(location.equals("commentDeleteAction.aws")) {
			System.out.println("commentDeleteAction");

			String cidx = request.getParameter("cidx");
			System.out.println("cidx"+cidx);

			// delyn Y로 업데이트 하는 메소드를 만들어서 호출한다
			CommentDao cd = new CommentDao();
			int value = cd.commentDelete(Integer.parseInt(cidx));
			
			// 그리고 나서 화면이 실행성공여부를 json파일로 보여준다
			PrintWriter out = response.getWriter();
			out.println("{\"value\":\""+value+"\"}");
			
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	
	// ip주소 추출
	public String getUserIp(HttpServletRequest request) throws Exception {
		
        String ip = null;
        // HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();

        ip = request.getHeader("X-Forwarded-For");
        
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("Proxy-Client-IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("WL-Proxy-Client-IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("HTTP_CLIENT_IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("X-Real-IP"); 
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("X-RealIP"); 
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("REMOTE_ADDR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getRemoteAddr(); 
        }
        if (ip.equals("0:0:0:0:0:0:0:1") || ip.equals("127.0.0.1")) { 
        	InetAddress address = InetAddress.getLocalHost();
        	ip = address.getHostAddress();
        }
		
		return ip;
	}

}
