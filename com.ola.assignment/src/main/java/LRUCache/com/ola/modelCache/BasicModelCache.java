package LRUCache.com.ola.modelCache;

public abstract class BasicModelCache {

	public boolean isValid;
	
	public long size;
	
	public abstract void put(String key, String value);
	
	public abstract CacheReturn get(String key);
		
	public long size()
	{
		return size;
	}
}
