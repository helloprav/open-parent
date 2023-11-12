package org.openframework.gurukul.pariksha.service.adapter;

import org.openframework.gurukul.pariksha.entity.Evaluation;
import org.openframework.gurukul.pariksha.vo.EvaluationVO;

public class EvaluationAdapter {

	private EvaluationAdapter () {}

	public static EvaluationVO toVO(Evaluation eval) {

		EvaluationVO evalVO = new EvaluationVO();
		evalVO.setId(eval.getId());
		evalVO.setEvalGroup(eval.getEvalGroup());
		evalVO.setEvalEnv(eval.getEvalEnv());
		evalVO.setName(eval.getName());
		evalVO.setDescription(eval.getDescription());
		evalVO.setQuestionsInEval(eval.getQuestionsInEval());
		evalVO.setQuestionsToPass(eval.getQuestionsToPass());
		evalVO.setShowReport(eval.getShowReport());
		evalVO.setQuestions(QuestionAdapter.toVOs(eval.getQuestions()));
		return evalVO;
	}

}
