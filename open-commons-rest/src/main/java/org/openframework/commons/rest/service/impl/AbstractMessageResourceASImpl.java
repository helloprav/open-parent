/**
 * 
 */
package org.openframework.commons.rest.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.openframework.commons.config.model.LanguageBean;
import org.openframework.commons.config.model.MessageResourceLocale;
import org.openframework.commons.config.service.as.MessageResourceAS;
import org.openframework.commons.utils.FileFolderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.FileSystemResource;

/**
 * @author its_me
 *
 */

// TODO: Check and Delete this file and related files
public abstract class AbstractMessageResourceASImpl implements MessageResourceAS {

	private static final Logger logger = LoggerFactory.getLogger(AbstractMessageResourceASImpl.class);

	private List<LanguageBean> supportedLanguages = new ArrayList<>();
	private Map<String, Properties> appConfigsMap = new HashMap<>();
	private Map<String, MessageResourceLocale> messageResourceMap;

	/**
	 * Service Initialization
	 */
	@PostConstruct
	public void init() {
		initMessageResources();
		initAppConfig();
		validateLanguageProperties();
	}

	/**
	 * Service Initialization
	 */
	private void initMessageResources() {

		Map<String, MessageResourceLocale> messageResourceMapTemp = new HashMap<>();
		List<LanguageBean> newSupportedLanguages = new ArrayList<>();
		final File[] propertyFileList = getMessagePropertyFileList();

		for (File file : propertyFileList) {
			if (file.isDirectory()) {
				MessageResourceLocale messageResourceLocale = new MessageResourceLocale();
				Collection<File> listFiles = FileUtils.listFiles(file, new RegexFileFilter("^.+\\.properties"),
						DirectoryFileFilter.DIRECTORY);
				for (File innerFile : listFiles) {
					saveProperies(file, innerFile, messageResourceLocale, newSupportedLanguages);
				}
				/*if (ApplicationConstants.MESSAGE_TYPE_DASHBOARD.equalsIgnoreCase(file.getName())) {
					supportedLanguages.addAll(newSupportedLanguages);
				}*/
				messageResourceMapTemp.put(file.getName(), messageResourceLocale);
			}
		}
		messageResourceMap = messageResourceMapTemp;
		logger.info("message resources initialization completed: {}", messageResourceMap);
	}

	private void saveProperies(File file, File innerFile, MessageResourceLocale messageResourceLocale,
			List<LanguageBean> newSupportedLanguages) {

		Properties props = FileFolderUtils.loadPropFromFile(innerFile);
		String localeCode = getLocale(innerFile.getName())[1];
		messageResourceLocale.getPropertiesMap().put(localeCode, props);
		/*if (!ApplicationConstants.DEFAULT.equals(localeCode)
				&& ApplicationConstants.MESSAGE_TYPE_DASHBOARD.equalsIgnoreCase(file.getName())) {

			// add above supported language/localeCode in a list
			String languageName = props.getProperty("languageName");
			String nameAsImgProperty = props.getProperty("languageImg");
			Boolean nameAsImg = false;
			if (nameAsImgProperty != null && !nameAsImgProperty.equalsIgnoreCase("no")) {
				nameAsImg = true;
			}
			Locale languageLocale = new Locale(localeCode);
			LanguageBean toAdd = new LanguageBean(languageName, languageLocale.getLanguage(), nameAsImg);
			newSupportedLanguages.add(toAdd);
		}*/
	}

	private void initAppConfig() {

		File[] globalPropertyFileList = getGlobalPropertyFileList();

		YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();

		for (File file : globalPropertyFileList) {
			final String fileName = file.getName();
			StringBuilder filePath = new StringBuilder();
			//filePath.append(getSharedLocation()).append(fileName);
			yaml.setResources(new FileSystemResource(new File(file.getPath())));
			final Properties props = yaml.getObject();
			appConfigsMap.put(FilenameUtils.removeExtension(fileName).toUpperCase(), props);
		}
	}

