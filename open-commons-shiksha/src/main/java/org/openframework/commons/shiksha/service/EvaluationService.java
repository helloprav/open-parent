package org.openframework.commons.shiksha.service;

import java.util.List;

import javax.validation.Valid;

import org.openframework.commons.rest.vo.UserVO;
import org.openframework.commons.shiksha.entity.Evaluation;
import org.springframework.web.multipart.MultipartFile;

public interface EvaluationService {

	public List<Evaluation> findEvaluations();

	public List<Evaluation> findEvaluationsByGroups(UserVO loggedInUser);

	public Evaluation findEvaluationById(Long evalId);

	public Evaluation findEvaluationByIdWithQuestions(Long evalId);

	public Evaluation saveEvaluation(Long userId, Evaluation evaluation);

	public Evaluation updateEvaluation(Evaluation evaluation);

	public void deleteEvaluationById(Long evalId);

	public Evaluation saveEvaluationFile(Long userId, @Valid Evaluation evaluation, MultipartFile evalFile, MultipartFile mediaFile);

	public List<String> getEvalGroups();

	public Evaluation updateStatusById(Evaluation eval);
}
