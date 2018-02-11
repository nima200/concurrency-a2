package q2.monitor.parts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Leg {

    private List<Toe> aToes;

    public Leg(Toe... pToes) {
        aToes = new ArrayList<>();
        aToes.addAll(Arrays.asList(pToes));
    }
}
