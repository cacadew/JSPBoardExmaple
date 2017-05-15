<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="user.UserDAO" %>
<%@ page import="java.io.PrintWriter"  %>  <%--자바스크립트를 사용하기 위해 사용 --%>
<% request.setCharacterEncoding("UTF-8"); %> <%-- 건너오는 데이터들을 모두 UTF-8로 받기 --%>
<jsp:useBean id="user" class="user.User" scope="page"/> <%-- 현재페이지 안에서만 빈즈가 사용되게끔 scope를 지정함 --%>
<jsp:setProperty name ="user" property="userID"/> <%-- 로그인 페이지에서 받은 userID를 user에 저장 --%>
<jsp:setProperty name ="user" property="userPassword"/>
<jsp:setProperty name ="user" property="userName"/>
<jsp:setProperty name ="user" property="userGender"/>
<jsp:setProperty name ="user" property="userEmail"/>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP 게시판 웹 사이트</title>
</head>
<body>
	<%
		if(user.getUserID() == null || user.getUserPassword() == null || user.getUserName()==null
		|| user.getUserGender() == null || user.getUserEmail() == null){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('입력이 안 된 사항이 있습니다.')");
			script.println("history.back()");
			script.println("</script>");
		}else{
			UserDAO userDAO = new UserDAO();
			int result = userDAO.join(user);
			if(result == -1){
				PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("alert('이미 존재하는 ID 입니다.')");
				script.println("history.back()");
				script.println("</script>");
			}
			else{
				PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("location.href='main.jsp'");
				script.println("</script>");
			}
		}
	
		
	%>
</body>
</html>