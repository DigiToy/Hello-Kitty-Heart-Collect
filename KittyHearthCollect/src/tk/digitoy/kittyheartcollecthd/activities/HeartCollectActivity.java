package tk.digitoy.kittyheartcollecthd.activities;

import java.util.ArrayList;

import tk.digitoy.kittyheartcollecthd.activities.R;
import tk.digitoy.kittyheartcollecthd.activities.HeartCollectActivity.GameView.GameFigure;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class HeartCollectActivity extends Activity {

	RelativeLayout collecteggs_rl;
	private TextView countTextView;

	public MediaPlayer gameSong;
	public static MediaPlayer tapSound;
	int height;
	int width;
	GameView myView;
	ImageView imgAnim;
	ImageButton topLeftButton;
	ImageButton topRightButton;
	ImageButton downLeftButton;
	ImageButton downRightButton;
	ImageButton pauseButton;
	ImageButton continuePlayButton;
	ImageButton replayButton;
	LinearLayout layoutPause;
	boolean isVisible;
	ImageView kittyBasket;
	ImageView kittyBasketLeft;
	ImageView kittyLife1;
	ImageView kittyLife2;
	ImageView kittyLife3;
	int kittyBasketPosition;
	TranslateAnimation ta;
	int startPointLeftTop;
	int endPointLeftTop;
	int startPointLeftDown;
	int endPointLeftDown;
	int startPointRightTop;
	int endPointRightTop;
	int startPointRightDown;
	int endPointRightDown;
	int i = 0;
	int dropCount = 0;
	LayoutParams lp;
	ArrayList<GameFigure> figuresArray;
	CountDownTimer countDownTimer;
	Runnable timerRun;
	Handler myHandler;
	Dialog alertDialog;
	int timePeriod;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eggs_collect);

		topLeftButton = (ImageButton) findViewById(R.id.left_top_btn);
		topRightButton = (ImageButton) findViewById(R.id.right_top_btn);
		downLeftButton = (ImageButton) findViewById(R.id.left_dawn_btn);
		downRightButton = (ImageButton) findViewById(R.id.right_dawn_btn);

		kittyBasketLeft = (ImageView) findViewById(R.id.kitty_left_img);
		kittyLife1 = (ImageView) findViewById(R.id.life_icon_img1);
		kittyLife2 = (ImageView) findViewById(R.id.life_icon_img2);
		kittyLife3 = (ImageView) findViewById(R.id.life_icon_img3);
		gameSong = MediaPlayer.create(getBaseContext(), R.raw.game_music);
		setListeners();
		collecteggs_rl = (RelativeLayout) findViewById(R.id.activity_eggs_collect);
		countTextView = (TextView) findViewById(R.id.count_of_images_txt);
		countTextView.setTextColor(Color.BLACK);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		height = metrics.heightPixels;
		width = metrics.widthPixels;
		countTextView.setTextSize(height / 20);
		myView = new GameView(this, height, width);
		imgAnim = new ImageView(getBaseContext());
		collecteggs_rl.addView(myView);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

		myHandler = new Handler();

		pauseButtonLayout();

		playTapSound();
		runOnUiThread(timerRun);
	}

	@Override
	protected void onResume() {
		playMusic();
		super.onResume();

	}

	public void pauseButtonLayout() {
		pauseButton = (ImageButton) findViewById(R.id.pause_img);
		layoutPause = (LinearLayout) findViewById(R.id.pause_layout);
		continuePlayButton = (ImageButton) findViewById(R.id.play_img);
		replayButton = (ImageButton) findViewById(R.id.replay_img);

		pauseButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (!isVisible) {
					layoutPause.setVisibility(View.VISIBLE);
					layoutPause
							.setAnimation(new TranslateAnimation(0, 0, 0, 10));

					isVisible = true;
				} 
//				else {
//					layoutPause.setAnimation(new TranslateAnimation(0, 0, 0,
//							-10));
//					layoutPause.setVisibility(View.GONE);
//					isVisible = false;
//					for (int i = 0; i < figuresArray.size(); i++) {
//						figuresArray.get(i).continueRun();
//
//					}
//					runOnUiThread(timerRun);
//				}
				for (int i = 0; i < figuresArray.size(); i++) {
					figuresArray.get(i).pause();

				}
				myHandler.removeCallbacks(timerRun);
				pauseButton.setVisibility(View.INVISIBLE);
			}

		});

		continuePlayButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				
				layoutPause.setAnimation(new TranslateAnimation(0, 0, 0, -10));
				layoutPause.setVisibility(View.INVISIBLE);
				isVisible = false;

				for (int i = 0; i < figuresArray.size(); i++) {
					figuresArray.get(i).continueRun();

				}
				runOnUiThread(timerRun);
				pauseButton.setVisibility(View.VISIBLE);
			}
		});
		
		replayButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				closeAllThreads();
				
				Intent intent = new Intent(HeartCollectActivity.this,
						HeartCollectActivity.class);
				startActivity(intent);
				finish();
			}
		});

	}

	public void playTapSound() {
		tapSound = MediaPlayer.create(getBaseContext(), R.raw.tap_sound);
		tapSound.start();
	}

	public void playMusic() {
		if (MainMenu.soundIsOn) {
			gameSong.setLooping(true);
			gameSong.start();
		}
	}

	private void setListeners() {
		this.topLeftButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				kittyBasketPosition = 1;
				kittyBasketLeft.setBackgroundResource(R.drawable.kitty_left);

				lp.topMargin = height / 8 + height / 76;
				lp.leftMargin = width / 4;
				kittyBasketLeft.setLayoutParams(lp);

			}
		});

		this.topRightButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				kittyBasketPosition = 2;
				kittyBasketLeft.setBackgroundResource(R.drawable.kitty_right);

				lp.topMargin = height / 8 + height / 76;
				lp.leftMargin = 3 * width / 4 - kittyBasketLeft.getWidth();
				kittyBasketLeft.setLayoutParams(lp);

			}
		});

		this.downLeftButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				kittyBasketPosition = 3;

				kittyBasketLeft.setBackgroundResource(R.drawable.kitty_left);

				lp.topMargin = 3 * height / 8 + height / 15;
				lp.leftMargin = width / 4;
				kittyBasketLeft.setLayoutParams(lp);

			}
		});

		this.downRightButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				kittyBasketPosition = 4;
				kittyBasketLeft.setBackgroundResource(R.drawable.kitty_right);

				lp.topMargin = 3 * height / 8 + height / 15;
				lp.leftMargin = 3 * width / 4 - kittyBasketLeft.getWidth();
				kittyBasketLeft.setLayoutParams(lp);

			}
		});

	}

	@SuppressLint("DrawAllocation")
	public class GameView extends View {

		int height;
		int width;

		private int cutchImages;
		PathMeasure pm;
		ArrayList<Point> aCoordinates;

		Point currentPoint;
		Point topLeftPointStart;
		Point topRightPointStart;
		Point downLeftPointStart;
		Point downRightPointStart;
		Point topLeftPointTurn;
		Point topRightPointTurn;
		Point downLeftPointTurn;
		Point downRightPointTurn;
		Point topLeftPointEnd;
		Point topRightPointEnd;
		Point downLeftPointEnd;
		Point downRightPointEnd;
		Point fallDawnPoint1;
		Point fallDawnPoint2;

		int k = 0;

		public GameView(final Context context, final int height, final int width) {
			super(context);
			setFocusable(true);
			figuresArray = new ArrayList<GameFigure>();
			topLeftPointStart = new Point(width / 20 - width / 36, height / 6);
			topLeftPointTurn = new Point(width / 20 - width / 36 + 50,
					height / 6);
			topLeftPointEnd = new Point(width / 10 - width / 36 + 200,
					height / 6 + 110);
			topRightPointStart = new Point(19 * width / 20, height / 6);
			topRightPointTurn = new Point(19 * width / 20 - 50, height / 6);
			topRightPointEnd = new Point(9 * width / 10 - 200, height / 6 + 110);
			downLeftPointStart = new Point(width / 20 - width / 36, height / 2);
			downLeftPointTurn = new Point(width / 20 - width / 36 + 50,
					height / 2);
			downLeftPointEnd = new Point(width / 10 - width / 36 + 200,
					height / 2 + 110);
			downRightPointStart = new Point(19 * width / 20, height / 2);
			downRightPointTurn = new Point(19 * width / 20 - 50, height / 2);
			downRightPointEnd = new Point(9 * width / 10 - 200,
					height / 2 + 110);
			fallDawnPoint1 = new Point(width / 10 - width / 36 + 200,
					height - 80);
			fallDawnPoint2 = new Point(9 * width / 10 - 200, height - 80);
			this.height = height;
			this.width = width;

			timePeriod = 5000;
			timerRun = new Runnable() {

				public void run() {

					// if (dropCount == 4) {
					//
					// for (int i = 0; i < figuresArray.size(); i++) {
					// figuresArray.get(i).stop();
					//
					// }
					// myHandler.removeCallbacks(timerRun);
					// showWinDialog();
					//
					// } else {
					if (cutchImages == 20) {
						timePeriod -= 800;
					}
					if (cutchImages == 50) {
						timePeriod -= 800;
					}
					if (cutchImages == 100) {
						timePeriod -= 400;
					}
					if (cutchImages == 100) {
						timePeriod -= 200;
					}
					if (cutchImages == 130) {
						timePeriod -= 200;
					}
					if (cutchImages == 150) {
						timePeriod -= 200;
					}
					if (cutchImages == 170) {
						timePeriod -= 200;
					}
					if (cutchImages == 200) {
						timePeriod -= 200;
					}
					if (cutchImages == 230) {
						timePeriod -= 200;
					}
					if (cutchImages == 250) {
						timePeriod -= 200;
					}
					if (cutchImages == 280) {
						timePeriod -= 200;
					}
					if (cutchImages == 300) {
						timePeriod -= 200;
					}
					int randompoint = 1 + (int) (Math.random() * 4);
					switch (randompoint) {

					case 1:
						figuresArray.add(new GameFigure(context,
								R.drawable.heart_1, topLeftPointStart,
								width / 10, height / 12, 1));

						figuresArray.get(i).start();

						figuresArray.get(i).setSpeedX(2);
						figuresArray.get(i).setSpeedY(0);
						k = randompoint;
						break;

					case 2:
						figuresArray.add(new GameFigure(context,
								R.drawable.heart_2, topRightPointStart,
								width / 10, height / 12, 2));
						figuresArray.get(i).start();
						figuresArray.get(i).setSpeedX(-2);
						figuresArray.get(i).setSpeedY(0);
						break;
					case 3:
						figuresArray.add(new GameFigure(context,
								R.drawable.heart_3, downLeftPointStart,
								width / 10, height / 12, 3));
						figuresArray.get(i).start();
						figuresArray.get(i).setSpeedX(2);
						figuresArray.get(i).setSpeedY(0);
						break;
					case 4:
						figuresArray.add(new GameFigure(context,
								R.drawable.heart_4, downRightPointStart,
								width / 10, height / 12, 4));
						figuresArray.get(i).start();
						figuresArray.get(i).setSpeedX(-2);
						figuresArray.get(i).setSpeedY(0);
						break;

					}
					i++;
					myHandler.postDelayed(timerRun, timePeriod);
				}

				// }
			};

		}

		@Override
		protected void onDraw(Canvas canvas) {
			for (int i = 0; i < figuresArray.size(); i++) {
				canvas.drawBitmap(figuresArray.get(i).getBitmap(), figuresArray
						.get(i).getX(), figuresArray.get(i).getY(), null);
				// figuresArray.get(i).fallDownFromPoint1();
			}

			countTextView.setText("- " + cutchImages + " -");
			super.onDraw(canvas);
		}

		public class GameFigure implements Runnable {
			private Bitmap img;
			private int coordX = 0;
			private int coordY = 0;
			protected int xS = 0;
			protected int yS = 0;
			private int lastSpeedX;
			private int lastSpeedY;
			private int id;
			private int count = 0;
			protected boolean isRunning = false;

			private int imgWidth;
			private int imgHeight;
			private boolean isDrawable;
			Canvas myCanvas = null;
			Thread t = null;
			// protected boolean isRunning = false;
			protected boolean isPause = false;
			int bgwidth;
			int bgheight;

			int startpoint;

			public GameFigure(Context context, int drawable, Point point,
					int height, int width, int startpoint) {

				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;
				img = BitmapFactory.decodeResource(context.getResources(),
						drawable);
				imgWidth = img.getWidth();
				imgHeight = img.getHeight();
				isDrawable = true;
				id = count;
				count++;
				coordX = point.x;
				coordY = point.y;
				this.startpoint = startpoint;
			}

			public int getCount() {
				return count;
			}

			void setX(int newValue) {
				coordX = newValue;
			}

			public int getX() {
				return coordX;
			}

			void setY(int newValue) {
				coordY = newValue;
			}

			public int getY() {
				return coordY;
			}

			public int getID() {
				return id;
			}

			public Bitmap getBitmap() {
				return img;
			}

			public int getWidth() {
				return imgWidth;
			}

			public void setWidth(int newWidth) {
				imgWidth = newWidth;
			}

			public int getHeight() {
				return imgHeight;
			}

			public void setSpeedX(int xS) {
				this.xS = xS;
			}

			public void setSpeedY(int yS) {
				this.yS = yS;
			}

			public int getSpeedX() {
				return xS;
			}

			public int getSpeedY() {
				return yS;
			}

			public boolean getIsDrawable() {
				return isDrawable;
			}

			public void setIsDragable(boolean value) {
				isDrawable = value;
			}

			public void move(int dX, int dY) {
				this.coordX += dX;
				this.coordY += dY;
			}

			private boolean fallDownFromPoint() {
				boolean isFallDawn = false;
				switch (startpoint) {
				case 1:
					if (coordX == topLeftPointTurn.x) {
						this.setSpeedX(2);
						this.setSpeedY(1);
					}
					if (coordX == topLeftPointEnd.x) {

						if (kittyBasketPosition == 1
								&& Math.abs(coordY - topLeftPointEnd.y) <= 5) {
							figuresArray.remove(this);
							this.stop();
							i--;
							cutchImages++;
						} else {

							this.setSpeedX(0);
							this.setSpeedY(10);

							if (coordY > fallDawnPoint1.y) {
								figuresArray.remove(this);
								this.stop();
								i--;

								lifeCount();
							}
							isFallDawn = true;

						}
					}

					break;
				case 2:
					if (coordX == topRightPointTurn.x) {
						this.setSpeedX(-2);
						this.setSpeedY(1);
					}
					if (coordX == topRightPointEnd.x) {
						if (kittyBasketPosition == 2
								&& Math.abs(coordY - topRightPointEnd.y) <= 5) {
							figuresArray.remove(this);
							this.stop();
							i--;
							cutchImages++;
						} else {

							this.setSpeedX(0);
							this.setSpeedY(10);
							if (coordY > fallDawnPoint2.y) {
								figuresArray.remove(this);
								this.stop();
								i--;
								lifeCount();

							}
							isFallDawn = true;
						}
					}

					break;
				case 3:
					if (coordX == downLeftPointTurn.x) {
						this.setSpeedX(2);
						this.setSpeedY(1);
					}
					if (coordX == downLeftPointEnd.x) {
						if (kittyBasketPosition == 3
								&& Math.abs(coordY - downLeftPointEnd.y) <= 5) {
							figuresArray.remove(this);
							this.stop();
							i--;
							cutchImages++;
						} else {

							this.setSpeedX(0);
							this.setSpeedY(10);
							if (coordY > fallDawnPoint1.y) {
								figuresArray.remove(this);
								this.stop();
								i--;

								lifeCount();

							}
							isFallDawn = true;
						}
					}
					break;
				case 4:
					if (coordX == downRightPointTurn.x) {
						this.setSpeedX(-2);
						this.setSpeedY(1);
					}
					if (coordX == downRightPointEnd.x) {
						if (kittyBasketPosition == 4
								&& Math.abs(coordY - downRightPointEnd.y) <= 5) {
							figuresArray.remove(this);
							this.stop();
							i--;
							cutchImages++;
						} else {

							this.setSpeedX(0);
							this.setSpeedY(10);

							if (coordY > fallDawnPoint2.y) {
								figuresArray.remove(this);
								this.stop();
								i--;

								lifeCount();

							}
							isFallDawn = true;
						}
					}
					break;

				}
				return isFallDawn;

			}

			public void lifeCount() {
				dropCount++;
				if (dropCount == 1) {
					HeartCollectActivity.this.runOnUiThread(new Runnable() {

						public void run() {
							kittyLife3
									.setBackgroundResource(R.drawable.life_icon_gone);
						}
					});

				}
				if (dropCount == 2) {
					HeartCollectActivity.this.runOnUiThread(new Runnable() {

						public void run() {
							kittyLife2
									.setBackgroundResource(R.drawable.life_icon_gone);
						}
					});
				}
				if (dropCount == 3) {
					HeartCollectActivity.this.runOnUiThread(new Runnable() {

						public void run() {
							kittyLife1
									.setBackgroundResource(R.drawable.life_icon_gone);
						}
					});
				}
				if (dropCount == 4) {

					closeAllThreads();
					HeartCollectActivity.this.runOnUiThread(new Runnable() {

						public void run() {
							showWinDialog();
						}
					});

				}

			}

			public void run() {
				while (isRunning) {
					try {

						fallDownFromPoint();

						move(xS, yS);
						postInvalidate();
						Thread.sleep(100);
						while (isPause) {
							synchronized (this) {
								wait();
							}
						}
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
				}

			}

			public void start() {
				if (t != null) {
					try {
						stop();
						t.join();
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}

				}
				t = new Thread(this);

				isRunning = true;
				// setSpeedX(5);
				// setSpeedY(5);
				t.start();

			}

			public synchronized void continueRun() {
				isPause = false;

				setSpeedX(lastSpeedX);
				setSpeedY(lastSpeedY);
				notify();

			}

			public void stop() {
				continueRun();
				isRunning = false;
			}

			public void pause() {
				isPause = true;
				lastSpeedX = this.getSpeedX();
				lastSpeedY = this.getSpeedY();
				setSpeedX(0);
				setSpeedY(0);

			}

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	public void showWinDialog() {
		alertDialog = new Dialog(this);
		alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alertDialog = new Dialog(this,
				android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		alertDialog.setContentView(R.layout.dialog);
		alertDialog.setCancelable(true);

		Button replayButton = (Button) alertDialog
				.findViewById(R.id.replay_btn);
		Button exitButton = (Button) alertDialog.findViewById(R.id.exit_btn);
		replayButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(HeartCollectActivity.this,
						HeartCollectActivity.class);
				startActivity(intent);
				finish();
			}
		});

		exitButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				alertDialog.cancel();

				finish();
			}
		});

		alertDialog.show();
	}
	
	public void closeAllThreads()
	{
		for (int i = 0; i < figuresArray.size(); i++) {
			figuresArray.get(i).stop();
		}
		figuresArray.removeAll(figuresArray);
		myHandler.removeCallbacks(timerRun);
	}

	@Override
	public void onBackPressed() {
		if (gameSong.isPlaying()) {

			gameSong.release();
			gameSong = null;
		}
		closeAllThreads();
		finish();
		super.onBackPressed();
	}

	@Override
	protected void onPause() {
		if (gameSong != null && gameSong.isPlaying()) {
			gameSong.pause();
		}
		if (alertDialog != null && alertDialog.isShowing()) {
			alertDialog.dismiss();
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (gameSong != null) {
			gameSong.release();
			gameSong = null;
		}
		if (tapSound != null) {
			tapSound.release();
			tapSound = null;
		}
		super.onDestroy();
	}

}
