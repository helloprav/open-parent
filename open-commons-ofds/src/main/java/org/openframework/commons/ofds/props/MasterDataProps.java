package org.openframework.commons.ofds.props;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("")
public class MasterDataProps implements Props {

	private Map<String, String> map;

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}


	@Override
	public String toString() {

		System.out.println("Map: "+this.map);
		return "MasterDataProps[map=" + this.map + "]";
	}


}
