package tk.digitoy.kittyheartcollecthd.activities;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;

public class InfoActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_activity);
	}

	@Override
	protected void onResume() {
		super.onResume();
		playMusic();
	}

	private void playMusic() {
		if (MainMenu.kittySong == null) {
			MainMenu.kittySong = MediaPlayer.create(getBaseContext(),
					R.raw.main_music);
		}
		if (MainMenu.soundIsOn && !MainMenu.kittySong.isPlaying()) {
			MainMenu.kittySong.start();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.info_activity, menu);
		return true;
	}

	@Override
	protected void onPause() {
		if (MainMenu.kittySong != null
				&& MainMenu.kittySong.isPlaying()
				&& MainMenu
						.isApplicationSentToBackground(getApplicationContext())) {
			MainMenu.kittySong.pause();
		}
		super.onPause();
	}
}
