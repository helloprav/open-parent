package org.openframework.commons.cache.props;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "commons.caching")
public class CommonsCacheProperties {

	private Map<String, CacheConfig> config = new TreeMap<String, CacheConfig>();

	/**
	 * @return the config
	 */
	public Map<String, CacheConfig> getConfig() {
		return config;
	}

	/**
	 * @return the config
	 */
	public CacheConfig getCacheConfig(String key) {
		if(!config.containsKey(key)) {
			throw new IllegalArgumentException(String.format("Illegal cache manager name {}", key));
		}
		return config.get(key);
	}

	/**
	 * @param config the config to set
	 */
	public void setConfig(Map<String, CacheConfig> config) {
		this.config = config;
	}

	/**
	 * @return the cacheNames
	 */
	public Set<String> getCacheNames(String cacheManage) {
		if (null == config.get(cacheManage)) {
			return null;
		}
		return config.get(cacheManage).getCacheNames();
	}

	public static class CacheConfig {

		private Set<String> cacheNames;
		private int initialCapacity;
		private int maximumSize;
		private int expireAfterAccess;
		/**
		 * @return the expireAfterAccess
		 */
		public int getExpireAfterAccess() {
			return expireAfterAccess;
		}

		/**
		 * @param expireAfterAccess the expireAfterAccess to set
		 */
		public void setExpireAfterAccess(int expireAfterAccess) {
			this.expireAfterAccess = expireAfterAccess;
		}

		/**
		 * @return the initialCapacity
		 */
		public int getInitialCapacity() {
			return initialCapacity;
		}

		/**
		 * @param initialCapacity the initialCapacity to set
		 */
		public void setInitialCapacity(int initialCapacity) {
			this.initialCapacity = initialCapacity;
		}

		/**
		 * @return the maximumSize
		 */
		public int getMaximumSize() {
			return maximumSize;
		}

		/**
		 * @param maximumSize the maximumSize to set
		 */
		public void setMaximumSize(int maximumSize) {
			this.maximumSize = maximumSize;
		}

		/**
		 * @return the cacheNames
		 */
		public Set<String> getCacheNames() {
			return cacheNames;
		}

		/**
		 * @param cacheNames the cacheNames to set
		 */
		public void setCacheNames(Set<String> cacheNames) {
			this.cacheNames = cacheNames;
		}

		// getters and setters

	}
}
