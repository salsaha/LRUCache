package LRUCache.com.ola.cacheService;

import java.io.File;

import LRUCache.com.ola.modelCache.BasicModelCache;
import LRUCache.com.ola.modelCache.LRUCache;

public class LRUCacheService extends BasicCacheService {

	private static final BasicCacheService cacheService = new LRUCacheService();
	
	public LRUCacheService()
	{
		
	}
	
	/**
	 * Create the instance of LRUCache.
	 */
	@Override
	public BasicModelCache CreateBasicModelCache() {
		LRUCache cache = new LRUCache(cacheSize, valuCountPerKey, cacheDirectory);
		return cache;
	}
	
	/**
	 * Return the instance of the cache service.
	 * @return
	 */
	public static BasicCacheService getInstance()
	{
		return cacheService;
	}	
}
