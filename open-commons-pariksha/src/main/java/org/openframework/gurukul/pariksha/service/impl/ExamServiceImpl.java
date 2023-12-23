package org.openframework.gurukul.pariksha.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.openframework.commons.domain.exceptions.EntityNotFoundException;
import org.openframework.commons.jpa.entity.User;
import org.openframework.commons.redis.RedisConfiguration;
import org.openframework.commons.rest.vo.UserVO;
import org.openframework.commons.utils.CookieUtils;
import org.openframework.commons.utils.JsonUtils;
import org.openframework.gurukul.pariksha.ParikshaConstants;
import org.openframework.gurukul.pariksha.entity.EvalStats;
import org.openframework.gurukul.pariksha.entity.Evaluation;
import org.openframework.gurukul.pariksha.entity.Question;
import org.openframework.gurukul.pariksha.service.EvaluationService;
import org.openframework.gurukul.pariksha.service.ExamService;
import org.openframework.gurukul.pariksha.service.adapter.EvalStatsAdapter;
import org.openframework.gurukul.pariksha.service.adapter.EvaluationAdapter;
import org.openframework.gurukul.pariksha.service.adapter.QuestionAdapter;
import org.openframework.gurukul.pariksha.service.repository.EvalStatsRepository;
import org.openframework.gurukul.pariksha.service.repository.EvaluationRepository;
import org.openframework.gurukul.pariksha.service.repository.QuestionRepository;
import org.openframework.gurukul.pariksha.utils.EvaluationUtils;
import org.openframework.gurukul.pariksha.utils.QuestionVOUtils;
import org.openframework.gurukul.pariksha.vo.AnswerVO;
import org.openframework.gurukul.pariksha.vo.EvalResult;
import org.openframework.gurukul.pariksha.vo.EvalStatsVO;
import org.openframework.gurukul.pariksha.vo.EvaluationVO;
import org.openframework.gurukul.pariksha.vo.ExamState;
import org.openframework.gurukul.pariksha.vo.QuestionVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;

@Lazy
@Service
public class ExamServiceImpl implements ExamService {

	protected static final String USER_RESPONSE_KEY_FORMAT = "urk_%s";

	protected static final List<String> ALLOWED_STORAGE_TYPES = new ArrayList<>();

	private static boolean redisStorageEnabled = false;

	private static boolean cookieStorageEnabled = false;

	@Autowired
	private EvaluationService evaluationService;

	@Autowired
	private EvaluationRepository evaluationRepository;

	@Autowired
	private EvalStatsRepository evalStatsRepository;

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	/** Logger that is available to subclasses */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${pariksha.exam.session.storage:}")
	private List<String> sessionStorages;

	static {
		ALLOWED_STORAGE_TYPES.add(ParikshaConstants.STORAGE_TYPE_COOKIE);
		// TODO add support for below database storage type
		// ALLOWED_STORAGE_TYPES.add(ParikshaConstants.STORAGE_TYPE_DATABASE);
		ALLOWED_STORAGE_TYPES.add(ParikshaConstants.STORAGE_TYPE_REDIS);
	}

	public static boolean isRedisStorageEnabled() {
		return redisStorageEnabled;
	}
	public static void setRedisStorageEnabled(boolean redisStorageEnabled) {
		ExamServiceImpl.redisStorageEnabled = redisStorageEnabled;
	}
	public static boolean isCookieStorageEnabled() {
		return cookieStorageEnabled;
	}
	public static void setCookieStorageEnabled(boolean cookieStorageEnabled) {
		ExamServiceImpl.cookieStorageEnabled = cookieStorageEnabled;
	}

	public static String getUserResponseKey(String id) {
		return String.format(USER_RESPONSE_KEY_FORMAT, id);
	}

