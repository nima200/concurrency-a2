package q2.monitor;

import q2.monitor.robots.*;
import q2.parts.*;

import java.util.ArrayList;
import java.util.List;

public class catmaker {

    private static final Bins aBins = new Bins();
    private static final List<Cat> aCats = new ArrayList<>();

    public static void main(String[] args) {
        /* Prepare the bins by adding resources for 1000000 ("infinite") productions */
        for (int i = 0; i < 1000000; i++) {
            aBins.getWhiskers().push(new Whisker());
            aBins.getHeads().push(new Head());
            aBins.getEyes().push(new Eye());
            aBins.getEyes().push(new Eye());
            aBins.getLegs().push(new Leg());
            aBins.getLegs().push(new Leg());
            aBins.getLegs().push(new Leg());
            aBins.getLegs().push(new Leg());
            aBins.getTails().push(new Tail());
            aBins.getBodies().push(new Body());
            aBins.getToes().push(new Toe());
            aBins.getToes().push(new Toe());
            aBins.getToes().push(new Toe());
            aBins.getToes().push(new Toe());
            aBins.getToes().push(new Toe());
            aBins.getToes().push(new Toe());
        }

        /* Create the worker robot threads */
        Thread eyeAttacher_1 = new Thread(new EyeAttacher(aBins), "Eye Attacher 1");
        Thread eyeAttacher_2 = new Thread(new EyeAttacher(aBins), "Eye Attacher 2");
        Thread legAttacher_1 = new Thread(new LegAttacher(aBins), "Leg Attacher 1");
        Thread legAttacher_2 = new Thread(new LegAttacher(aBins), "Leg Attacher 2");
        Thread legMaker_1 = new Thread(new LegMaker(aBins), "Leg Maker 1");
        Thread legMaker_2 = new Thread(new LegMaker(aBins), "Leg Maker 2");
        Thread tailAttacher_1 = new Thread(new TailAttacher(aBins), "Tail Attacher 1");
        Thread tailAttacher_2 = new Thread(new TailAttacher(aBins), "Tail Attacher 2");
        Thread whiskerAttacher_1 = new Thread(new WhiskerAttacher(aBins), "Whisker Attacher 1");
        Thread whiskerAttacher_2 = new Thread(new WhiskerAttacher(aBins), "Whisker Attacher 2");
        Thread[] robots = {eyeAttacher_1, eyeAttacher_2, legAttacher_1, legAttacher_2, legMaker_1, legMaker_2,
                            tailAttacher_1, tailAttacher_2, whiskerAttacher_1, whiskerAttacher_2};

        Thread catMaker = new Thread(() -> {
            long idleTime = 0;
            long start;
            long stop;

            while (aCats.size() < 250) {
                Body body;
                Head head;

                /* Get a completed body */
                start = System.currentTimeMillis();
                synchronized (aBins.getBodyCompleted()) {
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    while (aBins.getBodyCompleted().isEmpty()) {
                        try {
                            aBins.getBodyCompleted().wait();
                        } catch (InterruptedException ignored) {
                            System.out.println(Thread.currentThread().getName() + " idle time: " + idleTime);
                        }
                    }
                    body = aBins.getBodyCompleted().pop();
                }

                /* Get a completed head */
                start = System.currentTimeMillis();
                synchronized (aBins.getHeadCompleted()) {
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    while (aBins.getHeadCompleted().isEmpty()) {
                        try {
                            aBins.getHeadCompleted().wait();
                        } catch (InterruptedException ignored) {
                            System.out.println(Thread.currentThread().getName() + " idle time: " + idleTime);
                        }
                    }
                    head = aBins.getHeadCompleted().pop();
                }
                /* Create the cat out of the completed head and body */
                Cat cat = new Cat(head, body);
                aCats.add(cat);
            }
            for (Thread robot: robots) {
                robot.interrupt();
            }
            System.out.println(Thread.currentThread().getName() + " idle time: " + idleTime);
        }, "Cat Maker");

        for (Thread robot :
                robots) {
            robot.start();
        }

        catMaker.start();
    }
}
