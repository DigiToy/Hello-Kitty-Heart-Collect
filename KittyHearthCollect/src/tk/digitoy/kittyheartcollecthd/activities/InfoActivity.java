package tk.digitoy.kittyheartcollecthd.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class InfoActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_activity);
        playMusic();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info_activity, menu);
        return true;
        
        
    }
    private void playMusic() {
		if (MainMenu.soundIsOn) {
			MainMenu.kittySong.start();
		}
	}
    
    @Override
	protected void onResume() {
		super.onResume();
		playMusic();
	}
    
    @Override
	public void onBackPressed() {
		if (MainMenu.kittySong != null) {
			MainMenu.kittySong.release();
			MainMenu.kittySong = null;
		}
		super.onBackPressed();
	}
    
    @Override
	protected void onPause() {
		if (MainMenu.kittySong != null && MainMenu.kittySong.isPlaying()) {
			MainMenu.kittySong.pause();
		}
		super.onPause();
	}
}
