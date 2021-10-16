package org.openframework.commons.masterdata.vo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("masterdata") // prefix masterData, find masterData.* values
public class MasterData {

	private List<QueryData> appInstallQueryList = new ArrayList<>();

	private List<QueryData> appStartQueryList = new ArrayList<>();

	public List<QueryData> getAppInstallQueryList() {
		return appInstallQueryList;
	}

	public void setAppInstallQueryList(List<QueryData> appInstallQueryList) {
		this.appInstallQueryList = appInstallQueryList;
	}

	public List<QueryData> getAppStartQueryList() {
		return appStartQueryList;
	}

	public void setAppStartQueryList(List<QueryData> appStartQueryList) {
		this.appStartQueryList = appStartQueryList;
	}

	public static class QueryData {

		private String name;
		private Boolean condition;
		private String read;
		private List<String> sqls = new ArrayList<>();

		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Boolean getCondition() {
			return condition;
		}
		public void setCondition(Boolean condition) {
			this.condition = condition;
		}
		public String getRead() {
			return read;
		}
		public void setRead(String read) {
			this.read = read;
		}
		public List<String> getSqls() {
			return sqls;
		}
		public void setSqls(List<String> sqls) {
			this.sqls = sqls;
		}

		@Override
		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append("QueryData [name: ");
			sb.append(name).append(", condition: ");
			sb.append(condition).append(", sqlSize: ");
			sb.append(sqls.size()).append("]");
			return sb.toString();
		}
	}

}
