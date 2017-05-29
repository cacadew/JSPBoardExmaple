package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

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
	
	public ArrayList<Bbs> getList(int pageNumber){
		String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1 ORDER BY bbsID DESC LIMIT 10";//위에서부터 10개까지만
		ArrayList<Bbs> list = new ArrayList<Bbs>();
		try{
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - (pageNumber-1)*10);
			rs = pstmt.executeQuery();
			while(rs.next()){//결과가 하나씩 나올때마다
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				list.add(bbs);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	public boolean nextPage(int pageNumber){//다음페이지 버튼이 필요한지 아닌지
		String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1";
		try{
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - (pageNumber-1)*10);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return true;
			}
		}catch (Exception e){
			e.printStackTrace() ;
		}
		return false;
	}
	
	public Bbs getBbs(int bbsID){
		String SQL = "SELECT * FROM BBS WHERE bbsID = ?";
		try{
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, bbsID);
			rs = pstmt.executeQuery();
			if(rs.next()){
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				return bbs;
			}
		}catch (Exception e){
			e.printStackTrace() ;
		}
		return null;
	}
	
	public int update(int bbsID, String bbsTitle, String bbsContent){
		String SQL = "UPDATE BBS SET bbsTitle =?, bbsContent = ? WHERE bbsID = ?";
		try{
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, bbsTitle);
			pstmt.setString(2, bbsContent);
			pstmt.setInt(3, bbsID);
			return pstmt.executeUpdate();			
		}catch(Exception e){
			e.printStackTrace();
		}
		return -1; //db오류
	}
	
	public int delete(int bbsID){
		String SQL = "UPDATE BBS SET bbsAvailable = 0 WHERE bbsID = ?";
		try{
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, bbsID);
			return pstmt.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		return -1; // db 오류
	}
	
}