	@PostConstruct
	public void init() {
		ListIterator<String> iterator = sessionStorages.listIterator();
		while (iterator.hasNext()) {
			String storageType = iterator.next();
			if (!ALLOWED_STORAGE_TYPES.contains(storageType)) {
				logger.error("Configured storage type [{}] is not supported", storageType);
				iterator.remove();
			}
		}
		if(sessionStorages.isEmpty()) {
			sessionStorages.add(ParikshaConstants.STORAGE_TYPE_COOKIE);
		}
		if(sessionStorages.contains(ParikshaConstants.STORAGE_TYPE_COOKIE)) {
			setCookieStorageEnabled(true);
		}
		if(sessionStorages.contains(ParikshaConstants.STORAGE_TYPE_REDIS)) {
			setRedisStorageEnabled(true);
		}
	}

	public boolean isRedisSessionStorageEnabled() {
		return isRedisStorageEnabled() && RedisConfiguration.isRedisServerEnabled();
	}

	@Override
	public EvaluationVO startExam(Long evalId, boolean testRun, Long userId, HttpServletResponse response) {

		EvaluationVO evaluationVO = evaluationService.findEvaluationVOById(evalId);

		ExamState examState = prepareExamState(evalId, userId, EvaluationUtils.getQIdsFromEvaluation(evaluationVO), testRun);
		createExamStateCookie(examState, response);
		return evaluationVO;
	}

	@Override
	public List<QuestionVO> findQuestionsByEvalId(Long evalId) {

		EvaluationVO evaluationVO;
		Optional<Evaluation> eval = evaluationRepository.findById(evalId);
		if (eval.isPresent()) {
			evaluationVO = EvaluationAdapter.toVO(eval.get());
			logger.debug("question size in eval: {}", evaluationVO.getQuestions().size());
		} else {
			throw new EntityNotFoundException("Eval id not found");
		}

		return evaluationVO.getQuestions();
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public Map<String, Object> evaluationCompleted(String examStateCookie, String userQuestionMapCookie, UserVO loggedInUser) {

		int marks = 0;
		Integer questAttemptCount = 0;
		Date evalCompetedDate = new Date();
		Map<String, Object> result = new HashMap<>();

		// prepare required objects i.e. examState, evaluationVO, questionSet
		ExamState examState = CookieUtils.readObjectFromCookie(examStateCookie, ExamState.class);
		EvaluationVO evaluationVO = evaluationService.findEvaluationVOById(examState.getEid());
		List<QuestionVO> questionSet = evaluationVO.getQuestions();

		Map<String, List<String>> userQuestionMap = null;

		if(cookieStorageEnabled && StringUtils.isNotBlank(userQuestionMapCookie)) {
			userQuestionMap = CookieUtils.readObjectFromCookie(userQuestionMapCookie, Map.class);
		}

		if(isRedisSessionStorageEnabled()) {

			userQuestionMap = findAllUserResponsesFromRedis(examState.getId());
		}

		if(null == userQuestionMap) {
			userQuestionMap = new HashMap<>();
		}

		// check answers and calculate marks
		marks = checkAnswers(userQuestionMap, questionSet, questAttemptCount);

		String resultStr = "You received " + marks + "/" + evaluationVO.getQuestionsInEval();
		logger.debug("Result: {}", resultStr);

		// Prepare and save evalStats in database
		EvalStats evalStats = new EvalStats();
		evalStats.setUser(new User(loggedInUser.getId()));
		evalStats.setEvaluation(new Evaluation(examState.getEid()));
		evalStats.setEvalStartDateTime(examState.getStartDate());
		evalStats.setEvalEndDateTime(evalCompetedDate);
		evalStats.setEvaluationStatPassed(marks >= evaluationVO.getQuestionsToPass());
		evalStats.setCorrectAnswerCount(marks);
		evalStats.setQuestAttemptCount(questAttemptCount);
		evalStats.setEvalQuestCount(evaluationVO.getQuestionsInEval());

		// save stats if not a test run
		if(!examState.isTestRun()) {
			evalStatsRepository.save(evalStats);
		}

		deleteUserQuestionMapFromRedis(examState.getId());

		// Prepare and save exam result for return to UI
		EvalResult evalResult = new EvalResult();
		evalResult.setEvalId(examState.getEid());
		evalResult.setStartDate(examState.getStartDate());
		evalResult.setEndDate(evalCompetedDate);
		evalResult.setEvalPassed(marks >= evaluationVO.getQuestionsToPass());
		evalResult.setTotalQuestions(examState.getqSize());
		evalResult.setPassMarks(evaluationVO.getQuestionsToPass());
		evalResult.setAttemptedQuestions(questAttemptCount);
		evalResult.setCorrectQuestions(marks);

		// clear the redis cache
		result.put("evalStats", evalStats);
		result.put("questionSet", questionSet);
		result.put("evalResult", evalResult);
		return result;
	}

	/**
	 * 
	 * @param examState
	 * @param questionSet
	 * @return
	 */
	private int checkAnswers(Map<String, List<String>> userQuestionMap, List<QuestionVO> questionSet, Integer questAttemptCount) {

		int marks = 0;
		// Outer loop: iterate over user's answer
		Iterator<Entry<String, List<String>>> iterator = userQuestionMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, List<String>> userEntry = iterator.next();
			String qId = userEntry.getKey();
			List<String> userQuestionVO = userEntry.getValue();

			// Inner loop: iterate over database question
			Iterator<QuestionVO> questionVOIterator = questionSet.iterator();
			while (questionVOIterator.hasNext()) {
				QuestionVO questionVOInDB = questionVOIterator.next();
				// db's question id matches with user's question id
				if (qId.equals(questionVOInDB.getId().toString())) {
					questionVOInDB.setUserAnswerList(userQuestionVO);
					if(!userQuestionVO.isEmpty()) {
						++questAttemptCount;
					}
					if(isCorrectAnswer(userQuestionVO, questionVOInDB)) {
						marks++;
						// this property (isOk) is not saved in db here. It is used to display the result on UI
						questionVOInDB.setIsOk(true);
						logger.debug("Q %d. correctAnswer: TRUE,  {} {}", qId, userQuestionVO);
					} else {
						// this property (isOk) is not saved in db here. It is used to display the result on UI
						questionVOInDB.setIsOk(false);
						logger.debug("Q %d. wrongAnswer:  TRUE, {} {}", qId, userQuestionVO);
					}
					break;
				}
			}
		}
		return marks;
	}

