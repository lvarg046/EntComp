/*
 * Luis Vargas - Due February 13, 2022
 * CNT 4714 - Project 2 - Spring 2022
 * Multi-threaded programming in Java
 */
package conveyorpacking;

import java.util.*;

public class RoutingStation {
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
    }
}
