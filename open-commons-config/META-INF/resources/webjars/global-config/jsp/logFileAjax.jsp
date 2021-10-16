<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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

<%-- For Search Result Input Criteria --%>

<div style="border: 0px">
	<br> <label><b>${fn:toUpperCase(fn:substring(title, 0, 1))}${fn:toLowerCase(fn:substring(title, 1,fn:length(title)))}
			Messages</b></label>
	<table class="table" cellpadding="10">
		<tr>
			<td><b>Logs: </b><span><a onclick="return downloadFile()">Download
						as Zip <img
						src="${pageContext.request.contextPath}/webjars/global-config/images/file_download.png"
						height="50" width="50">
				</a></span></td>
		</tr>
		<tr>
			<td><c:forEach var="entry" items="${messages}"
					varStatus="status">${entry}<br>
				</c:forEach></td>
		</tr>
	</table>
</div>
<br>

<div id="messages"></div>