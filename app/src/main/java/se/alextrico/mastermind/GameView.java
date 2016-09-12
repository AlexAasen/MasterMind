package se.alextrico.mastermind;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

/**
 * Created by Alextrico on 2016-08-12.
 */
public class GameView extends SurfaceView implements Runnable {

    private Background base;
    private int screenX;
    private int screenY;
    private int intersectedEmptyPeg;
    private Context context;
    private Thread gameThread = null;
    private SurfaceHolder surfaceHolder;
    public static volatile boolean playing;
    private boolean gameOver;
    private boolean gameWon;
    private Canvas canvas;
    private Paint paint;
    private Bitmap background;

    private OneRow currentRow;
    private int rowNumber;

    private HashSet<Peg> tempAvailablePegs;
    private HashMap<Integer, Peg> tempPegs;
    private HashMap<Integer, Peg> evaluatedGuesses;
    private HashSet<Peg> onScreenPegSelect;
    private HashMap<Integer, OneRow> completeBoard;
    private HashMap<Integer, SolutionPeg> solution;
    private HashMap<Integer, Peg> processedGuesses;

    private RectF firstRect;
    private RectF secondRect;
    private RectF thirdRect;
    private RectF fourthRect;
    private PegBoard pegBoard;
    private PegBoardSmall pegBoardSmall;


    public GameView(Context context, int screenX, int screenY) {
        super(context);
        this.context = context;
        playing = true;

        surfaceHolder = getHolder();
        paint = new Paint();
        this.screenX = screenX;
        this.screenY = screenY;

        tempAvailablePegs = new HashSet<>();
        tempPegs = new HashMap<>();
        evaluatedGuesses = new HashMap<>();
        onScreenPegSelect = new HashSet<>();
        completeBoard = new HashMap<>();
        solution = new HashMap<>();
        processedGuesses = new HashMap<>();

        gameThread = new Thread(this);
        rowNumber = 9;

        prepareLevel();
        currentRow = completeBoard.get(rowNumber);
    }

    private void prepareLevel(){
        base = new Background(context, screenX, screenY);
        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.background2);
        background = background.createScaledBitmap(background, screenX, screenY, false);
        base.setBackgroundImage(background);
        gameOver = false;
        gameWon = false;

