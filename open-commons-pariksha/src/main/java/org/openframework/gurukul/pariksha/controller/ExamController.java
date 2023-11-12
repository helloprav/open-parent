package org.openframework.gurukul.pariksha.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.openframework.commons.rest.vo.UserVO;
import org.openframework.commons.utils.CookieUtils;
import org.openframework.commons.utils.JsonUtils;
import org.openframework.gurukul.pariksha.ParikshaConstants;
import org.openframework.gurukul.pariksha.entity.Evaluation;
import org.openframework.gurukul.pariksha.service.EvaluationService;
import org.openframework.gurukul.pariksha.service.ExamService;
import org.openframework.gurukul.pariksha.utils.CourseUtils;
import org.openframework.gurukul.pariksha.utils.EvaluationUtils;
import org.openframework.gurukul.pariksha.utils.QuestionVOUtils;
import org.openframework.gurukul.pariksha.vo.EvaluationVO;
import org.openframework.gurukul.pariksha.vo.ExamState;
import org.openframework.gurukul.pariksha.vo.QuestionVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/pariksha/exams")
public class ExamController {

	@Inject
	private HttpServletRequest request;

	@Inject
	private HttpServletResponse response;

	@Autowired
	private EvaluationService evaluationService;

	@Autowired
	private ExamService examService;

	/** Logger that is available to subclasses */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@GetMapping()
	public String init() {
		return "redirect:/pariksha/exams/list";
	}

	@GetMapping({ "/", "/list" })
	public String findEvaluations(Model model, UserVO loggedInUser) {
		List<Evaluation> evaluations = evaluationService.findEvaluationsByGroups(loggedInUser);
		model.addAttribute("items", evaluations);
		return "pariksha/exams/exam-list";
	}

	@GetMapping("/start-eval")
	public String evaluationStart(Model model, @RequestParam Long evalId, @RequestParam (required = false) boolean testRun) {

		EvaluationVO evaluationVO = evaluationService.findEvaluationVOById(evalId);

		createExamStateCookie(evalId, EvaluationUtils.getQIdsFromEvaluation(evaluationVO), testRun);

		model.addAttribute("eval", evaluationVO);
		model.addAttribute("evalId", evalId);
		model.addAttribute("testRun", testRun);

		return "/pariksha/exams/exam-start";
	}

	private void createExamStateCookie(Long evalId, List<Long> qIdList, boolean testRun) {

		logger.debug("Entered createUserEvalCookie(eval:{})", evalId);
		logger.debug("List of qId for this test: {}", qIdList);
		ExamState examState = new ExamState();
		examState.setId(evalId);
		examState.setTestRun(testRun);
		examState.setStartDate(new Date());
		examState.setqSize(qIdList.size());
		examState.setqIds(qIdList);

		String jsonString = JsonUtils.convertObjectToJsonString(examState);
		response.addCookie(CookieUtils.createCookieEncoded(ParikshaConstants.COOKIE_EXAM_STATE, jsonString));
	}

