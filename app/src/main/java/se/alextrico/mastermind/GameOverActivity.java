package se.alextrico.mastermind;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

/**
 * Created by Alextrico on 2016-09-12.
 */
public class GameOverActivity extends Activity {
    private GameOverView gameOverView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);

        gameOverView = new GameOverView(this, size.x, size.y);
        if(gameOverView.playing){
            setContentView(gameOverView);
        }

    }

    @Override
    protected void onResume(){
        super.onResume();
        if(!gameOverView.playing){
        }
        else{
            gameOverView.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Tell the gameView pause method to execute
        gameOverView.pause();
    }
}
