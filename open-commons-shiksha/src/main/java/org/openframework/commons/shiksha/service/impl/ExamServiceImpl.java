package org.openframework.commons.shiksha.service.impl;

import java.util.ArrayList;
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
import org.openframework.commons.shiksha.ShikshaConstants;
import org.openframework.commons.shiksha.entity.EvalStats;
import org.openframework.commons.shiksha.entity.Evaluation;
import org.openframework.commons.shiksha.entity.Question;
import org.openframework.commons.shiksha.service.ExamService;
import org.openframework.commons.shiksha.service.adapter.EvalStatsAdapter;
import org.openframework.commons.shiksha.service.adapter.EvaluationAdapter;
import org.openframework.commons.shiksha.service.adapter.QuestionAdapter;
import org.openframework.commons.shiksha.service.repository.EvalStatsRepository;
import org.openframework.commons.shiksha.service.repository.EvaluationRepository;
import org.openframework.commons.shiksha.service.repository.QuestionRepository;
import org.openframework.commons.shiksha.vo.AnswerVO;
import org.openframework.commons.shiksha.vo.EvalStatsVO;
import org.openframework.commons.shiksha.vo.EvaluationVO;
import org.openframework.commons.shiksha.vo.QuestionVO;
import org.openframework.commons.shiksha.vo.UserEvaluation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExamServiceImpl implements ExamService {

	protected static final String EXAM_CACHE = "group-cache";

	@Autowired
	private EvaluationRepository evaluationRepository;

	@Autowired
	private EvalStatsRepository evalStatsRepository;

	@Autowired
	private QuestionRepository questionRepository;

	@Override
	public Map<String, Object> findEvaluationByIdWithQuestions(Long evalId) {

		EvaluationVO evaluationVO;
		Map<String, Object> evalContainer = new HashMap<>();
		Optional<Evaluation> eval = evaluationRepository.findById(evalId);
		if (eval.isPresent()) {
			evaluationVO = EvaluationAdapter.toVO(eval.get());
			System.out.println(evaluationVO.getQuestions().size());
		} else {
			throw new EntityNotFoundException("Eval id not found");
		}

		// 1. prepare quesionMap
		int i = 0;
		int totalNoOfQuestions = evaluationVO.getQuestionsInEval();
		List<QuestionVO> questionSet = evaluationVO.getQuestions();
		List<Long> questionIds = new ArrayList<>();
		Iterator<QuestionVO> iterator = questionSet.iterator();
		while (iterator.hasNext()) {

			QuestionVO quest = iterator.next();
			System.out.println("Question ID: " + quest.getId());
			if (i < totalNoOfQuestions) {
				questionIds.add(quest.getId());
			}
		}
		evalContainer.put("questionSet", questionSet);

		// 2. prepare evaluationForReference
		EvaluationVO evaluationForReference = copyEvaluationVO(evaluationVO);
		evalContainer.put("questionIds", questionIds);
		evalContainer.put("evaluationForReference", evaluationForReference);

		return evalContainer;
	}

	@Override
	public List<QuestionVO> findQuestionsByEvalId(Long evalId) {

		EvaluationVO evaluationVO;
		Optional<Evaluation> eval = evaluationRepository.findById(evalId);
		if (eval.isPresent()) {
			evaluationVO = EvaluationAdapter.toVO(eval.get());
			System.out.println(evaluationVO.getQuestions().size());
		} else {
			throw new EntityNotFoundException("Eval id not found");
		}

		return evaluationVO.getQuestions();
	}

	private EvaluationVO copyEvaluationVO(EvaluationVO evaluationVO) {

		EvaluationVO sessionEvaluationVO = new EvaluationVO();
		sessionEvaluationVO.setDescription(evaluationVO.getDescription());
		sessionEvaluationVO.setEvalGroup(evaluationVO.getEvalGroup());
		sessionEvaluationVO.setId(evaluationVO.getId());
		sessionEvaluationVO.setName(evaluationVO.getName());
		sessionEvaluationVO.setQuestionsInEval(evaluationVO.getQuestionsInEval());
		sessionEvaluationVO.setQuestionsToAttempt(evaluationVO.getQuestionsToAttempt());
		sessionEvaluationVO.setQuestionsToPass(evaluationVO.getQuestionsToPass());
		return sessionEvaluationVO;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> evaluationCompleted(UserEvaluation userEvaluation, UserVO loggedInUser) {

		Map<String, Object> result = new HashMap<>();
		int marks = 0;
		int questAttemptCount = 0;
		Date evalCompetedDate = new Date();
		Map<String, Object> evalContainer = findEvaluationByIdWithQuestions(userEvaluation.getEvaluationID());
		List<QuestionVO> questionSet = (List<QuestionVO>) evalContainer.get("questionSet");

		Iterator<Entry<Long, QuestionVO>> iterator = userEvaluation.getUserQuestionMap().entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Long, QuestionVO> userEntry = iterator.next();
			Long qId = userEntry.getKey();
			QuestionVO userQuestionVO = userEntry.getValue();
			if(null!=userQuestionVO) {
				
			}
			Iterator<QuestionVO> questionVOIterator = questionSet.iterator();
			while (questionVOIterator.hasNext()) {
				QuestionVO questionVO2 = questionVOIterator.next();
				if (!questionVO2.getId().equals(userQuestionVO.getId())) {
					continue;
				}
				questionVO2.setUserAnswerList(userQuestionVO.getUserAnswerList());
				if(!userQuestionVO.getUserAnswerList().isEmpty()) {
					++questAttemptCount;
				}
				if(questionVO2.getQuestionType().equalsIgnoreCase(ShikshaConstants.Q_TYPE_FILL_WORD)) {
					if (isCorrectFillGapsAnswer(userQuestionVO, questionVO2)) {
						marks++;
						questionVO2.setIsOk(true);
						System.out.printf("Q %d. correctAnswer: TRUE, %s %n", qId, userQuestionVO.getUserAnswerList());
					} else {
						questionVO2.setIsOk(false);
						System.out.printf("Q %d. wrongAnswer: TRUE, %s %n", qId, userQuestionVO.getUserAnswerList());
					}
				} else {
					if (isCorrectAnswer(userQuestionVO, questionVO2)) {
						marks++;
						questionVO2.setIsOk(true);
						System.out.printf("Q %d. correctAnswer: TRUE, %s %n", qId, userQuestionVO.getUserAnswerList());
					} else {
						questionVO2.setIsOk(false);
						System.out.printf("Q %d. wrongAnswer: TRUE, %s %n", qId, userQuestionVO.getUserAnswerList());
					}
				}
				break;
			}
		}

		EvaluationVO evaluationForReference = (EvaluationVO) evalContainer.get("evaluationForReference");
		String resultStr = "You received " + marks + "/" + evaluationForReference.getQuestionsToAttempt();
		System.out.println("Result: " + resultStr);
		userEvaluation.setCorrectAttempts(marks);

		EvalStats evalStats = new EvalStats();
		evalStats.setUser(new User(loggedInUser.getId()));
		evalStats.setEvaluation(new Evaluation(userEvaluation.getEvaluationID()));
		evalStats.setEvalStartDateTime(userEvaluation.getEvalStartDate());
		evalStats.setEvalEndDateTime(evalCompetedDate);
		evalStats.setEvaluationStatPassed(marks >= evaluationForReference.getQuestionsToPass());
		evalStats.setCorrectAnswerCount(marks);
		evalStats.setQuestAttemptCount(questAttemptCount);
		evalStats.setEvalQuestCount(evaluationForReference.getQuestionsInEval());

		result.put("evalStats", evalStats);
		result.put("report", questionSet);
		return result;
		//return evaluationForReference.getQuestionsToPass() <= marks;
	}

	private boolean isCorrectFillGapsAnswer(QuestionVO userQuestionVO, QuestionVO questionVOInDB) {

		boolean corretAnswer = false;
		List<String> answers = userQuestionVO.getUserAnswerList();
		if(answers.isEmpty()) {
			return false;
		}
		List<AnswerVO> answersInDB = questionVOInDB.getAnswers();
		Iterator<AnswerVO> iterator = answersInDB.iterator();
		while (iterator.hasNext()) {
			AnswerVO answerVO = iterator.next();
			if (answerVO.getCorrectOption()) {
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


	private boolean isCorrectAnswer(QuestionVO userQuestionVO, QuestionVO questionVOInDB) {

		boolean correctAnswer = true;
		List<String> answers = userQuestionVO.getUserAnswerList();
		List<AnswerVO> answersInDB = questionVOInDB.getAnswers();
		Iterator<AnswerVO> iterator = answersInDB.iterator();
		while (iterator.hasNext()) {
			AnswerVO answerVO = iterator.next();
			if(answerVO.getCorrectOption()) {
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
		List<EvalStatsVO> evalStatsVOList = EvalStatsAdapter.toVO(evalStats);
		return evalStatsVOList;
	}

	@Override
	public boolean checkAnswer(QuestionVO questionVO) {

		boolean correctAnswer = false;
		Optional<Question> questionOpt = questionRepository.findById(questionVO.getId());
		if(questionOpt.isPresent()) {
			Question question = questionOpt.get();
			QuestionVO questionVOInDB = QuestionAdapter.toVO(question);
			if(question.getQuestionType().equalsIgnoreCase(ShikshaConstants.Q_TYPE_FILL_WORD)) {
				if (isCorrectFillGapsAnswer(questionVO, questionVOInDB)) {
					correctAnswer = true;
					System.out.printf("Q %d. correctAnswer: TRUE, %s %n", questionVO.getId(), questionVO.getUserAnswerList());
				} else {
					System.out.printf("Q %d. wrongAnswer: TRUE, %s %n", questionVO.getId(), questionVO.getUserAnswerList());
				}
			} else {
				if (isCorrectAnswer(questionVO, questionVOInDB)) {
					correctAnswer = true;
					System.out.printf("Q %d. correctAnswer: TRUE, %s %n", questionVO.getId(), questionVO.getUserAnswerList());
				} else {
					System.out.printf("Q %d. wrongAnswer: TRUE, %s %n", questionVO.getId(), questionVO.getUserAnswerList());
				}
			}
		}
		return correctAnswer;
	}
}
