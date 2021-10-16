package org.openframework.commons.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import org.openframework.commons.cache.props.CommonsCacheProperties;
import org.openframework.commons.cache.props.CommonsCacheProperties.CacheConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * Hello world!
 *
 */
@Configuration
@EnableCaching
@EnableConfigurationProperties({ CommonsCacheProperties.class })
@ConditionalOnProperty(name = "org.openframework.commons.cache.enabled", havingValue = "true", matchIfMissing = true)
public class GlobalCacheApp {

	public static final int initialCapacity = 100;
	public static final int maximumSize = 500;
	private static final int expireAfterAccess = 60;
	public static final String simpleCacheManager = "simpleCacheManager";
	public static final String caffeineCacheManager = "caffeineCacheManager";
	private static Set<String> cacheManagerNameList = new TreeSet<String>();
	private static Map<String, Set<String>> cacheManagerNameMap = new HashMap<String, Set<String>>();
	private static boolean caffeineReady = false;

	@Autowired
	private CommonsCacheProperties commonsCacheProperties;

	static {
		cacheManagerNameList.add(simpleCacheManager);
		cacheManagerNameList.add(caffeineCacheManager);
	}

	public static void addCacheName(String cacheName) {
		addCacheName(simpleCacheManager, cacheName);
	}

	public static void addCacheName(String cacheManagerName, String cacheName) {

		if (!cacheManagerNameList.contains(cacheManagerName)) {
			throw new IllegalArgumentException("Unsupported cacheManager: " + cacheManagerName);
		}
		Set<String> cacheNames = cacheManagerNameMap.get(cacheManagerName);
		if (null == cacheNames) {
			cacheNames = new TreeSet<String>();
		}
		cacheNames.add(cacheName);
		cacheManagerNameMap.put(cacheManagerName, cacheNames);
	}

	@Lazy
	@Primary
	@Bean(name = simpleCacheManager)
	public CacheManager simpleCacheManager() {

		Set<String> simpleCacheNames = getSimpleCacheNames();
		SimpleCacheManager cacheManager = new SimpleCacheManager();
		List<Cache> caches = new ArrayList<Cache>();
		for (String simpleCacheName : simpleCacheNames) {
			caches.add(new ConcurrentMapCache(simpleCacheName));
		}
		cacheManager.setCaches(caches);
		return cacheManager;
	}

	private Set<String> getSimpleCacheNames() {
		Set<String> simpleCacheNamesFromCode = getSimpleCacheNamesFromCode();
		Set<String> simpleCacheNamesFromProp = getSimpleCacheNamesFromProperties();
		simpleCacheNamesFromCode.addAll(simpleCacheNamesFromProp);
		return simpleCacheNamesFromCode;
	}

	private Set<String> getSimpleCacheNamesFromCode() {
		Set<String> simpleCacheNames = cacheManagerNameMap.get(simpleCacheManager);
		if (null == simpleCacheNames) {
			simpleCacheNames = new TreeSet<String>();
		}
		return simpleCacheNames;
	}

	private Set<String> getSimpleCacheNamesFromProperties() {
		Set<String> simpleCacheNames = commonsCacheProperties.getCacheNames(simpleCacheManager);
		if (null == simpleCacheNames) {
			simpleCacheNames = new TreeSet<String>();
		}
		return simpleCacheNames;
	}

	@Lazy
	@Bean(caffeineCacheManager)
	public CacheManager caffeineCacheManager() {

		Set<String> caffeineCacheNames = getCaffeineCacheNames();
		CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
		caffeineCacheManager.setCacheNames(caffeineCacheNames);
		caffeineCacheManager.setCaffeine(caffeineCacheBuilder());
		return caffeineCacheManager;
	}

	private Set<String> getCaffeineCacheNames() {
		Set<String> caffeineCacheNamesFromCode = getCaffeineCacheNamesFromCode();
		Set<String> caffeineCacheNamesFromProp = getCaffeineCacheNamesFromProperties();
		caffeineCacheNamesFromCode.addAll(caffeineCacheNamesFromProp);
		return caffeineCacheNamesFromCode;
	}

	private Set<String> getCaffeineCacheNamesFromCode() {
		Set<String> caffeineCacheNames = cacheManagerNameMap.get(caffeineCacheManager);
		if (null == caffeineCacheNames) {
			throw new IllegalArgumentException("caffeineCacheNames are not initialized for caffeineCacheManager");
		}
		return caffeineCacheNames;
	}

	private Set<String> getCaffeineCacheNamesFromProperties() {
		Set<String> caffeineCacheNames = commonsCacheProperties.getCacheNames(caffeineCacheManager);
		if (null == caffeineCacheNames && caffeineReady) {
			//throw new IllegalArgumentException("caffeineCacheNames are not initialized for CommonsCacheProperties");
			caffeineCacheNames = new TreeSet<String>();
		} else {
			caffeineReady = true;
		}
		return caffeineCacheNames;
	}

	Caffeine<Object, Object> caffeineCacheBuilder() {
		CacheConfig cacheConfig = commonsCacheProperties.getCacheConfig(caffeineCacheManager);
		int initialCap = initialCapacity;
		int maxSize = maximumSize;
		int expireAftAccTimeinMin = expireAfterAccess;
		if(0 != cacheConfig.getInitialCapacity()) {
			initialCap = cacheConfig.getInitialCapacity();
		}
		if(0 != cacheConfig.getMaximumSize()) {
			maxSize = cacheConfig.getMaximumSize();
		}
		if(0 != cacheConfig.getExpireAfterAccess()) {
			expireAftAccTimeinMin = cacheConfig.getExpireAfterAccess();
		}
		return Caffeine.newBuilder()
				.initialCapacity(initialCap)
				.maximumSize(maxSize)
				.expireAfterAccess(expireAftAccTimeinMin, TimeUnit.MINUTES)
				.weakKeys()
				.recordStats();
	}

//	@Bean
	public CommonsCacheProperties commonsCacheProperties() {
		return new CommonsCacheProperties();
	}

	public static boolean cacheManagerExists(String cacheManager) {
		return cacheManagerNameList.contains(cacheManager);
	}
}
