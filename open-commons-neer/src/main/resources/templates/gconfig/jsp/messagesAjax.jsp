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

	<table class="table" border="0px">
		<tr>
			<td></td>
			<td><b>Key</b></td>
			<td><b>Value</b></td>
			<td><b>Action</b></td>
		</tr>

		<c:forEach var="entry" items="${messages}" varStatus="status">
			<tr>
				<td>${status.count}.</td>
				<td><input id="key${status.count}" type="text"
					value="${entry.key}" readonly="readonly"/></td>
				<td><textarea id="textArea${status.count}" cols="70">${entry.value}</textarea></td>
				<td><input type="button" onclick="saveMessage(${status.count}, 'update')" value="SAVE"></td>
			</tr>
		</c:forEach>
        <tr>
            <td>NEW</td>
            <td><input id="key0" type="text"
                value="${entry.key}" /></td>
            <td><textarea id="textArea0" cols="70">${entry.value}</textarea></td>
            <td><input type="button" onclick="saveMessage(0, 'create')" value="CREATE"></td>
        </tr>
	</table>
</div>