	@PostMapping("/question/{nextQuestionSeq}")
	public String evaluateQuestion(@PathVariable int nextQuestionSeq, @ModelAttribute("question") QuestionVO questionVOFromUserResponse,
			Model model, @CookieValue(name = ParikshaConstants.COOKIE_EXAM_STATE, defaultValue = "") String examStateCookie,
			@RequestParam(required = false) Long evalId, @RequestParam(required = false) Integer questionID,
			@RequestParam(required = false) String skipQuestion,
			@RequestParam(required = false) String[] userAnswerList) {

		logger.debug("Entered evaluateQuestion(nextQuestionSeq:{})", nextQuestionSeq);
		ExamState examState = convert2ExamState(questionVOFromUserResponse, examStateCookie, skipQuestion);

		int questionSize = 0;
		List<QuestionVO> questionsVOList = null;

		questionsVOList = examService.findQuestionsByEvalId(evalId);

		questionSize = questionsVOList.size();

		model.addAttribute("evalId", evalId);
		model.addAttribute("nextQuestionSeq", nextQuestionSeq);
		model.addAttribute("questionSize", questionSize);
		model.addAttribute("testRun", examState.isTestRun());

		if (nextQuestionSeq > questionSize) {
			model.addAttribute("evalCompleted", "evalCompleted");
		} else {
			Long questionId = examState.getqIds().get(nextQuestionSeq - 1);
			QuestionVO question = QuestionVOUtils.getQuestionsVOFromList(questionsVOList, questionId);

			// Populate User submitted answer if any, & set in userQuestion
			 List<String> userSubmittedAnswers = examState.getUserQuestionMap().get(questionId);
			model.addAttribute("userQuestion", userSubmittedAnswers);

			if (null != userSubmittedAnswers && !userSubmittedAnswers.isEmpty()) {
				question.setUserAnswerList(userSubmittedAnswers);
			}
			model.addAttribute("question", question);
			model.addAttribute("answers", question.getAnswers());
		}
		model.addAttribute("userQuestion", questionVOFromUserResponse);

		return "/pariksha/exams/evaluateQuestionAjax";
	}

	private ExamState convert2ExamState(QuestionVO questionVOFromUserResponse, String examStateCookie, String skipQuestion) {

		Long questionID = questionVOFromUserResponse.getId();

		// Step 1: Read UerEvaluatio from request cookie
		ExamState examState = CookieUtils.readObjectFromCookie(examStateCookie, ExamState.class);

		if (null == skipQuestion) {
			// Step 2: Update userEvaluation with user's answer/response to the question
			if (questionID != null) {
				examState.getUserQuestionMap().put(questionID, questionVOFromUserResponse.getUserAnswerList());
			}

			// Step 3: Save UserEvaluation into Cookie
			String jsonString = JsonUtils.convertObjectToJsonString(examState);
			response.addCookie(CookieUtils.createCookieEncoded(ParikshaConstants.COOKIE_EXAM_STATE, jsonString));
		}
		return examState;
	}

	@PostMapping("/checkAnswer/{questionSeq}")
	public ResponseEntity<String> checkAnswer(@PathVariable int questionSeq, @ModelAttribute("question") QuestionVO questionVOFromUserResponse,
			Model model, 
			@RequestParam(required = false) Integer questionID,
			@RequestParam (required = false) boolean testRun) {

		boolean result = false;
		if(testRun) {
			result = examService.checkAnswer(questionVOFromUserResponse);
		}

		return new ResponseEntity<>(String.valueOf(result), HttpStatus.OK);
	}

	@PostMapping(value = "/evaluationCompleted")
	public String evaluationCompleted(Model model,
			@CookieValue(name = ParikshaConstants.COOKIE_EXAM_STATE, defaultValue = "") String examStateCookie, 
			UserVO loggedInUser) {

		logger.debug("Entered evaluationCompleted");

		Map<String, Object> result = examService.evaluationCompleted(examStateCookie, loggedInUser);

		CookieUtils.deleteCookiesByName(request, response, List.of(ParikshaConstants.COOKIE_EXAM_STATE));

		model.addAttribute("questionSet", result.get("questionSet"));
		model.addAttribute("evalResult", result.get("evalResult"));

		logger.debug("Exiting evaluationCompleted");
		return "/pariksha/exams/exam-completed";
	}

	@GetMapping(value = "/image/{evalId}/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public void getImage(HttpServletResponse response, @PathVariable String evalId, @PathVariable String imageName)
			throws IOException {

		String mediaDirName = CourseUtils.getParikshaMediaDir();
		String imageFileName = mediaDirName.concat(evalId).concat(File.separator).concat(imageName);
		File imageFile = new File(imageFileName);
		try (InputStream inputStream = new FileInputStream(imageFile)) {
			response.setContentType(MediaType.IMAGE_JPEG_VALUE);
			StreamUtils.copy(inputStream, response.getOutputStream());
		}
	}
}