	private void validateLanguageProperties() {

		List<String> languageList = getLocaleList();
		Iterator<Entry<String, MessageResourceLocale>> messageEntrySetIterator = getMessageResourceMap().entrySet()
				.iterator();
		while (messageEntrySetIterator.hasNext()) {
			Entry<String, MessageResourceLocale> messageEntry = messageEntrySetIterator.next();
			/*if (!ApplicationConstants.MESSAGE_TYPE_DASHBOARD.equals(messageEntry.getKey())) {

				Map<String, Properties> propertiesMap = messageEntry.getValue().getPropertiesMap();
				Iterator<String> propertiesMapIterator = propertiesMap.keySet().iterator();
				while (propertiesMapIterator.hasNext()) {
					String propertiesMapLanguage = propertiesMapIterator.next();
					if (languageList.contains(propertiesMapLanguage)) {
						languageList.remove(propertiesMapLanguage);
					} else {
						logger.error("The Properties of type {} is NOT supported for language {}",
								messageEntry.getKey(), propertiesMapLanguage);
					}
				}
				logger.error("No properties of languages {} found for the type {}", languageList,
						messageEntry.getKey());
			}*/
		}
	}

	public boolean containsName(final List<LanguageBean> list, final String name) {
		return list.stream().anyMatch(o -> o.getLocale().equals(name));
	}

	/**
	 * This method return an array that contains in position 0 base filename, in
	 * position 1 locale code. For example getLocale(messages_IT.properties) return
	 * {messages,IT}
	 * 
	 * @param fileName
	 * @return
	 * @throws I18nServiceException
	 */
	public String[] getLocale(String fileName) {
		try {
			logger.trace("Start getLocale");
			if (fileName == null || fileName.trim().isEmpty())
				throw new IllegalArgumentException("wrong input parameter: " + fileName);
			String[] split = fileName.split(".properties");
			int fromIndex = split[0].indexOf("_");
			if (fromIndex == -1) {
				return new String[] { split[0], "DEFAULT" };
			}
			return new String[] { split[0].substring(0, fromIndex), (split[0].substring(++fromIndex).toUpperCase()) };
		} catch (Exception e) {
			logger.error("Unable to getLocale");
			throw new IllegalArgumentException("Unable to getLocale", e);
		} finally {
			logger.trace("End getLocale");
		}
	}

	private File[] getMessagePropertyFileList() {

		String messageResourceDir = "";
		//String messageResourceDir = getSharedLocation().concat(ApplicationConstants.APPLICATION_MESSAGE_DIR);
		return FileFolderUtils.getMessagePropertyFileList(messageResourceDir);
	}

	/**
	 * More Read:
	 * https://stackoverflow.com/questions/29316310/java-8-lambda-expression-for-filenamefilter
	 * 
	 * @return
	 */
	private File[] getGlobalPropertyFileList() {

		String globalConfigDir = "";
		//String globalConfigDir = getSharedLocation().concat(ApplicationConstants.APPLICATION_CONFIG_DIR);
		final File dir = new File(globalConfigDir);
		return dir.listFiles((dir1, name) -> name.toLowerCase().endsWith(".yml"));
	}

	@Override
	public void updateMessageProperties(String language, String messageType, String key, String value) {

		if (messageResourceMap.get(messageType).getPropertiesMap().containsKey(language.toUpperCase())) {
			messageResourceMap.get(messageType).getPropertiesMap().get(language.toUpperCase()).put(key, value);
		} else {
			logger.error("Message Properties NOT found for messageType: {} and language {}", messageType, language);
		}
	}

	private List<String> getLocaleList() {

		List<String> localeList = new ArrayList<>();
		ListIterator<LanguageBean> iterator = supportedLanguages.listIterator();
		while (iterator.hasNext()) {
			localeList.add(iterator.next().getLocale().toUpperCase());
		}
		return localeList;
	}

	@Override
	public void updateConfigProperties(String configName, String key, String value) {

		appConfigsMap.get(configName).put(key, value);
	}

	@Override
	public List<LanguageBean> getSupportedLanguages() {
		return supportedLanguages;
	}

	@Override
	public Map<String, Properties> getAppConfigsMap() {
		return appConfigsMap;
	}

	@Override
	public Map<String, MessageResourceLocale> getMessageResourceMap() {
		return messageResourceMap;
	}

	@Override
	public String getErrorMessageValue(String locale, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDashboardMessageValue(String locale, String key) {
		// TODO Auto-generated method stub
		return null;
	}
}
