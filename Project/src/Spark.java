
import org.apache.spark.sql.SparkSession;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;


public class Spark
{

	static SparkSession spark;

  
	static Dataset<Row> df;
	static List<String> correlationConditions = new ArrayList<String>(); //Stores the correlation conditions that are found to be interesting
	static String[] columns; //Stores the column names
	static double alpha, beta;
	
	
	public void run() throws FileNotFoundException{
		distinctConditions();
		sharedConditions();
		pi_Ratio();
	}
	
	//Prints process instances to file.txt
	public void instances() throws FileNotFoundException{
		PrintWriter out = new PrintWriter("file.txt");
        
        for(String condition : correlationConditions){
        	String temp = condition;
        	Dataset<Row> value = spark.sql("SELECT DISTINCT "+temp+" FROM input WHERE NOT "+temp+"=='Nu'");
        	
        	List<Row> arrayList= new ArrayList<>();
        	arrayList=value.collectAsList();
        	
        	for(Row row : arrayList){
        		String info = row.getString(0);
        		long count = (spark.sql("SELECT * FROM input WHERE "+temp+"='"+info+"'")).count();
        		out.println(temp+"=="+info+": {"+count+"}");
        	}
        }
        out.close();
        spark.close();
	}
	
	public Spark(int cores, String inputFile, double alpha, double beta) throws FileNotFoundException{
	

		this.alpha = alpha; //Sets alpha and beta parameters to given values
		this.beta = beta;
		
		spark = SparkSession
		  	     .builder()
		  	     .appName("Event Correlation")
		  	     .config("spark.master", "local["+cores+"]") //Creates spark session using given number of cores
		  	     .getOrCreate();

		
		df = spark.read().format("csv").option("delimiter", "	").option("header", "true").load(inputFile); //Reads input log
		df.createOrReplaceTempView("input");
		  
		columns = df.columns(); //Gets column names
		
	}
	
 
    
    //Checks the distinct ratio for all key-based conditions
	public static void distinctConditions() throws FileNotFoundException{ 
    	
    	double distinct, nonNull, distinctRatio;
    	Dataset<Row> sqlDR,sqlNN;
    	String temp;
    	
    	
    	for(int i=0;i<columns.length;i++){
        	temp = columns[i];
        	
        	sqlDR = spark.sql("SELECT DISTINCT "+temp+" FROM input WHERE NOT "+temp+"=='Nu'"); //Counts number of distinct values
        	distinct = sqlDR.count();
        	
        	sqlNN = spark.sql("SELECT "+temp+" FROM input WHERE NOT "+temp+"=='Nu'"); //Counts number of nonnull values
        	nonNull = sqlNN.count();
        	
        	distinctRatio = distinct/nonNull; //Calculates distinct ratio
        	        	
        	if (distinctRatio < 1 && distinctRatio > alpha){ //Checks if distinct ratio meets the criteria to be considered interesting
        		correlationConditions.add(temp);             //If it does, adds the condition to correlationConditions
        	}
    	}
    }
    
    //Calculates shared ratio for all reference based conditions
    public static void sharedConditions(){
    	
    	String temp1,temp2, condition;
    	double distinct, d1, d2, max, sharedRatio;
    	
    	
    	 for(int x=0;x<columns.length;x++){
         	for(int y=x+1;y<columns.length;y++){
         		if(x<y){
         			temp1 = columns[x];
         			temp2 = columns[y];
         			
         			distinct = spark.sql("SELECT "+temp1+" FROM input WHERE "+temp1+"=="+temp2+" AND NOT "+temp1+"=='Nu' AND NOT "+temp2+"=='Nu'").count();
         			d1 = spark.sql("SELECT DISTINCT "+temp1+" FROM input WHERE NOT "+temp1+"=='Nu'").count();
         			d2 = spark.sql("SELECT DISTINCT "+temp2+" FROM input WHERE NOT "+temp2+"=='Nu'").count();
 
         			//Determines which column has the larger number of distinct values
         			if(d1>d2){
         				max=d1;
         			}
         			else{
         				max=d2;
         			}
         			
         			sharedRatio = distinct/max; //Calculates shared ratio
         	
         			if (sharedRatio > alpha){ //If shared ratio is greater than alpha, adds condition to correlationConditions
         				condition = temp1+", "+temp2;
         				correlationConditions.add(condition);
         			}
         		}
         	}
    	
    	 }
    
    }
    
    //Calculates PI Ratio for all conditions that have been found to be interesting 
    public static void pi_Ratio(){
    	
    	Dataset<Row> sqlPI;
    	double processInstances, nonNull, pi_Ratio;
    	List<String> found = new ArrayList<String>();
    	
    	for(String cond : correlationConditions){
    		sqlPI = spark.sql("SELECT DISTINCT "+cond+" FROM input");
    		processInstances = sqlPI.count();
    		nonNull = df.count();
    		pi_Ratio = processInstances/nonNull;
    		if(pi_Ratio > beta){
    			found.add(cond);
    		}
    	}
    	correlationConditions.removeAll(found); //Any conditions that do not meet the criteria are removed
    }
}