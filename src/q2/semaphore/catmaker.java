package q2.semaphore;

import q2.parts.Body;
import q2.parts.Cat;
import q2.parts.Head;
import q2.parts.Leg;
import q2.semaphore.robots.*;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class catmaker {
    private static final LinkedList<Head> heads_incomplete = new LinkedList<>();
    private static final LinkedList<Head> heads_complete = new LinkedList<>();
    private static final LinkedList<Body> bodies_incomplete = new LinkedList<>();
    private static final LinkedList<Body> bodies_complete = new LinkedList<>();
    private static final LinkedList<Leg> front_legs = new LinkedList<>();
    private static final LinkedList<Leg> hind_legs = new LinkedList<>();
    private static final LinkedList<Cat> cats = new LinkedList<>();

    private static final Semaphore heads_inc_binKey = new Semaphore(1);
    private static final Semaphore heads_com_binKey = new Semaphore(1);
    private static final Semaphore bodies_inc_binKey = new Semaphore(1);
    private static final Semaphore bodies_com_binKey = new Semaphore(1);
    private static final Semaphore fLegs_binKey = new Semaphore(1);
    private static final Semaphore hLegs_binKey = new Semaphore(1);

    private static final Semaphore head_inc_produced = new Semaphore(0);
    private static final Semaphore head_com_produced = new Semaphore(0);
    private static final Semaphore body_inc_produced = new Semaphore(0);
    private static final Semaphore body_com_produced = new Semaphore(0);
    private static final Semaphore hLeg_produced = new Semaphore(0);
    private static final Semaphore fLeg_produced = new Semaphore(0);

    public static void main(String[] args) {
        Thread legMaker_1 = new Thread(
                new LegMaker(front_legs, hind_legs, fLegs_binKey, fLeg_produced, hLegs_binKey, hLeg_produced),
                "Leg Maker 1");
        Thread legMaker_2 = new Thread(
                new LegMaker(front_legs, hind_legs, fLegs_binKey, fLeg_produced, hLegs_binKey, hLeg_produced),
                "Leg Maker 2");
        Thread tailAttacher_1 = new Thread(
                new TailAttacher(bodies_incomplete, bodies_inc_binKey, body_inc_produced),
                "Tail Attacher 1");
        Thread tailAttacher_2 = new Thread(
                new TailAttacher(bodies_incomplete, bodies_inc_binKey, body_inc_produced),
                "Tail Attacher 2");
        Thread legAttacher_1 = new Thread(
                new LegAttacher(bodies_incomplete, bodies_complete, front_legs, hind_legs, bodies_inc_binKey, bodies_com_binKey,
                        fLegs_binKey, hLegs_binKey, fLeg_produced, hLeg_produced, body_inc_produced, body_com_produced),
                "Leg Attacher 1");
        Thread legAttacher_2 = new Thread(
                new LegAttacher(bodies_incomplete, bodies_complete, front_legs, hind_legs, bodies_inc_binKey, bodies_com_binKey,
                        fLegs_binKey, hLegs_binKey, fLeg_produced, hLeg_produced, body_inc_produced, body_com_produced),
                "Leg Attacher 2");
        Thread eyeAttacher_1 = new Thread(
                new EyeAttacher(heads_incomplete, heads_inc_binKey, head_inc_produced),
                "Eye Attacher 1");
        Thread eyeAttacher_2 = new Thread(
                new EyeAttacher(heads_incomplete, heads_inc_binKey, head_inc_produced),
                "Eye Attacher 2");
        Thread whiskerAttacher_1 = new Thread(
                new WhiskerAttacher(heads_incomplete, heads_complete, heads_inc_binKey, heads_com_binKey, head_inc_produced, head_com_produced),
                "Whisker Attacher 1");
        Thread whiskerAttacher_2 = new Thread(
                new WhiskerAttacher(heads_incomplete, heads_complete, heads_inc_binKey, heads_com_binKey, head_inc_produced, head_com_produced),
                "Whisker Attacher 2");

        Thread[] robots = {legAttacher_1, legAttacher_2, legMaker_1, legMaker_2, tailAttacher_1, tailAttacher_2,
                eyeAttacher_1, eyeAttacher_2, whiskerAttacher_1, whiskerAttacher_2};

        Thread catMaker = new Thread(() -> {
            long idleTime = 0;
            long start;
            long stop;
            while (cats.size() < 250) {
                Body body;
                Head head;
                try {
                    /* Wait for a body to get produced */
                    start = System.currentTimeMillis();
                    body_com_produced.acquire();
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    /* Wait for access on the bin */
                    start = System.currentTimeMillis();
                    bodies_com_binKey.acquire();
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    /* Take the body */
                    body = bodies_complete.pop();
                    /* Let go of the bin */
                    bodies_com_binKey.release();
                    /* Wait for a completed head to get produced */
                    start = System.currentTimeMillis();
                    head_com_produced.acquire();
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    /* Wait for access to the bin */
                    start = System.currentTimeMillis();
                    heads_com_binKey.acquire();
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    /* Take the head */
                    head = heads_complete.pop();
                    /* Let go of the bin */
                    heads_com_binKey.release();
                    /* Create the cat */
                    Cat cat = new Cat(head, body);
                    cats.push(cat);
                } catch (InterruptedException ignored) {
                    System.out.println(Thread.currentThread().getName() + " idle time: " + idleTime);
                    return;
                }
            }
            for (Thread robot: robots) {
                robot.interrupt();
            }
            System.out.println(Thread.currentThread().getName() + " idle time: " + idleTime);
        }, "Cat Maker");
        catMaker.start();
        for (Thread robot: robots) {
            robot.start();
        }
    }
}
