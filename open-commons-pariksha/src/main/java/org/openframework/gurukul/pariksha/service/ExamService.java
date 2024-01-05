package org.openframework.gurukul.pariksha.service;

import java.util.List;
import java.util.Map;

import org.openframework.commons.rest.vo.UserVO;
import org.openframework.gurukul.pariksha.entity.EvalStats;
import org.openframework.gurukul.pariksha.vo.EvalStatsVO;
import org.openframework.gurukul.pariksha.vo.EvaluationVO;
import org.openframework.gurukul.pariksha.vo.ExamState;
import org.openframework.gurukul.pariksha.vo.QuestionVO;

import jakarta.servlet.http.HttpServletResponse;

public interface ExamService {

	EvaluationVO startExam(Long evalId, boolean testRun, Long userId, HttpServletResponse response);

	List<QuestionVO> evaluateAllQuestions(Long evalId, Long userId, String skipQuestion);

	Map<String, Object> evaluateQuestion(int nextQuestionSeq, Long evalId, Long uid,
			QuestionVO questionVOFromUserResponse, String examStateCookie, String userQuestionMapCookie,
			String skipQuestion, HttpServletResponse response);

	Map<String, Object> evaluationCompleted(String examStateCookie, UserVO loggedInUser, String userQuestionMapCookie);

	Map<String, Object> evaluationAllCompleted(String examStateCookie, EvaluationVO evaluationVO,
			UserVO loggedInUser);

	List<QuestionVO> findQuestionsByEvalId(Long evalId);

	void saveEvalStats(EvalStats result);

	List<EvalStatsVO> findEvaluationStatsByUserId(Long id);

	boolean checkAnswer(QuestionVO questionVO);

	ExamState prepareExamState(Long evalId, Long userId, List<Long> qIdList, boolean testRun);

}
