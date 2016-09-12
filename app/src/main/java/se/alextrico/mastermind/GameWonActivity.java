package se.alextrico.mastermind;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

/**
 * Created by Alextrico on 2016-08-19.
 */
public class GameWonActivity extends Activity {
    private GameWonView gameWonView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);

        gameWonView = new GameWonView(this, size.x, size.y);
        if(gameWonView.playing){
            setContentView(gameWonView);
        }

    }

    @Override
    protected void onResume(){
        super.onResume();
        if(!gameWonView.playing){
        }
        else{
            gameWonView.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Tell the gameView pause method to execute
        gameWonView.pause();
    }
}
