package q2.parts;

public class Head {
    private Whisker[] aWhiskers;
    private Eye[] aEyes;

    public Head() {
        aWhiskers = new Whisker[6];
        aEyes = new Eye[2];
    }

    public void attachWhiskers(Whisker[] pWhiskers) {
        aWhiskers = pWhiskers;
    }

    public void attachEyes(Eye[] pEyes) {
        aEyes = pEyes;
    }
}
