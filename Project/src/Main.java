

import org.apache.spark.sql.SparkSession;

//import default package.GraphPanel;
//import Spark;

import java.util.ArrayList;
import java.util.List;


//Main class used to run Spark tests
public class Main 
{
    public static void main( String[] args ) throws Exception// throws FileNotFoundException 
    {
    	double startTime, endTime, elapsedTime=0;
    	String[] inputFiles = {"ScmLog.csv", "ScmLog8100.csv", "ScmLog12150.csv", "ScmLog16200.csv"}; //Input logs of various sizes
	   	List<Double> runTimes = new ArrayList<>(); //Stores the recorded runtimes
	   	List<String> xLabels = new ArrayList<>();
	   		   	
	   	//Different possible x-axis labels for the graph
	   	List<String> cores = new ArrayList<>();
	   	cores.add("1");
	   	cores.add("2");
	   	cores.add("3");
	   	cores.add("4");
	   	
	   	List<String> inputs = new ArrayList<>();
	   	inputs.add("4050 Events");
	   	inputs.add("8100 Events");
	   	inputs.add("12150 Events");
	   	inputs.add("16200 Events");
	   	
	   	List<String> betas = new ArrayList<>();
	   	betas.add("0.4");
	   	betas.add("0.45");
	   	betas.add("0.5");
	   	betas.add("0.55");
	   	betas.add("0.6");
	   	

	   	//Creates Spark Session with given parameters, which can be adjusted
	   	Spark runTest = new Spark(4, "ScmLog.csv", 0.1, 0.5);           //(No. of Cores, Input Log, alpha, beta)
		startTime = System.currentTimeMillis(); //Records start time
		runTest.run(); //Runs test
		endTime = System.currentTimeMillis(); //Records end time
	
		elapsedTime = ((double) (endTime - startTime))/1000; //Calculates and records total runtime
		
		System.out.println("Elapsed Time in Seconds : "+elapsedTime+"s");
		
		//runTimes.add((double) elapsedTime);    //Can be used to store multiple runtime when generating graph
		
		//runTest.instances(); //Used to print process instances to file.txt
		
		
		//Used to generate graphs
		//GraphPanel graph = new GraphPanel(runTimes1, runTimes2, xAxisLabels, "Y axis Label");
		//graph.drawGraph();
	   	
    }
}