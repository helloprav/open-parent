/**
 * 
 */
package org.openframework.gurukul.pariksha.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

/**
 * @author emiprav
 *
 */
public class UserQuestion {

	int questionID;
	private List<Integer> userAnswerList = new ArrayList<Integer>(0);
	/**
	 * @return the questionID
	 */
	public int getQuestionID() {
		return questionID;
	}
	/**
	 * @param questionID the questionID to set
	 */
	public void setQuestionID(int questionID) {
		this.questionID = questionID;
	}
	/**
	 * @return the userAnswerList
	 */
	public List<Integer> getUserAnswerList() {
		return userAnswerList;
	}
	/**
	 * @param userAnswerList the userAnswerList to set
	 */
	public void setUserAnswerList(List<Integer> userAnswerList) {
		this.userAnswerList = userAnswerList;
	}


	/**
	 * Return string representation of the Bean Object
	 * @return String representation of object
	 */
	public String toString() {

		String message = "";
		try {
			message = BeanUtils.describe(this).toString();
		} catch (Exception e) {
			// Eat Exception Do Nothing
		}
		return message;
	}
}
