package tk.digitoy.kittyheartcollecthd.activities;

import java.util.List;

import tk.digitoy.kittyheartcollecthd.activities.R;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;

public class MainMenu extends Activity {
	private LayoutParams paramPlay;
	LinearLayout layout;
	ImageButton soundsButton;
	ImageButton musicButton;
	ImageButton soundEffectButton;
	public static int dispWidth;
	public static int dispHeight;
	boolean isVisible;
	private boolean toGame;
	public static boolean effectsIsOn;
	public static boolean soundIsOn;
	// Kitty song
	public static MediaPlayer kittySong;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		dispHeight = metrics.heightPixels;
		dispWidth = metrics.widthPixels;

		playButtonInit();
		infoButtonInit();

		isVisible = false;
		effectsIsOn = true;
		soundIsOn = true;
		soundButtonLayout();

	}

	@Override
	protected void onResume() {
		super.onResume();
		playMusic();
	}

	private void playMusic() {
		if (kittySong == null) {
			kittySong = MediaPlayer.create(getBaseContext(), R.raw.main_music);
		}
		if (soundIsOn && !kittySong.isPlaying()) {
			kittySong.start();
		}
	}

	public void playButtonInit() {
		// Image Parameters
		paramPlay = new LayoutParams(dispWidth * 130 / 1280,
				dispHeight * 130 / 768);
		paramPlay.leftMargin = dispWidth * 892 / 1280;
		paramPlay.topMargin = dispHeight * 200 / 768;

		ImageButton playButton = (ImageButton) findViewById(R.id.play);
		playButton.setLayoutParams(paramPlay);

		playButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				toGame = true;
				Intent intent = new Intent(MainMenu.this,
						HeartCollectActivity.class);
				startActivity(intent);
			}
		});

	}

	public void infoButtonInit() {
		ImageButton infoButton = (ImageButton) findViewById(R.id.info_btn);
		infoButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				toGame = false;
				Intent intent = new Intent(MainMenu.this, InfoActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	public void soundButtonLayout() {
		layout = (LinearLayout) findViewById(R.id.layout_sounds);
		musicButton = (ImageButton) findViewById(R.id.sounds_music);
		soundEffectButton = (ImageButton) findViewById(R.id.sounds_effects);
		soundsButton = (ImageButton) findViewById(R.id.sounds);
		soundsButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (!isVisible) {
					layout.setVisibility(View.VISIBLE);
					layout.setAnimation(new TranslateAnimation(0, 0, 0, 10));
					// soundsButton.setVisibility(View.INVISIBLE);
					isVisible = true;
				} else {
					layout.setAnimation(new TranslateAnimation(0, 0, 0, -10));
					layout.setVisibility(View.GONE);
					isVisible = false;
				}

			}
		});

		soundEffectButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (effectsIsOn) {
					effectsIsOn = !effectsIsOn;
					soundEffectButton
							.setBackgroundResource(R.drawable.effects_off);
				} else {
					effectsIsOn = !effectsIsOn;
					soundEffectButton
							.setBackgroundResource(R.drawable.effects_on);
				}
			}
		});

		musicButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (kittySong.isPlaying()) {
					musicButton.setBackgroundResource(R.drawable.music_off);
					kittySong.pause();
					soundIsOn = false;
				} else {
					musicButton.setBackgroundResource(R.drawable.music_on);
					kittySong.start();
					soundIsOn = true;
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		if (kittySong != null) {
			kittySong.release();
			kittySong = null;
		}
		super.onBackPressed();
	}

	public static boolean isApplicationSentToBackground(final Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		if (!pm.isScreenOn()) {
			return true;
		}
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}

		return false;
	}

	@Override
	protected void onPause() {
		if (kittySong != null
				&& kittySong.isPlaying()
				&& (isApplicationSentToBackground(getApplicationContext()) || toGame)) {
			kittySong.pause();
		}
		super.onPause();
	}
}