	private boolean isCorrectAnswer(List<String> userQuestionVO, QuestionVO questionVOInDB) {

		boolean isCorrect = false;
		if(questionVOInDB.getQuestionType().equalsIgnoreCase(ParikshaConstants.Q_TYPE_FILL_WORD)) {
			if (isCorrectAnswerForFillGaps(userQuestionVO, questionVOInDB)) {
				isCorrect = true;
			}
		} else {
			if (isCorrectAnswerForOthers(userQuestionVO, questionVOInDB)) {
				isCorrect = true;
			}
		}
		return isCorrect;
	}

	private boolean isCorrectAnswerForFillGaps(List<String> answers, QuestionVO questionVOInDB) {

		boolean corretAnswer = false;
		if(answers.isEmpty()) {
			return false;
		}
		List<AnswerVO> answersInDB = questionVOInDB.getAnswers();
		Iterator<AnswerVO> iterator = answersInDB.iterator();
		while (iterator.hasNext()) {
			AnswerVO answerVO = iterator.next();
			if (Boolean.TRUE.equals(answerVO.getCorrectOption())) {
				String userSubmittedAnswer = answers.get(0);
				userSubmittedAnswer = userSubmittedAnswer.trim().replaceAll("\\s+"," ");
				if (answerVO.getAnswerText().equalsIgnoreCase(userSubmittedAnswer)) {
					corretAnswer = true;
					break;
				}
			}
		}
		return corretAnswer;
	}

