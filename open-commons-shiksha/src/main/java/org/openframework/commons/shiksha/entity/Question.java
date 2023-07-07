package org.openframework.commons.shiksha.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * Question generated by hbm2java
 */
@Entity
@Table(name = "Shiksha_Question")
public class Question implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4656643215242416161L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "eval_id", nullable = false)
	private Evaluation evaluation;

	@Column(name = "question_group", length = 500)
	private String questionGroup;

	@Column(name = "question_text",length = 1500)
	private String questionText;

	@Column(name = "question_type", length = 15)
	private String questionType;

	@Column(name = "image_name",length = 500)
	private String imageName;

//	@Column(name = "ActiveInd")
//	private Boolean activeInd;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "question", cascade = CascadeType.REMOVE)
	private Set<Answer> answers = new HashSet<Answer>(0);

	public Question() {
	}

	public Question(Long id, Evaluation evaluation) {
		this.id = id;
		this.evaluation = evaluation;
	}

	public Question(Long id, Evaluation evaluation, String text,
			String questionType, Boolean activeInd, String grp ) {
			//List<Answer> answers) {
		this.id = id;
		this.evaluation = evaluation;
		this.questionText = text;
		this.questionType = questionType;
//		this.activeInd = activeInd;
		this.questionGroup = grp;
//		this.answers = answers;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Evaluation getEvaluation() {
		return this.evaluation;
	}

	public void setEvaluation(Evaluation evaluation) {
		this.evaluation = evaluation;
	}

	public String getQuestionText() {
		return this.questionText;
	}

	public void setQuestionText(String text) {
		this.questionText = text;
	}

	public String getQuestionType() {
		return this.questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public String getQuestionGroup() {
		return this.questionGroup;
	}

	public void setQuestionGroup(String grp) {
		this.questionGroup = grp;
	}


	public Set<Answer> getAnswers() {
		return this.answers;
	}

	public void setAnswers(Set<Answer> answers) {
		this.answers = answers;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

}
