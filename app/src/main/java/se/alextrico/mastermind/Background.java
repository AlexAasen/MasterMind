package se.alextrico.mastermind;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;

/**
 * Created by Alextrico on 2016-08-11.
 */
public class Background {
    private RectF rect;

    //The background is represented by a Bitmap
    private Bitmap background;

    //How large is the background going to be?
    private float length;
    private float height;

    public Background(Context context, int screenX, int screenY){
        rect = new RectF();

        length = screenX;
        height = screenY;

    }

    public RectF getRect(){
        return rect;
    }

    //This is a getter method to make the rectangle that
    //defines our paddle available in BreakoutView class
    public Bitmap getBackgroundImage(){
        return background;
    }

    public void setBackgroundImage(Bitmap bitmap){
        this.background = bitmap;
    }

    public float getLength(){
        return length;
    }

    public float getHeight(){
        return height;
    }


}
