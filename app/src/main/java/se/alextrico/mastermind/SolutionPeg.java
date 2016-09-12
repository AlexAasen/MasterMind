package se.alextrico.mastermind;

/**
 * Created by Alextrico on 2016-08-18.
 */
public class SolutionPeg {

    private Peg hiddenPeg;
    private Peg solutionPeg;

    public SolutionPeg(Peg hiddenPeg, Peg solutionPeg){
        this.hiddenPeg = hiddenPeg;
        this.solutionPeg = solutionPeg;
    }

    public Peg getHiddenPeg() {
        return hiddenPeg;
    }

    public Peg getSolutionPeg() {
        return solutionPeg;
    }

}
