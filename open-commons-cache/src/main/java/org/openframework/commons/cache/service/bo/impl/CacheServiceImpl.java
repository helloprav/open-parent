package org.openframework.commons.cache.service.bo.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.openframework.commons.cache.GlobalCacheApp;
import org.openframework.commons.cache.service.bo.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class CacheServiceImpl implements CacheService {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Inject
	ApplicationContext appContext;

	@Override
	public Map<String, Collection<String>> findCaches() {

		String[] beanNames = appContext.getBeanNamesForType(CacheManager.class);
		//String[] beanNames = appContext.getBeanDefinitionNames();
		Arrays.parallelSort(beanNames);

		Map<String, Collection<String>> cacheNameMap = new HashMap<String, Collection<String>>();
		for (String beanName : beanNames) {
			System.out.println("beanName: "+beanName);
			CacheManager cache = (CacheManager)appContext.getBean(beanName);
			scanCacheManager(cache);
			cacheNameMap.put(beanName, cache.getCacheNames());
		}
		return cacheNameMap;
	}

	private static void scanCacheManager(CacheManager cacheManager) {

		Collection<String> cacheNames = cacheManager.getCacheNames();
		for(String cacheName: cacheNames) {
			System.out.println("			cacheName: "+cacheName);
			Cache cache = cacheManager.getCache(cacheName);
			scanCache(cache);
		}
	}
	private static void scanCache(Cache cache) {

		//cache.getName()
		System.out.println(cache.getName());
		System.out.println("			cache: "+cache);
	}

	@Override
	public Collection<String> findCachesByCacheManager(String cacheManagerName) {

		if(!GlobalCacheApp.cacheManagerExists(cacheManagerName)) {
			throw new IllegalArgumentException(cacheManagerName + " is not a valid cacheManager");
		}

		CacheManager cacheManager = (CacheManager)appContext.getBean(cacheManagerName);

		return cacheManager.getCacheNames();
	}

	@Override
	public void evictCachesForCacheMgr(String cacheManagerName) {

		if(!GlobalCacheApp.cacheManagerExists(cacheManagerName)) {
			throw new IllegalArgumentException(cacheManagerName + " is not a valid cacheManager");
		}

		CacheManager cacheManager = (CacheManager)appContext.getBean(cacheManagerName);

		Collection<String> cacheNames = cacheManager.getCacheNames();
		for (String cacheName: cacheNames) {
			cacheManager.getCache(cacheName).invalidate();
		}
	}

	@Override
	public void evictCacheForCacheMgr(String cacheManagerName, String cacheName) {

		if(!GlobalCacheApp.cacheManagerExists(cacheManagerName)) {
			throw new IllegalArgumentException(cacheManagerName + " is not a valid cacheManager");
		}

		CacheManager cacheManager = (CacheManager)appContext.getBean(cacheManagerName);

		cacheManager.getCache(cacheName).invalidate();
	}

}
