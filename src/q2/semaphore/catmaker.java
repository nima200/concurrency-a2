package q2.semaphore;

import q2.parts.*;
import q2.semaphore.robots.*;
import util.Util;

import java.util.ArrayList;
import java.util.List;

public class catmaker {
    private static final Bins aBins = new Bins();
    private static final List<Cat> aCats = new ArrayList<>();

    public static void main(String[] args) {
        /* Fill up the basic bins with "infinite" supply */
        for (int i = 0; i < 1000000; i++) {
            aBins.getHeadBin().push(new Head());
            aBins.getToeBin().push(new Toe());
            aBins.getLegBin().push(new Leg());
            aBins.getEyeBin().push(new Eye());
            aBins.getWhiskerBin().push(new Whisker());
            aBins.getBodyBin().push(new Body());
            aBins.getTailBin().push(new Tail());
        }

        /* Create the robot threads */
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
            long start;
            long stop;
            long idleTime = 0;
            while (aCats.size() < 250) {
                try {
                    Body body;
                    Head head;
                    /* Wait for a completed body */
                    start = System.currentTimeMillis();
                    aBins.getBodyCompleted().consume();
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    /* Take the completed body */
                    start = System.currentTimeMillis();
                    aBins.getBodyCompleted().getAccess();
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    body = aBins.getBodyCompleted().pop();
                    aBins.getBodyCompleted().releaseAccess();

                    /* Wait for a completed head */
                    start = System.currentTimeMillis();
                    aBins.getHeadCompleted().consume();
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    /* Take the completed head */
                    start = System.currentTimeMillis();
                    aBins.getHeadCompleted().getAccess();
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    head = aBins.getHeadCompleted().pop();
                    aBins.getHeadCompleted().releaseAccess();

                    /* Create a cat out of the completed head and body */
                    Cat cat = new Cat(head, body);
                    aCats.add(cat);
                    /* Simulate Assembly*/
                    Thread.sleep(Util.randInt(10, 20));
                } catch (InterruptedException pE) {
                    System.out.println(Thread.currentThread().getName() + " idle time: " + idleTime);
                    return;
                }
            }
            for (Thread robot: robots) {
                robot.interrupt();
            }
            System.out.println(Thread.currentThread().getName() + " idle time: " + idleTime);
        }, "Cat Maker");

        for (Thread robot: robots) {
            robot.start();
        }

        catMaker.start();
    }
}
