package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BbsDAO {
	private Connection conn;
	private ResultSet rs;
	
	public BbsDAO(){
		try{
			String dbURL = "jdbc:mysql://localhost:3306/BBS";
			String dbID = "root";
			String dbPassword = "root";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	//여러개의 함수가 사용되기 때문에 각각 합수끼리 데이터베이스 접근에 있어서 마찰이 생기지 않도록 하기위해
	//PreparedStatement는 함수 안에다가 넣는다.	
	public String getDate(){//현재 시간 구하기
		String SQL = "SELECT NOW()";
		try{
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return rs.getString(1);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return "";//db오류
	}
	
	public int getNext(){//글번호 구하기
		String SQL = "SELECT bbsID FROM BBS ORDER BY bbsID DESC";//글번호를 내림차순해서 가장 마지막 글번호를 찾는다. 그리고 +1 해줌
		try{
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return rs.getInt(1)+1;
			}
			return 1;//첫번째로 쓰는 글일때는 글번호 1 리턴
		}catch (Exception e){
			e.printStackTrace();
		}
		return -1;//db오류
	}
	
	public int write(String bbsTitle, String userID, String bbsContent){
		String SQL = "INSERT INTO BBS VALUES (?, ?, ?, ?, ?, ?)";
		try{
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext());
			pstmt.setString(2, bbsTitle);
			pstmt.setString(3, userID);
			pstmt.setString(4, getDate());
			pstmt.setString(5, bbsContent);
			pstmt.setInt(6, 1);
			return pstmt.executeUpdate();
		}catch (Exception e){
			e.printStackTrace();
		}
		return -1;//db오류
	}
}
