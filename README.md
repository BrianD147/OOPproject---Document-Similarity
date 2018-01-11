# OOPproject---Document-Similarity
Java program to determine similarity between two documents

Author: Brian Doyle - G00330969

Source files:

Runner.java - 		main() method is ran when the application is started, and calls UI.show().

UI.java - 		show() displays a menu and prompts user to start the program or exit. 
	  		Option 1 will prompt doc1 and doc2 names to be input, and the similarity will then be calculated.

FileParser.java - 	run() reads each file input line by line, makes all characters upperCase, splits each line into words, 
			and divid the words into shingles of a set size. the shingles are then put onto a blocking queue.

Consumer.java - 	run() produces hashCodes for each shingle in each file, determines the lowest values and then 
			compares the lowest minHash values of each file against each other. 
			Jaccard index formula is then used to get a percentage of similarity.

Shingle.java -		Constructor for a shingle, with parameters of docID and hashCode.

Poision.java - 		Constructor for a poision which extends shingle, with parameters of docID and hashCode.

In order to run the program, run "java –cp ./oop.jar ie.gmit.sw.Runner" on command line, from the downloaded folder.
