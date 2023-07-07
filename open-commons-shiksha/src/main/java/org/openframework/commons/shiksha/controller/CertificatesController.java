package org.openframework.commons.shiksha.controller;

import java.util.List;

import org.openframework.commons.rest.vo.UserVO;
import org.openframework.commons.shiksha.service.ExamService;
import org.openframework.commons.shiksha.vo.EvalStatsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shiksha/certificates")
public class CertificatesController {

	@Autowired
	private ExamService examService;

	@GetMapping({"", "/"})
	public String findEvaluations(Model model, UserVO loggedInUser) {
		List<EvalStatsVO> evaluationStats = examService.findEvaluationStatsByUserId(loggedInUser.getId());
		model.addAttribute("items", evaluationStats);
		return "shiksha/certificates/cert-list";
	}

}