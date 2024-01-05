package org.openframework.gurukul.pariksha.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.openframework.commons.domain.exceptions.EntityNotFoundException;
import org.openframework.commons.jpa.entity.User;
import org.openframework.commons.ofds.service.repository.GroupRepository;
import org.openframework.commons.rest.vo.UserVO;
import org.openframework.commons.utils.ExcelUtils;
import org.openframework.gurukul.pariksha.entity.Answer;
import org.openframework.gurukul.pariksha.entity.Evaluation;
import org.openframework.gurukul.pariksha.entity.Question;
import org.openframework.gurukul.pariksha.service.EvaluationService;
import org.openframework.gurukul.pariksha.service.adapter.EvaluationAdapter;
import org.openframework.gurukul.pariksha.service.repository.AnswerRepository;
import org.openframework.gurukul.pariksha.service.repository.EvaluationRepository;
import org.openframework.gurukul.pariksha.service.repository.QuestionRepository;
import org.openframework.gurukul.pariksha.utils.CourseUtils;
import org.openframework.gurukul.pariksha.vo.EvaluationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackForClassName = "Exception", isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public class EvaluationServiceImpl implements EvaluationService {

	private static final String EVAL_ID_NOT_FOUND = "Eval id not found";
	@Autowired
	private EvaluationRepository evaluationRepository;

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;

	@Autowired
	private GroupRepository groupRepository;

	@Override
	public List<Evaluation> findEvaluations() {

		return evaluationRepository.findAllActive();
	}

	@Override
	public List<Evaluation> findEvaluationsByGroups(UserVO loggedInUser) {

		@SuppressWarnings("unchecked")
		List<String> groupNames = (List<String>)loggedInUser.getOtherData().get("groupNames");
		return evaluationRepository.findEvaluationsByGroups(groupNames);
	}

	@Override
	public EvaluationVO findEvaluationVOById(Long evalId) {

		Evaluation evaluation = null;
		Optional<Evaluation> eval = evaluationRepository.findById(evalId);
		if(eval.isPresent()) {
			evaluation = eval.get();
			return EvaluationAdapter.toVO(evaluation);
		} else {
			throw new EntityNotFoundException(EVAL_ID_NOT_FOUND);
		}
	}

	@Override
	public Evaluation findEvaluationById(Long evalId) {

		Evaluation evaluation = null;
		Optional<Evaluation> eval = evaluationRepository.findById(evalId);
		if(eval.isPresent()) {
			evaluation = eval.get();
		} else {
			throw new EntityNotFoundException(EVAL_ID_NOT_FOUND);
		}
		return evaluation;
	}

	@Override
	public Evaluation findEvaluationByIdWithQuestions(Long evalId) {

		Optional<Evaluation> eval = evaluationRepository.findById(evalId);
		if (eval.isPresent()) {
			int qSize = eval.get().getQuestions().size();
			System.out.println(qSize);
		} else {
			throw new EntityNotFoundException(EVAL_ID_NOT_FOUND);
		}
		return eval.get();
	}

	@Override
	public Evaluation saveEvaluation(Long userId, Evaluation evaluation) {

		evaluation.setModifiedBy(new User(userId));
		evaluation.setModifiedDate(new Date());
		Evaluation evaluation1 = evaluationRepository.save(evaluation);

		evaluation.getQuestions().forEach(pl -> pl.setEvaluation(evaluation));
		return evaluation1;
	}

	@Override
	@RolesAllowed({ "EVAL_CREATOR", "ROLE_EDITOR" })
	public Evaluation saveEvaluationFile(Long userId, @Valid Evaluation evaluation, MultipartFile evalFile, MultipartFile mediaFile) {

		Boolean isEvalUpdate = evaluation.getId() != null;
		List<List<String>> excelData;
		User loggedInUser = new User(userId);
		String fileName = evalFile.getOriginalFilename();
		if(StringUtils.isNotBlank(fileName)) {

			// retrieve missing attributes from filename.
			String nameParts[] = fileName.split("_");
			if(StringUtils.isBlank(evaluation.getName())) {
				String evalNameFromFileName = CourseUtils.getEvalNameFromFileName(fileName);
				evaluation.setName(evalNameFromFileName);
			}
			if(StringUtils.isBlank(evaluation.getEvalGroup())) {
				evaluation.setEvalGroup(getEvalGroup("Grade "+nameParts[0]));
			}

			long timeInMillis = System.currentTimeMillis();
			try {
				excelData = ExcelUtils.processExcel(evalFile.getInputStream());
				Set<Question> questionSet = CourseUtils.populateQuestions(excelData, loggedInUser);
				evaluation.setQuestions(questionSet);
				evaluation.setQuestionsInEval(questionSet.size());
				if(null == evaluation.getQuestionsToPass()) {
					double pass = questionSet.size() * 0.6;
					evaluation.setQuestionsToPass((int)pass);
				}
				if(StringUtils.isNotBlank(mediaFile.getOriginalFilename())) {
					CourseUtils.processMediaFile(mediaFile, timeInMillis);
				}
			} catch (IOException e) {
	
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			evaluation.setCreatedBy(loggedInUser);
			evaluation.setCreatedDate(new Date());
			Evaluation savedEval = evaluationRepository.save(evaluation);
			createQuestions(savedEval, isEvalUpdate);
			renameEvalMediaDir(timeInMillis, savedEval.getId());
		}
		return evaluation;
	}

	private void renameEvalMediaDir(long timeInMillis, long evalId) {

		String appHome = System.getProperty("APP_HOME");
		String mediaDir = appHome.concat("/pariksha/media/");
		System.out.println("mediaDir: "+mediaDir);
		String tempDirFullName = mediaDir.concat(String.valueOf(timeInMillis));
		File srcDir = new File(tempDirFullName);
		File targetDir = new File(mediaDir.concat(String.valueOf(evalId)));
		srcDir.renameTo(targetDir);
	}

	/**
	 * 
	 * @param evaluation
	 * @param isEvalUpdate if true, soft delete old answers
	 */
	private void createQuestions(Evaluation evaluation, boolean isEvalUpdate) {

		Iterator<Question> questionIterator = evaluation.getQuestions().iterator();
		while (questionIterator.hasNext()) {
			Question question = questionIterator.next();
			question.setEvaluation(evaluation);
			questionRepository.save(question);
			System.out.println("Created Question ID: " + question.getId());

			// If question created successfully, create the answers
			if (question.getId() != 0) {
				if (isEvalUpdate) {
					deactivateExistingAnswers(question);
				}
				createAnswers(question);
			}
		}
	}

	private void createAnswers(Question question) {

		Iterator<Answer> answerIterator = question.getAnswers().iterator();
		while (answerIterator.hasNext()) {
			Answer answer = answerIterator.next();
			answer.setQuestion(question);
			answerRepository.save(answer);
			System.out.println("Created Answer ID: " + answer.getId());
		}
	}

	private void deactivateExistingAnswers(Question question) {

		/*
		 * Query query = getEntityManager().
		 * createQuery("update Answer ans SET ans.activeInd=:activeInd, ans.recordInfo.modifiedBy=:modifiedBy, ans.recordInfo.modifiedDate=:modifiedDate where ans.question.id=:questID"
		 * ); query.setParameter("activeInd", false); query.setParameter("modifiedBy",
		 * question.getRecordInfo().getModifiedBy()); query.setParameter("modifiedDate",
		 * question.getRecordInfo().getModifiedDate()); query.setParameter("questID",
		 * question.getId()); int updated = query.executeUpdate();
		 * logger.debug("Total answers deactivated is "+updated);
		 */
	}

	@Override
	public Evaluation updateEvaluation(Evaluation evaluation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteEvaluationById(Long evalId) {

		evaluationRepository.deleteById(evalId);
	}

	@Override
	public List<String> getEvalGroups() {

		return groupRepository.findGroupNameByGroupNameStartsWith("Grade %");
	}

	@Override
	public String getEvalGroup(String evalGroupName) {

		return groupRepository.findGroupNameByGroupName(evalGroupName);
	}

	@Override
	public Evaluation updateStatusById(Evaluation eval) {

		Integer count = evaluationRepository.updateStatusById(eval.getId(), eval.getIsValid(), eval.getModifiedBy().getId(), eval.getModifiedDate());
		if(count==0) {
			throw new EntityNotFoundException(String.format("Requested entity %s not found", eval.getId()));
		}
		return eval;
	}

	@Override
	public boolean isValidEvalCode(String evalCode) {

		return null != getEvalGroup("Grade "+evalCode);
	}

}
