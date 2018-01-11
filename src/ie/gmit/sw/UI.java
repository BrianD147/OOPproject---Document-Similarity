package ie.gmit.sw;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 
 * @author Brian Doyle - G00330969
 *
 */

public class UI {
	Scanner s = new Scanner(System.in);
	private boolean keepMenuAlive = true;
	private int option;
	
	private String doc1;
	private String doc2;
	private int shingleSize = 3;
	private int k = 10;
	private int blockingQueueSize = 100;
	private int[] minHashes;
	
	/**
	 * @param doc1 - first document name
	 * @param doc2 - second document name
	 * @param shingleSize - number of words in each shingle
	 * @param k - amount of minHashes
	 * @param blockingQueueSize - the size of the blocking queue
	 * @param minHashes - used to store the minHashes of 
	 */

	
	public void show() {
		while(keepMenuAlive) {
			System.out.println();
			System.out.println("======================");
			System.out.println("Main Menu");
			System.out.println("======================");
			System.out.println("1: Begin Comparison");
			System.out.println("2: Exit Application");
			System.out.println("======================");
			System.out.print("Enter option: ");
			option = s.nextInt();
			
			switch(option) {
				case 1:
					//Ask for the two documents to compare
					System.out.println("Enter Document 1 name: ");
					doc1 = s.next();
					doc1 = "./" + doc1 + ".txt";
					System.out.println("Enter Document 2 name: ");
					doc2 = s.next();
					doc2 = "./" + doc2 + ".txt";
					
					init();
					
					try {
						BlockingQueue<Shingle> bq1 = new LinkedBlockingQueue<>(blockingQueueSize);
						BlockingQueue<Shingle> bq2 = new LinkedBlockingQueue<>(blockingQueueSize);
						
						Map<Integer, List<Integer>> map1 = new ConcurrentHashMap<Integer, List<Integer>>();
						Map<Integer, List<Integer>> map2 = new ConcurrentHashMap<Integer, List<Integer>>();
						
						Thread t1 = new Thread(new FileParser(doc1, bq1, shingleSize, k), "T1");
						Thread t2 = new Thread(new FileParser(doc2, bq2, shingleSize, k), "T2");
						
						t1.start();
						t2.start();
						
						t1.join();
						t2.join();
						
						Thread t3 = new Thread(new Consumer(bq1, map1, k, minHashes), "T3");
						Thread t4 = new Thread(new Consumer(bq2, map2, k, minHashes), "T4");
						
						t3.start();
						t4.start();
						
						t3.join();
						t4.join();
						
						float result = Jaccard(map1.get(0), map2.get(0));
						System.out.println("Similarity: " + result + "%");
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					break;
				case 2:
					keepMenuAlive = false;
					break;
				default:
					System.out.println("Invalid Entry");
					break;
			}
		}
	}
	
	public void init() {
		Random random = new Random();
		minHashes = new int[k];
		for(int i=0; i<minHashes.length; i++) {
			minHashes[i] = random.nextInt();
		}
	}
	
	float Jaccard(List<Integer> x,List<Integer> y) {
		
		List<Integer> intersection = new ArrayList<Integer>(x);
		intersection.retainAll(y);
		//System.out.println("Common Mini-Hashes: "+intersection.size());
		
		float jaccard =((float)intersection.size())/((k));
		jaccard = (jaccard*100)/2;
		return jaccard;
		
	}
}
