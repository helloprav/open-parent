package org.openframework.gurukul.pariksha.service;

import java.util.List;
import java.util.Map;

import org.openframework.commons.rest.vo.UserVO;
import org.openframework.gurukul.pariksha.entity.EvalStats;
import org.openframework.gurukul.pariksha.vo.EvalStatsVO;
import org.openframework.gurukul.pariksha.vo.QuestionVO;

public interface ExamService {

	Map<String, Object> evaluationCompleted(String examStateCookie, UserVO loggedInUser);

	List<QuestionVO> findQuestionsByEvalId(Long evalId);

	void saveEvalStats(EvalStats result);

	List<EvalStatsVO> findEvaluationStatsByUserId(Long id);

	boolean checkAnswer(QuestionVO questionVO);

}
