
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

//Used expand the given data log by creating "clones" of the existing events
public class Clone {	

	public static void main(String[] args) throws IOException {

		
		// Read and store existing log 
		CSVReader reader = new CSVReader(new FileReader("SCMLog.csv"), '	');
		List<String[]> csvFile = reader.readAll();
		
		//Make a copy of log to be altered
		CSVReader reader2 = new CSVReader(new FileReader("SCMLogClone.csv"), '	');
		List<String[]> csvFileClone = reader2.readAll();
				
		csvFileClone.remove(0); //Remove headers (no need to duplicate these)
		
		for(int i=0;i<csvFileClone.size();i++){
			for(int j=0;j<=19;j++){
				if(csvFileClone.get(i)[j].toString().equals("Nu")){
					//Don't alter Null entries
				}
				else{
					csvFileClone.get(i)[j] = csvFileClone.get(i)[j]+"1"; //Append a single character to each value (in this case 1, can be changed)
				}
			}
		}
		
		reader.close();
		reader2.close();		
		
		CSVWriter writer = new CSVWriter(new FileWriter("Test.csv"), '	', CSVWriter.NO_QUOTE_CHARACTER); //Choose location to write to
		
		csvFile.addAll(csvFileClone); //Append new cloned events onto existing events
		
		writer.writeAll(csvFile); //Write to file
		writer.flush();
		writer.close();
		}
	
	
}
