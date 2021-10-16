package org.openframework.commons.cache.service.bo;

import java.util.Collection;
import java.util.Map;

public interface CacheService {

	public Map<String, Collection<String>> findCaches();

	public Collection<String> findCachesByCacheManager(String cacheManager);

	public void evictCachesForCacheMgr(String cacheManager);

	public void evictCacheForCacheMgr(String cacheManager, String cacheName);
}
