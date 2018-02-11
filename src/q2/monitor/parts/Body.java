package q2.monitor.parts;

public class Body {
    private Tail aTail;
    private Leg[] aForelegs;
    private Leg[] aHindlegs;

    public void attachTail(Tail pTail) {
        aTail = pTail;
    }

    public void attachForeLegs(Leg[] pForelegs) {
        aForelegs = pForelegs;
    }

    public void attachHindLegs(Leg[] pHindlegs) {
        aHindlegs = pHindlegs;
    }
}
