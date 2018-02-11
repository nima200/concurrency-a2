package q2.monitor.robots;

import q2.monitor.parts.Body;
import q2.monitor.parts.Tail;
import util.Util;

import java.util.LinkedList;

public class TailAttacher implements Runnable {

    private final LinkedList<Body> aBodies_incomplete;
    private final long idleTime = 0;


    public TailAttacher(LinkedList<Body> pBodies_incomplete) {
        aBodies_incomplete = pBodies_incomplete;
    }

    @Override
    public void run() {
        while(true) {
            Body body = new Body();
            body.attachTail(new Tail());
            synchronized (aBodies_incomplete) {
                aBodies_incomplete.push(body);
                aBodies_incomplete.notify();
            }
            try {
                Thread.sleep(Util.randInt(10, 20));
            } catch (InterruptedException ignored) {
                System.out.println(Thread.currentThread().getName() + " idle time: " + idleTime);
                return;
            }
        }
    }
}
