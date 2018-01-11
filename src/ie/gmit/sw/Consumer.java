package ie.gmit.sw;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * 
 * @author Brian Doyle - G00330969
 *
 */

public class Consumer implements Runnable {
	private BlockingQueue<Shingle> bq;
	private int k;
	private int[] minHashes;
	private ExecutorService pool;
	
	private Map<Integer, List<Integer>> map = new ConcurrentHashMap<Integer, List<Integer>>();
	
	public Consumer (BlockingQueue<Shingle> bq, Map <Integer, List<Integer>> map, int k, int[] minHashes) {
		super();
		this.bq = bq;
		this.map = map;
		this.k = k;
		this.minHashes = minHashes;
		//init();
	}
	
	public void init() {
		Random random = new Random();
		minHashes = new int[k];
		for (int i = 0; i < minHashes.length; i++) {
			minHashes[i] = random.nextInt();
			
		}
	}

	public void run() {
		int fileCount = 2;
		while(fileCount > 1) {
			try {
				Shingle s = bq.take();
				if(s.getHashCode() == 0) {
					fileCount--;
					continue;
				}
				
				List<Integer> list = map.get(s.getDocID());
				
				if(list == null) {
					list = new ArrayList<Integer>(k);
					
					for(int j=0; j<k; j++) {
						list.add(j, Integer.MAX_VALUE);
					}
					map.put(s.getDocID(), list);
				}
				
				for(int i=0; i<minHashes.length; i++) {
					int value = s.getHashCode() ^ minHashes[i];
					if(list.get(i) > value) {
						list.add(value);
					}
				}
				//map.put(s.getDocID(), list);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
