<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@page
	import="org.springframework.web.servlet.support.RequestContextUtils"%>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/webjars/global-config/css/table.css" />

<script type="text/javascript">
	$('#iaasForm1').submit(function(e) {
		e.preventDefault();
		doSearch();
	});
	$(function() {
		//selectCheckBox();
	});
</script>

<%
	System.out.println("1212");
%>

<%-- For Search Result Input Criteria --%>
<div>
	<label><b></b></label>

	<table class="table" cellpadding="10">
		<tr>
			<td><b>#</b></td>
			<td><b>Bean Name</b></td>
			<td><b>Bean Class</b></td>
		</tr>

		<c:forEach var="entry" items="${messages}" varStatus="status">
			<tr>
				<td>${status.count}.</td>
				<td >${entry.key}</td>
				<td >${entry.value}</td>
			</tr>
		</c:forEach>
	</table>
</div>