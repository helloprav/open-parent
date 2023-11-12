package org.openframework.gurukul.pariksha.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openframework.gurukul.pariksha.vo.EvaluationVO;
import org.openframework.gurukul.pariksha.vo.QuestionVO;

public class EvaluationUtils {

	private EvaluationUtils() {
	}

	public static List<Long> getQIdsFromEvaluation(EvaluationVO evaluationVO) {

		List<Long> questionIds = new ArrayList<>();
		Iterator<QuestionVO> iterator = evaluationVO.getQuestions().iterator();
		while (iterator.hasNext()) {

			QuestionVO quest = iterator.next();
			questionIds.add(quest.getId());
		}
		return questionIds;
	}
}
