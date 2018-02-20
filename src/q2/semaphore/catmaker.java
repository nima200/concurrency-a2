package q2.semaphore;

import q2.Robot;
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
        Robot eyeAttacher_1 = new EyeAttacher(aBins, "Eye Attacher 1");
        Robot eyeAttacher_2 = new EyeAttacher(aBins, "Eye Attacher 2");
        Robot legAttacher_1 = new LegAttacher(aBins, "Leg Attacher 1");
        Robot legAttacher_2 = new LegAttacher(aBins, "Leg Attacher 2");
        Robot legMaker_1 = new LegMaker(aBins, "Leg Maker 1");
        Robot legMaker_2 = new LegMaker(aBins, "Leg Maker 2");
        Robot tailAttacher_1 = new TailAttacher(aBins, "Tail Attacher 1");
        Robot tailAttacher_2 = new TailAttacher(aBins, "Tail Attacher 2");
        Robot whiskerAttacher_1 = new WhiskerAttacher(aBins, "Whisker Attacher 1");
        Robot whiskerAttacher_2 = new WhiskerAttacher(aBins, "Whisker Attacher 2");

        Robot[] robots = {eyeAttacher_1, eyeAttacher_2, legAttacher_1, legAttacher_2, legMaker_1, legMaker_2,
                        tailAttacher_1, tailAttacher_2, whiskerAttacher_1, whiskerAttacher_2};

        Thread catMaker = new Thread(() -> {
            long idleStart;
            long idleStop;
            long idleTime = 0;
            long start = System.currentTimeMillis();
            while (aCats.size() < 250) {
                try {
                    Body body;
                    Head head;
                    /* Wait for a completed body */
                    idleStart = System.currentTimeMillis();
                    aBins.getBodyCompleted().consume();
                    idleStop = System.currentTimeMillis();
                    idleTime += idleStop - idleStart;
                    /* Take the completed body */
                    idleStart = System.currentTimeMillis();
                    aBins.getBodyCompleted().getAccess();
                    idleStop = System.currentTimeMillis();
                    idleTime += idleStop - idleStart;
                    body = aBins.getBodyCompleted().pop();
                    aBins.getBodyCompleted().releaseAccess();

                    /* Wait for a completed head */
                    idleStart = System.currentTimeMillis();
                    aBins.getHeadCompleted().consume();
                    idleStop = System.currentTimeMillis();
                    idleTime += idleStop - idleStart;
                    /* Take the completed head */
                    idleStart = System.currentTimeMillis();
                    aBins.getHeadCompleted().getAccess();
                    idleStop = System.currentTimeMillis();
                    idleTime += idleStop - idleStart;
                    head = aBins.getHeadCompleted().pop();
                    aBins.getHeadCompleted().releaseAccess();

                    /* Create a cat out of the completed head and body */
                    Cat cat = new Cat(head, body);
                    aCats.add(cat);
                    /* Simulate Assembly*/
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

        for (Thread robot: robots) {
            robot.start();
        }

        catMaker.start();
    }
}
