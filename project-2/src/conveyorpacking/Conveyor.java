/*
 * Luis Vargas - Due February 13, 2022
 * CNT 4714 - Project 2 - Spring 2022
 * Multi-threaded programming in Java
 */
package conveyorpacking;

import java.util.*;
import java.util.concurrent.locks.*;

public class Conveyor {
    int conveyorNum;
    private Lock theLock = new ReentrantLock();
    public Conveyor( int conveyorNum ){ // Assigns conveyor it's number
        this.conveyorNum = conveyorNum;
    }

    public boolean getLock(){
        return theLock.tryLock();
    }
    public void setLock(){
        theLock.lock();
    }
    public void unlockConveyor(){
        // Simply call unlock() on theLock
        try{
            theLock.unlock();
        } catch ( Exception e){
            System.out.print("");
        }
    }
}
