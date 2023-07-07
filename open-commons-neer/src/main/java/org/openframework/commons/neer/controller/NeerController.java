package org.openframework.commons.neer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class NeerController {

	@GetMapping({"", "/"})
	public String findEvaluations(Model model) {

		return "redirect:/ofds";
	}

}
