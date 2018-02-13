package q2.monitor;

import q2.monitor.parts.Body;
import q2.monitor.parts.Cat;
import q2.monitor.parts.Head;
import q2.monitor.parts.Leg;
import q2.monitor.robots.*;

import java.util.*;

public class catmaker {
    private static final LinkedList<Head> heads_incomplete = new LinkedList<>();
    private static final LinkedList<Head> heads_complete = new LinkedList<>();
    private static final LinkedList<Body> bodies_incomplete = new LinkedList<>();
    private static final LinkedList<Body> bodies_complete = new LinkedList<>();
    private static final LinkedList<Leg> front_legs = new LinkedList<>();
    private static final LinkedList<Leg> hind_legs = new LinkedList<>();
    private static final LinkedList<Cat> cats = new LinkedList<>();

    public static void main(String[] args) {
        Thread legMaker_1 = new Thread(new LegMaker(front_legs, hind_legs), "Leg Maker 1");
        Thread legMaker_2 = new Thread(new LegMaker(front_legs, hind_legs), "Leg Maker 2");

        Thread tailAttacher_1 = new Thread(new TailAttacher(bodies_incomplete), "Tail Attacher 1");
        Thread tailAttacher_2 = new Thread(new TailAttacher(bodies_incomplete), "Tail Attacher 2");

        Thread legAttacher_1 = new Thread(new LegAttacher(bodies_incomplete, bodies_complete, front_legs, hind_legs), "Leg Attacher 1");
        Thread legAttacher_2 = new Thread(new LegAttacher(bodies_incomplete, bodies_complete, front_legs, hind_legs), "Leg Attacher 2");

        Thread eyeAttacher_1 = new Thread(new EyeAttacher(heads_incomplete), "Eye Attacher 1");
        Thread eyeAttacher_2 = new Thread(new EyeAttacher(heads_incomplete), "Eye Attacher 2");

        Thread whiskerAttacher_1 = new Thread(new WhiskerAttacher(heads_incomplete, heads_complete), "Whisker Attacher 1");
        Thread whiskerAttacher_2 = new Thread(new WhiskerAttacher(heads_incomplete, heads_complete), "Whisker Attacher 2");

        Thread[] robots = {legAttacher_1, legAttacher_2, tailAttacher_1, tailAttacher_2, legMaker_1, legMaker_2,
                whiskerAttacher_1, whiskerAttacher_2, eyeAttacher_1, eyeAttacher_2};

        Thread catMaker = new Thread(() -> {
            long idleTime = 0;
            while (cats.size() < 250) {
                Body body;
                Head head;
                synchronized (bodies_complete) {
                    while (bodies_complete.isEmpty()) {
                        try {
                            long start = System.currentTimeMillis();
                            bodies_complete.wait();
                            long stop = System.currentTimeMillis();
                            idleTime += stop - start;
                        } catch (InterruptedException ignored) {
                            System.out.println(Thread.currentThread().getName() + " idle time: " + idleTime);
                            return;
                        }
                    }
                    body = bodies_complete.pop();
                }
                synchronized (heads_complete) {
                    while (heads_complete.isEmpty()) {
                        try {
                            long start = System.currentTimeMillis();
                            heads_complete.wait();
                            long stop = System.currentTimeMillis();
                            idleTime += stop - start;
                        } catch (InterruptedException pE) {
                            System.out.println(Thread.currentThread().getName() + " idle time: " + idleTime);
                            return;
                        }
                    }
                    head = heads_complete.pop();
                }
                Cat cat = new Cat(head, body);
                cats.push(cat);
            }
            for (Thread robot: robots) {
                robot.interrupt();
            }
        }, "Cat Maker");

        catMaker.start();
        for(Thread robot: robots) {
            robot.start();
        }
    }

}
