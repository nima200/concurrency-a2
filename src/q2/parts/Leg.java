package q2.parts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Leg {

    private List<Toe> aToes;

    public Leg() {
        aToes = new ArrayList<>();
    }

    public void addToes(Toe[] pToes) {
        aToes.addAll(Arrays.asList(pToes));
    }

}
