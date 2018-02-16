package q2.monitor;

import q2.parts.*;

import java.util.LinkedList;

public class Bins {
    private final LinkedList<Toe> aToes = new LinkedList<>();
    private final LinkedList<Eye> aEyes = new LinkedList<>();
    private final LinkedList<Head> aHeads = new LinkedList<>();
    private final LinkedList<Leg> aLegs = new LinkedList<>();
    private final LinkedList<Tail> aTails = new LinkedList<>();
    private final LinkedList<Whisker> aWhiskers = new LinkedList<>();
    private final LinkedList<Body> aBodies = new LinkedList<>();

    private final LinkedList<Leg> aForeLegs = new LinkedList<>();
    private final LinkedList<Leg> aHindLegs = new LinkedList<>();

    private final LinkedList<Body> aBodyWithTail = new LinkedList<>();
    private final LinkedList<Body> aBodyWithLegs = new LinkedList<>();
    private final LinkedList<Body> aBodyCompleted = new LinkedList<>();

    private final LinkedList<Head> aHeadWithEyes = new LinkedList<>();
    private final LinkedList<Head> aHeadWithWhiskers = new LinkedList<>();
    private final LinkedList<Head> aHeadCompleted = new LinkedList<>();

    public LinkedList<Toe> getToes() {
        return aToes;
    }

    public LinkedList<Eye> getEyes() {
        return aEyes;
    }

    public LinkedList<Head> getHeads() {
        return aHeads;
    }

    public LinkedList<Leg> getLegs() {
        return aLegs;
    }

    public LinkedList<Tail> getTails() {
        return aTails;
    }

    public LinkedList<Whisker> getWhiskers() {
        return aWhiskers;
    }

    public LinkedList<Body> getBodies() {
        return aBodies;
    }

    public LinkedList<Leg> getForeLegs() {
        return aForeLegs;
    }

    public LinkedList<Leg> getHindLegs() {
        return aHindLegs;
    }

    public LinkedList<Body> getBodyWithTail() {
        return aBodyWithTail;
    }

    public LinkedList<Body> getBodyWithLegs() {
        return aBodyWithLegs;
    }

    public LinkedList<Body> getBodyCompleted() {
        return aBodyCompleted;
    }

    public LinkedList<Head> getHeadWithEyes() {
        return aHeadWithEyes;
    }

    public LinkedList<Head> getHeadWithWhiskers() {
        return aHeadWithWhiskers;
    }

    public LinkedList<Head> getHeadCompleted() {
        return aHeadCompleted;
    }
}
