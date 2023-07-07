package org.openframework.commons.shiksha.service.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openframework.commons.shiksha.entity.EvalStats;
import org.openframework.commons.shiksha.vo.EvalStatsVO;

public class EvalStatsAdapter {

	public static List<EvalStatsVO> toVO(List<EvalStats> evalStatsList) {

		List<EvalStatsVO> evalStatsVOList = new ArrayList<>();
		Iterator<EvalStats> iterator = evalStatsList.listIterator();
		while (iterator.hasNext()) {
			EvalStats evalStats = iterator.next();
			EvalStatsVO evalStatsVO = toVO(evalStats);
			evalStatsVOList.add(evalStatsVO);
		}
		return evalStatsVOList;
	}

	private static EvalStatsVO toVO(EvalStats evalStats) {

		EvalStatsVO evalStatsVO = new EvalStatsVO();
		evalStatsVO.setId(evalStats.getId());
		evalStatsVO.setEndDateTime(evalStats.getEvalEndDateTime());
		evalStatsVO.setEvalGroup(evalStats.getEvaluation().getEvalGroup());
		evalStatsVO.setEvalId(evalStats.getEvaluation().getId());
		evalStatsVO.setEvalName(evalStats.getEvaluation().getName());
		evalStatsVO.setUserId(evalStats.getUser().getId());
		evalStatsVO.setUserFullName(evalStats.getUser().getFullName());
		evalStatsVO.setPassed(evalStats.getEvaluationStatPassed());
		evalStatsVO.setResult(evalStats.getEvaluationStatResult());
		return evalStatsVO;
	}

}
