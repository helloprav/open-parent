package org.openframework.commons.config.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import jakarta.inject.Inject;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.openframework.commons.config.constants.ConfigAppConstants;
import org.openframework.commons.config.model.LanguageBean;
import org.openframework.commons.config.model.MessageResourceLocale;
import org.openframework.commons.config.service.I18nService;
import org.openframework.commons.config.service.as.MessageResourceAS;
import org.openframework.commons.spring.utils.SpringUtils;
import org.openframework.commons.utils.FileFolderUtils;
import org.openframework.commons.utils.LogbackUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/gconfig")
public class HomeController {

	private static final String LOG_MAX_LINE_COUNT = "logs.lines.maxCount";

	@Autowired
	protected HttpServletRequest request;

	@Autowired
	private I18nService i18nService;

	@Inject
	MessageResourceAS messageResourceAS;

	@GetMapping()
	public String init(Model model) {

		Map<String, MessageResourceLocale> messageResources = i18nService.getMessageResourceMap();
		model.addAttribute("messageResources", messageResources);
		model.addAttribute("configNames", i18nService.getAppConfigsMap().keySet());
		return "gconfig/html/home/index";
	}

	@GetMapping("/languages")
	public String getLanguages(String messageType, Model model) {
		printServlets(request);
		Map<String, MessageResourceLocale> messageResources = i18nService.getMessageResourceMap();
		request.setAttribute("messageResources", messageResources);
		model.addAttribute("configNames", i18nService.getAppConfigsMap().keySet());

		List<LanguageBean> languages = i18nService.retrieveSupportedLanguages();
		model.addAttribute("languages", languages);
		request.setAttribute("messageType", messageType);
		model.addAttribute("messageType", messageType);
		return "gconfig/html/home/languagesAjax";
	}

	private void printServlets(HttpServletRequest request) {

		Map<String, ? extends ServletRegistration> servletReg = request.getServletContext().getServletRegistrations();
		System.out.println(servletReg);
		Iterator<?> iterator = servletReg.entrySet().iterator();
		while (iterator.hasNext()) {
			Object object = iterator.next();
			System.out.println("entry: "+object);
		}
	}

	@GetMapping("/messages/{messageType}/{language}")
	public String getMessagesFromTypeAndLocale(@PathVariable String messageType,
			@PathVariable String language) {

		Map<String, String> messages = i18nService.getMessageProperties(language, messageType);
		request.setAttribute("messages", messages);
		return "messagesAjax";
	}

	@PostMapping("/messages/{messageType}/{language}")
	public String updateMessagesForTypeAndLocale(@PathVariable String messageType,
			@PathVariable String language) {

		i18nService.updateMessageProperties(language, messageType, request.getParameter("key"),
				request.getParameter("value"));
		return getMessagesFromTypeAndLocale(messageType, language);
	}

	@GetMapping("/config/{configName}")
	public String getConfig(@PathVariable String configName, Model model) {

		Map<String, MessageResourceLocale> messageResources = i18nService.getMessageResourceMap();
		model.addAttribute("messageResources", messageResources);
		model.addAttribute("configNames", i18nService.getAppConfigsMap().keySet());

		model.addAttribute("configName", configName);
		model.addAttribute("configTemp", i18nService.getAppConfigsMap().get(configName));
		model.addAttribute("config", i18nService.getAppConfigsMap().get(configName));
		return "gconfig/html/home/configAjax";
	}

	@GetMapping("/system/properties")
	public String getSystemProperties(Model model) {

		Map<String, MessageResourceLocale> messageResources = i18nService.getMessageResourceMap();
		model.addAttribute("messageResources", messageResources);
		model.addAttribute("configNames", i18nService.getAppConfigsMap().keySet());
		model.addAttribute("items", new TreeMap<Object, Object>(System.getProperties()));

		return "gconfig/html/home/sysProps";
	}

	@GetMapping("/system/beans")
	public String getSystemBeans(Model model) {

		Map<String, MessageResourceLocale> messageResources = i18nService.getMessageResourceMap();
		model.addAttribute("messageResources", messageResources);
		model.addAttribute("configNames", i18nService.getAppConfigsMap().keySet());
		model.addAttribute("items", SpringUtils.getSpringBeans());

		return "gconfig/html/home/sysBeans";
	}

	@GetMapping("/logs/form")
	public String getLogLevel(Model model) {

		Map<String, MessageResourceLocale> messageResources = i18nService.getMessageResourceMap();
		model.addAttribute("messageResources", messageResources);
		model.addAttribute("configNames", i18nService.getAppConfigsMap().keySet());
		model.addAttribute("items", LogbackUtils.findNamesOfConfiguredLoggers());

		return "gconfig/html/home/setLogAjax";
	}

	@GetMapping("/logs/view")
	public String viewLogs(Model model) {

		Map<String, MessageResourceLocale> messageResources = i18nService.getMessageResourceMap();
		model.addAttribute("messageResources", messageResources);
		model.addAttribute("configNames", i18nService.getAppConfigsMap().keySet());

		String logPath = LoggerConfigController.getLogPath();
		List<String> logFileList = FileFolderUtils.getFileNamesList(logPath, null, true);
		model.addAttribute("items", logFileList);

		return "gconfig/html/home/viewLogs";
	}

	@GetMapping(value = "/logfiles/getLogs")
	public String getLogsFromFile(Model model, String fileName) {

		List<String> lastLines = new ArrayList<>();
		String logPath = LoggerConfigController.getLogPath();
		String logFilePath = logPath.concat(File.separator).concat(fileName);
		int logLinesCount = messageResourceAS.getPropertyValueAsInteger(ConfigAppConstants.GLOBAL_CONFIG, LOG_MAX_LINE_COUNT);
		lastLines = FileFolderUtils.readFileLines(logFilePath, logLinesCount);
		model.addAttribute("title", "Logs");
		model.addAttribute("messages", lastLines);
		return "gconfig/html/home/logFileAjax";
	}

	//@GetMapping("/downloadFile/{fileName:.+}")
	@GetMapping("/logfiles/downloadLogs")
	public void downloadPDFResource(HttpServletResponse response,
			String fileName) throws IOException {

		String logPath = LoggerConfigController.getLogPath();
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
