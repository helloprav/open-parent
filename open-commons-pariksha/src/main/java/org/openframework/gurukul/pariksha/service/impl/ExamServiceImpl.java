package org.openframework.gurukul.pariksha.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.openframework.commons.domain.exceptions.EntityNotFoundException;
import org.openframework.commons.jpa.entity.User;
import org.openframework.commons.rest.vo.UserVO;
import org.openframework.commons.utils.CookieUtils;
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
import org.openframework.gurukul.pariksha.vo.AnswerVO;
import org.openframework.gurukul.pariksha.vo.EvalResult;
import org.openframework.gurukul.pariksha.vo.EvalStatsVO;
import org.openframework.gurukul.pariksha.vo.EvaluationVO;
import org.openframework.gurukul.pariksha.vo.ExamState;
import org.openframework.gurukul.pariksha.vo.QuestionVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExamServiceImpl implements ExamService {

	protected static final String EXAM_CACHE = "group-cache";

	@Autowired
	private EvaluationService evaluationService;

	@Autowired
	private EvaluationRepository evaluationRepository;

	@Autowired
	private EvalStatsRepository evalStatsRepository;

	@Autowired
	private QuestionRepository questionRepository;

	/** Logger that is available to subclasses */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

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

	@Override
	public Map<String, Object> evaluationCompleted(String examStateCookie, UserVO loggedInUser) {

		int marks = 0;
		Integer questAttemptCount = 0;
		Date evalCompetedDate = new Date();
		Map<String, Object> result = new HashMap<>();

		// prepare required objects i.e. examState, evaluationVO, questionSet
		ExamState examState = CookieUtils.readObjectFromCookie(examStateCookie, ExamState.class);
		EvaluationVO evaluationVO = evaluationService.findEvaluationVOById(examState.getId());
		List<QuestionVO> questionSet = evaluationVO.getQuestions();

		// check answers and calculate marks
		marks = checkAnswers(examState, questionSet, questAttemptCount);

		String resultStr = "You received " + marks + "/" + evaluationVO.getQuestionsInEval();
		logger.debug("Result: {}", resultStr);

		// Prepare and save evalStats in database
		EvalStats evalStats = new EvalStats();
		evalStats.setUser(new User(loggedInUser.getId()));
		evalStats.setEvaluation(new Evaluation(examState.getId()));
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

		// Prepare and save exam result for return to UI
		EvalResult evalResult = new EvalResult();
		evalResult.setEvalId(examState.getId());
		evalResult.setStartDate(examState.getStartDate());
		evalResult.setEndDate(evalCompetedDate);
		evalResult.setEvalPassed(marks >= evaluationVO.getQuestionsToPass());
		evalResult.setTotalQuestions(examState.getqSize());
		evalResult.setPassMarks(evaluationVO.getQuestionsToPass());
		evalResult.setAttemptedQuestions(questAttemptCount);
		evalResult.setCorrectQuestions(marks);

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
	private int checkAnswers(ExamState examState, List<QuestionVO> questionSet, Integer questAttemptCount) {

		int marks = 0;
		// Outer loop: iterate over user's answer
		Iterator<Entry<Long, List<String>>> iterator = examState.getUserQuestionMap().entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Long, List<String>> userEntry = iterator.next();
			Long qId = userEntry.getKey();
			List<String> userQuestionVO = userEntry.getValue();

			// Inner loop: iterate over database question
			Iterator<QuestionVO> questionVOIterator = questionSet.iterator();
			while (questionVOIterator.hasNext()) {
				QuestionVO questionVOInDB = questionVOIterator.next();
				// db's question id matches with user's question id
				if (questionVOInDB.getId().equals(qId)) {
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
}
