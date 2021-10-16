How to use Cache Application:
- Add the dependency in pom.xml
- Scan the beans defined in the email project by adding package [org.openframework.commons.cache] or Class (GlobalCacheApp.class) to scan.
	- e.g. Add GlobalCacheApp.class in @SpringBootApplication NeerApplication
- To add a cache name to be used, any of the following methods can be used: 
	1) add following method in any @Component class
		@PostConstruct
		public void initCacheName() {
			GlobalCacheApp.addCacheName(GlobalCacheApp.caffeineCacheManager, GROUP_CACHE);
		}
	2) add following configuration/properties in application's properties/yml file:
			commons:
			  caching:
			    config:
			      simpleCacheManager:
			        cacheNames:
			          - group11
			          - group21
			      caffeineCacheManager:
			        cacheNames:
			          - group12
			          - group42
- To use the cache name in any of the method, 
	1) add following on class annotation
		@CacheConfig(cacheNames = GroupServiceImpl.GROUP_CACHE, cacheManager = GlobalCacheApp.caffeineCacheManager)
	2) add following on method annotation
		@Cacheable(key = "#id")

