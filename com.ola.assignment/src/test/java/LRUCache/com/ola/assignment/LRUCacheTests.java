package LRUCache.com.ola.assignment;

import java.io.File;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;
import LRUCache.com.ola.cacheService.BasicCacheService;
import LRUCache.com.ola.cacheService.LRUCacheService;
import LRUCache.com.ola.modelCache.BasicModelCache;
import LRUCache.com.ola.modelCache.CacheEntry;
import LRUCache.com.ola.modelCache.LRUCache;
import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class LRUCacheTests extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public LRUCacheTests(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	/*
	 * public static Test suite() { return new TestSuite(AppTest.class); }
	 */

	@Test
	public void initializeCacheWithData_caheGrownBeyondMaxSize_evictLeaseRecentlyUsedKey() throws Exception {
		BasicCacheService cacheService = LRUCacheService.getInstance();
		cacheService.cacheDirectory = new File("C:/CacheDirectory2"); // all the cache files will be stored in this
																		// path.
		cacheService.cacheSize = 10; // max cache size is 10 bytes.
		cacheService.valuCountPerKey = 2;
		BasicModelCache cache = cacheService.CreateBasicModelCache();
		cache.put("Cache-1", "XYUV"); // cache size 4 bytes.
		cache.put("Cache-2", "UIYT"); // cache size 8 bytes.
		assertTrue(cache.size() == 8);

		String value = cache.get("Cache-2").getValue(0); // accessing data associated with Key Cache-2
		cache.put("Cache-3", "Salini"); // adding new value which will evict one key from cache to make space.

		// all the files with cache key "Cache-1" will be deleted to make space for
		// "Cache-3"
		Thread.currentThread().sleep(3000); // Cleaning up of cache files in disk happens in background thread. Waiting
											// for the files to be deleted.
		Set<String> remainingCacheKeys = ((LRUCache) cache).getAllCacheKeys();
		assertEquals(2, remainingCacheKeys.size());
		Iterator<String> it = remainingCacheKeys.iterator();
		String firstCacheKey = it.next();
		assertEquals("Cache-3", firstCacheKey);
		String secondCacheKey = it.next();
		assertEquals("Cache-2", secondCacheKey);
	}

	@Test
	public void initializeCache_multipleThreadsAreWriting_valuesAreWrittenCorrectlyIntoCache() throws InterruptedException  {
		BasicCacheService cacheService = LRUCacheService.getInstance();
		cacheService.cacheDirectory = new File("C:/CacheDirectory2"); // all the cache files will be stored in this
																		// path.
		cacheService.cacheSize = 100; // max cache size is 10 bytes.
		cacheService.valuCountPerKey = 2;
		LRUCache cache = (LRUCache)cacheService.CreateBasicModelCache();
		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {
				cache.put("Cache-1", "Sali");
				cache.put("Cache-2", "Shal");

			}
		});

		Thread t2 = new Thread(new Runnable() {

			@Override
			public void run() {
				cache.put("Cache-1", "Salini");

			}
		});

		t1.start();
		t2.start();

		t1.join();
		t2.join();
		
		Set<String> cacheKeys = cache.getAllCacheKeys();
		// only two distinct keys are added into cache.
		assertEquals(2, cacheKeys.size());
		
		CacheEntry entry = cache.getCacheEntry("Cache-1");
		assertEquals(2, entry.getIndex()); // No of files for key "Cache-1"
	}

	public void testApp() throws Exception {
		initializeCacheWithData_caheGrownBeyondMaxSize_evictLeaseRecentlyUsedKey();
		initializeCache_multipleThreadsAreWriting_valuesAreWrittenCorrectlyIntoCache();
	}
}
