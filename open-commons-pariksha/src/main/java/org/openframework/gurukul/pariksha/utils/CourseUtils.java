/**
 * 
 */
package org.openframework.gurukul.pariksha.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang3.StringUtils;
import org.openframework.commons.constants.CommonConstants;
import org.openframework.commons.constants.StringConstants;
import org.openframework.commons.jpa.entity.RecordInfo;
import org.openframework.commons.jpa.entity.User;
import org.openframework.commons.jpa.utils.WebUtils;
import org.openframework.commons.spring.utils.ApplicationContextProvider;
import org.openframework.commons.utils.StringUtil;
import org.openframework.gurukul.pariksha.ParikshaConstants;
import org.openframework.gurukul.pariksha.entity.Answer;
import org.openframework.gurukul.pariksha.entity.Question;
import org.openframework.gurukul.pariksha.entity.QuestionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author praveen
 *
 */
public class CourseUtils {

	private static Logger logger = LoggerFactory.getLogger(CourseUtils.class);
	private static final String MEDIA_DIR;
	private static final byte ANSWER_START_INDEX = 5;

	static {

		String appHome = System.getProperty("APP_HOME");
		File dir = new File(appHome);
		if(!dir.exists()) {
			throw new RuntimeException("APP_HOME dir["+dir+" is not already present");
		}
		String parikshaHomeDir = appHome.concat("/pariksha");
		dir = new File(parikshaHomeDir);
		if(!dir.exists()) {
			dir.mkdir();
		}
		String parikshaMediaDir = parikshaHomeDir.concat("/media");
		dir = new File(parikshaMediaDir);
		if(!dir.exists()) {
			dir.mkdir();
		}
		if(!dir.exists()) {
			throw new RuntimeException("Pariksha module could not be initialized. Media dir creation failed: "+parikshaMediaDir);
		}
		MEDIA_DIR = parikshaMediaDir.concat(File.separator);
	}

	private CourseUtils() {}

	public static Set<Question> populateQuestions(List<List<String>> recordList, User loggedInUser) {

		Set<Question> questionSet = new LinkedHashSet<>();
		ListIterator<List<String>> iterator = recordList.listIterator();
		while(iterator.hasNext()) {

			System.out.println("RowNum: "+iterator.nextIndex());
			// Skip the 1st record, as this is header section
			if(iterator.nextIndex()==0) {
				iterator.next();
				continue;
			}
			List<String> questionRecord = iterator.next();
			if(StringUtils.isNotBlank(questionRecord.get(1))){
				Question question = validateAndPopulateQuestion(questionRecord, loggedInUser);
				questionSet.add(question);
			}
		}
		return questionSet;
	}

	private static void populateAnswer(byte answerIndex, List<Answer> answerList, List<String> questionRecord, RecordInfo recordInfo) {
		Answer answer = null;
		if((questionRecord.size() > ++answerIndex) && (StringUtils.isNotEmpty(questionRecord.get(answerIndex))) ) {
			answer = validateAndPopulateAnswer(questionRecord.get(answerIndex), recordInfo);
			answerList.add(answer);
			populateAnswer(answerIndex, answerList, questionRecord, recordInfo);
		} else {
			return;
		}
	}
	private static Question validateAndPopulateQuestion(
			List<String> questionRecord, User loggedInUser) {

		byte answerIndex = ANSWER_START_INDEX;
		Question question = new Question();

		// Validate & save Group
		question.setQuestionGroup(questionRecord.get(0));

		// Validate & save questionType
		question.setQuestionType(questionRecord.get(1));

		// Validate & Save question text
		String questionText = questionRecord.get(2);
		if(ParikshaConstants.FUNC_JUMBLE.equalsIgnoreCase(questionText)) {
			questionText = shuffleWord(questionRecord.get(5));
		}
		question.setQuestionText(questionText);

		// Validate & Save image name
		if(StringUtils.isNotBlank(questionRecord.get(3))) {
			question.setImageName(questionRecord.get(3));
		}

		// Save RecordInfo
		RecordInfo recordInfo = WebUtils.populateCreateInfo(loggedInUser, null);

		// Populate Answers
		List<Answer> answerList = new ArrayList<>();

		// Populate Answers
		populateAnswer(answerIndex, answerList, questionRecord, recordInfo);

		Set<Answer> answerSet = populateCorrectAnswers(questionRecord, answerList);
		question.setAnswers(answerSet);

		return question;
	}

