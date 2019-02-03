# Test task: words occurrence frequency calculation from a file

## Task description   
Read big (100Mb) test file, build a statistics - words and punctuation marks occurrence frequency, words length.
Print out words and punctuation marks sorted by frequency (any acceptable format).
Cover with Unit tests. 
Use standard libraries only.

## Assumptions   
1. Words length is mentioned but not used for output requirements: adding it to output;
2. Use ascending sort;
3. File name to analyze is defined as a parameter for the application in possible file formats;
4. SLA are not defined in requirements, so assume there are none.
Common sense however says that the algorithm should be as optimal as possible.
Please see inline code comments for details.
5. Words comparison is case sensitive;
6. Inner sorting is used as well: to order words with same frequency; 

## How to run
Application JAR file is contained in  
`output/frequencycalculation.jar`  
  
To run it without build please use  
`$ java -jar frequencycalculation.jar <path to file for processing>`  

The project's Gradlew wrapper could be used to build or test application.  
Please use the following accordingly  
`gradlew build`  
`gradlew test`

Sunny day scenario output looks like the following  
`===================== <file name> statistics =====================`  
`Frequency | Word | Length`  
`<statistics itself>`  
`Time to calculate: <time to calculate>`  
 