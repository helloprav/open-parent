package org.openframework.commons.spring.utils;



import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ServerDetails {
	public ServletWebServerApplicationContext getServer() {
		return server;
	}

	public void setServer(ServletWebServerApplicationContext server) {
		this.server = server;
	}

	@Autowired
	private ServletWebServerApplicationContext server;

	public Map<String, String> getServerDetails() {

		String schema = "http";					// currently hard-coded
		String serverName = "localhost";		// currently hard-coded
		int port = this.getServer().getWebServer().getPort();
		String contextPath = ApplicationContextProvider.getApplicationContext().getApplicationName();
		String applicationUrl = schema+"://"+serverName+":"+port+contextPath;
		
		Map<String, String> serverInfos = new HashMap<String, String>();
		serverInfos.put("schema", schema);
		serverInfos.put("serverName", serverName);
		serverInfos.put("port", Integer.toString(port));
		serverInfos.put("contextPath", contextPath);
		serverInfos.put("applicationUrl", applicationUrl);

		return serverInfos;
	}
}
