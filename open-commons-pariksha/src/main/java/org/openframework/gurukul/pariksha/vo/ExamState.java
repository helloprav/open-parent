/**
 * 
 */
package org.openframework.gurukul.pariksha.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;


/**
 * @author emiprav
 *
 */
public class ExamState {

	private Long id;
	private boolean testRun;
	private Date startDate;
	private int qSize;
	private List<Long> qIds = new ArrayList<>();
	private Map<Long, List<String>> userQuestionMap = new HashMap<>(0);

	/**
	 * Return string representation of the Bean Object
	 * @return String representation of object
	 */
	public String toString() {

		String message = "";
		try {
			message = BeanUtils.describe(this).toString();
		} catch (Exception e) {
		}
		return message;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isTestRun() {
		return testRun;
	}

	public void setTestRun(boolean testRun) {
		this.testRun = testRun;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public int getqSize() {
		return qSize;
	}

	public void setqSize(int qSize) {
		this.qSize = qSize;
	}

	public List<Long> getqIds() {
		return qIds;
	}

	public void setqIds(List<Long> qIds) {
		this.qIds = qIds;
	}

	public Map<Long, List<String>> getUserQuestionMap() {
		return userQuestionMap;
	}

	public void setUserQuestionMap(Map<Long, List<String>> userQuestionMap) {
		this.userQuestionMap = userQuestionMap;
	}

}
