package org.openframework.gurukul.pariksha.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openframework.commons.rest.vo.UserVO;
import org.openframework.commons.utils.CookieUtils;
import org.openframework.gurukul.pariksha.entity.EvalStats;
import org.openframework.gurukul.pariksha.entity.Evaluation;
import org.openframework.gurukul.pariksha.service.EvaluationService;
import org.openframework.gurukul.pariksha.service.ExamService;
import org.openframework.gurukul.pariksha.utils.CourseUtils;
import org.openframework.gurukul.pariksha.vo.EvaluationVO;
import org.openframework.gurukul.pariksha.vo.QuestionVO;
import org.openframework.gurukul.pariksha.vo.UserEvaluation;
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

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/shiksha/exams")
public class ExamController {

	@Inject
	private HttpServletRequest request;

	@Inject
	private HttpServletResponse response;

	@Autowired
	private EvaluationService evaluationService;

	@Autowired
	private ExamService examService;

	@GetMapping()
	public String init() {
		return "redirect:/shiksha/exams/list";
	}

	@GetMapping({ "/", "/list" })
	public String findEvaluations(Model model, UserVO loggedInUser) {
		List<Evaluation> evaluations = evaluationService.findEvaluationsByGroups(loggedInUser);
		model.addAttribute("items", evaluations);
		return "shiksha/exams/exam-list";
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/start-eval")
	public String evaluationStart(Model model, @RequestParam Long evalId, @RequestParam (required = false) Boolean testRun) {

		Map<String, Object> evalContainer = examService.findEvaluationByIdWithQuestions(evalId);

		EvaluationVO evaluationForReference = (EvaluationVO) evalContainer.get("evaluationForReference");
		createUserEvalCookie(evalId, (List<Long>) evalContainer.get("questionIds"));

		model.addAttribute("eval", evaluationForReference);
		model.addAttribute("evalId", evalId);
		model.addAttribute("testRun", testRun);

		return "/shiksha/exams/exam-start";
	}

	private void createUserEvalCookie(Long evalId, List<Long> list) {

		System.out.println("List of qId for this test: " + list);
		UserEvaluation userEvaluation = new UserEvaluation();
		userEvaluation.setEvaluationID(evalId);
		userEvaluation.setAttemptedQuestions(list.size());
		userEvaluation.setEvalStartDate(new Date());
		userEvaluation.setQuestionIds(list);

		String jsonString = convertObjectToJsonString(userEvaluation);
		response.addCookie(CookieUtils.createCookieEncoded("userEval", jsonString));
	}

	private String convertObjectToJsonString(UserEvaluation userEvaluation) {

		String jsonString = null;
		ObjectMapper obj = new ObjectMapper();
		obj.setSerializationInclusion(Include.NON_NULL);
		try {
			// Converting the Java object into a JSON string
			jsonString = obj.writeValueAsString(userEvaluation);
			System.out.println(jsonString);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return jsonString;
	}

	@PostMapping("/question/{nextQuestionSeq}")
	public String evaluateQuestion(@PathVariable int nextQuestionSeq, @ModelAttribute("question") QuestionVO questionVO,
			Model model, @CookieValue(name = "userEval", defaultValue = "") String userEvalCookie,
			@RequestParam(required = false) Long evalId, @RequestParam(required = false) Integer questionID,
			@RequestParam(required = false) String skipQuestion,
			@RequestParam(required = false) String[] userAnswerList, 
			@RequestParam (required = false) Boolean testRun) {

		UserEvaluation userEvaluation = saveUserResponseInSession(questionVO, userEvalCookie, skipQuestion);

		int questionSize = 0;
		List<QuestionVO> questionsVOList = null;

		questionsVOList = examService.findQuestionsByEvalId(evalId);

		questionSize = questionsVOList.size();

		model.addAttribute("evalId", evalId);
		model.addAttribute("nextQuestionSeq", nextQuestionSeq);
		model.addAttribute("questionSize", questionSize);
		model.addAttribute("testRun", testRun);

		if (nextQuestionSeq > questionSize) {
			System.out.println("Completed. Show Complete Page.");
			// nextQuestionSeq = 1;
			model.addAttribute("evalCompleted", "evalCompleted");
		} else {
			Long questionId = userEvaluation.getQuestionIds().get(nextQuestionSeq - 1);
			QuestionVO question = getQuestionsVOFromList(questionsVOList, questionId);

			// Populate User submitted answer if any, & set in userQuestion
			QuestionVO userSubmittedQuestion = userEvaluation.getUserQuestionMap().get(questionId);
			model.addAttribute("userQuestion", userSubmittedQuestion);

			if (null != userSubmittedQuestion && null != userSubmittedQuestion.getUserAnswerList()) {
				question.setUserAnswerList(userSubmittedQuestion.getUserAnswerList());
			}
			model.addAttribute("question", question);
			model.addAttribute("answers", question.getAnswers());
		}
		model.addAttribute("userQuestion", questionVO);

		// return "evaluationStartAjaxTiles";
		return "/shiksha/exams/evaluateQuestionAjax";
	}

	@PostMapping("/checkAnswer/{questionSeq}")
	public ResponseEntity<String> checkAnswer(@PathVariable int questionSeq, @ModelAttribute("question") QuestionVO questionVO,
			Model model, 
			@RequestParam(required = false) Integer questionID,
			@RequestParam (required = false) Boolean testRun) throws IOException {

		boolean result = false;
		if(true == testRun) {
			result = examService.checkAnswer(questionVO);
		}
		System.out.println("Result: "+result);

		return new ResponseEntity<>(String.valueOf(result), HttpStatus.OK);
	}

	private QuestionVO getQuestionsVOFromList(List<QuestionVO> questionsVOList, long questionId) {

		QuestionVO questionVOToReturn = null;
		Iterator<QuestionVO> iterator = questionsVOList.iterator();
		while (iterator.hasNext()) {
			QuestionVO questionVO = (QuestionVO) iterator.next();
			if (questionVO.getId() == questionId) {
				questionVOToReturn = questionVO;
				break;
			}
		}
		return questionVOToReturn;
	}

	private UserEvaluation saveUserResponseInSession(QuestionVO questionVO, String userEvalCookie,
			String skipQuestion) {

		List<String> userAnswerList = questionVO.getUserAnswerList();
		Iterator<String> iterator = userAnswerList.iterator();
		while (iterator.hasNext()) {
			String userAnswer = iterator.next();
			System.out.println("userAnswer: " + userAnswer);
		}

		Long questionID = questionVO.getId();

		// Step 1: Read UerEvaluatio from request cookie
		UserEvaluation userEvaluation = readObjectFromCookie(userEvalCookie);

		if (null == skipQuestion) {
			// Step 2: Update userEvaluation with user's answer/response to the question
			if (questionID != null) {
				userEvaluation.getUserQuestionMap().put(questionID, questionVO);
			}

			// Step 3: TODO: Save UserEvaluation into Cookie
			// WebUtils.saveUserEvaluationInSession(session, userEvaluation);
			String jsonString = convertObjectToJsonString(userEvaluation);
			response.addCookie(CookieUtils.createCookieEncoded("userEval", jsonString));
		}
		return userEvaluation;
	}

	private UserEvaluation readObjectFromCookie(String cookieStr) {

		UserEvaluation userEvaluation;
		try {
			cookieStr = URLDecoder.decode(cookieStr, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw new RuntimeException(e1);
		}

		ObjectMapper Obj = new ObjectMapper();
		try {
			// Converting the Java object into a JSON string
			userEvaluation = Obj.readValue(cookieStr, UserEvaluation.class);
			// Displaying Java object into a JSON string
			System.out.println(userEvaluation);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return userEvaluation;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = "/evaluationCompleted")
	public String evaluationCompleted(Model model,
			@CookieValue(name = "userEval", defaultValue = "") String userEvalCookie, UserVO loggedInUser,
			@RequestParam (required = false) Boolean testRun) {

		System.out.println("Evaluation Completed Invoked");

		UserEvaluation userEvaluation = readObjectFromCookie(userEvalCookie);
		Map<String, Object> result = examService.evaluationCompleted(userEvaluation, loggedInUser);
		EvalStats evalStats = (EvalStats)result.get("evalStats");
		List<QuestionVO> report = (List<QuestionVO>) result.get("report");

		System.out.println(userEvaluation.getCorrectAttempts());
		if(null == testRun || true != testRun) {
			// the the evaluation stats in db

			examService.saveEvalStats(evalStats);
			CookieUtils.deleteCookiesByName(request, response, List.of("userEval", "questionSet"));
		}
		model.addAttribute("evalPassed", evalStats.getEvaluationStatPassed());
		model.addAttribute("userEvaluation", userEvaluation);
		model.addAttribute("report", report);
		return "/shiksha/exams/exam-completed";
	}

	@GetMapping(value = "/image/{evalId}/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public void getImage(HttpServletResponse response, @PathVariable String evalId, @PathVariable String imageName)
			throws IOException {

		String mediaDirName = CourseUtils.getShikshaMediaDir();
		String imageFileName = mediaDirName.concat(evalId).concat(File.separator).concat(imageName);
		File imageFile = new File(imageFileName);
		InputStream inputStream = new FileInputStream(imageFile);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(inputStream, response.getOutputStream());
	}
}
