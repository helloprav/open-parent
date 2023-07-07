package org.openframework.commons.shiksha.vo;

import java.util.ArrayList;
import java.util.List;

public class QuestionVO {

	private Long id;

	private String questionGroup;

	private String questionText;

	private String questionType;

	private String imageName;

	private Boolean isOk;

	private List<AnswerVO> answers = new ArrayList<AnswerVO>(0);

	private List<String> userAnswerList = new ArrayList<String>(0);

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQuestionGroup() {
		return questionGroup;
	}

	public void setQuestionGroup(String questionGroup) {
		this.questionGroup = questionGroup;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public List<AnswerVO> getAnswers() {
		return answers;
	}

	public void setAnswers(List<AnswerVO> answers) {
		this.answers = answers;
	}

	public List<String> getUserAnswerList() {
		return userAnswerList;
	}

	public void setUserAnswerList(List<String> userAnswerList) {
		this.userAnswerList = userAnswerList;
	}

	@Override
	public String toString() {
		return "id: "+this.id.toString();
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public Boolean getIsOk() {
		return isOk;
	}

	public void setIsOk(Boolean isOk) {
		this.isOk = isOk;
	}
}
