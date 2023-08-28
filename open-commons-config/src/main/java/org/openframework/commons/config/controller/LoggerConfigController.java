package org.openframework.commons.config.controller;

import java.io.File;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.openframework.commons.config.constants.ConfigAppConstants;
import org.openframework.commons.config.service.as.MessageResourceAS;
import org.openframework.commons.utils.FileFolderUtils;
import org.openframework.commons.utils.LogbackUtils;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gconfig/api/logger")
//@ApiIgnore
public class LoggerConfigController {

	@Inject
	private HttpServletRequest request;

	@Inject
	MessageResourceAS messageResourceAS;

	@GetMapping()
	public String getLogger() {
		request.setAttribute("messages", LogbackUtils.findNamesOfConfiguredLoggers());
		return "setLogAjax";
	}

	@PostMapping()
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void setLogger() {

		final String levels = request.getParameter("levels");
		final String loggerName = request.getParameter("logger");
		//logger.info("setting new level.................. {}", levels);
		LogbackUtils.setLevel(levels, loggerName);
	}

	@GetMapping(value = "/showlogfiles")
	public String getLogFileNames(Model model) {

		String logPath = getLogPath();
		List<String> logFileList = FileFolderUtils.getFileNamesList(logPath, null, true);
		request.setAttribute("messages", logFileList);
		model.addAttribute("items", logFileList);
		return "logFilesAjax";
	}

	public static String getLogPath() {
		String logPath = System.getProperty(ConfigAppConstants.ARGS_APP_HOME);
		if (null != logPath) {
			if (!logPath.endsWith(File.separator)) {
				logPath = logPath.concat(File.separator);
			}
			logPath = logPath.concat(ConfigAppConstants.APPLICATION_LOG_DIR);
		}
		if(null == logPath) {
			logPath = ConfigAppConstants.APPLICATION_LOG_DIR + "_IS_UNDEFINED";
		}
		return logPath;
	}

}
