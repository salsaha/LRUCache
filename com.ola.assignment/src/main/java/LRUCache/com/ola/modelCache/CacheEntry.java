package LRUCache.com.ola.modelCache;

import java.io.File;
import java.io.InputStream;

/**
 * The entry for each key is represented by this class.
 * 
 * @author ssaha
 *
 */
public class CacheEntry {
	
	private String key;
	
	private int index;
	    
    private long[] length;
    
    private File storageDiractory;      
    
    private boolean dirty;
    
    public CacheEntry(String cacheKey, File directory, int valueLength)
    {
    	key = cacheKey;
    	index = 0;
    	storageDiractory = directory;
    	// valueLength is the no of maximum files can be stored for a key.
    	length = new long[valueLength];
    }

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public long[] getLength() {
		return length;
	}

	public void setLength(int length, int i) {
		this.length[i] = length;
	}
	
	public File getFile(int i) {
	      return new File(storageDiractory, key + "." + i);
	    }
	
	public int getIndex() {
		return index;
	}

	public void incrementIndex()
	{
		index++;
	}
	
	public void decrementIndex()
	{
		index--;
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}	
	
}
