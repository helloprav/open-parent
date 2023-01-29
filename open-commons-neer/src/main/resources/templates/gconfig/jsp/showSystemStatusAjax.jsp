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
<br>
<%-- For Search Result Input Criteria --%>
<div id="messages">
	<label>&nbsp;<b>${configName} properties</b></label>

	<table class="table" border="0px">
        <tr>
            <td><b>System Name</b></td>
            <td><b>Status</b></td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td></td>
        </tr>

		<c:forEach var="entry" items="${systemStatus}" varStatus="status">
			<tr>
				<td>${entry.key}</td>
				<td>${entry.value}</td>
			</tr>
		</c:forEach>
	</table>
</div>