	private static Set<Answer> populateCorrectAnswers(List<String> questionRecord, List<Answer> answerList) {

		int dataType = 0;
		String correctAnswers = questionRecord.get(ANSWER_START_INDEX);
		if(null == correctAnswers) {
			correctAnswers = StringConstants.EMPTY_STRING;
		}

		if(correctAnswers.toLowerCase().startsWith(ParikshaConstants.STRING_VALUE_INT_COLON)) {
			dataType = 1;
			correctAnswers = correctAnswers.substring(ParikshaConstants.STRING_VALUE_INT_COLON.length());
		} else if(correctAnswers.equalsIgnoreCase(ParikshaConstants.STRING_VALUE_ANY)) {
			dataType = 999;
		} else {
			throw new IllegalArgumentException("Correct Answers has not supported value of ["+correctAnswers+"]");
		}
		String[] correctAnswerArray = correctAnswers.split(CommonConstants.STR_COMMA);
		logger.debug("Array : {}", correctAnswerArray.length);
		for(int i=0;i<correctAnswerArray.length;i++)
		{
			logger.debug("array{}: {}", i, correctAnswerArray[i]);
			int correctAnswerIndex = 0;
			String correctAns = correctAnswerArray[i];
			logger.debug("length of correctAns: {}", correctAns.length());
			if(StringUtils.isBlank(correctAns)) {
				continue;
			}
			if(dataType == 1) {

				correctAnswerIndex = Integer.parseInt(correctAns.trim());
				answerList.get(correctAnswerIndex-1).setCorrectOption(true);
            } else if (dataType == 999){

				correctAnswerIndex = i+1;
				// set all answers as correct option
				answerList.stream().forEach(t -> t.setCorrectOption(true));
            } else {
            	System.out.println(""+correctAnswerIndex);
            }
        }

		// Save answerList
		Set<Answer> answerSet = new LinkedHashSet<>();
		answerSet.addAll(answerList);
		return answerSet;
	}

	private static Answer validateAndPopulateAnswer(String answerText, RecordInfo recordInfo) {

		Answer answer = new Answer();
		if(answerText.toLowerCase().startsWith(ParikshaConstants.STRING_VALUE_INT_COLON)) {
			answerText = answerText.substring(ParikshaConstants.STRING_VALUE_INT_COLON.length());
		}
		answer.setAnswerText(answerText.trim().replaceAll("\\s+"," "));
		answer.setCorrectOption(false);

		return answer;
	}

	public static boolean containsQuestionType(String type) {

		for (QuestionType c : QuestionType.values()) {
			if (c.name().equals(type)) {
				return true;
			}
		}
		return false;
	}

	public static String incrementVersion(Integer version) {

		Integer upgradedVersion = 1;
		if(null != version) {
			upgradedVersion = version +1;
		}
		return upgradedVersion.toString();
	}

	public static void processMediaFile(MultipartFile mediaFile, long timeInMillis) throws IOException {

		ApplicationContext appCtx = ApplicationContextProvider.getApplicationContext();
		if(null == mediaFile) {
			return;
		}

		long mediaFileSize = mediaFile.getSize();
		Long maxSizeSupported = appCtx.getEnvironment().getProperty("pariksha.eval.mediaFile.max-size", Long.class);
		if(null == maxSizeSupported) {
			maxSizeSupported = 1024 * 1024 * 10l;
		}
		if(mediaFileSize>maxSizeSupported) {
			throw new IllegalArgumentException("Exceeds supported file size: "+maxSizeSupported);
		}
		String mediaDir = getParikshaMediaDir();
		System.out.println("mediaDir: "+mediaDir);
		String tempDirFullName = mediaDir.concat(String.valueOf(timeInMillis));
		File tempDir = new File(tempDirFullName);
		if(!tempDir.mkdir()) {
			throw new RuntimeException("Could not create temp dir: "+mediaDir.concat(String.valueOf(timeInMillis)));
		}
		String targetMediaFileName = tempDirFullName.concat(File.separator).concat(mediaFile.getOriginalFilename());
		File targetMediaFile = new File(targetMediaFileName);

		try {
			mediaFile.transferTo( targetMediaFile );
		} catch (IllegalStateException|IOException e) {
			e.printStackTrace();
			throw e;
		}
		unZipUploadedFile(targetMediaFileName, tempDirFullName);
		targetMediaFile.delete();
	}

	public static String getParikshaMediaDir() {

		return MEDIA_DIR;
	}

	public static String unZipUploadedFile(String zipFile, String outputFolder) {

		byte[] buffer = new byte[1024];
		StringBuilder errorMessage = new StringBuilder();
		try {
			ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
			ZipEntry ze = zis.getNextEntry();
			// FileUtils.getFileName(zipFile);
			while (ze != null) {
				String fileName = ze.getName();
				File newFile = new File(outputFolder + File.separator + fileName);
				logger.debug("file unzip" + newFile.getAbsolutePath());
				if (ze.isDirectory()) {
					String temp = newFile.getCanonicalPath();
					new File(temp).mkdir();
				} else {
					FileOutputStream fos = new FileOutputStream(newFile);
					int len;
					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}
					fos.close();
				}
				ze = zis.getNextEntry();
			}
			zis.closeEntry();
			zis.close();
			logger.debug("unzip is completed");

		} catch (IOException ex) {
			// errorMessage.append("<br>Error in uploaded media file.");
			System.out.println(ex);
			logger.info("Exception caught in unZipUploadedFile(): " + ex.getMessage());
		}
		return errorMessage.toString();
	}

	private static String shuffleWord(String questionText) {

		return StringUtil.getShuffledWord(questionText);
	}

	public static String getEvalNameFromFileName(String fileName) {
		int underscoreIdx = fileName.indexOf("_");
		underscoreIdx = fileName.indexOf("_", underscoreIdx+1);
		return fileName.substring(underscoreIdx+1);
	}

}
