package org.openframework.gurukul.pariksha.service.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openframework.gurukul.pariksha.entity.Answer;
import org.openframework.gurukul.pariksha.vo.AnswerVO;

public class AnswerAdapter {

	public static List<AnswerVO> toVOs(Set<Answer> answers) {

		List<AnswerVO> answerVOs = new ArrayList<>();

		Iterator<Answer> iterator = answers.iterator();
		while (iterator.hasNext()) {
			Answer answer = iterator.next();
			AnswerVO answerVO = toVO(answer);
			answerVOs.add(answerVO);
		}
		return answerVOs;
	}

	private static AnswerVO toVO(Answer answer) {

		AnswerVO answerVO = new AnswerVO();
		answerVO.setId(answer.getId());
		answerVO.setAnswerText(answer.getAnswerText());
		answerVO.setCorrectOption(answer.getCorrectOption());
		return answerVO;
	}

}
