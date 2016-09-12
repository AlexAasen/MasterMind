package se.alextrico.mastermind;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Alextrico on 2016-08-11.
 */
public class MenuView extends SurfaceView implements Runnable {

    private Thread menuThread;
    private Context context;
    private SurfaceHolder surfaceHolder;
    public volatile boolean playing;
    private Canvas canvas;
    private Paint paint;
    private int screenX;
    private int screenY;
    private Background background;
    private Bitmap clickToSTart;

    public MenuView(Context context, int x, int y)
    {
        super(context);
        this.context = context;
        surfaceHolder = getHolder();
        paint = new Paint();
        playing = true;

        screenX = x;
        screenY = y;

        prepareMenu();
    }

    public void prepareMenu(){
        //Here we set up the menu
        background = new Background(context, screenX, screenY);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.startscreen);
        bitmap = bitmap.createScaledBitmap(bitmap, (int)background.getLength(), (int) background.getHeight(), false);
        background.setBackgroundImage(bitmap);
        clickToSTart = BitmapFactory.decodeResource(context.getResources(), R.drawable.clicktostart);
        clickToSTart = clickToSTart.createScaledBitmap(clickToSTart, screenX, screenY/12, false);
    }

    @Override
    public void run(){
        while(playing){
            draw();
        }
    }

    private void draw(){
        //Make sure our drawing surface is valid
        if(surfaceHolder.getSurface().isValid()){
            canvas = surfaceHolder.lockCanvas();

            canvas.drawBitmap(background.getBackgroundImage(), 0, 0, paint);
            canvas.drawBitmap(clickToSTart, screenX/25, screenY - (clickToSTart.getHeight()*2), paint);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause(){
        playing = false;
        try {
            menuThread.join();
        }catch(InterruptedException e){
            Log.e("Error:", "joining thread");
        }
    }

    public void resume(){
        playing = true;
        menuThread = new Thread(this);
        menuThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        switch(motionEvent.getAction() & MotionEvent.ACTION_MASK){
            //Player has touched the screen
            case MotionEvent.ACTION_DOWN:
                playing = false;
                context.startActivity(new Intent(context, GameActivity.class));

                break;

            //Player has removed finger from screen
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }
}
