UCD Computer Science Final Year Project 2020
By Daniel Graham - Student No.15319536
Supervisor: Liam Murphy

"Event Correlation for Service Computing using Spark SQL"

The goal of this project is to increase the efficiency of existing event correlation techniques by creating a parallel
implementation using Spark SQL and then evaluate the increase in efficiency by comparing the runtime of this implementation
to that of a sequential implementation while varying a number of parameters.


The classes are all stored in the src folder.
Main is used to run tests on the parallel implementation which is located in Spark.
Use of this class requires installation of the Spark platform which can be found at spark.apache.org
Sequential contains the sequential impementation.
GraphPanel was used to generate graphs for the report.
Clone was used to produce data logs of increased size by taking the existing log and generating "clones" of the existing events.
The csv files are all input logs of varying sizes, the numbers reresenting the number of events each contains.
