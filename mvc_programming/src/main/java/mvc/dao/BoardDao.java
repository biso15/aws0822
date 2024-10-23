package mvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import mvc.dbcon.Dbconn;
import mvc.vo.BoardVo;
import mvc.vo.Criteria;

public class BoardDao {

	private Connection conn;  // 연결객체를 전역적으로 쓴다.
	private PreparedStatement pstmt = null;  // 구문 객체
	
	public BoardDao() {  // 생성자를 만든다. 왜 ? DB 연결하는 Dbconn 객체 생성하려고. 생성해야 mysql 접속하니까
		Dbconn db = new Dbconn();
		this.conn = db.getConnection();
	}
	
	// 게시판 목록 조회
	public ArrayList<BoardVo> boardSelectAll(Criteria cri) {
		int page = cri.getPage();  // 페이지 번호
		int perPageNum = cri.getPerPageNum();  // 화면 노출 리스트 갯수
	
		ArrayList<BoardVo> alist = new ArrayList<BoardVo>();  // ArrayList 컬렉션 객체에 BoardVo를 담겠다. BoardVo는 컬럼값을 담겠다.
		
		String sql = "select * from board  where delyn='N' order by originbidx desc, depth limit ?, ?";
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, (page - 1) * perPageNum);
			pstmt.setInt(2, perPageNum);
			
			rs = pstmt.executeQuery();
			
			int bidx;
			String subject;
			String writer;
			int viewcnt;
			int recom;
			String writeday;
			
