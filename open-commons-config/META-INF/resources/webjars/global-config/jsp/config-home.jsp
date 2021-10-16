<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/webjars/global-config/css/layout.css" />
<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/webjars/global-config/css/jquery.dialogbox.css" />

<script type="text/javascript"
    src="<c:url value="/webjars/global-config/js/jquery-3.2.1.min.js" />"></script>
<script type="text/javascript"
    src="<c:url value="/webjars/global-config/js/app.js" />"></script>
<script type="text/javascript"
	src="<c:url value="/webjars/global-config/js/jquery.dialogBox.js" />"></script>

<title>Home</title>
</head>
	
<body>
	<div>
		<div id="header">
			<jsp:include page="./header.jsp" /> 
		</div>
		
		<div id="menu">
			<jsp:include page="./menu.jsp" /> 
		</div>

		<div id="content"></div>

		<div id="Footer" style="FONT-FAMILY: 'Century Schoolbook L';">
			<jsp:include page="./footer.jsp" /> 
		</div>
	</div>

</body>

</html>