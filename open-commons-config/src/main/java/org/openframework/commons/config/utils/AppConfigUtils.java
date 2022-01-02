/**
 * 
 */
package org.openframework.commons.config.utils;

import java.io.File;
import java.net.URL;

import org.openframework.commons.config.constants.ConfigAppConstants;
import org.openframework.commons.utils.FileFolderUtils;

/**
 * @author praku
 *
 */
public class AppConfigUtils {

	public static String getAppInitializerConfigFile() {

		return getConfigFilePath(ConfigAppConstants.APP_INITI_FILE);
	}

	public static String getConfigFilePath(String fileName) {

		return getConfigPath().concat(File.separator).concat(fileName);
	}

	public static String getConfigPath() {

		return getSharedFolderPath().concat(File.separator).concat(ConfigAppConstants.APPLICATION_CONFIG_DIR);
	}

	public static String getMessageFilePath() {

		return getSharedFolderPath().concat(File.separator).concat(ConfigAppConstants.APPLICATION_MESSAGE_DIR);
	}

	public static String getSharedFolderPath() {

		String sharedPath = System.getProperty(ConfigAppConstants.SHARED_PATH);
		// check if sharedPath exists
		if(!FileFolderUtils.isDirectoryExists(sharedPath)) {
			// if no sharedPath, load from classpath
			sharedPath = getResourceFolderFiles(ConfigAppConstants.APPLICATION_ENV_DEV);
		}
		return sharedPath;
	}

	// not used
	public static String getFileFullPath(String filePath, String fileName) {

		return filePath.concat(File.separator).concat(fileName);
	}

	private static String getResourceFolderFiles(String folder) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		URL url = loader.getResource(folder);
		if(null == url) {
			return null;
		}
		return url.getPath();
	}
}
