package se.alextrico.mastermind;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

public class MasterMindGame extends Activity {
    private MenuView menuView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get a Display object to access screen details
        Display display = getWindowManager().getDefaultDisplay();
        //Load the resolution into a point object
        Point size = new Point();
        display.getSize(size);

        menuView = new MenuView(this, size.x, size.y);
        if(menuView.playing) {
            setContentView(menuView);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tell the gameView resume method to execute
        if(!menuView.playing){
        }
        else{
            menuView.resume();
        }

    }

    // This method executes when the player quits the game
    @Override
    protected void onPause() {
        super.onPause();
        // Tell the gameView pause method to execute
        menuView.pause();
    }
}
