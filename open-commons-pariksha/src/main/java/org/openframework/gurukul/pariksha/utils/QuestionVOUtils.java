package org.openframework.gurukul.pariksha.utils;

import java.util.Iterator;
import java.util.List;

import org.openframework.gurukul.pariksha.vo.QuestionVO;

public class QuestionVOUtils {

	private QuestionVOUtils() {
	}

	public static QuestionVO getQuestionsVOFromList(List<QuestionVO> questionsVOList, long questionId) {

		QuestionVO questionVOToReturn = null;
		Iterator<QuestionVO> iterator = questionsVOList.iterator();
		while (iterator.hasNext()) {
			QuestionVO questionVO = iterator.next();
			if (questionVO.getId() == questionId) {
				questionVOToReturn = questionVO;
				break;
			}
		}
		if(null == questionVOToReturn) {
			questionVOToReturn = new QuestionVO();
		}
		return questionVOToReturn;
	}
}
