package org.openframework.commons.shiksha.service.adapter;

import org.openframework.commons.shiksha.entity.Evaluation;
import org.openframework.commons.shiksha.vo.EvaluationVO;

public class EvaluationAdapter {

	public static EvaluationVO toVO(Evaluation eval) {

		EvaluationVO evalVO = new EvaluationVO();
		evalVO.setId(eval.getId());
		evalVO.setEvalGroup(eval.getEvalGroup());
		evalVO.setEvalEnv(eval.getEvalEnv());
		evalVO.setName(eval.getName());
		evalVO.setDescription(eval.getDescription());
		evalVO.setQuestionsInEval(eval.getQuestionsInEval());
		evalVO.setQuestionsToAttempt(eval.getQuestionsToAttempt());
		evalVO.setQuestionsToPass(eval.getQuestionsToPass());
		evalVO.setShowReport(eval.getShowReport());
		evalVO.setQuestions(QuestionAdapter.toVOs(eval.getQuestions()));
		return evalVO;
	}

}
