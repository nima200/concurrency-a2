package q2.monitor;

import q2.Robot;
import q2.monitor.robots.*;
import q2.parts.*;
import util.Util;

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
            aBins.getLegs().push(new Leg());
            aBins.getTails().push(new Tail());
            aBins.getBodies().push(new Body());
            aBins.getToes().push(new Toe());
        }

        /* Create the worker robot threads */
        Robot eyeAttacher_1 = new EyeAttacher(aBins, "Eye Attacher 1");
        Robot eyeAttacher_2 = new EyeAttacher(aBins, "Eye Attacher 2");
        Robot legAttacher_1 = new LegAttacher(aBins, "Leg Attacher 1");
        Robot legAttacher_2 = new LegAttacher(aBins, "Leg Attacher 2");
        Robot legMaker_1 = new LegMaker(aBins,"Leg Maker 1");
        Robot legMaker_2 = new LegMaker(aBins,"Leg Maker 2");
        Robot tailAttacher_1 = new TailAttacher(aBins,"Tail Attacher 1");
        Robot tailAttacher_2 = new TailAttacher(aBins,"Tail Attacher 2");
        Robot whiskerAttacher_1 = new WhiskerAttacher(aBins,"Whisker Attacher 1");
        Robot whiskerAttacher_2 = new WhiskerAttacher(aBins,"Whisker Attacher 2");
        Robot[] robots = {eyeAttacher_1, eyeAttacher_2, legAttacher_1, legAttacher_2, legMaker_1, legMaker_2,
                            tailAttacher_1, tailAttacher_2, whiskerAttacher_1, whiskerAttacher_2};

        Thread catMaker = new Thread(() -> {
            long idleTime = 0;
            long idleStart;
            long idleStop;
            long start = System.currentTimeMillis();
            while (aCats.size() < 250) {
                Body body;
                Head head;

                /* Get a completed body */
                idleStart = System.currentTimeMillis();
                synchronized (aBins.getBodyCompleted()) {
                    idleStop = System.currentTimeMillis();
                    idleTime += idleStop - idleStart;
                    while (aBins.getBodyCompleted().isEmpty()) {
                        try {
                            aBins.getBodyCompleted().wait();
                        } catch (InterruptedException ignored) {
                            return;
                        }
                    }
                    body = aBins.getBodyCompleted().pop();
                }

                /* Get a completed head */
                idleStart = System.currentTimeMillis();
                synchronized (aBins.getHeadCompleted()) {
                    idleStop = System.currentTimeMillis();
                    idleTime += idleStop - idleStart;
                    while (aBins.getHeadCompleted().isEmpty()) {
                        try {
                            aBins.getHeadCompleted().wait();
                        } catch (InterruptedException ignored) {
                            return;
                        }
                    }
                    head = aBins.getHeadCompleted().pop();
                }
                /* Create the cat out of the completed head and body */
                Cat cat = new Cat(head, body);
                aCats.add(cat);
                try {
                    /* Simulate assembly */
                    Thread.sleep(Util.randInt(10, 20));
                } catch (InterruptedException ignored) {
                    return;
                }
            }
            long stop = System.currentTimeMillis();
            float idlePercentage = ((float) idleTime / ((float) (stop - start))) * 100;
            System.out.println(Thread.currentThread().getName() + " idle proportion: " + idlePercentage);
            for (Robot robot: robots) {
                robot.interrupt();
                System.out.println(robot.getName() + " idle proportion: " + robot.getIdlePercentage());
            }
        }, "Cat Maker");

        for (Robot robot :
                robots) {
            robot.start();
        }

        catMaker.start();
    }
}
