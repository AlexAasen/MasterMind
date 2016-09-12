package se.alextrico.mastermind;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import java.util.HashMap;

/**
 * Created by Alextrico on 2016-08-14.
 */
public class PegBoard {
    private RectF rectF;
    private Context context;
    private  float xPos;
    private float yPos;
    private float length;
    private float height;
    private Bitmap bitmap;
    private HashMap<Integer, Peg> selectedPegs;
    private HashMap<Integer, RectF> quarters;
    private Peg peg;

    public PegBoard(Context context, float screenHeight){
        this.context = context;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pegboard);
        this.height = screenHeight/16;
        float diff = this.height/bitmap.getHeight();
        this.length = diff * bitmap.getWidth();
        bitmap = bitmap.createScaledBitmap(bitmap, (int) length, (int) height, false);
        rectF = new RectF();
        selectedPegs = new HashMap<>();
        quarters = new HashMap<>();
    }

    public void update(){
        rectF.left = xPos;
        rectF.right = xPos + length;
        rectF.top = yPos;
        rectF.bottom = yPos + height;
    }

    public void addIntersected(Integer intersectedEmptyPeg){
        selectedPegs.put(intersectedEmptyPeg, peg);
    }

    public HashMap<Integer, RectF> getQuarters() {
        return quarters;
    }

    public void addToQuarters(int integer, RectF rectF){
        quarters.put(integer, rectF);
    }

    public void setPeg(Peg peg) {
        this.peg = peg;
    }

    public void addSelectedPeg(Peg intersectedPeg){
        peg = intersectedPeg;
    }

    public HashMap<Integer, Peg> getSelectedPegs() {
        return selectedPegs;
    }

    public RectF getRectF() {
        return rectF;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public void setxPos(float xPos) {
        this.xPos = xPos;
    }

    public void setyPos(float yPos) {
        this.yPos = yPos;
    }

    public Context getContext() {
        return context;
    }

    public float getHeight() {
        return height;
    }

    public float getLength() {
        return length;
    }

    public float getxPos() {
        return xPos;
    }

    public float getyPos() {
        return yPos;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
