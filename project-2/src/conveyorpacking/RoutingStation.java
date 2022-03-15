/*
 * Luis Vargas - Due February 13, 2022
 * CNT 4714 - Project 2 - Spring 2022
 * Multi-threaded programming in Java
 */
package conveyorpacking;

import java.util.*;

public class RoutingStation implements Runnable{
    protected Random gen = new Random();
    protected int stationNum;
    protected int workload;
    protected Conveyor inconveyor;
    protected Conveyor outconveyor;
    protected boolean bothLocks = false;
    protected int workLoadCounter;

    // RoutingStation constructor method
    public RoutingStation( int stationNum, int workload, Conveyor inconveyor, Conveyor outconveyor){
        this.stationNum = stationNum;
        this.workload = workload;
        this.inconveyor = inconveyor;
        this.outconveyor = outconveyor;
        workLoadCounter = workload;
        System.out.println("Routing Station "+stationNum+": Input conveyor set to conveyor number C"+inconveyor.conveyorNum);
        System.out.println("Routing Station "+stationNum+": Output conveyor set to conveyor number C"+outconveyor.conveyorNum);
        System.out.println("Routing Station "+stationNum+": Workload set. Station "+stationNum+" has a total of "+workload+" packages to move");

        // this.goToSleep();
    }

    // method for threads to go to sleep
    // Note: a sleeping thread in java maintains all resources allocated to it, including locks.
    //      locks are not relinquished during a sleep cycle.

    public void goToSleep(){
        try{
            Thread.sleep(gen.nextInt(500)); // sleep a random time up to 500ms, adjust time as needed
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }// end go to sleep

    public void doWork(){
        System.out.println("\n ****** Routing Station "+ stationNum + ": * * * * CURRENTLY HARD AT WORK MOVING PACKAGES * * * * * * \n");
        System.out.println("Routing Station "+ stationNum +": successfully moves packages into station on input conveyor C" + inconveyor.conveyorNum + ".\n");
        System.out.println("Routing Station "+ stationNum +": successfully moves packages out of station on output conveyor C"+ outconveyor.conveyorNum+".\n\n");
        workLoadCounter--;
        System.out.println("Routing Station "+ stationNum+": has "+workLoadCounter+" package groups left to move. \n\n");
        goToSleep();

        if(workLoadCounter == 0){
            System.out.println("\n # # # # # Routing Station "+stationNum+": WORKLOAD SUCCESSFULLY COMPLETED. * * * Routing Station "+stationNum+" preparing to go offline. # # # # \n");
            bothLocks = true;
            inconveyor.unlockConveyor();
            outconveyor.unlockConveyor();
            goToSleep();
        }
    }

    public void run() {
        // dump out the conveyor assignments and workload settings for the station - simulation output criteria.
        // example System.out.println("\n %%%% ROUTING STATION "+ stationNum +": ENTERING LOCK ACQUISITION PHASE\n);

        // Run the simulation on the station for its entire workload.
        for( int i=0; i < workload; i++ ){
            System.out.println("Routing Station " + stationNum + ": holds lock on input conveyor C" + inconveyor.conveyorNum + ".");
            bothLocks = false;
            while( !bothLocks ){
                // Get input conveyor
                if( inconveyor.getLock() ){ // Input conveyor lock is available
                    System.out.println("Routing Station "+ stationNum +": holds lock on input conveyor C"+ inconveyor.conveyorNum+".");
                    // Get output conveyor
                    if( outconveyor.getLock() ){ // Output conveyor lock is available
                        System.out.println("Routing Station " + stationNum + ": holds lock on output conveyor C"+outconveyor.conveyorNum+".");
                        bothLocks = true;
                        inconveyor.setLock();
                        outconveyor.setLock();
                        System.out.println("\n * * * * * * Routing Station "+ stationNum +": holds locks on both input conveyor C"+inconveyor.conveyorNum+" and on output conveyor C"+outconveyor.conveyorNum+". * * * * *  \n");

                        //start work flow - packages moving
                        doWork();
                        inconveyor.unlockConveyor();
                        System.out.println("Routing Station "+ stationNum+": Unlocks/releases input conveyor C"+inconveyor.conveyorNum);
                        outconveyor.unlockConveyor();
                        System.out.println("Routing Station "+ stationNum+": Unlocks/releases output conveyor C"+outconveyor.conveyorNum);
                    } else {
                        // unlock the input conveyor if the output conveyor is busy( i.e. locked by another routing station).
                        // wait a bit before trying again.
                        this.inconveyor.unlockConveyor();
                        System.out.println("Routing Station "+ stationNum+": Unable to lock output conveyor C"+outconveyor.conveyorNum+", unlocks input conveyor C"+inconveyor.conveyorNum);
//                        this.outconveyor.unlockConveyor();
                        bothLocks = true;
                        goToSleep();
                    }
                } else {
                    this.inconveyor.unlockConveyor();
                    this.outconveyor.unlockConveyor();
                    goToSleep();
                }

            }
        }
        inconveyor.unlockConveyor();
        outconveyor.unlockConveyor();
    }
}
