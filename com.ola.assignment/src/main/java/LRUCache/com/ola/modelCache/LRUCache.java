package LRUCache.com.ola.modelCache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The LRUCache which implements LRU eviction policy.
 * @author ssaha
 *
 */
public class LRUCache extends BasicModelCache {

	private int maxSize;
	private int valueCountPerKey;
	private File directory;
	ExecutorService executor;

	HashMap<String, CacheEntry> cacheEntryStorage = new HashMap<String, CacheEntry>();
	List<CacheEntry> orderList = new ArrayList<CacheEntry>();

	public LRUCache(int cacheSize, int valueCount, File dr) {
		this.maxSize = cacheSize;
		this.valueCountPerKey = valueCount;
		this.directory = dr;
		size = 0;
		executor = Executors.newFixedThreadPool(2);
		initializeCache();
	}

	public void initializeCache() {
		if (maxSize <= 0) {
			throw new IllegalArgumentException("maxSize <= 0");
		}
		if (valueCountPerKey <= 0) {
			throw new IllegalArgumentException("valueCount <= 0");
		}
		try {
			directory.mkdirs();
		} catch (Exception e) {
			System.out.println("Failed to create directory");
		}
	}

	@Override
	public synchronized void put(String key, String value) {
		try {
			CacheEntry entry = getOrCreateCacheEntry(key);
			edit(entry);
			OutputStream outputStream = getNewOutputStream(entry);
			outputStream.write(value.getBytes());
			outputStream.close();
			entry.setLength(value.getBytes().length, entry.getIndex());
			size = size + entry.getLength()[entry.getIndex()];
			entry.incrementIndex();
			commit(entry);			
		} catch (IOException e) {
			System.out.println("Failed to put value in cache");
		} catch (IllegalArgumentException e) {
			System.out.println(e.toString());
		}
	}

	@Override
	public synchronized CacheReturn get(String key) {
		CacheEntry entry = getCacheEntry(key);
		if (entry == null) {
			return null;
		}

		// Moving the recently used at the bottom of the orderList.
		// That way the least recently used will be at the top of the orderList and the
		// entries will be removed
		// from the top when the cache is full.
		orderList.remove(entry);
		orderList.add(entry);
		InputStream[] ins = new InputStream[valueCountPerKey];
		try {
			for (int i = 0; i < entry.getIndex(); i++) {
				ins[i] = new FileInputStream(entry.getFile(i));
			}
		} catch (FileNotFoundException e) {
			// A file must have been deleted manually!
			System.out.println("File must have been deleted manually !!!");
		}
		return new CacheReturn(key, ins);
	}
	
	public synchronized CacheEntry getCacheEntry(String key) {
		if (cacheEntryStorage.containsKey(key)) {
			return cacheEntryStorage.get(key);
		}
		return null;
	}

	private synchronized CacheEntry getOrCreateCacheEntry(String key) {
		CacheEntry entry = getCacheEntry(key);
		if (entry == null) {
			entry = new CacheEntry(key, directory, valueCountPerKey);
			cacheEntryStorage.put(key, entry);
			orderList.add(entry);
		}
		return entry;
	}

	private OutputStream getNewOutputStream(CacheEntry entry) {
		int index = entry.getIndex();
		if (index < 0 || index >= valueCountPerKey) {
			throw new IllegalArgumentException("Expected index " + index + " to "
					+ "be greater than 0 and less than the maximum value count " + "of " + valueCountPerKey);
		}

		FileOutputStream outputStream;
		synchronized (LRUCache.this) {
			File cacheFile = entry.getFile(index);			
			try {
				outputStream = new FileOutputStream(cacheFile);
			} catch (FileNotFoundException e) {
				// if directory doesn't exist create a directory
				directory.mkdirs();
				try {
					outputStream = new FileOutputStream(cacheFile);
				} catch (FileNotFoundException e2) {
					return null;
				}
			}
		}
		return outputStream;
	}

	private synchronized void edit(CacheEntry entry) {
		entry.setDirty(true);
	}

	private final Callable<Void> cleanupCallable = new Callable<Void>() {
		public Void call() throws Exception {
			synchronized (LRUCache.this) {
				trimToSize();

			}
			return null;
		}
	};

	private synchronized void trimToSize() throws IOException {
		while (size > maxSize) {
			CacheEntry toEvict = orderList.get(0);
			remove(toEvict);
		}
	}

	private synchronized void remove(CacheEntry entry) throws IOException {

		for (int i = 0; i < entry.getIndex(); i++) {
			File file = entry.getFile(i);			
			if (file.exists()) {
				System.out.println(file.getAbsolutePath());
				Files.delete(file.toPath());
				/*if (file.delete())
				    System.out.println("  Deleted!");
				  else
				    System.out.println("  Delete failed - reason unknown");*/
				size -= entry.getLength()[i];
				entry.setLength(0, i);				
			}
		}

		cacheEntryStorage.remove(entry.getKey());
		orderList.remove(entry);
	}

	private synchronized void commit(CacheEntry entry) {
		entry.setDirty(false);
		if (size > maxSize) {
			executor.submit(cleanupCallable);
		}

	}

	public Set<String> getAllCacheKeys()
	{
		return cacheEntryStorage.keySet();
	}
}
