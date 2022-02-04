/*
 * Luis Vargas - Due February 13, 2022
 * CNT 4714 - Project 2 - Spring 2022
 * Multi-threaded programming in Java
 */
package conveyorpacking;

import java.util.*;

public class Conveyor {
    int conveyorNum;
    private Lock theLock == new ReentrantLock();
    public Conveyor( int conveyorNum ){ // Assigns conveyor it's number
        this.conveyorNum = conveyorNum;
    }
    public boolean lockConveyor(){
        // theLock.lock();
        // use tryLock();
        // tryLock() returns true if the lock request is granted by the Lock Manager
        // i.e. the lock was free and was granted to the requesting thread - otherwise return is false.
        return false;
    }
    public void unlockConveyor(){
        // Simply call unlock() on theLock
    }
}
