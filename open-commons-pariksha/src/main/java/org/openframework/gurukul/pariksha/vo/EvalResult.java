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
public class EvalResult {

	private Long evalId;
	private Date startDate;
	private Date endDate;
	private boolean evalPassed;
	private int totalQuestions;
	private int passMarks;
	private int attemptedQuestions;
	private int correctQuestions;
	private List<Long> qIds = new ArrayList<>();
	private Map<Long, List<String>> userQuestionMap = new HashMap<>(0);

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

	public Long getEvalId() {
		return evalId;
	}

	public void setEvalId(Long evalId) {
		this.evalId = evalId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public boolean isEvalPassed() {
		return evalPassed;
	}

	public void setEvalPassed(boolean evalPassed) {
		this.evalPassed = evalPassed;
	}

	public int getTotalQuestions() {
		return totalQuestions;
	}

	public void setTotalQuestions(int totalQuestions) {
		this.totalQuestions = totalQuestions;
	}

	public int getPassMarks() {
		return passMarks;
	}

	public void setPassMarks(int passMarks) {
		this.passMarks = passMarks;
	}

	public int getAttempedQuestions() {
		return attemptedQuestions;
	}

	public void setAttemptedQuestions(int attempedQuestions) {
		this.attemptedQuestions = attempedQuestions;
	}

	public int getCorrectQuestions() {
		return correctQuestions;
	}

	public void setCorrectQuestions(int correctQuestions) {
		this.correctQuestions = correctQuestions;
	}

	public List<Long> getqIds() {
		return qIds;
	}

	public void setqIds(List<Long> qIds) {
		this.qIds = qIds;
	}

	public Map<Long, List<String>> getUserQuestionMap() {
		return userQuestionMap;
	}

	public void setUserQuestionMap(Map<Long, List<String>> userQuestionMap) {
		this.userQuestionMap = userQuestionMap;
	}

}
