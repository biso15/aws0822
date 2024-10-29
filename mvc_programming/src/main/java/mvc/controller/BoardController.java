package mvc.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import mvc.dao.BoardDao;
import mvc.vo.BoardVo;
import mvc.vo.PageMaker;
import mvc.vo.SearchCriteria;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.util.ArrayList;

@WebServlet("/BoardController")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
    private String location;  // 멤버변수(전역) 초기화 => 이동할 페이지
    
	public BoardController(String location) { 
		this.location = location;
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		String paramMethod = "";  // 전송방식이 sendRedirect면 S, forward 방식이면 F
		String url = "";
		
		if (location.equals("boardList.aws")) {  // 가상경로

			String page = request.getParameter("page");  // parameter는 모두 문자형
			
			if (page == null || page == "")  page = "1";
			int pageInt = Integer.parseInt(page);  // 문자를 숫자로 변경한다
			
			String searchType = request.getParameter("searchType");
			String keyword = request.getParameter("keyword");
			
			if(keyword == null) keyword = "";
			
			SearchCriteria scri = new SearchCriteria();
			scri.setPage(pageInt);
			scri.setSearchType(searchType);
			scri.setKeyword(keyword);
			
			PageMaker pm = new PageMaker();
			pm.setScri(scri);  // <-- PageMaker에 SearhCriteria 담아서 가지고 다닌다
			
			BoardDao bd = new BoardDao();

			// 페이징 처리하기 위한 전체 데이터 갯수 가져오기
			int boardCnt = bd.boardTotalCount(scri);
			
			pm.setTotalCount(boardCnt);  // <-- PageMaker에 전체게시물수를 담아서 페이지 계산
			
			ArrayList<BoardVo> alist = bd.boardSelectAll(scri);
			
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
			
			// 파일첨부 : 자바가 가지고 있는 라이브러리를 가지고 만들었기 때문에 보안이 취약할 수 있음
			// String savePath = "D:/dev/uploadFiles/";
			String savePath = "C:\\Users\\admin\\git\\aws0822\\mvc_programming\\src\\main\\webapp\\image\\";
			System.out.println(savePath);
			
			int fsize = (int)request.getPart("filename").getSize();
			System.out.println("fsize: " + fsize);
			
			String originFileName = "";
			if(fsize != 0) {
				Part filePart = (Part)request.getPart("filename");  // 넘어온 멀티파트 파일을 Part클래스로 담는다
				System.out.println("filePart ==> " + filePart);
				
				originFileName = getFileName(filePart);  // 파일 이름 추출
				System.out.println("originFileName ==> " + originFileName);

				System.out.println("저장되는 위치 ==> " + savePath + originFileName);
				
				File file = new File(savePath + originFileName);  // 파일 객체 생성
				InputStream is = filePart.getInputStream();  // 파일 읽어들이는 스트림 생성. Stream : 흐름. InputStream : 들어가는 부분의 데이터 흐름
				FileOutputStream fos = null;
				
				fos = new FileOutputStream(file);  // 파일 작성 및 완성하는 스트림 생성
				
				int temp = -1;
				
				while ((temp = is.read()) != -1) {  // 반복문을 돌려서 읽어들인 데이터를 output에 작성한다
					fos.write(temp);
				}
				
				is.close();  // input 스트림 객체 소멸
				fos.close();  // Output 스트림 객체 소멸

			}
			
			// ==================================================================================================================
			
			// 1. 파라미터값을 넘겨받는다.
			String subject = request.getParameter("subject");
			String contents = request.getParameter("contents");
			String writer = request.getParameter("writer");
			String password = request.getParameter("password");
			
			HttpSession session = request.getSession();  // 세션 객체를 불러와서
			int midx = Integer.parseInt(session.getAttribute("midx").toString());  // 로그인할 때 담았던 세션변수 midx값을 꺼낸다
			
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
						
			BoardVo bv = new BoardVo();
			bv.setSubject(subject);
			bv.setContents(contents);
			bv.setWriter(writer);
			bv.setPassword(password);
			bv.setMidx(midx);
			bv.setFilename(originFileName);  // 파일이름 DB컬럼 추가
			bv.setIp(ip);
			
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
			int value = bd.boardViewCntUpdate(bidxInt);  // 조회수 +1 업데이트 하기
			
			
			// 뒤로가기로 접속했는지 확인
			String isBack = (String)request.getParameter("isBack");
			System.out.println(isBack);
			request.setAttribute("isBack", isBack);
			System.out.println(isBack);
			
			if (isBack == null) {  // 링크로 접속한 경우

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
				
			} else {  // 뒤로가기로 접속한 경우
				
				int viewcntValue = bd.boardViewCntUpdate(bidxInt);
				
				PrintWriter out = response.getWriter();
				out.println("{\"viewcntValue\":\""+viewcntValue+"\"}");
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
				
				// 파일첨부 : 자바가 가지고 있는 라이브러리를 가지고 만들었기 때문에 보안이 취약할 수 있음
				// String savePath = "D:/dev/uploadFiles/";
				String savePath = "C:\\Users\\admin\\git\\aws0822\\mvc_programming\\src\\main\\webapp\\image\\";
				System.out.println(savePath);
				
				int fsize = (int)request.getPart("filename").getSize();
				System.out.println("fsize: " + fsize);
				
				String originFileName = "";
				if(fsize != 0) {
					Part filePart = (Part)request.getPart("filename");  // 넘어온 멀티파트 파일을 Part클래스로 담는다
					System.out.println("filePart ==> " + filePart);
					
					originFileName = getFileName(filePart);  // 파일 이름 추출
					System.out.println("originFileName ==> " + originFileName);

					System.out.println("저장되는 위치 ==> " + savePath + originFileName);
					
					File file = new File(savePath + originFileName);  // 파일 객체 생성
					InputStream is = filePart.getInputStream();  // 파일 읽어들이는 스트림 생성. Stream : 흐름. InputStream : 들어가는 부분의 데이터 흐름
					FileOutputStream fos = null;
					
					fos = new FileOutputStream(file);  // 파일 작성 및 완성하는 스트림 생성
					
					int temp = -1;
					
					while ((temp = is.read()) != -1) {  // 반복문을 돌려서 읽어들인 데이터를 output에 작성한다
						fos.write(temp);
					}
					
					is.close();  // input 스트림 객체 소멸
					fos.close();  // Output 스트림 객체 소멸

				}
				
				// ==================================================================================================================
				
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
				
				BoardDao bd2 = new BoardDao();
				BoardVo bv2 = new BoardVo();  // 수정할 게시글을 담을 bv
				bv2.setSubject(subject);
				bv2.setContents(contents);
				bv2.setWriter(writer);
				bv2.setPassword(password);
				bv2.setBidx(bidxInt);
				bv2.setFilename(originFileName);  // 파일이름 DB컬럼 추가
				bv2.setIp(ip);
				
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
			
		} else if(location.equals("boardDelete.aws")) {

			String bidx = request.getParameter("bidx");
			request.setAttribute("bidx", bidx);  // foward라서 공유 가능
						
			paramMethod = "F";
			url = "/board/boardDelete.jsp";
			
		} else if(location.equals("boardDeleteAction.aws")) {

			String bidx = request.getParameter("bidx");
			String password = request.getParameter("password");

			// 처리하기
			BoardDao bd = new BoardDao();
			int value = bd.boardDelete(Integer.parseInt(bidx), password);  // 0 또는 1을 반환받는다

			paramMethod = "S";
						
			if(value == 1) {  // 성공	
				url = "/board/boardList.aws";
				
			} else {
				url = "/board/boardDelete.aws?bidx=" + bidx;
			}
			
		} else if(location.equals("boardReply.aws")) {

			String bidx = request.getParameter("bidx");

			BoardDao bd = new BoardDao();
			BoardVo bv = bd.boardSelectOne(Integer.parseInt(bidx));

			int originbidx = bv.getOriginbidx();
			int depth = bv.getDepth();
			int level_ = bv.getLevel_();

			request.setAttribute("bidx", bidx);
			request.setAttribute("originbidx", originbidx);
			request.setAttribute("depth", depth);
			request.setAttribute("level_", level_);

			paramMethod = "F";			
			url = "/board/boardReply.jsp";
			
		} else if(location.equals("boardReplyAction.aws")) {
			System.out.println("boardReplyAction.aws");
			
			String savePath = "C:\\Users\\admin\\git\\aws0822\\mvc_programming\\src\\main\\webapp\\image\\";
			System.out.println(savePath);
			
			int fsize = (int)request.getPart("filename").getSize();
			System.out.println("fsize: " + fsize);
			
			String originFileName = "";
			if(fsize != 0) {
				Part filePart = (Part)request.getPart("filename");  // 넘어온 멀티파트 파일을 Part클래스로 담는다
				System.out.println("filePart ==> " + filePart);
				
				originFileName = getFileName(filePart);  // 파일 이름 추출
				System.out.println("originFileName ==> " + originFileName);

				System.out.println("저장되는 위치 ==> " + savePath + originFileName);
				
				File file = new File(savePath + originFileName);  // 파일 객체 생성
				InputStream is = filePart.getInputStream();  // 파일 읽어들이는 스트림 생성. Stream : 흐름. InputStream : 들어가는 부분의 데이터 흐름
				FileOutputStream fos = null;
				
				fos = new FileOutputStream(file);  // 파일 작성 및 완성하는 스트림 생성
				
				int temp = -1;
				
				while ((temp = is.read()) != -1) {  // 반복문을 돌려서 읽어들인 데이터를 output에 작성한다
					fos.write(temp);
				}
				
				is.close();  // input 스트림 객체 소멸
				fos.close();  // Output 스트림 객체 소멸

			}
			
			// ==================================================================================================================
			
			// 1. 파라미터값을 넘겨받는다.
			String subject = request.getParameter("subject");
			String contents = request.getParameter("contents");
			String writer = request.getParameter("writer");
			String password = request.getParameter("password");
			
			String bidx = request.getParameter("bidx");
			String originbidx = request.getParameter("originbidx");
			String depth = request.getParameter("depth");
			String level_ = request.getParameter("level_");
			
			HttpSession session = request.getSession();  // 세션 객체를 불러와서
			int midx = Integer.parseInt(session.getAttribute("midx").toString());  // 로그인할 때 담았던 세션변수 midx값을 꺼낸다
			
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
			
			BoardVo bv = new BoardVo();
			bv.setSubject(subject);
			bv.setContents(contents);
			bv.setWriter(writer);
			bv.setPassword(password);
			bv.setMidx(midx);			
			bv.setFilename(originFileName);
			bv.setIp(ip);
			
			bv.setBidx(Integer.parseInt(bidx));
			bv.setOriginbidx(Integer.parseInt(originbidx));  // 파일이름 DB컬럼 추가
			bv.setDepth(Integer.parseInt(depth));
			bv.setLevel_(Integer.parseInt(level_));

			// 2. DB 처리한다.
			BoardDao bd = new BoardDao();
			int maxbidx = bd.boardReply(bv);

			// 3. 처리 후 이동한다. sendRedirect
			if (maxbidx != 0) {  // 초기세팅이 0이므로, 0이 아니면 답글쓰기가 성공해서 바뀌었다는 뜻
				paramMethod = "S";
				url = "/board/boardContents.aws?bidx=" + maxbidx;
				
			} else {  // 실패했으면
				paramMethod = "S";
				url ="/board/boardReply.aws?bidx=" + bidx;
				
			}
			
		} else if(location.equals("boardDownload.aws")) {
			System.out.println("boardDownload.aws");
			
			String filename = request.getParameter("filename");			
			String savePath = "C:\\Users\\admin\\git\\aws0822\\mvc_programming\\src\\main\\webapp\\image\\";
			
			ServletOutputStream sos = response.getOutputStream();
			
			String downfile = savePath + filename;
			
			System.out.println("downfile : " + downfile);			
			
			File f = new File(downfile);
			
			String header = request.getHeader("User-Agent");
			String fileName = "";

			response.setHeader("Cache-Control", "no-cashe");
			
			if (header.contains("Chrome") || header.contains("Opera")) {
				
				fileName = new String(filename.getBytes("UTF-8"),"ISO-8859-1");
				response.setHeader("Content-Disposition", "attach;fileName=" + fileName);  // 타입은 첨부 형태로 파일 저장
				
			} else if (header.contains("MSIE") || header.contains("Trident") || header.contains("Edge")) {  // 인터넷 익스플로러는 IE 11 버전 이상이면 trident를, IE 10 버전 이하이면 msie 를 뱉는다
				
				fileName = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
				response.setHeader("Content-Disposition", "attach;fileName=" + fileName);
				
			} else {
				
				response.setHeader("Content-Disposition", "attach;fileName=" + fileName);
			}
			
			FileInputStream in = new FileInputStream(f);  // 파일을 버퍼로 읽어서 출력한다
			
			byte[] buffer = new byte[1024*8];
			
			while(true) {
				
				int count = in.read(buffer);
				
				if (count == -1) {
					break;
				}
				
				sos.write(buffer, 0, count);
			}
			
			in.close();
			sos.close();
			
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
	
	
	// 파일 업로드
	public String getFileName(Part filePart) {
		for(String filePartData : filePart.getHeader("Content-Disposition").split(";")) {
			System.out.println(filePartData);
			
			if(filePartData.trim().startsWith("filename")) {
				return filePartData.substring(filePartData.indexOf("=") + 1).trim().replace("\"","");
			}
		}
		
		return null;
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
