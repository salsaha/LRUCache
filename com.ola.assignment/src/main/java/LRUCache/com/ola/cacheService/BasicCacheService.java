package LRUCache.com.ola.cacheService;

import java.io.File;

import LRUCache.com.ola.modelCache.BasicModelCache;
/**
 * cache service for BasicModelCache.
 * @author ssaha
 *
 */
public abstract class BasicCacheService {
	
	/**
	 * Cache storage.
	 */
	private BasicModelCache basicModelCache; 
	
	/**
	 * Cache directory where the cache files will be stored.
	 */
	public File cacheDirectory;
	
	/**
	 * Multiple Cache files can be stored with same key. This is to limit the no of files can be stored with the same key.
	 */
	public int valuCountPerKey;
	
	/**
	 * Maximum cache size.
	 */
	public int cacheSize;
	
	public BasicModelCache getBasicModelCache() {
		return basicModelCache;
	}

	public abstract BasicModelCache CreateBasicModelCache();
	
	public boolean isInitialized()
	{
		return basicModelCache.isValid;
	}
	
	public BasicModelCache getCache()
	{
		return basicModelCache;
	}
	
}
