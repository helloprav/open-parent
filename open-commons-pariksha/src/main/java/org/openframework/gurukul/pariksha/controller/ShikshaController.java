package org.openframework.gurukul.pariksha.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shiksha")
public class ShikshaController {

	@GetMapping({"", "/"})
	public String findEvaluations(Model model) {

		return "redirect:/shiksha/exams";
	}

}
