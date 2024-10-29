package mvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import mvc.dbcon.Dbconn;
import mvc.vo.CommentVo;

public class CommentDao {

	private Connection conn;  // 연결객체를 전역적으로 쓴다.
	private PreparedStatement pstmt = null;  // 구문 객체
	
	public CommentDao() {  // 생성자를 만든다. 왜 ? DB 연결하는 Dbconn 객체 생성하려고. 생성해야 mysql 접속하니까
		Dbconn db = new Dbconn();
		this.conn = db.getConnection();
	}
	
	// 게시판 목록 조회
	public ArrayList<CommentVo> commentSelectAll(int bidx) {
		
		ArrayList<CommentVo> alist = new ArrayList<CommentVo>();  // ArrayList 컬렉션 객체에 CommentVo를 담겠다. CommentVo는 컬럼값을 담겠다.
		
		String sql = "select * from comment where delyn='N' and bidx = ? order by writeday desc";
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				CommentVo cv = new CommentVo();
				cv.setCidx(rs.getInt("cidx"));
				cv.setCcontents(rs.getString("ccontents"));
				cv.setCwriter(rs.getString("cwriter"));
				cv.setWriteday(rs.getString("writeday"));
				cv.setDelyn(rs.getString("delyn"));
				cv.setMidx(rs.getInt("midx"));
				alist.add(cv);
				
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
		
	// 비밀번호 확인하기
	public int boardDelete(int bidx, String password) {
	
		int value = 0;		
		String sql = "update board set delyn = 'Y' where bidx = ? and password = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);
			pstmt.setString(2, password);
			value = pstmt.executeUpdate();  // 성공하면 1. 실패하면 0
			
		} catch (SQLException e) {			 
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
	
	// 게시글 작성하기
	public int commentInsert(CommentVo cv) {
		int value = 0;
		
		String csubject = cv.getCwriter();
		String ccontents = cv.getCcontents();
		String cwriter = cv.getCwriter();
		String cip = cv.getCip();
		int midx = cv.getMidx();
		int bidx = cv.getBidx();
		
		
		String sql = "insert into comment(csubject, ccontents, cwriter, midx, bidx, cip)"
				+ "values(null, ?, ?, ?, ?, ?)";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ccontents);
			pstmt.setString(2, cwriter);
			pstmt.setInt(3, midx);
			pstmt.setInt(4, bidx);
			pstmt.setString(5, cip);
			
			value = pstmt.executeUpdate();  // 실행되면 1, 안되면 0
			
		} catch (SQLException e) {
			 e.printStackTrace();
			 
		} finally {
			try {  // 각 개체도 소멸시키고 DB연결을 끊는다.
				pstmt.close();
				conn.close();
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
				
		return value;
	}
	
	public int commentDelete(int cidx) {
		
		int value = 0;		
		String sql = "update comment set delyn = 'Y' where cidx = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, cidx);
			value = pstmt.executeUpdate();  // 성공하면 1. 실패하면 0
			
		} catch (SQLException e) {			 
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
	
}
