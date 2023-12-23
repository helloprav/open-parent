/**
 * 
 */
package org.openframework.gurukul.pariksha.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;


/**
 * @author emiprav
 *
 */
@RedisHash("ExamStateVal")
public class ExamState implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Indexed
	private String id;
	private Long eid;
	private Long uid;
	private boolean testRun;
	private Date startDate;
	private int qSize;
	private List<Long> qIds = new ArrayList<>();

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

	public String getId() {
		return String.format("%s_%s", eid, uid);
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getEid() {
		return eid;
	}

	public void setEid(Long eid) {
		this.eid = eid;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
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

}
