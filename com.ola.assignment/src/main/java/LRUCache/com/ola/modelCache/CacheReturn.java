package LRUCache.com.ola.modelCache;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import LRUCache.com.ola.util.Util;
/**
 * This class represents the object read from the cache for a given key.
 * @author ssaha
 *
 */
public class CacheReturn {

	private String key;
	
	private InputStream[] inputs;
	
	long[] lengths;
	
	public CacheReturn()
	{
		
	}
	public CacheReturn(String key, InputStream[] ins)
	{
		key = key;
		inputs = ins;
	}

	public String getKey() {
		return key;
	}

	public InputStream getInputStream(int index) {
		return inputs[index];
	}

	public long[] getLengths() {
		return lengths;
	}
	
	public String getValue(int index) throws IOException{
	      InputStream in = getInputStream(index);
	      Reader reader = new InputStreamReader(in, "UTF-8");	      
		  return Util.readFully(reader);	      	      
	    }
	
}