        placePegs();
        placePegBoards();
        placeAndAssignHiddenPegs();
    }

    private HashSet<Peg> createFullSetOfPegs(){
        HashSet<Peg> availablePegs = new HashSet<>();
        availablePegs.add(new Peg(context, Peg.Color.BLACK, 0, 0));
        availablePegs.add(new Peg(context, Peg.Color.PURPLE, 0, 0));
        availablePegs.add(new Peg(context, Peg.Color.BLUE, 0, 0));
        availablePegs.add(new Peg(context, Peg.Color.GREEN, 0, 0));
        availablePegs.add(new Peg(context, Peg.Color.RED, 0, 0));
        availablePegs.add(new Peg(context, Peg.Color.ORANGE, 0, 0));
        availablePegs.add(new Peg(context, Peg.Color.YELLOW, 0, 0));
        availablePegs.add(new Peg(context, Peg.Color.WHITE, 0, 0));
        return availablePegs;
    }

    private void placeAndAssignHiddenPegs(){
        float xPos = this.screenX/4;
        float yPos = 10;
        int i = 0;

        tempPegs= new HashMap<>();
        tempAvailablePegs = new HashSet<>(createFullSetOfPegs());
        solution = new HashMap<>();

        for (Peg peg : tempAvailablePegs) {
            tempPegs.put(i, peg);
            i++;
        }

        for(int y = 0; y < 4; y++) {
            Random generator = new Random();
            int randomIndex = generator.nextInt(i);
            Peg hiddenPeg = new Peg(context, Peg.Color.HIDDEN, screenX, screenY);
            Peg chosenPeg = new Peg(context, tempPegs.get(randomIndex).getColor(), tempPegs.get(randomIndex).getX(), tempPegs.get(randomIndex).getY());

            chosenPeg.setX(xPos);
            chosenPeg.setY(yPos);
            chosenPeg.setSizeExtraLargePeg(100);
            chosenPeg.update();

            hiddenPeg.setX(xPos);
            hiddenPeg.setY(yPos);
            hiddenPeg.setSizeExtraLargePeg(100);
            SolutionPeg solutionPeg = new SolutionPeg(hiddenPeg, chosenPeg);
            solution.put(y, solutionPeg);
            xPos += hiddenPeg.getLength();
        }
    }

    private void placePegBoards(){
        float yPos = (this.screenY/9)*2;
        float xPos = this.screenX/3;
        completeBoard = new HashMap<>();

        for (int i = 0; i < 10; i++){
            pegBoard = new PegBoard(context, screenY);
            pegBoardSmall = new PegBoardSmall(context, pegBoard.getHeight());
            pegBoard.setxPos(xPos);
            pegBoard.setyPos(yPos);
            pegBoard.update();

            pegBoardSmall.setxPos(xPos + pegBoard.getLength());
            pegBoardSmall.setyPos(yPos);
            pegBoardSmall.update();
            OneRow oneRow = new OneRow(context, pegBoard, pegBoardSmall);
            yPos += pegBoard.getHeight();
            completeBoard.put(i, oneRow);
            completeBoard.put(i, oneRow);
        }
    }

    private void placePegs(){
        float xPos = this.screenX/7;
        float yPos = (this.screenY/13)*3;
        PegBoard pegBoard = new PegBoard(context, screenY);
        onScreenPegSelect = new HashSet<>(createFullSetOfPegs());

        for(Peg peg: onScreenPegSelect){
            peg.setSizeLargePeg(pegBoard.getHeight());
            yPos = (yPos + peg.getHeight());
            peg.setX(xPos);
            peg.setY(yPos);
            peg.update();
        }
    }

    public void resume(){
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void pause(){
        playing = false;
        try {
            gameThread.join();
        }catch(InterruptedException e){
            Log.e("Error:", "joining thread");
        }
    }

    @Override
    public void run(){
        while(playing){
            draw();
        }
    }

    private void draw(){
//Make sure our drawing surface is valid
        if(surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();

            canvas.drawBitmap(base.getBackgroundImage(), 0, 0, paint);

            for (Peg peg : onScreenPegSelect) {
                canvas.drawBitmap(peg.getBitmap(), peg.getX(), peg.getY(), paint);
            }

            for (int i = 0; i < solution.size(); i++) {
                if (gameOver) {
                    Peg hiddenPeg = solution.get(i).getHiddenPeg();
                    Peg solutionPeg = solution.get(i).getSolutionPeg();
                    canvas.drawBitmap(hiddenPeg.getBitmap(), hiddenPeg.getX(), hiddenPeg.getY(), paint);
                    canvas.drawBitmap(solutionPeg.getBitmap(), solutionPeg.getX(), solutionPeg.getY(), paint);
                } else if (gameWon){
                    Peg hiddenPeg = solution.get(i).getHiddenPeg();
                    Peg solutionPeg = solution.get(i).getSolutionPeg();
                    canvas.drawBitmap(hiddenPeg.getBitmap(), hiddenPeg.getX(), hiddenPeg.getY(), paint);
                    canvas.drawBitmap(solutionPeg.getBitmap(), solutionPeg.getX(), solutionPeg.getY(), paint);
                }
                else{
                    Peg hiddenPeg = solution.get(i).getHiddenPeg();
                    Peg solutionPeg = solution.get(i).getSolutionPeg();
                    canvas.drawBitmap(hiddenPeg.getBitmap(), hiddenPeg.getX(), hiddenPeg.getY(), paint);
                }

            }

            int a = 50;
            for (int i = 0; i < completeBoard.size(); i++) {
                canvas.drawBitmap(completeBoard.get(i).getPegBoard().getBitmap(), completeBoard.get(i).getPegBoard().getxPos(), completeBoard.get(i).getPegBoard().getyPos(), paint);
                canvas.drawBitmap(completeBoard.get(i).getPegBoardSmall().getBitmap(), completeBoard.get(i).getPegBoardSmall().getxPos(), completeBoard.get(i).getPegBoardSmall().getyPos(), paint);

                    for (Map.Entry<Integer, Peg> entry : completeBoard.get(i).getPegBoard().getSelectedPegs().entrySet()) {
                        canvas.drawBitmap(entry.getValue().getBitmap(), completeBoard.get(i).getPegBoard().getQuarters().get(entry.getKey()).left,completeBoard.get(i).getPegBoard().getQuarters().get(entry.getKey()).top, paint);
                    }


                if (completeBoard.get(i).getRowSubmited()) {
                        for (Map.Entry<Integer, Peg> entry : completeBoard.get(i).getEvaluatedGuess().entrySet()) {
                            if(!entry.getValue().getColor().equals(Peg.Color.HIDDEN)){
                                canvas.drawBitmap(entry.getValue().getBitmap(),completeBoard.get(i).getPegBoardSmall().getSmallRects().get(entry.getKey()).left, completeBoard.get(i).getPegBoardSmall().getSmallRects().get(entry.getKey()).top, paint);
                                canvas.drawText("Evaluated pegs : X " + entry.getValue().getX() + "  Y: " + entry.getValue().getY(), 10, a, paint);
                                a = a + 50;
                            }
                        }
                        canvas.drawText("Selected pegs : " + completeBoard.get(i).evaluatedGuess.size(), 10, 50, paint);
                }
            }
            surfaceHolder.unlockCanvasAndPost(canvas);
            }

    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        switch(motionEvent.getAction() & MotionEvent.ACTION_MASK){
            //Player has touched the screen
            case MotionEvent.ACTION_DOWN:
                float xPos = motionEvent.getX();
                float yPos = motionEvent.getY();
                RectF rect = new RectF();
                rect.set(xPos, yPos, xPos+1, yPos+1);

                for(Peg peg:onScreenPegSelect){
                    if(RectF.intersects(peg.getRectF(), rect)){
                        currentRow.getPegBoard().addSelectedPeg(peg);
                    }
                }
                break;

            //Player has removed finger from screen
            case MotionEvent.ACTION_UP:

                float xPosRelease = motionEvent.getX();
                float yPosRelease = motionEvent.getY();
                RectF rect2 = new RectF();
                rect2.set(xPosRelease, yPosRelease, xPosRelease+1, yPosRelease+1);

                    if(currentRow.getPegBoard().getSelectedPegs().size() == 4) {
                        if (RectF.intersects(currentRow.getPegBoardSmall().getRectF(), rect2)) {
                            currentRow.setRowSubmited(true);
                            evaluateGuess();
                            break;
                        }
                    }
                    else {
                        if (RectF.intersects(currentRow.getPegBoard().getRectF(), rect2)) {

                            RectF rectF1 = new RectF();
                            RectF rectF2 = new RectF();
                            RectF rectF3 = new RectF();
                            RectF rectF4 = new RectF();
                            rectF1.set(currentRow.getPegBoard().getxPos(), currentRow.getPegBoard().getyPos(), (currentRow.getPegBoard().getxPos() + (currentRow.getPegBoard().getLength() / 4)), (currentRow.getPegBoard().getyPos() + currentRow.getPegBoard().getHeight()));
                            rectF2.set((currentRow.getPegBoard().getxPos() + (currentRow.getPegBoard().getLength() / 4)), currentRow.getPegBoard().getyPos(), (currentRow.getPegBoard().getxPos() + ((currentRow.getPegBoard().getLength() / 4) * 2)), (currentRow.getPegBoard().getyPos() + currentRow.getPegBoard().getHeight()));
                            rectF3.set((currentRow.getPegBoard().getxPos() + ((currentRow.getPegBoard().getLength() / 4) * 2)), currentRow.getPegBoard().getyPos(), (currentRow.getPegBoard().getxPos() + ((currentRow.getPegBoard().getLength() / 4) * 3)), (currentRow.getPegBoard().getyPos() + currentRow.getPegBoard().getHeight()));
                            rectF4.set((currentRow.getPegBoard().getxPos() + ((currentRow.getPegBoard().getLength() / 4) * 3)), currentRow.getPegBoard().getyPos(), (currentRow.getPegBoard().getxPos() + ((currentRow.getPegBoard().getLength() / 4) * 4)), (currentRow.getPegBoard().getyPos() + currentRow.getPegBoard().getHeight()));

                            currentRow.getPegBoard().addToQuarters(0, rectF1);
                            currentRow.getPegBoard().addToQuarters(1, rectF2);
                            currentRow.getPegBoard().addToQuarters(2, rectF3);
                            currentRow.getPegBoard().addToQuarters(3, rectF4);

                            if (RectF.intersects(rectF1, rect2)) {
                                intersectedEmptyPeg = 0;
                            } else if (RectF.intersects(rectF2, rect2)) {
                                intersectedEmptyPeg = 1;
                            } else if (RectF.intersects(rectF3, rect2)) {
                                intersectedEmptyPeg = 2;
                            } else if (RectF.intersects(rectF4, rect2)) {
                                intersectedEmptyPeg = 3;
                            }
                            currentRow.getPegBoard().addIntersected(intersectedEmptyPeg);
                        }
                        else {
                            currentRow.getPegBoard().setPeg(null);
                        }
                    }
                break;
            }
        return true;
    }

    public void setGameThread(Thread gameThread) {
        this.gameThread = gameThread;
    }

    public Thread getGameThread() {
        return gameThread;
    }

    public void evaluateGuess(){
        evaluatedGuesses = new HashMap<>();
        processedGuesses = new HashMap<>();

        firstRect = new RectF();
        secondRect = new RectF();
        thirdRect =  new RectF();
        fourthRect =  new RectF();

        firstRect.set((currentRow.getPegBoardSmall().getxPos() - 10) + (currentRow.getPegBoardSmall().getLength()/4), currentRow.getPegBoardSmall().getyPos(), 50, 50);
        secondRect.set(currentRow.getPegBoardSmall().getxPos() + (currentRow.getPegBoardSmall().getLength()/2), currentRow.getPegBoardSmall().getyPos(), 50, 50);
        thirdRect.set((currentRow.getPegBoardSmall().getxPos()-10) + (currentRow.getPegBoardSmall().getLength()/4), currentRow.getPegBoardSmall().getyPos() + (currentRow.getPegBoardSmall().getHeight()/2), 50, 50);
        fourthRect.set(currentRow.getPegBoardSmall().getxPos() + (currentRow.getPegBoardSmall().getLength()/2), currentRow.getPegBoardSmall().getyPos() + (currentRow.getPegBoardSmall().getHeight()/2), 50, 50);
        currentRow.getPegBoardSmall().addToSmallRects(0, firstRect);
        currentRow.getPegBoardSmall().addToSmallRects(1, secondRect);
        currentRow.getPegBoardSmall().addToSmallRects(2, thirdRect);
        currentRow.getPegBoardSmall().addToSmallRects(3, fourthRect);

            for(int i = 0; i < 4; i++) {
                if(currentRow.getPegBoard().getSelectedPegs().containsValue(solution.get(i).getSolutionPeg())){
                    for(Map.Entry<Integer, Peg> entry : currentRow.getPegBoard().getSelectedPegs().entrySet()){
                        if(entry.getValue().getColor().equals(solution.get(i).getSolutionPeg().getColor())){
                            int placement = entry.getKey();
                            if(placement == i) {
                                Peg peg = new Peg(context, Peg.Color.CORRECT, currentRow.getPegBoardSmall().getSmallRects().get(i).left, currentRow.getPegBoardSmall().getSmallRects().get(i).top);
                                peg.setSizeSmallPeg(currentRow.getPegBoard().getHeight());
                                evaluatedGuesses.put(i, peg);
                                processedGuesses.put(i, entry.getValue());
                            }
                            else{
                                if(processedGuesses.containsKey(i)){
                                }
                                else{
                                    Peg peg = new Peg(context, Peg.Color.ALMOST, currentRow.getPegBoardSmall().getSmallRects().get(i).left, currentRow.getPegBoardSmall().getSmallRects().get(i).top);
                                    peg.setSizeSmallPeg(currentRow.getPegBoard().getHeight());
                                    evaluatedGuesses.put(i, peg);
                                    processedGuesses.put(i, peg);
                                }
                            }
                        }
                    }
                }
                else
                {
                    evaluatedGuesses.put(i, new Peg(context, Peg.Color.HIDDEN, screenX, screenY));
                }
            }
        hasPlayerWon();
        currentRow.addEvaluatedGuesses(evaluatedGuesses);
        completeBoard.put(rowNumber, currentRow);
        if(rowNumber == 0){
            gameOver();
        }
        else {
            rowNumber = rowNumber - 1;
            currentRow = completeBoard.get(rowNumber);
        }
    }

    private void hasPlayerWon(){
        int correctGuesses = 0;
        for(Map.Entry<Integer, Peg> guess : evaluatedGuesses.entrySet()){
            if(guess.getValue().getColor().equals(Peg.Color.CORRECT)){
                correctGuesses++;
            }
        }
        if(correctGuesses == 4){
            gameWon = true;
            playing = false;
            context.startActivity(new Intent(context, GameWonActivity.class));
        }
    }

    private void gameOver() {
        playing = false;
        context.startActivity(new Intent(context, GameOverActivity.class));
    }
}
