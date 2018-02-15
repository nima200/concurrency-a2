package q2.semaphore.robots;

import q2.parts.Leg;
import q2.parts.Toe;
import util.Util;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class LegMaker implements Runnable {
    private final LinkedList<Leg> front_legs;
    private final LinkedList<Leg> hind_legs;
    private final Semaphore fLeg_binKey;
    private final Semaphore fLeg_produced;
    private final Semaphore hLeg_binKey;
    private final Semaphore hLeg_produced;
    private long idleTime;

    public LegMaker(LinkedList<Leg> pFront_legs,
                    LinkedList<Leg> pHind_legs,
                    Semaphore pFLeg_binKey,
                    Semaphore pFLeg_produced,
                    Semaphore pHLeg_binKey,
                    Semaphore pHLeg_produced) {
        front_legs = pFront_legs;
        hind_legs = pHind_legs;
        fLeg_binKey = pFLeg_binKey;
        fLeg_produced = pFLeg_produced;
        hLeg_binKey = pHLeg_binKey;
        hLeg_produced = pHLeg_produced;
    }

    @Override
    public void run() {
        long start;
        long stop;
        while(true) {
            Random random = new Random();
            if (random.nextFloat() > 0.5) { /* Make a front leg */
                /* 5 Toes for front legs*/
                Toe[] toes = {new Toe(), new Toe(), new Toe(), new Toe(), new Toe()};
                Leg leg = new Leg(toes);
                try {
                    /* Get access to the bin */
                    start = System.currentTimeMillis();
                    fLeg_binKey.acquire();
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    /* Put created leg in bin */
                    front_legs.push(leg);
                    /* Let go of the bin */
                    fLeg_binKey.release();
                    /* Inform about leg creation */
                    fLeg_produced.release();
                } catch (InterruptedException ignored) {
                    System.out.println(Thread.currentThread().getName() + " idle time: " + idleTime);
                    return;
                }
            } else { /* Make a hind leg */
                /* 4 Toes for hind legs */
                Toe[] toes = {new Toe(), new Toe(), new Toe(), new Toe()};
                Leg leg = new Leg(toes);
                try {
                    /* Get access to the bin */
                    start = System.currentTimeMillis();
                    hLeg_binKey.acquire();
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    /* Put created leg in the bin */
                    hind_legs.push(leg);
                    /* Let go of the bin */
                    hLeg_binKey.release();
                    /* Inform about leg production */
                    hLeg_produced.release();
                } catch (InterruptedException pE) {
                    System.out.println(Thread.currentThread().getName() + " idle time: " + idleTime);
                    return;
                }
            }
            try {
                /* Simulate assembly time */
                Thread.sleep(Util.randInt(10, 20));
            } catch (InterruptedException ignored) {
                System.out.println(Thread.currentThread().getName() + " idle time: " + idleTime);
                return;
            }
        }
    }
}
