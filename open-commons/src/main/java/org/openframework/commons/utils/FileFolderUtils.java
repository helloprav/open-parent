package org.openframework.commons.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileFolderUtils {

	private static final Logger logger = LoggerFactory.getLogger(FileFolderUtils.class);

	private FileFolderUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static Properties loadPropFromFile(File innerFile) {

		Properties props = new Properties();
		try {
			props.load(new InputStreamReader(new FileInputStream(innerFile), "utf-8"));
		} catch (IOException e) {
			logger.error("Error in loading innerFile: {}", innerFile.getName(), e);
		}
		return props;
	}

	public static File[] getMessagePropertyFileList(String path) {

		return getMessagePropertyFileList(path, null);
	}

	public static File[] getMessagePropertyFileList(String path, String fileExtn) {

		final File dir = new File(path);
		if (StringUtils.isBlank(fileExtn)) {
			return dir.listFiles();
		} else {
			return dir.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.trim().toLowerCase().endsWith(fileExtn.toLowerCase());
				}
			});
		}
	}

	public static List<String> getFileNamesList(String path) {
		return getFileNamesList(path, null);
	}

	public static List<String> getFileNamesList(String path, String fileExtn) {
		return getFileNamesList(path, fileExtn, false);
	}

	public static List<String> getFileNamesList(String path, final String fileExtn, final boolean listSubFiles) {
		File[] files = getMessagePropertyFileList(path, fileExtn);
		List<String> fileNamesList = new ArrayList<String>();
		for (int i = 0; i < files.length; i++) {

			if(files[i].isDirectory() && listSubFiles) {
				String dirName = files[i].getName();
				List<String> subFileNamesList = getFileNamesList(path.concat(File.separator).concat(dirName), fileExtn, listSubFiles);
				for(int j=0; j<subFileNamesList.size(); j++) {
					fileNamesList.add(dirName.concat(File.separator).concat(subFileNamesList.get(j)));
				}
			} else {
				fileNamesList.add(files[i].getName());
			}
		}
		return fileNamesList;
	}

	public static boolean isDirectoryExists(String directoryPath) {

		if (null == directoryPath) {
			return false;
		}
		File dir = new File(directoryPath);
		return dir.exists() && dir.isDirectory();
	}

	public static String getPathFromClasspathFolder(String classpathFolder) {

		logger.debug("classpathFolder: {}", classpathFolder);
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		URL url = loader.getResource("classpath:"+classpathFolder);
		logger.debug("url: {}", url);
		if (null != url) {

			return url.getPath();
		}
		return null;
	}

	public static List<String> readFileLines(String path) {
		return readFileLines(path, Integer.MAX_VALUE);
	}

	/**
	 * This method is used to top n lines from server log fies.
	 * 
	 * @param lineNumber top lines
	 * @param path       log file path
	 * @return top n lines
	 */
	public static List<String> readFileLines(String path, int maxCount) {
		List<String> lastLines = new ArrayList<>();
		Path file = Paths.get(path);

		try (Stream<String> lines = Files.lines(file, StandardCharsets.UTF_8);) {

			int i = 1;
			for (String line : (Iterable<String>) lines::iterator) {
				if(i<=maxCount) {
					lastLines.add(line);
					i++;
				} else {
					break;
				}
			}
		} catch (IOException e) {
			logger.error("IOException in readFileLines for the file: {}", path, e);
		}
		return lastLines;
	}
}
