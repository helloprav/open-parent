package org.openframework.commons.masterdata.service.adaptor;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.openframework.commons.masterdata.entity.AdminStatus;
import org.openframework.commons.masterdata.vo.AdminStatusVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class AdminAdapter {

	public List<AdminStatusVO> toVO(List<AdminStatus> adminStatusList) {

		List<AdminStatusVO> adminStatusVOList = new ArrayList<>();
		ListIterator<AdminStatus> listIterator = adminStatusList.listIterator();
		while (listIterator.hasNext()) {
			AdminStatus group = listIterator.next();
			AdminStatusVO adminStatusVO = toVO(group);
			if (null != adminStatusVO) {
				adminStatusVOList.add(adminStatusVO);
			}
		}
		return adminStatusVOList;
	}

	private AdminStatusVO toVO(AdminStatus adminStatus) {

		if (null == adminStatus) {
			return null;
		}
		AdminStatusVO adminStatusVO = new AdminStatusVO();
		BeanUtils.copyProperties(adminStatus, adminStatusVO);
		return adminStatusVO;
	}

}
