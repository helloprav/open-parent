<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fieldset>
    <legend>Select Log Level</legend>
    <select name="levels" id="levels" size="7">
        <option value="OFF">OFF</option>
        <option value="ERROR">ERROR</option>
        <option value="WARN">WARN</option>
        <option value="INFO">INFO</option>
        <option value="DEBUG">DEBUG</option>
        <option value="TRACE">TRACE</option>
        <option value="ALL">ALL</option>
    </select>
</fieldset>
<br />
<fieldset>
    <legend>Select Logger Name</legend>
    <select name="logger" id="logger">
		<option value="-1">--Select Logger--</option>
        <c:forEach items="${messages}" var="logger">
            <option value="${logger.name}">${logger.name}:${logger.level}</option>
        </c:forEach>
    </select>
</fieldset>
<br />
<div align="center">
	<input type="button" value="SUBMIT" onclick="setLogLevel()">
</div>