			while(rs.next()) {
				bidx = rs.getInt("bidx");
				subject = rs.getString("subject");
				writer = rs.getString("writer");
				viewcnt = rs.getInt("viewcnt");
				recom = rs.getInt("recom");
				writeday = rs.getString("writeday");
				
				BoardVo bv = new BoardVo();
				bv.setBidx(bidx);
				bv.setSubject(subject);
				bv.setWriter(writer);
				bv.setViewcnt(viewcnt);
				bv.setRecom(recom);
				bv.setWriteday(writeday);
				
				alist.add(bv);
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
	
	// 게시글 전체 갯수 구하기
	public int boardTotalCount() {
		
		int value = 0;
		
		// 1. 쿼리 만들기
		String sql = "select count(*) as cnt from board where delyn='N'";
				
		// 2. conn 객체 안에 있는 구문 클래스 호출하기
		// 3. DB 컬럼값을 받는 전용 클래스 ResultSet 호출(ResultSet 특징은 데이터를 그대로 복사하기 때문에 전달이 빠름)
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {  // 커서를 이동시켜서 첫줄로 옮긴다
				value = rs.getInt("cnt");  // 지역변수 value에 담아서 리턴해서 가져간다.
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			
		} finally {
			try {  // 각 객체도 소멸시키고 DB 연결 끊는다
				rs.close();
				pstmt.close();
				// conn.close();  // boardSelectAll()을 실행해야 하므로 지금 연결 끊지 않는다
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return value;
	}
	
	// 게시글 작성하기
	public int boardInsert(BoardVo bv) {
		int value = 0;
		
		String subject = bv.getSubject();
		String contents = bv.getContents();
		String writer = bv.getWriter();
		String password = bv.getPassword();
		int midx = bv. getMidx();		
		
		String sql = "insert into board(originbidx, depth, level_, subject, contents, writer, password, midx) "
				+ "values(null, 0, 0, ?, ?, ?, ?, ?)";
				
		String sql2 = "update board set originbidx = (select A.maxbidx from (select max(bidx) as maxbidx from board) A) "
				+ "where bidx = (select A.maxbidx from (select max(bidx) as maxbidx from board) A)";
		
		try {
			conn.setAutoCommit(false);  // 수동커밋으로 하겠다
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, subject);
			pstmt.setString(2, contents);
			pstmt.setString(3, writer);
			pstmt.setString(4, password);
			pstmt.setInt(5, midx);
			
			int exec = pstmt.executeUpdate();  // 실행되면 1, 안되면 0

			pstmt = conn.prepareStatement(sql2);
			int exec2 = pstmt.executeUpdate();  // 실행되면 1, 안되면 0
			
			conn.commit();  // 일괄처리 커밋

			conn.setAutoCommit(true);  // 자동커밋으로 다시 변경
			
			value = exec + exec2;
			
		} catch (SQLException e) {
			 try {
		            conn.rollback();   // 실행중 오류발생시 롤백처리
		         } catch (SQLException e1) {
		            e1.printStackTrace();
		         }
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
	
	// 게시글 조회하기
	public BoardVo boardSelectOne(int bidx) {
		
		// 1. 형식부터 만든다
		BoardVo bv = null;
		
		// 2. 사용할 쿼리를 준비한다
		String sql = "select * from board where delyn = 'N' and bidx = ?";
		
		ResultSet rs = null;
		try {
			
			// 3. conn 연결객체에서 쿼리실행 구문 클래스를 불러온다	
			pstmt = conn.prepareStatement(sql);  // 멤버변수(전역변수)로 선언한 PreparedStatement 객체로 담음
			pstmt.setInt(1, bidx);  // 첫번째 물음표에 매개변수 bidx값을 담아서 구문을 완성한다
			
			rs = pstmt.executeQuery();  // 쿼리를 실행해서 결과값을 컬럼 전용클래스인 ResultSet 객체에 담는다(복사기능)
			
			if(rs.next() == true) {  // rs.next()는 커서를 다음줄로 이동시킨다. 맨 처음 커서는 상단에 위치되어있다.
				
				// 값이 존재한다면 BoardVo 객체에 담는다
				String subject = rs.getString("subject");
				String contents = rs.getString("contents");
				String writer = rs.getString("writer");
				String writeday = rs.getString("writeday");
				int viewcnt = rs.getInt("viewcnt");
				int recom = rs.getInt("recom");
				String filename = rs.getString("filename");
			
				// 여기부터는 답글에 필요해서 가져오는 데이터이다
				int rtnBidx = rs.getInt("bidx");
				int originbidx = rs.getInt("originbidx");
				int depth = rs.getInt("depth");
				int level_ = rs.getInt("level_");
				
				// 수정할때 필요해서 가져오는 데이터
				String password = rs.getString("password");
								
				bv = new BoardVo();  // 객체 생성해서 지역변수 bv로 담아서 리턴해서 가져간다
				bv.setSubject(subject);
				bv.setContents(contents);
				bv.setWriter(writer);
				bv.setWriteday(writeday);
				bv.setViewcnt(viewcnt);
				bv.setRecom(recom);
				bv.setFilename(filename);	
				bv.setBidx(rtnBidx);
				bv.setOriginbidx(originbidx);
				bv.setDepth(depth);
				bv.setLevel_(level_);
				bv.setPassword(password);
			}
			
		} catch (SQLException e) {
			 e.printStackTrace();
		
		} finally {
			try {  // 각 개체도 소멸시키고 DB연결을 끊는다.
				rs.close();
				pstmt.close();
				conn.close();
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return bv;
	}
	
	// 게시글 수정하기
	public int boardUpdate(BoardVo bv) {
	
		int value = 0;
		
		String sql = "update board set subject = ?, contents = ?, writer = ?, modifyday = now() where bidx = ? and password = ?";
		try {
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bv.getSubject());
			pstmt.setString(2, bv.getContents());
			pstmt.setString(3, bv.getWriter());
			pstmt.setInt(4, bv.getBidx());
			pstmt.setString(5, bv.getPassword());
			
			value = pstmt.executeUpdate();
						
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
	
	// 조회수 업데이트하기
	public int boardViewCntUpdatet(int bidx) {
		
		int value = 0;
		
		String sql = "update board SET viewcnt = viewcnt + 1 where bidx = ?";
		try {
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);
			
			value = pstmt.executeUpdate();
						
		} catch (SQLException e) {
			 e.printStackTrace();
		
		} finally {
			try {
				pstmt.close();
				// conn.close();  // 게시글 조회하기 메서드 실행해야되서 conn 연결은 끊지 않는다
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return value;
				
	}
	
	// 추천수 업데이트하기
	public int boardRecomUpdate(int bidx) {
		
		int value = 0;
		int recom = 0;
		
		String sql = "update board set recom = recom + 1 where bidx = ?";
		String sql2 = "select recom from board where bidx = ?";
		ResultSet rs = null;
		
		try {
			
			conn.setAutoCommit(false);  // 수동커밋으로 하겠다
			
			// 추천수 업데이트 쿼리 실행
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);
			
			value = pstmt.executeUpdate();

			// 추천수 업데이트 쿼리 실행 후 추천수 조회 쿼리 실행
			pstmt = conn.prepareStatement(sql2);
			pstmt.setInt(1, bidx);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				recom = rs.getInt("recom");				
			}

			conn.commit();
			conn.setAutoCommit(true);
						
		} catch (SQLException e) {

			 try {
		            conn.rollback();   // 실행중 오류발생시 롤백처리
		         } catch (SQLException e1) {
		            e1.printStackTrace();
		         }
			 
			 e.printStackTrace();
			 
		} finally {
			try {
				pstmt.close();
				conn.close();
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return recom;
				
	}
	
}
