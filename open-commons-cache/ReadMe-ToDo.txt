- Display the list of all cacheManagers and their cacheNames on UI and allow user to manage it.
- Limit the number of cache names to may be 50 for individual cacheManager or all cacheManagers
- Limit the initialCapacity and maximumSize for Caffeine cache implementation



==============DONE===================================================================================
- Use @ConfigurationProperties to provide external configuration support for
	- all cache managers: cache names	[https://www.baeldung.com/spring-yaml-inject-map]
	- caffeine:
		- initial capacity
		- maximum size
	- https://reflectoring.io/spring-boot-configuration-properties/
- GlobalCacheApp.addCacheName() is used to add individual cache name. 
- There is a way to configure all cache names through properties file also
- 
