package org.openframework.commons.cache.controller;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;

import org.openframework.commons.cache.props.CommonsCacheProperties;
import org.openframework.commons.cache.service.bo.CacheService;
import org.openframework.commons.rest.beans.ResponseBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/caching/api/caches")
@Api(value = "Cache Controller", consumes = "JSON", produces = "JSON")
public class CacheController {

	@Inject
	private CacheService cacheService;

	@Inject
	private CommonsCacheProperties commonsCacheProperties;

	@GetMapping(path = { "", "/" })
	public Map<String, Collection<String>> findCaches() {
		System.out.println(commonsCacheProperties);
		return cacheService.findCaches();
	}

	@GetMapping(path = { "/{cacheManager}" })
	public Collection<String> findCachesForCacheMgr(@PathVariable String cacheManager) {
		System.out.println(commonsCacheProperties);
		return cacheService.findCachesByCacheManager(cacheManager);
	}

	@DeleteMapping(path = { "/{cacheManager}" })
	public ResponseBean<Object> deleteCachesForCacheMgr(@PathVariable String cacheManager) {
		cacheService.evictCachesForCacheMgr(cacheManager);
		return new ResponseBean<>(HttpStatus.OK.value(), String.format("CacheManager [%s] evicted successfully", cacheManager));
	}

	@DeleteMapping(path = { "/{cacheManager}/{cacheName}" })
	public ResponseBean<Object> evictCacheForCacheMgr(@PathVariable String cacheManager, @PathVariable String cacheName) {
		cacheService.evictCacheForCacheMgr(cacheManager, cacheName);
		return new ResponseBean<>(HttpStatus.OK.value(), String.format("CacheManager.cache [%s.%s] evicted successfully", cacheManager, cacheName));
	}

}
