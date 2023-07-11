package org.openframework.gurukul.pariksha.service;

import java.util.List;
import java.util.Map;

import org.openframework.commons.rest.vo.UserVO;
import org.openframework.gurukul.pariksha.entity.EvalStats;
import org.openframework.gurukul.pariksha.vo.EvalStatsVO;
import org.openframework.gurukul.pariksha.vo.QuestionVO;
import org.openframework.gurukul.pariksha.vo.UserEvaluation;

public interface ExamService {

	Map<String, Object> findEvaluationByIdWithQuestions(Long evalId);

	Map<String, Object> evaluationCompleted(UserEvaluation userEvaluation, UserVO loggedInUser);

	List<QuestionVO> findQuestionsByEvalId(Long evalId);

	void saveEvalStats(EvalStats result);

	List<EvalStatsVO> findEvaluationStatsByUserId(Long id);

	boolean checkAnswer(QuestionVO questionVO);

}
