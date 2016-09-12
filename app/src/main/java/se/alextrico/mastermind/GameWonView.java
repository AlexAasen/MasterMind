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
 * Created by Alextrico on 2016-08-19.
 */
public class GameWonView extends SurfaceView implements Runnable {

    private Thread gameWonThread;
    private Context context;
    private SurfaceHolder surfaceHolder;
    public volatile boolean playing;
    private Canvas canvas;
    private Paint paint;
    private int screenX;
    private int screenY;
    private Background background;
    private Bitmap youWon;

    public GameWonView(Context context, int screenX, int screenY){
        super(context);
        this.context = context;
        surfaceHolder = getHolder();
        paint = new Paint();
        playing = true;

        this.screenX = screenX;
        this.screenY = screenY;

        prepareWonScreen();
    }

    public void prepareWonScreen(){
        background = new Background(context, screenX, screenY);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.background2);
        bitmap = bitmap.createScaledBitmap(bitmap, screenX, screenY, false);
        background.setBackgroundImage(bitmap);

        youWon = BitmapFactory.decodeResource(context.getResources(), R.drawable.youwon);
        youWon = youWon.createScaledBitmap(youWon, screenX, screenY/8, false);
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
            canvas.drawBitmap(youWon, screenX/25, (screenY/2) - youWon.getHeight(), paint);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause(){
        playing = false;
        try {
            gameWonThread.join();
        }catch(InterruptedException e){
            Log.e("Error:", "joining thread");
        }
    }

    public void resume(){
        playing = true;
        gameWonThread = new Thread(this);
        gameWonThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        switch(motionEvent.getAction() & MotionEvent.ACTION_MASK){
            //Player has touched the screen
            case MotionEvent.ACTION_DOWN:
                playing = false;
                context.startActivity(new Intent(context, MasterMindGame.class));
                break;

            //Player has removed finger from screen
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

}
