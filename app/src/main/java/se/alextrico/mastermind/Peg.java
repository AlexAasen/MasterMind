package se.alextrico.mastermind;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

/**
 * Created by Alextrico on 2016-08-14.
 */
public class Peg {
    private RectF rectF;
    private float x;
    private float y;
    private float length;
    private float height;
    private Color color;
    private Bitmap bitmap;

    public enum Color
    {
        BLACK, BLUE, PURPLE, GREEN, ORANGE, RED, WHITE, YELLOW, HIDDEN, ALMOST, CORRECT
    }

    public Peg(Context context, Color color, float xPos, float yPos){
        this.x = xPos;
        this.y = yPos;
        this.color = color;

        rectF = new RectF();

        switch (color){
            case BLACK:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.blackpeg);
                break;
            case BLUE:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bluepeg);
                break;
            case PURPLE:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.purplepeg);
                break;
            case GREEN:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.greenpeg);
                break;
            case ORANGE:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.orangepeg);
                break;
            case RED:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.redpeg);
                break;
            case WHITE:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.whitepeg);
                break;
            case YELLOW:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.yellowpeg);
                break;
            case HIDDEN:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.hiddenpeg);
                break;
            case ALMOST:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.whitepeg);
                break;
            case CORRECT:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.redpeg);
                break;
        }
    }

    public void update(){
        rectF.left = x;
        rectF.right = x + length;
        rectF.top = y;
        rectF.bottom = y + height;
    }

    public RectF getRectF() {
        return rectF;
    }

    public void setSizeSmallPeg(float pegBoardHeight){
        float diff = (pegBoardHeight/2)/bitmap.getHeight();
        float newWidth = bitmap.getWidth() * diff;
        this.length = newWidth;
        this.height = pegBoardHeight/2;
        bitmap = bitmap.createScaledBitmap(bitmap, (int) length, (int) height, false);
    }

    public void setSizeMediumPeg(float pegBoardHeight){
        float diff =pegBoardHeight/bitmap.getHeight();
        float newWidth = bitmap.getWidth() * diff;
        this.length = newWidth;
        this.height = pegBoardHeight;
        bitmap = bitmap.createScaledBitmap(bitmap, (int) length, (int) height, false);
    }

    public void setSizeLargePeg(float pegBoardHeight){
        float diff = pegBoardHeight/bitmap.getHeight();
        float newWidth = bitmap.getWidth() * diff;
        this.length = newWidth;
        this.height = pegBoardHeight;
        bitmap = bitmap.createScaledBitmap(bitmap, (int) length, (int) height, false);
    }

    public void setSizeExtraLargePeg(float pegBoardHeight){
        float diff = pegBoardHeight/bitmap.getHeight();
        float newWidth = bitmap.getWidth() * diff;
        this.length = newWidth * 2;
        this.height = pegBoardHeight * 2;
        bitmap = bitmap.createScaledBitmap(bitmap, (int) length, (int) height, false);
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getLength() {
        return length;
    }

    public float getHeight() {
        return height;
    }

    public Color getColor() {
        return color;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public int hashCode()
    {
        int result = 11;
        result += 37 * getColor().hashCode();

        return result;
    }

    @Override
    public boolean equals(Object other)
    {
        if (this == other)
        {
            return true;
        }
        if (other instanceof Peg)
        {
            Peg otherPeg = (Peg) other;
            return this.getColor().equals(otherPeg.getColor());
        }
        return false;
    }
}
