package mvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import mvc.dbcon.Dbconn;
import mvc.vo.MemberVo;

public class MemberDao {  // MVC 방식으로 가기 전에 첫번째 model1 방식
	
	private Connection conn;  // 전역변수로 사용. 페이지 어느곳에서도 사용할 수 있다
	private PreparedStatement pstmt = null;
	
	// 생성자를 통해서 db 연결해서 메소드 사용
	public MemberDao() {  // public을 안쓰면 다른곳에서 사용 못함
		Dbconn dbconn = new Dbconn();  // DB 객체 생성
		conn = dbconn.getConnection();  // 메소드 호출해서 연결객체를 가져온다
	}

	// 회원가입
	public int memberInsert(String memberId, String memberPw, String memberName, String memberGender, String memberBirth, 
				String memberAddress, String memberPhon, String memberMail, String memberInHobby) {

		int value = 0;
		String sql = "";
		// PreparedStatement pstmt = null;
		try {
			sql = "insert into " +
					"member(memberid,memberpwd,membername,membergender,memberbirth,memberaddr,memberphone,memberemail,memberhobby) " + 
					"values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
			 pstmt = conn.prepareStatement(sql);
			 
			 pstmt.setString(1, memberId);
			 pstmt.setString(2, memberPw); 
			 pstmt.setString(3, memberName);
			 pstmt.setString(4, memberGender);
			 pstmt.setString(5, memberBirth);
			 pstmt.setString(6, memberAddress);
			 pstmt.setString(7, memberPhon);
			 pstmt.setString(8, memberMail);
			 pstmt.setString(9, memberInHobby);
			 value = pstmt.executeUpdate();  // 구문객체 실행하면 성공시 1, 실패시 0 리턴
			
		} catch(Exception e) {
			e.printStackTrace();
			
		} finally {
			try {
				pstmt.close();
				conn.close();			
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return value;
	}
	
	// 로그인을 통해서 회원정보를 담아오는 메소드이다
	public MemberVo memberLoginCheck(String memberId, String memberPwd) {

		MemberVo mv = null;
		String sql = "select * from member where memberid = ? and memberpwd = ?";
		ResultSet rs = null;  // db에서 결과 데이터를 받아오는 전용 클래스
		
		try {
			 pstmt = conn.prepareStatement(sql);
			 pstmt.setString(1, memberId);
			 pstmt.setString(2, memberPwd);
			 rs = pstmt.executeQuery();
			 
			 if (rs.next() == true) {  // 커서가 이동해서 데이터 값이 있으면 -> if(rs.next())와 같은 표현
				 String memberid = rs.getString("memberid");  // 결과값에서 아이디값을 뽑는다
				 int midx = rs.getInt("midx");  // 결과값에서 회원번호를 뽑는다
				 String membername = rs.getString("membername");  // 결과값에서 이름값 뽑는다
				 
				 mv = new MemberVo();  // 화면에 가지고 갈 데이터를 담을 Vo 객체 생성
				 mv.setMemberid(memberid);  // 옮겨담는다
				 mv.setMidx(midx);
				 mv.setMembername(membername);
				 
			 }
		} catch(Exception e) {
			e.printStackTrace();
			
		} finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return mv;
	}
	
	// 회원목록
	public ArrayList<MemberVo> memberSelectAll() {
		
		ArrayList<MemberVo> alist = new ArrayList<MemberVo>();  // 여러개의 객체를 받아와야 하므로 ArrayList에 담아서 리턴
		
		String sql = "select * from member where delyn = 'N' order by midx desc";
		ResultSet rs = null;  // DB값을 가져오기 위한 전용클래스
		
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			int midx;
			String memberId;
			String memberName;
			String memberGender;
			String writeday;
			
			while(rs.next()) {  // 커서가 다음으로 이동해서 첫 글이 있느냐 물어보고 true면 진행
				midx = rs.getInt("midx");
				memberId = rs.getString("memberid");
				memberName = rs.getString("membername");
				memberGender = rs.getString("membergender");
				writeday = rs.getString("writeday");
				
				MemberVo mv = new MemberVo();
				mv.setMidx(midx);
				mv.setMemberid(memberId);
				mv.setMembername(memberName);
				mv.setMembergender(memberGender);
				mv.setWriteday(writeday);
				
				alist.add(mv);  // ArrayList객체에 하나씩 추가한다
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			
		} finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return alist;
	}
	
	// 아이디 중복체크
	public int memberIdCheck(String memberId) {
		
		int cnt = 0;
		String sql = "select count(*) as cnt from member where delyn = 'N' and memberid = ?";
		ResultSet rs = null;  // DB값을 가져오기 위한 전용클래스
		
		try {
			 pstmt = conn.prepareStatement(sql);
			 
			 pstmt.setString(1, memberId);
			 rs = pstmt.executeQuery();
			
			if (rs.next()) {
				cnt = rs.getInt("cnt");
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			
		} finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return cnt;
	}
	 
}
