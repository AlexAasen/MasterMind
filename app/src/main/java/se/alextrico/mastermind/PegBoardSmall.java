package se.alextrico.mastermind;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import java.util.HashMap;

/**
 * Created by Alextrico on 2016-08-14.
 */
public class PegBoardSmall {
    private RectF rectF;
    private  Context context;
    private float xPos;
    private float yPos;
    private float length;
    private float height;
    private Bitmap bitmap;
    private Bitmap almost;
    private Bitmap correct;
    private HashMap<Integer, RectF> smallRects;

    public PegBoardSmall(Context context, float pegBoardHeight){
        this.context = context;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pegboardsmall);
        almost = BitmapFactory.decodeResource(context.getResources(), R.drawable.redpeg);
        correct = BitmapFactory.decodeResource(context.getResources(), R.drawable.whitepeg);
        float diff = pegBoardHeight/bitmap.getHeight();
        this.height = pegBoardHeight;
        this.length = bitmap.getWidth() * diff;

        bitmap = bitmap.createScaledBitmap(bitmap, (int) length, (int) height, false);
        rectF = new RectF();
        smallRects = new HashMap<>();
    }

    public void update(){
        rectF.left = xPos;
        rectF.right = xPos + length;
        rectF.top = yPos;
        rectF.bottom = yPos + height;
    }


    public RectF getRectF() {
        return rectF;
    }

    public void setSmallRects(HashMap<Integer, RectF> smallRects) {
        this.smallRects = smallRects;
    }

    public HashMap<Integer, RectF> getSmallRects() {
        return smallRects;
    }

    public void addToSmallRects(int integer, RectF rectF)
    {
        smallRects.put(integer, rectF);
    }

    public Bitmap getAlmost() {
        return almost;
    }

    public Bitmap getCorrect() {
        return correct;
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

    public Bitmap getBitmap() {
        return bitmap;
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

}
