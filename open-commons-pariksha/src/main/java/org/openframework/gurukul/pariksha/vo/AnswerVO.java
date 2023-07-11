package org.openframework.gurukul.pariksha.vo;

public class AnswerVO {

	private Long id;

	private String answerText;

	private Boolean correctOption;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAnswerText() {
		return answerText;
	}

	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}

	public Boolean getCorrectOption() {
		return correctOption;
	}

	public void setCorrectOption(Boolean correctOption) {
		this.correctOption = correctOption;
	}

}
