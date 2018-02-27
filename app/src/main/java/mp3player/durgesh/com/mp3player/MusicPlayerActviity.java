package mp3player.durgesh.com.mp3player;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import musicplayer.SongsManager;
import musicplayer.Utilities;

/**
 * Created by rebelute13 on 19/2/18.
 */

public class MusicPlayerActviity extends Activity implements MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener {

    private static ImageButton btnPlay;
    private ImageButton btnForward;
    private ImageButton btnBackward;
    private ImageButton btnNext;
    private ImageButton btnPrevious;
    private ImageButton btnRepeat;
    private ImageButton btnShuffle;
    private SeekBar songProgressBar;
    private TextView songTitleLabel;
    private TextView songCurrentDurationLabel;
    private TextView songTotalDurationLabel;
    // Media Player

    // Handler to update UI timer, progress bar etc,.
    private Handler mHandler = new Handler();

    private SongsManager songManager;
    private Utilities utils;
    private int seekForwardTime = 5000; // 5000 milliseconds
    private int seekBackwardTime = 5000; // 5000 milliseconds


    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

    private ImageView displayPic;
    Activity activity;

    private static final String PLAY_ACTION_FULL = "play_full";
    private static final String PAUSE_ACTION_FULL = "pause_full";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);

        activity = MusicPlayerActviity.this;

        setIds();

        String songTitle = getIntent().getStringExtra("SongName");
        songTitleLabel.setText(songTitle);
        displayPic.setImageBitmap(MainActivity.bitmap);

        if (MainActivity.mediaPlayer.isPlaying()) {
            if (MainActivity.mediaPlayer != null) {
                //ActivitySiloMusicPlayerList.mediaPlayer.pause();
                // Changing button image to play button
                btnPlay.setImageResource(R.drawable.btn_pause);
            }
        } else {
            // Resume song
            if (MainActivity.mediaPlayer != null) {
                //ActivitySiloMusicPlayerList.mediaPlayer.start();
                // Changing button image to pause button
                btnPlay.setImageResource(R.drawable.btn_play);
            }
        }

        // By default play first song
        //playSong(0);

        /**
         * Play button click event
         * plays a song and changes button to pause image
         * pauses a song and changes button to play image
         * */
        btnPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check for already playing
                if (MainActivity.mediaPlayer.isPlaying()) {
                    if (MainActivity.mediaPlayer != null) {
                        MainActivity.mediaPlayer.pause();
                        // Changing button image to play button
                        btnPlay.setImageResource(R.drawable.btn_play);

                        Intent playReceive = new Intent();
                        playReceive.setAction(PAUSE_ACTION_FULL);
                        sendBroadcast(playReceive);

                    }
                } else {
                    // Resume song
                    if (MainActivity.mediaPlayer != null) {
                        MainActivity.mediaPlayer.start();
                        // Changing button image to pause button
                        btnPlay.setImageResource(R.drawable.btn_pause);

                        Intent playReceive = new Intent();
                        playReceive.setAction(PLAY_ACTION_FULL);

                        sendBroadcast(playReceive);

                    }
                }

            }
        });

        /**
         * Forward button click event
         * Forwards song specified seconds
         * */
        btnForward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // get current song position
                int currentPosition = MainActivity.mediaPlayer.getCurrentPosition();
                // check if seekForward time is lesser than song duration
                if (currentPosition + seekForwardTime <= MainActivity.mediaPlayer.getDuration()) {
                    // forward song
                    MainActivity.mediaPlayer.seekTo(currentPosition + seekForwardTime);
                } else {
                    // forward to end position
                    MainActivity.mediaPlayer.seekTo(MainActivity.mediaPlayer.getDuration());
                }
            }
        });

        /**
         * Backward button click event
         * Backward song to specified seconds
         * */
        btnBackward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // get current song position
                int currentPosition = MainActivity.mediaPlayer.getCurrentPosition();
                // check if seekBackward time is greater than 0 sec
                if (currentPosition - seekBackwardTime >= 0) {
                    // forward song
                    MainActivity.mediaPlayer.seekTo(currentPosition - seekBackwardTime);
                } else {
                    // backward to starting position
                    MainActivity.mediaPlayer.seekTo(0);
                }

            }
        });

        /**
         * Next button click event
         * Plays next song by taking currentSongIndex + 1
         * */
        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check if next song is there or not
                if (MainActivity.currentSongIndex < (songsList.size() - 1)) {
                    playSong(MainActivity.currentSongIndex + 1);
                    MainActivity.currentSongIndex = MainActivity.currentSongIndex + 1;
                } else {
                    // play first song
                    playSong(0);
                    MainActivity.currentSongIndex = 0;
                }

            }
        });

        /**
         * Back button click event
         * Plays previous song by currentSongIndex - 1
         * */
        btnPrevious.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (MainActivity.currentSongIndex > 0) {
                    playSong(MainActivity.currentSongIndex - 1);
                    MainActivity.currentSongIndex = MainActivity.currentSongIndex - 1;
                } else {
                    // play last song
                    playSong(songsList.size() - 1);
                    MainActivity.currentSongIndex = songsList.size() - 1;
                }

            }
        });

        /**
         * Button Click event for Repeat button
         * Enables repeat flag to true
         * */
        btnRepeat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (MainActivity.isRepeat) {
                    MainActivity.isRepeat = false;
                    Toast.makeText(getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
                    btnRepeat.setImageResource(R.drawable.btn_repeat);
                } else {
                    // make repeat to true
                    MainActivity.isRepeat = true;
                    Toast.makeText(getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    MainActivity.isShuffle = false;
                    btnRepeat.setImageResource(R.drawable.btn_repeat_focused);
                    btnShuffle.setImageResource(R.drawable.btn_shuffle);
                }
            }
        });

        /**
         * Button Click event for Shuffle button
         * Enables shuffle flag to true
         * */
        btnShuffle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (MainActivity.isShuffle) {
                    MainActivity.isShuffle = false;
                    Toast.makeText(getApplicationContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
                    btnShuffle.setImageResource(R.drawable.btn_shuffle);
                } else {
                    // make repeat to true
                    MainActivity.isShuffle = true;
                    Toast.makeText(getApplicationContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    MainActivity.isRepeat = false;
                    btnShuffle.setImageResource(R.drawable.btn_shuffle_focused);
                    btnRepeat.setImageResource(R.drawable.btn_repeat);
                }
            }
        });

        /**
         * Button Click event for Play list click event
         * Launches list activity which displays list of songs
         * */

        updateProgressBar();
    }

    private void setIds() {
        // All player buttons
        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        btnForward = (ImageButton) findViewById(R.id.btnForward);
        btnBackward = (ImageButton) findViewById(R.id.btnBackward);
        btnNext = (ImageButton) findViewById(R.id.btnNext);
        btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
        btnRepeat = (ImageButton) findViewById(R.id.btnRepeat);
        btnShuffle = (ImageButton) findViewById(R.id.btnShuffle);
        songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
        songTitleLabel = (TextView) findViewById(R.id.songTitle);
        songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
        songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);

        displayPic = (ImageView) findViewById(R.id.displayPic);


        songManager = new SongsManager();
        utils = new Utilities();

        // Listeners
        songProgressBar.setOnSeekBarChangeListener(this); // Important
        MainActivity.mediaPlayer.setOnCompletionListener(this); // Important

        // Getting all songs list
        songsList = songManager.getPlayList(MusicPlayerActviity.this);
    }

    /**
     * Receiving song index from playlist view
     * and play the song
     */
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) {
            int tempIndex = data.getExtras().getInt("songIndex");
            if (tempIndex != -1) {

                MainActivity.currentSongIndex = data.getExtras().getInt("songIndex");
                // play selected song
                //playSong(currentSongIndex);
            }
        }

    }


    /**
     * Update timer on seekbar
     */
    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable thread
     */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            if (MainActivity.payerRuning) {
                long totalDuration = MainActivity.mediaPlayer.getDuration();
                long currentDuration = MainActivity.mediaPlayer.getCurrentPosition();

                // Displaying Total Duration time
                songTotalDurationLabel.setText("" + utils.milliSecondsToTimer(totalDuration));
                // Displaying time completed playing
                songCurrentDurationLabel.setText("" + utils.milliSecondsToTimer(currentDuration));

                // Updating progress bar
                int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
                //Log.d("Progress", ""+progress);
                songProgressBar.setMax(MainActivity.mediaPlayer.getDuration());
                songProgressBar.setProgress(MainActivity.mediaPlayer.getCurrentPosition());

                // Running this thread after 100 milliseconds
                mHandler.postDelayed(this, 100);
            }

        }
    };

    /**
     *
     * */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

    }

    /**
     * When user starts moving the progress handler
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // remove message Handler from updating progress bar
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    /**
     * When user stops moving the progress hanlder
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = MainActivity.mediaPlayer.getDuration();
        int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        MainActivity.mediaPlayer.seekTo(songProgressBar.getProgress());

        // update timer progress again
        updateProgressBar();
    }

    public void playSong(int songIndex) {
        // Play song
        try {
            MainActivity.mediaPlayer.reset();
            MainActivity.mediaPlayer.setDataSource(songsList.get(songIndex).get("songPath"));
            MainActivity.mediaPlayer.prepare();
            MainActivity.mediaPlayer.start();
            // Displaying Song title
            String songTitle = songsList.get(songIndex).get("songTitle");
            songTitleLabel.setText(songTitle);

            int albumId = Integer.valueOf(songsList.get(songIndex).get("displayPic"));


            Uri sArtworkUri = Uri
                    .parse("content://media/external/audio/albumart");
            Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

            System.out.print(albumArtUri.toString());
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(
                        activity.getContentResolver(), albumArtUri);
                //bitmap = Bitmap.createScaledBitmap(bitmap, 30, 30, true);
                displayPic.setImageBitmap(bitmap);

            } catch (FileNotFoundException exception) {
                exception.printStackTrace();
                bitmap = BitmapFactory.decodeResource(activity.getResources(),
                        R.drawable.audio_download_icon);
                displayPic.setImageBitmap(bitmap);
            } catch (IOException e) {

                e.printStackTrace();
            }

            // Changing Button Image to pause image
            btnPlay.setImageResource(R.drawable.btn_pause);

            // set Progress bar values
//            songProgressBar.setProgress(0);
            songProgressBar.setMax(MainActivity.mediaPlayer.getDuration());

            // Updating progress bar
            updateProgressBar();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * On Song Playing completed
     * if repeat is ON play same song again
     * if shuffle is ON play random song
     */
    @Override
    public void onCompletion(MediaPlayer arg0) {

        // check for repeat is ON or OFF
        if (MainActivity.isRepeat) {
            // repeat is on play same song again
            playSong(MainActivity.currentSongIndex);
        } else if (MainActivity.isShuffle) {
            // shuffle is on - play a random song
            Random rand = new Random();
            MainActivity.currentSongIndex = rand.nextInt((songsList.size() - 1) - 0 + 1) + 0;
            playSong(MainActivity.currentSongIndex);
        } else {
            // no repeat or shuffle ON - play next song
            if (MainActivity.currentSongIndex < (songsList.size() - 1)) {
                playSong(MainActivity.currentSongIndex + 1);
                MainActivity.currentSongIndex = MainActivity.currentSongIndex + 1;
            } else {
                // play first song
                playSong(0);
                MainActivity.currentSongIndex = 0;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.print("onDestroy");
        /*payerRuning = false;
        ActivitySiloMusicPlayerList.mediaPlayer.release();*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.print("onResume");
    }


    public static void playPause() {
        try {
            if (MainActivity.mediaPlayer.isPlaying()) {
                if (MainActivity.mediaPlayer != null) {
                    //ActivitySiloMusicPlayerList.mediaPlayer.pause();
                    // Changing button image to play button
                    btnPlay.setImageResource(R.drawable.btn_pause);
                }
            } else {
                // Resume song
                if (MainActivity.mediaPlayer != null) {
                    //ActivitySiloMusicPlayerList.mediaPlayer.start();
                    // Changing button image to pause button
                    btnPlay.setImageResource(R.drawable.btn_play);
                }
            }

        }catch (Exception ex){

        }
    }
}