	private boolean isCorrectAnswerForFillGaps(QuestionVO userQuestionVO, QuestionVO questionVOInDB) {

		boolean corretAnswer = false;
		List<String> answers = userQuestionVO.getUserAnswerList();
		if(answers.isEmpty()) {
			return false;
		}
		List<AnswerVO> answersInDB = questionVOInDB.getAnswers();
		Iterator<AnswerVO> iterator = answersInDB.iterator();
		while (iterator.hasNext()) {
			AnswerVO answerVO = iterator.next();
			if (Boolean.TRUE.equals(answerVO.getCorrectOption())) {
				String userSubmittedAnswer = answers.get(0);
				userSubmittedAnswer = userSubmittedAnswer.trim().replaceAll("\\s+"," ");
				if (answerVO.getAnswerText().equalsIgnoreCase(userSubmittedAnswer)) {
					corretAnswer = true;
					break;
				}
			}
		}
		return corretAnswer;
	}


	private boolean isCorrectAnswerForOthers(List<String> answers, QuestionVO questionVOInDB) {

		boolean correctAnswer = true;
		List<AnswerVO> answersInDB = questionVOInDB.getAnswers();
		Iterator<AnswerVO> iterator = answersInDB.iterator();
		while (iterator.hasNext()) {
			AnswerVO answerVO = iterator.next();
			if(Boolean.TRUE.equals(answerVO.getCorrectOption())) {
				if(!answers.contains(Long.toString(answerVO.getId()))) {
					correctAnswer = false;
					break;
				}
			} else {
				if(answers.contains(Long.toString(answerVO.getId()))) {
					correctAnswer = false;
					break;
				}
			}
		}
		return correctAnswer;
	}

	private boolean isCorrectAnswerForOthers(QuestionVO userQuestionVO, QuestionVO questionVOInDB) {

		boolean correctAnswer = true;
		List<String> answers = userQuestionVO.getUserAnswerList();
		List<AnswerVO> answersInDB = questionVOInDB.getAnswers();
		Iterator<AnswerVO> iterator = answersInDB.iterator();
		while (iterator.hasNext()) {
			AnswerVO answerVO = iterator.next();
			if(Boolean.TRUE.equals(answerVO.getCorrectOption())) {
				if(!answers.contains(Long.toString(answerVO.getId()))) {
					correctAnswer = false;
					break;
				}
			} else {
				if(answers.contains(Long.toString(answerVO.getId()))) {
					correctAnswer = false;
					break;
				}
			}
		}
		return correctAnswer;
	}

	@Override
	public void saveEvalStats(EvalStats evalStats) {

		evalStatsRepository.save(evalStats);
	}

	@Override
	public List<EvalStatsVO> findEvaluationStatsByUserId(Long userId) {

		List<EvalStats> evalStats = evalStatsRepository.getByUserId(userId);
		return EvalStatsAdapter.toVO(evalStats);
	}

	@Override
	public boolean checkAnswer(QuestionVO questionVOFromUserResponse) {

		// TODO: ensure checkAnswer feature is allowed for this eval/question
		boolean correctAnswer = false;
		Optional<Question> questionOpt = questionRepository.findById(questionVOFromUserResponse.getId());
		if(questionOpt.isPresent()) {
			Question question = questionOpt.get();
			QuestionVO questionVOInDB = QuestionAdapter.toVO(question);
			if(question.getQuestionType().equalsIgnoreCase(ParikshaConstants.Q_TYPE_FILL_WORD)) {
				if (isCorrectAnswerForFillGaps(questionVOFromUserResponse, questionVOInDB)) {
					correctAnswer = true;
					logger.debug("Q %d. correctAnswer: TRUE, {} {}", questionVOFromUserResponse.getId(), questionVOFromUserResponse.getUserAnswerList());
				} else {
					logger.debug("Q %d. wrongAnswer: TRUE, {} {}", questionVOFromUserResponse.getId(), questionVOFromUserResponse.getUserAnswerList());
				}
			} else {
				if (isCorrectAnswerForOthers(questionVOFromUserResponse, questionVOInDB)) {
					correctAnswer = true;
					logger.debug("Q %d. correctAnswer: TRUE, {} {}", questionVOFromUserResponse.getId(), questionVOFromUserResponse.getUserAnswerList());
				} else {
					logger.debug("Q %d. wrongAnswer: TRUE, {} {}", questionVOFromUserResponse.getId(), questionVOFromUserResponse.getUserAnswerList());
				}
			}
		}
		return correctAnswer;
	}

