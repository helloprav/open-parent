<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page
    import="org.springframework.web.servlet.support.RequestContextUtils" %>
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

<div style="border: 0px">
	<br> <label><b>${fn:toUpperCase(fn:substring(messageType, 0, 1))}${fn:toLowerCase(fn:substring(messageType, 1,fn:length(messageType)))}
			Messages</b></label>
	<table class="table" align="center">
        <tr>
            <td>Select Language</td>
            <td><select id="selectLanguage" onchange="loadMessages()">
                    <option value="-1">--Select Language--</option>
                    <c:forEach items="${languages}" var="languageBean">
                        <option value="${languageBean.locale}">${languageBean.name}</option>
                    </c:forEach>
            </select></td>
            <br>
        </tr>
        <input type="hidden" id="messageType" name="messageType" value="${messageType}" />
    </table>
</div><br>

<div id="messages"></div>