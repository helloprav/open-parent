package org.openframework.gurukul.pariksha.vo;

import java.util.List;

public class EvaluationVO {

	private Long id;

	private String evalGroup;

	private String evalEnv;

	private String name;

	private String description;

	private Integer questionsInEval;

	private Integer questionsToPass;

	private String questionsPerPage;

	private Boolean showReport;

	private List<QuestionVO> questions = null;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEvalGroup() {
		return evalGroup;
	}

	public void setEvalGroup(String evalGroup) {
		this.evalGroup = evalGroup;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getQuestionsInEval() {
		return questionsInEval;
	}

	public void setQuestionsInEval(Integer questionsInEval) {
		this.questionsInEval = questionsInEval;
	}

	public Integer getQuestionsToPass() {
		return questionsToPass;
	}

	public void setQuestionsToPass(Integer questionsToPass) {
		this.questionsToPass = questionsToPass;
	}

	public List<QuestionVO> getQuestions() {
		return questions;
	}

	public void setQuestions(List<QuestionVO> questions) {
		this.questions = questions;
	}

	public String getEvalEnv() {
		return evalEnv;
	}

	public void setEvalEnv(String evalEnv) {
		this.evalEnv = evalEnv;
	}

	public String getQuestionsPerPage() {
		return questionsPerPage;
	}

	public void setQuestionsPerPage(String questionsPerPage) {
		this.questionsPerPage = questionsPerPage;
	}

	public Boolean getShowReport() {
		return showReport;
	}

	public void setShowReport(Boolean showReport) {
		this.showReport = showReport;
	}
	
}
