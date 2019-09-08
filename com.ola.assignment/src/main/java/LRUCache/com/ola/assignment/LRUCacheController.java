package LRUCache.com.ola.assignment;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import LRUCache.com.ola.cacheService.BasicCacheService;
import LRUCache.com.ola.cacheService.LRUCacheService;
import LRUCache.com.ola.modelCache.BasicModelCache;
import LRUCache.com.ola.modelCache.LRUCache;

/**
 * Hello world!
 *
 */
public class LRUCacheController {
	public static void main(String[] args) throws IOException, InterruptedException {

		BasicCacheService cacheService = LRUCacheService.getInstance();
		cacheService.cacheDirectory = new File("C:/CacheDirectory3");
		cacheService.cacheSize = 100;
		cacheService.valuCountPerKey = 2;
		BasicModelCache cache = cacheService.CreateBasicModelCache();
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

		Thread t3 = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					String value = cache.get("Cache-1").getValue(0);
					System.out.println("Cache valus is : " + value);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		t1.start();
		
		
		t1.join();
		t2.start();
		t3.start();
		// All the files for Cache-1 key will be deleted to rebalance the cache size.
	}
}
