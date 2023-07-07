package org.openframework.commons.shiksha.service;

import java.util.List;
import java.util.Map;

import org.openframework.commons.rest.vo.UserVO;
import org.openframework.commons.shiksha.entity.EvalStats;
import org.openframework.commons.shiksha.vo.EvalStatsVO;
import org.openframework.commons.shiksha.vo.QuestionVO;
import org.openframework.commons.shiksha.vo.UserEvaluation;

public interface ExamService {

	Map<String, Object> findEvaluationByIdWithQuestions(Long evalId);

	Map<String, Object> evaluationCompleted(UserEvaluation userEvaluation, UserVO loggedInUser);

	List<QuestionVO> findQuestionsByEvalId(Long evalId);

	void saveEvalStats(EvalStats result);

	List<EvalStatsVO> findEvaluationStatsByUserId(Long id);

	boolean checkAnswer(QuestionVO questionVO);

}
