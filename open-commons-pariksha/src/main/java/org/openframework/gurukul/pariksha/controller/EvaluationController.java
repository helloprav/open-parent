package org.openframework.gurukul.pariksha.controller;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.openframework.commons.jpa.entity.User;
import org.openframework.commons.rest.vo.UserVO;
import org.openframework.commons.utils.StringUtil;
import org.openframework.gurukul.pariksha.entity.Evaluation;
import org.openframework.gurukul.pariksha.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/pariksha/evaluations")
public class EvaluationController {

	@Autowired
	private EvaluationService evaluationService;

	@GetMapping()
	public String init() {
		return "redirect:/pariksha/evaluations/list";
	}

	@GetMapping({ "/", "/list" })
	public String findEvaluations(Model model) {
		List<Evaluation> evaluations = evaluationService.findEvaluations();
		model.addAttribute("items", evaluations);
		return "pariksha/evaluations/index";
	}

	@GetMapping("/add")
	public String addEvaluation(Model model, Evaluation evaluation, UserVO loggedInUser) {

		evaluation.setIsValid(true);
		model.addAttribute("evalGroups", evaluationService.getEvalGroups());
		return "/pariksha/evaluations/add-evaluation";
	}

	@PostMapping("save")
	public String saveEvaluation(@Valid Evaluation evaluation, BindingResult result, Model model, @RequestParam("evalFile") MultipartFile evalFile, @RequestParam("mediaFile") MultipartFile mediaFile, UserVO loggedInUser) {

		// apply @Valid detected validation
		if (result.hasErrors()) {
			model.addAttribute("evalGroups", evaluationService.getEvalGroups());
			return "/pariksha/evaluations/add-evaluation";
		}

		ObjectError missingEvalFileError = null;
		// apply custom validation
		if(null == evaluation.getId()) {
			// apply validation for create scenario.
			if(StringUtils.isBlank(evalFile.getOriginalFilename())) {
				// file is not uploaded as fileName is missing
				missingEvalFileError = new ObjectError("globalError", "Evaluation File is required.");
				//result.addError(missingEvalFileError);
				//model.addAttribute("evalGroups", evaluationService.getEvalGroups());
				//return "/pariksha/evaluations/add-evaluation";
			} else {
				// apply validation from filename.
				String fileName = evalFile.getOriginalFilename();
				String nameParts[] = fileName.split("_");
				if(StringUtils.isBlank(evaluation.getName())) {
					if(nameParts.length<2 || StringUtils.isBlank(nameParts[2])) {
						missingEvalFileError = new ObjectError("globalError", "Evaluation Name is required.");
					}
				}
				if(StringUtils.isBlank(evaluation.getEvalGroup())) {
					if(!evaluationService.isValidEvalCode(nameParts[0])) {
						missingEvalFileError = new ObjectError("globalError", "Evaluation Group is required.");
						//result.addError(missingEvalFileError);
						//model.addAttribute("evalGroups", evaluationService.getEvalGroups());
						//return "/pariksha/evaluations/add-evaluation";
					}
				}
			}
		} else {
			// apply validation for update scenario.
			// nothing to validate as of now.
		}

		if(null != missingEvalFileError) {
			result.addError(missingEvalFileError);
			model.addAttribute("evalGroups", evaluationService.getEvalGroups());
			return "/pariksha/evaluations/add-evaluation";
		}

		// Save the data
		if(null == evaluation.getId()) {
			evaluationService.saveEvaluationFile(loggedInUser.getId(), evaluation, evalFile, mediaFile);
		} else {
			evaluationService.saveEvaluation(loggedInUser.getId(), evaluation);
		}
		return "redirect:list";
	}

	@GetMapping("edit/{id}")
	public String showUpdateForm(@PathVariable("id") Long id, Model model) {
		Evaluation evaluation = evaluationService.findEvaluationById(id);
		model.addAttribute("evaluation", evaluation);
		model.addAttribute("evalId", id);
		model.addAttribute("evalGroups", evaluationService.getEvalGroups());
		return "/pariksha/evaluations/add-evaluation";
	}

	@GetMapping(path = "disable")
	public String disableStatus(@RequestParam Long id, UserVO loggedInUser, Model model) {

		Evaluation eval = new Evaluation();
		eval.setId(id);
		eval.setIsValid(false);
		eval.setModifiedBy(new User(loggedInUser.getId()));
		eval.setModifiedDate(new Date());
		evaluationService.updateStatusById(eval);
		//return findEvaluations(model);
		return "redirect:list";
	}

	@GetMapping("delete/{id}")
	public String disableEvaluation(@PathVariable("id") long id, Model model) {

		evaluationService.deleteEvaluationById(id);
//		return findEvaluations(model);
		return "redirect:/pariksha/evaluations";
	}
}
