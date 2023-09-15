package org.openframework.gurukul.pariksha.entity;

// Generated Aug 15, 2012 6:42:42 PM by Hibernate Tools 3.4.0.CR1

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;

import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


/**
 * Answer generated by hbm2java
 */
@Entity
@Table(name = "Pariksha_Answer")
public class Answer implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5354943540370106282L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Question_Id", nullable = false)
	private Question question;

	@Column(name = "AnswerText")
	private String answerText;

	@Column(name = "CorrectOption")
	private Boolean correctOption;
	//private Boolean activeInd;

	public Answer() {
	}

	public Answer(Question question) {
		this.question = question;
	}

	public Answer(Question question, String answerText, Boolean correctOption ) {
		this.question = question;
		this.answerText = answerText;
		this.correctOption = correctOption;
		//this.activeInd = activeInd;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Question getQuestion() {
		return this.question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public String getAnswerText() {
		return this.answerText;
	}

	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}

	public Boolean getCorrectOption() {
		return this.correctOption;
	}

	public void setCorrectOption(Boolean correctOption) {
		this.correctOption = correctOption;
	}

}
