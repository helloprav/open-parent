package org.openframework.gurukul.pariksha.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openframework.commons.utils.CookieUtils;
import org.openframework.commons.utils.JsonUtils;
import org.openframework.gurukul.pariksha.ParikshaConstants;
import org.openframework.gurukul.pariksha.vo.EvaluationVO;
import org.openframework.gurukul.pariksha.vo.ExamState;
import org.openframework.gurukul.pariksha.vo.QuestionVO;

import jakarta.servlet.http.HttpServletResponse;

public class EvaluationUtils {

	private EvaluationUtils() {
	}

	public static void createExamStateCookie(ExamState examState, HttpServletResponse response) {
		String jsonString = JsonUtils.convertObjectToJsonString(examState);
		response.addCookie(CookieUtils.createCookieEncoded(ParikshaConstants.COOKIE_EXAM_STATE, jsonString));
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
