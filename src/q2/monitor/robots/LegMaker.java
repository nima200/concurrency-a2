package q2.monitor.robots;

import q2.monitor.parts.Leg;
import q2.monitor.parts.Toe;
import util.Util;

import java.util.LinkedList;
import java.util.Random;

public class LegMaker implements Runnable {

    private final LinkedList<Leg> front_legs;
    private final LinkedList<Leg> hind_legs;
    private long idleTime = 0;

    public LegMaker(LinkedList<Leg> pFront_legs, LinkedList<Leg> pHind_legs) {
        front_legs = pFront_legs;
        hind_legs = pHind_legs;
    }

    @Override
    public void run() {
        while (true) {
            Random random = new Random();
            if (random.nextFloat() > 0.5) { /* Make a front leg */
                /* 5 Toes for front legs*/
                Toe[] toes = {new Toe(), new Toe(), new Toe(), new Toe(), new Toe()};
                Leg leg = new Leg(toes);
                synchronized (front_legs) {
                    front_legs.push(leg);
                    front_legs.notify();
                }
            } else { /* Make a hind leg */
                /* 4 Toes for hind legs */
                Toe[] toes = {new Toe(), new Toe(), new Toe(), new Toe()};
                Leg leg = new Leg(toes);
                synchronized (hind_legs) {
                    hind_legs.push(leg);
                    hind_legs.notify();
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
