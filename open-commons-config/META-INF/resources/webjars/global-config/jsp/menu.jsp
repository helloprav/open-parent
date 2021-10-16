<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/webjars/global-config/css/menu.css" />


<div id="leftmenu">

	<div class="menu" hidden="true">
		<ul class="nav">
			<p class="menuSection">Request Status</p>
			<p>Requester Details:-</p>
			<p>User Name : ${loggedInUser.firstName} ${loggedInUser.lastName}</p>
			<p>Signum : ${loggedInUser.signum}</p>
			<p>Cost Center : ${userdata.costcentre}</p>
			<p>Approver : ${userdata.reportingmanager}</p>
	</div>

	<div class="menu">
		<ul class="nav">
			<p class="menuSection">Messages</p>
			<c:forEach items="${messageResources}" var="messageTypes">
				<p>
					<a
						onclick="return loadPage('/languages?messageType=${messageTypes.key}')">${messageTypes.key}
						messages</a>
				</p>
			</c:forEach>
	</div>

	<div class="menu">
		<ul class="nav">
			<p class="menuSection">Env Properties</p>
			<c:forEach items="${configNames}" var="configName">
				<p>
					<a
						onclick="return loadPage('/config/${configName}')">${configName}</a>
				</p>
			</c:forEach>
		</ul>
	</div>

	<div class="menu" style="display: none123;">
		<ul class="nav">
			<p class="menuSection">System Config</p>
            <p><a onclick="return getSystemStatus('/system/status')"><s>system status</s></a></p>
            <p><a onclick="return getSystemProperties('/system/properties')">system properties</a></p>
            <p><a onclick="return getSystemProperties('/system/beans')">system beans</a></p>
            <p><a onclick="return loadLogger('/logger')">change log level</a></p>
            <p><a onclick="return getSystemProperties('/logger/logfiles')">Download Logs</a></p>
	</div>
</div>