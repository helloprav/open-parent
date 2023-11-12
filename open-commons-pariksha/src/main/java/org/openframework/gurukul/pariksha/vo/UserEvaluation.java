/**
 * 
 */
package org.openframework.gurukul.pariksha.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;


/**
 * @author emiprav
 *
 */
public class UserEvaluation {

	private Long evaluationID;
	private Integer trainingStatID;
	private Map<Long, QuestionVO> userQuestionMap = new HashMap<>(0);
	private Date evalStartDate;
	private Integer attemptedQuestions;
	private Integer correctAttempts;
	private List<Long> questionIds = new ArrayList<>();

	/**
	 * @return the evaluationID
	 */
	public Long getEvaluationID() {
		return evaluationID;
	}

	/**
	 * @param evaluationID the evaluationID to set
	 */
	public void setEvaluationID(Long evaluationID) {
		this.evaluationID = evaluationID;
	}

	/**
	 * @return the userQuestionMap
	 */
	public Map<Long, QuestionVO> getUserQuestionMap() {
		return userQuestionMap;
	}

	/**
	 * @param userQuestionMap the userQuestionMap to set
	 */
	public void setUserQuestionMap(Map<Long, QuestionVO> userQuestionMap) {
		this.userQuestionMap = userQuestionMap;
	}

	/**
	 * @return the evalStartDate
	 */
	public Date getEvalStartDate() {
		return evalStartDate;
	}

	/**
	 * @param evalStartDate the evalStartDate to set
	 */
	public void setEvalStartDate(Date evalStartDate) {
		this.evalStartDate = evalStartDate;
	}


	/**
	 * Return string representation of the Bean Object
	 * @return String representation of object
	 */
	public String toString() {

		String message = "";
		try {
			message = BeanUtils.describe(this).toString();
		} catch (Exception e) {
		}
		return message;
	}

	/**
	 * @return the trainingStatID
	 */
	public Integer getTrainingStatID() {
		return trainingStatID;
	}

	/**
	 * @param trainingStatID the trainingStatID to set
	 */
	public void setTrainingStatID(Integer trainingStatID) {
		this.trainingStatID = trainingStatID;
	}

	/**
	 * @return the attemptedQuestions
	 */
	public Integer getAttemptedQuestions() {
		return attemptedQuestions;
	}

	/**
	 * @param attemptedQuestions the attemptedQuestions to set
	 */
	public void setAttemptedQuestions(Integer attemptedQuestions) {
		this.attemptedQuestions = attemptedQuestions;
	}

	public Integer getCorrectAttempts() {
		return correctAttempts;
	}

	public void setCorrectAttempts(Integer correctAttempts) {
		this.correctAttempts = correctAttempts;
	}

	public List<Long> getQuestionIds() {
		return questionIds;
	}

	public void setQuestionIds(List<Long> questionIds) {
		this.questionIds = questionIds;
	}

	public void addQuestionId(Long questionId) {
		this.questionIds.add(questionId);
	}
}
