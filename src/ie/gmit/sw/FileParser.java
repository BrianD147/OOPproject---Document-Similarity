package ie.gmit.sw;

import java.io.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;

/**
 * 
 * @author Brian Doyle - G00330969
 *
 */

public class FileParser implements Runnable {
	private int docID = 0;
	private Deque<String> buffer = new LinkedList<>();
	private BlockingQueue<Shingle> bq;
	private String file;
	private int shingleSize;
	private int k;
	
	/**
	 * Constructor for parsing a file
	 * @param file - name of file which is being parsed
	 * @param bq - blocking queue of shingles
	 * @param shingleSize - determine how many words will be in each shingle
	 * @param k - the number of minHashes
	 */
	
	public FileParser(String file, BlockingQueue<Shingle> bq, int shingleSize, int k){
		//super();
		this.file = file;
		this.bq = bq;
		this.shingleSize = shingleSize;
		this.k = k;
	}

	@Override
	public void run() {
		/**
		 *  Reads in file line by line, makes all characters upper case, splits line by spaces into words, divide the words into shingles, and add shingles to queue
		 */
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = null;
			while((line = br.readLine()) != null) {
				String uLine = line.toUpperCase();
				String[] words = uLine.split(" ");
				addWordsToBuffer(words);
				Shingle s = getNextShingle();
				bq.put(s);
			}
			bq.put(new Shingle(0, 0));
			flushBuffer();
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Shingle getNextShingle() {
		StringBuilder sb = new StringBuilder();
		int counter = 0;
		while(counter < shingleSize) {
			if(buffer.peek() != null) {
				sb.append(buffer.poll());
			}
			counter++;
		}
		if(sb.length() > 0) {
			return (new Shingle(docID, sb.toString().hashCode()));
		} else {
			return null;
		}
	}

	private void addWordsToBuffer(String[] words) {
		for(String s : words) {
			buffer.addLast(s);
		}
	}
	
	private void flushBuffer() throws Exception{
		while(buffer.size()>0) {
			Shingle  s = getNextShingle();
			if(s!=null) {
				bq.put(s);
			}else {
				bq.put(new Poision(docID,0));
			}
		}
}
}
