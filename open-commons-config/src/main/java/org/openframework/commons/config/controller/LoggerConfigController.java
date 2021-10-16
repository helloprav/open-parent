package org.openframework.commons.config.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openframework.commons.config.constants.AppConstants;
import org.openframework.commons.config.service.as.MessageResourceAS;
import org.openframework.commons.utils.FileFolderUtils;
import org.openframework.commons.utils.LogbackUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/secure")
//@ApiIgnore
public class LoggerConfigController {

	private static final String LOG_DIR = "LOG_DIR";
	private static final String LOG_MAX_LINE_COUNT = "logs.lines.maxCount";

	@Inject
	MessageResourceAS messageResourceAS;

	@RequestMapping(method = RequestMethod.GET, value = "/logger")
	public String getLogger(HttpServletRequest request) {
		request.setAttribute("messages", LogbackUtils.findNamesOfConfiguredLoggers());
		return "setLogAjax";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/logger")
	public String setLogger(HttpServletRequest request) {

		final String levels = request.getParameter("levels");
		final String loggerName = request.getParameter("logger");
		//logger.info("setting new level.................. {}", levels);
		LogbackUtils.setLevel(levels, loggerName);
		return getLogger(request);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/logger/logfiles")
	public String getLogFileNames(HttpServletRequest request) {

		String logPath = getLogPath();
		List<String> logFileList = FileFolderUtils.getFileNamesList(logPath, null, true);
		request.setAttribute("messages", logFileList);
		return "logFilesAjax";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/logger/logfiles/getLogs")
	public String getLogsFromFile(HttpServletRequest request, String fileName) {

		List<String> lastLines = new ArrayList<>();
		String logPath = getLogPath();
		String logFilePath = logPath.concat(File.separator).concat(fileName);
		int logLinesCount = messageResourceAS.getPropertyValueAsInteger(AppConstants.GLOBAL_CONFIG, LOG_MAX_LINE_COUNT);
		lastLines = FileFolderUtils.readFileLines(logFilePath, logLinesCount);
		request.setAttribute("title", "Logs");
		request.setAttribute("messages", lastLines);
		return "logFileAjax";
	}

	private String getLogPath() {
		String logPath = System.getProperty(LOG_DIR);
		if(null == logPath) {
			logPath = LOG_DIR + "_IS_UNDEFINED";
		}
		return logPath;
	}

	//@GetMapping("/downloadFile/{fileName:.+}")
	@GetMapping("/logger/logfiles/downloadLogs")
	public void downloadPDFResource(HttpServletRequest request, HttpServletResponse response,
			String fileName) throws IOException {

		String logPath = getLogPath();
		String logFilePath = logPath.concat(File.separator).concat(fileName);
		File file = new File(logFilePath);
		if (file.exists()) {

			//get the mimetype
			String mimeType = URLConnection.guessContentTypeFromName(file.getName());
			if (mimeType == null) {
				//unknown mimetype so set the mimetype to application/octet-stream
				mimeType = "application/octet-stream";
			}

			response.setContentType(mimeType);

			/**
			 * In a regular HTTP response, the Content-Disposition response header is a
			 * header indicating if the content is expected to be displayed inline in the
			 * browser, that is, as a Web page or as part of a Web page, or as an
			 * attachment, that is downloaded and saved locally.
			 * 
			 */

			/**
			 * Here we have mentioned it to show inline
			 */
			//response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));

			 //Here we have mentioned it to show as attachment
			 response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));

			response.setContentLength((int) file.length());

			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

			FileCopyUtils.copy(inputStream, response.getOutputStream());
			response.getOutputStream().flush();
		}
	}

}
