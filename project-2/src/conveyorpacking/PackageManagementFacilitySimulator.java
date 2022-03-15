/*
 * Luis Vargas - Due February 13, 2022
 * CNT 4714 - Project 2 - Spring 2022
 * Multi-threaded programming in Java
 */
package conveyorpacking;
 import java.io.*;
 import java.util.*;
 import java.util.concurrent.ExecutorService;
 import java.util.concurrent.Executors;

public class PackageManagementFacilitySimulator {
    static int MAX = 10;

    public static void main ( String args[] ){
        try{
            System.out.println("\n * * * * * * * * * * PACKAGE MANAGEMENT FACILITY SIMULATION BEGINS * * * * * * * * * * \n");
            // Read in config.txt file
            Scanner file = new Scanner( new File("src/config.txt"));

            // array list to store the integers from config.txt
            ArrayList<Integer> config = new ArrayList<>();

            // create threadpool of MAX size
            ExecutorService application = Executors.newFixedThreadPool(MAX);

            // Read config.txt file into the config array
            while( file.hasNext() ) {
                int number = file.nextInt();
                config.add(number);
            }
            file.close();

            // Save the first integer in the config.txt file as number of routing stations in the simulation run
            int numberOfRoutingStations = config.get(0);

            // Assign the workloads to each station from the values in the config.txt file
            for ( int j = 0; j < numberOfRoutingStations; j++ ){
                System.out.println("Routing Station " + j + " Has a total workload of "+ config.get(j+1) );
            }
            System.out.println();
            System.out.println();

            // Create an array of conveyor objects
            Conveyor[] conveyors = new Conveyor[numberOfRoutingStations];
            // fill the array with the conveyors for this simulation run
            for ( int i = 0; i <numberOfRoutingStations; i++ ){
                // create the conveyor objects here
                conveyors[i] = new Conveyor(i);
            }

            // creates the routing stations for this simulation run
            for( int i = 0; i < numberOfRoutingStations; i++){
                try{
                    // start threads executing using the ExecutorService objects
                    // Routing Station constructor parameter order: station number. station-workload, input conveyor assignment, output conveyor assignment
                    application.execute(new RoutingStation(i, config.get(i+1), conveyors[i], conveyors[(i + numberOfRoutingStations - 1) % numberOfRoutingStations]));
                } catch ( Exception e){
                    e.printStackTrace();
                }
            }
            application.shutdown();
            while(!application.isTerminated() ){
                // Simulation running
            }
            System.out.println("\n * * * * * * * * * * ALL WORKLOADS COMPLETE * * * PACKAGE MANAGEMENT FACILITY SIMULATION ENDS * * * * * * * * * * \n");
        } catch(FileNotFoundException ex){
            System.out.println("File not found");
        }
    }
}
