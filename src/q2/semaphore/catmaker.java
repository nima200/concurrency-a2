package q2.semaphore;

import q2.parts.Body;
import q2.parts.Cat;
import q2.parts.Head;
import q2.parts.Leg;

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

    private static final Semaphore heads_inc_s = new Semaphore(1, true);
    private static final Semaphore heads_com_s = new Semaphore(1, true);
    private static final Semaphore bodies_inc_s = new Semaphore(1, true);
    private static final Semaphore bodies_com_s = new Semaphore(1, true);
    private static final Semaphore fLegs_s = new Semaphore(1, true);
    private static final Semaphore hLegs_s = new Semaphore(1, true);



}
