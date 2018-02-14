package q2.semaphore.robots;

import q2.parts.Body;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class TailAttacher implements Runnable {

    private final LinkedList<Body> aBodies_incomplete;
    private final Semaphore aBodies_inc_binKey;
    private final Semaphore aBodies_inc_produced;
    private long idleTime = 0;

    public TailAttacher(LinkedList<Body> pBodies_incomplete,
                        Semaphore pBodies_inc_binKey,
                        Semaphore pBodies_inc_produced) {
        aBodies_incomplete = pBodies_incomplete;
        aBodies_inc_binKey = pBodies_inc_binKey;
        aBodies_inc_produced = pBodies_inc_produced;
    }

    @Override
    public void run() {

    }
}
