<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="bbs.BbsDAO" %>
<%@ page import="java.io.PrintWriter"  %>  <%--자바스크립트를 사용하기 위해 사용 --%>
<% request.setCharacterEncoding("UTF-8"); %> <%-- 건너오는 데이터들을 모두 UTF-8로 받기 --%>
<jsp:useBean id="bbs" class="bbs.Bbs" scope="page"/> <%-- 현재페이지 안에서만 빈즈가 사용되게끔 scope를 지정함 --%>
<jsp:setProperty name ="bbs" property="bbsTitle"/> <%-- 로그인 페이지에서 받은 userID를 user에 저장 --%>
<jsp:setProperty name ="bbs" property="bbsContent"/>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP 게시판 웹 사이트</title>
</head>
<body>
	<%
		String userID = null;
		if(session.getAttribute("userID") != null){
			userID = (String) session.getAttribute("userID");
		}
		if(userID == null){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('로그인을 하세요.')");
			script.println("location.href='login.jsp'");
			script.println("</script>");
		}else{
			if(bbs.getBbsTitle() == null || bbs.getBbsContent() == null){
						PrintWriter script = response.getWriter();
						script.println("<script>");
						script.println("alert('입력이 안 된 사항이 있습니다.')");
						script.println("history.back()");
						script.println("</script>");
					}else{
						BbsDAO bbsDAO = new BbsDAO();
						int result = bbsDAO.write(bbs.getBbsTitle(),userID, bbs.getBbsContent());
						if(result == -1){
							PrintWriter script = response.getWriter();
							script.println("<script>");
							script.println("alert('글쓰기에 실패하였습니다.')");
							script.println("history.back()");
							script.println("</script>");
						}
						else{
							PrintWriter script = response.getWriter();
							script.println("<script>");
							script.println("location.href='bbs.jsp'");
							script.println("</script>");
						}
					}
		}
		
	
		
	%>
</body>
</html>