	private ExamState prepareExamState(Long evalId, Long userId, List<Long> qIdList, boolean testRun) {
		logger.debug("Entered createUserEvalCookie(eval:{})", evalId);
		logger.debug("List of qId for this test: {}", qIdList);
		ExamState examState = new ExamState();
		examState.setEid(evalId);
		examState.setUid(userId);
		examState.setTestRun(testRun);
		examState.setStartDate(new Date());
		examState.setqSize(qIdList.size());
		examState.setqIds(qIdList);
		return examState;
	}

	private void createExamStateCookie(ExamState examState, HttpServletResponse response) {
		String jsonString = JsonUtils.convertObjectToJsonString(examState);
		response.addCookie(CookieUtils.createCookieEncoded(ParikshaConstants.COOKIE_EXAM_STATE, jsonString));
	}

	@Override
	public Map<String, Object> evaluateQuestion(int nextQuestionSeq, Long evalId, Long uid,
			QuestionVO questionVOFromUserResponse, String examStateCookie, String userQuestionMapCookie,
			String skipQuestion, HttpServletResponse response) {

		logger.debug("Entered evaluateQuestion(nextQuestionSeq:{})", nextQuestionSeq);

		// Step 1: Read UerEvaluation from request cookie
		ExamState examState = CookieUtils.readObjectFromCookie(examStateCookie, ExamState.class);

		int questionSize = 0;
		QuestionVO questionVO = null;
		List<QuestionVO> questionsVOList = null;
		Map<String, Object> result = new HashMap<>();

		// TODO cache the below function
		questionsVOList = findQuestionsByEvalId(evalId);

		questionSize = questionsVOList.size();
		result.put("questionSize", questionSize);
		result.put("examState", examState);

		if (nextQuestionSeq > questionSize) {

//			Map<String, List<String>> userResponseMap = 
			updateUserResponseCookie(userQuestionMapCookie, questionVOFromUserResponse, response);

			// save submitted response in redis
			if (isRedisSessionStorageEnabled()) {
				createUserResponseInRedis(examState.getId(), questionVOFromUserResponse);
			}

			result.put("evalCompleted", "evalCompleted");
		} else {
			Long questionId = examState.getqIds().get(nextQuestionSeq - 1);
			questionVO = QuestionVOUtils.getQuestionsVOFromList(questionsVOList, questionId);

			Map<String, List<String>> userResponseMap = updateUserResponse(examState.getqIds().get(nextQuestionSeq - 1),
					questionVOFromUserResponse, examState, userQuestionMapCookie, skipQuestion, response);

			// Populate User submitted answer if any, & set in userQuestion
			List<String> userSubmittedAnswers = userResponseMap.get(questionId.toString());
			result.put("userQuestion", userSubmittedAnswers);

			if (null != userSubmittedAnswers && !userSubmittedAnswers.isEmpty()) {
				questionVO.setUserAnswerList(userSubmittedAnswers);
			}
			result.put("question", questionVO);
			result.put("answers", questionVO.getAnswers());
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private Map<String, List<String>> updateUserResponseCookie(String userQuestionMapCookie,
			QuestionVO questionVOFromUserResponse, HttpServletResponse response) {

		Map<String, List<String>> userResponseMap = null;

		if(cookieStorageEnabled) {

			// retrieve UserQuestionMap from cookie
			if(StringUtils.isNotBlank(userQuestionMapCookie)) {
				userResponseMap = CookieUtils.readObjectFromCookie(userQuestionMapCookie, Map.class);
			}

			if(null == userResponseMap) {
				userResponseMap = new HashMap<>();
			}

			// update the UserQuestionMap with submitted question and save map in cookie
			if(null!= questionVOFromUserResponse.getId()) {
				userResponseMap.put(questionVOFromUserResponse.getId().toString(), questionVOFromUserResponse.getUserAnswerList());
			}
			String jsonString = JsonUtils.convertObjectToJsonString(userResponseMap);
			response.addCookie(CookieUtils.createCookieEncoded(ParikshaConstants.COOKIE_QUEST_MAP, jsonString));
		}
		return userResponseMap;
	}

	private Map<String, List<String>> updateUserResponse(Long nextQuestionId, QuestionVO questionVOFromUserResponse,
			ExamState examState, String userQuestionMapCookie, String skipQuestion, HttpServletResponse response) {

		// Step 1: Read UerEvaluation from request cookie
		Map<String, List<String>> userResponseMap = updateUserResponseCookie(userQuestionMapCookie, questionVOFromUserResponse, response);

		if(null == userResponseMap) {
			userResponseMap = new HashMap<>();
		}

		// skipQuestion==null means fetch and show next questions
		// skipQuestion=='skip' means it is last question.
		if (isRedisSessionStorageEnabled()) {
			if(null == skipQuestion) {

				// save submitted response in redis
				createUserResponseInRedis(examState.getId(), questionVOFromUserResponse);
			}

			//retrieve already submitted (if any) response to be populated on UI
			List<String> savedResponse = findUserResponseByQuestionIdFrmRedis(examState.getId(), nextQuestionId);
			userResponseMap.put(nextQuestionId.toString(), savedResponse);
		}
		return userResponseMap;
	}

	private void createUserResponseInRedis(String id, QuestionVO questionVOFromUserResponse) {

		if(null != questionVOFromUserResponse.getId()) {
			redisTemplate.opsForHash().put(getUserResponseKey(id), questionVOFromUserResponse.getId(), questionVOFromUserResponse.getUserAnswerList());
		}
	}

	@SuppressWarnings("unchecked")
	private List<String> findUserResponseByQuestionIdFrmRedis(String id, Long nextQuestionId) {

		Object result = null;
		if(null != nextQuestionId) {
			result = redisTemplate.opsForHash().get(getUserResponseKey(id), nextQuestionId);
			if(result instanceof List) {
				return (List<String>)result;
			} else {
				return new ArrayList<>();
			}
		}
		return new ArrayList<>();
	}

	@SuppressWarnings("unchecked")
	private Map<String, List<String>> findAllUserResponsesFromRedis(String id) {

		Map<String, List<String>> userQuestionMap = new HashMap<>();
		Map<Object, Object> result = redisTemplate.opsForHash().entries(getUserResponseKey(id));
		for (Map.Entry<Object, Object> entry : result.entrySet()) {
			userQuestionMap.put(entry.getKey().toString(), (List<String>)entry.getValue());
			System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		}
		System.out.println("userQuestionMap: "+userQuestionMap);
		System.out.println("id: "+id);
		return userQuestionMap;
	}

	private void deleteUserQuestionMapFromRedis(String id) {

		Map<Object, Object> result = redisTemplate.opsForHash().entries(getUserResponseKey(id));
		for (Map.Entry<Object, Object> entry : result.entrySet()) {
			System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
			redisTemplate.opsForHash().delete(getUserResponseKey(id), entry.getKey());
		}
		System.out.println("Entries deleted for key: "+id);
	}

}
