package se.alextrico.mastermind;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

/**
 * Created by Alextrico on 2016-08-16.
 */
public class GameActivity extends Activity {
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get a Display object to access screen details
        Display display = getWindowManager().getDefaultDisplay();
        //Load the resolution into a point object
        Point size = new Point();
        display.getSize(size);

        gameView = new GameView(this, size.x, size.y);
        if(gameView.playing) {
            setContentView(gameView);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tell the gameView resume method to execute
        if(!gameView.playing){
        }
        else{
            gameView.resume();
        }

    }

    // This method executes when the player quits the game
    @Override
    protected void onPause() {
        super.onPause();
        // Tell the gameView pause method to execute
        gameView.pause();
    }
}
