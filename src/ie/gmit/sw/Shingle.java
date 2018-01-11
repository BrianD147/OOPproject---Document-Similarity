package ie.gmit.sw;

/**
 * 
 * @author Brian Doyle - G00330969
 *
 */

public class Shingle {
	private int docID;
	private int hashCode;
	
	/**
	 * 
	 * @param docID - Used to easier identify documents
	 * @param hashCode - given to each document and will later be compared
	 */
	
	public Shingle(int docID, int hashCode) {
		super();
		this.docID = docID;
		this.hashCode= hashCode;
	}

	public int getDocID() {
		return docID;
	}

	public void setDocID(int docID) {
		this.docID = docID;
	}

	public int getHashCode() {
		return hashCode;
	}

	public void setHashCode(int hashCode) {
		this.hashCode = hashCode;
	}
}
