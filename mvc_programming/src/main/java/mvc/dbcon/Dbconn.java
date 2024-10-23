package mvc.dbcon;

import java.sql.Connection;
import java.sql.DriverManager;

public class Dbconn {
	private Connection conn;  // 멤버변수는 선언만 해도 자동초기화가 됨
	String url = "jdbc:mysql://127.0.0.1/aws0822?serverTimezone=UTC";
	String user = "root";
	String password = "1234";
	
	public Connection getConnection () {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");  // driver 등록
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("객체생성확인 ==> " + conn);  // 연결이 null값이면 연결이 되지 않았다 라는 뜻
		return conn;  // 연결객체가 생겨났을때의 객체정보를 담고있는 객체참조변수
	}
}
