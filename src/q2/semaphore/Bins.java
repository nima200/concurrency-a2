package q2.semaphore;

import q2.parts.*;

public class Bins {

    private final BasicBin<Body> aBodyBin = new BasicBin<>(1);
    private final BasicBin<Eye> aEyeBin = new BasicBin<>(1);
    private final BasicBin<Head> aHeadBin = new BasicBin<>(1);
    private final BasicBin<Leg> aLegBin = new BasicBin<>(1);
    private final BasicBin<Tail> aTailBin = new BasicBin<>(1);
    private final BasicBin<Toe> aToeBin = new BasicBin<>(1);
    private final BasicBin<Whisker> aWhiskerBin = new BasicBin<>(1);

    private final ProductBin<Body> aBodyWithLegs = new ProductBin<>(1);
    private final ProductBin<Body> aBodyWithTail = new ProductBin<>(1);
    private final ProductBin<Body> aBodyCompleted = new ProductBin<>(1);

    private final ProductBin<Head> aHeadWithEyes = new ProductBin<>(1);
    private final ProductBin<Head> aHeadWithWhiskers = new ProductBin<>(1);
    private final ProductBin<Head> aHeadCompleted = new ProductBin<>(1);

    private final ProductBin<Leg> aForeLegs = new ProductBin<>(1);
    private final ProductBin<Leg> aHindLegs = new ProductBin<>(1);

    public BasicBin<Body> getBodyBin() {
        return aBodyBin;
    }

    public BasicBin<Eye> getEyeBin() {
        return aEyeBin;
    }

    public BasicBin<Head> getHeadBin() {
        return aHeadBin;
    }

    public BasicBin<Leg> getLegBin() {
        return aLegBin;
    }

    public BasicBin<Tail> getTailBin() {
        return aTailBin;
    }

    public BasicBin<Toe> getToeBin() {
        return aToeBin;
    }

    public BasicBin<Whisker> getWhiskerBin() {
        return aWhiskerBin;
    }

    public ProductBin<Body> getBodyWithLegs() {
        return aBodyWithLegs;
    }

    public ProductBin<Body> getBodyWithTail() {
        return aBodyWithTail;
    }

    public ProductBin<Body> getBodyCompleted() {
        return aBodyCompleted;
    }

    public ProductBin<Head> getHeadWithEyes() {
        return aHeadWithEyes;
    }

    public ProductBin<Head> getHeadWithWhiskers() {
        return aHeadWithWhiskers;
    }

    public ProductBin<Head> getHeadCompleted() {
        return aHeadCompleted;
    }

    public ProductBin<Leg> getForeLegs() {
        return aForeLegs;
    }

    public ProductBin<Leg> getHindLegs() {
        return aHindLegs;
    }
}
