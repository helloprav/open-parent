package org.openframework.gurukul.pariksha.service.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openframework.gurukul.pariksha.entity.Question;
import org.openframework.gurukul.pariksha.vo.QuestionVO;

public class QuestionAdapter {

	public static List<QuestionVO> toVOs(Set<Question> questions) {

		List<QuestionVO> questionVOs = new ArrayList<>();
		Iterator<Question> iterator = questions.iterator();
		while (iterator.hasNext()) {
			Question question = (Question) iterator.next();
			QuestionVO questionVO = toVO(question);
			questionVOs.add(questionVO);
		}
		return questionVOs;
	}

	public static QuestionVO toVO(Question question) {

		QuestionVO questionVO = new QuestionVO();
		questionVO.setId(question.getId());
		questionVO.setQuestionGroup(question.getQuestionGroup());
		questionVO.setQuestionText(question.getQuestionText());
		questionVO.setQuestionType(question.getQuestionType());
		questionVO.setImageName(question.getImageName());
		questionVO.setAnswers(AnswerAdapter.toVOs(question.getAnswers()));
		return questionVO;
	}

}
