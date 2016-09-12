package se.alextrico.mastermind;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by Alextrico on 2016-08-17.
 */
public class OneRow {
    private Context context;
    private PegBoard pegBoard;
    private PegBoardSmall pegBoardSmall;
    private boolean rowSubmited;
    public HashMap<Integer, Peg> evaluatedGuess;

    public OneRow(Context context, PegBoard pegBoard, PegBoardSmall pegBoardSmall){
        this.context = context;
        this.pegBoard = pegBoard;
        this.pegBoardSmall = pegBoardSmall;
        rowSubmited = false;
        evaluatedGuess = new HashMap<>();
    }

    public void addEvaluatedGuesses(HashMap<Integer, Peg> evaluatedGuesses){
        evaluatedGuess = new HashMap<>(evaluatedGuesses);
    }

    public HashMap<Integer, Peg> getEvaluatedGuess() {
        return evaluatedGuess;
    }

    public OneRow(Context context){
        this.context = context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setRowSubmited(boolean rowSubmited) {
        this.rowSubmited = rowSubmited;
    }

    public boolean getRowSubmited(){
        return rowSubmited;
    }

    public void setPegBoard(PegBoard pegBoard) {
        this.pegBoard = pegBoard;
    }

    public void setPegBoardSmall(PegBoardSmall pegBoardSmall) {
        this.pegBoardSmall = pegBoardSmall;
    }

    public Context getContext() {
        return context;
    }

    public PegBoard getPegBoard() {
        return pegBoard;
    }

    public PegBoardSmall getPegBoardSmall() {
        return pegBoardSmall;
    }


}

