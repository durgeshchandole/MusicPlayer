package mp3player.durgesh.com.mp3player;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import Interface.CustomDialogListner;
import Interface.MusicPlayerInterface;
import adapter.PagerView;
import mp3player.durgesh.com.mp3player.bean.Audio_Bean;
import mp3player.durgesh.com.mp3player.fragment.AlbumFragment;
import mp3player.durgesh.com.mp3player.fragment.FragmentSiloMusicSongGrid;
import mp3player.durgesh.com.mp3player.fragment.TrackFragment;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener , CustomDialogListner,
        MusicPlayerInterface {


    private static final String PLAY_ACTION = "play";
    private static final String PAUSE_ACTION = "pause";
    private static final String PLAY_ACTION_FULL = "play_full";
    private static final String PAUSE_ACTION_FULL = "pause_full";
    ImageView back,uploadSong;
    // Songs list
    ViewPager viewPager;
    ArrayList<Fragment> tabList;
    TabLayout tabLayout;

    public static MediaPlayer mediaPlayer;
    public static int currentSongIndex = 0;
    public static boolean isShuffle = false;
    public static boolean isRepeat = false;
    public static boolean payerRuning = true;
    public static Bitmap bitmap = null;
    public static String songTitle="";
    private CustomDialogListner customDialogListner;
    private MediaSession mediaSession;
    private Bundle mSessionExtras;
    private BroadcastReceiver receiver = null;
    private Fragment fragment;
    private TrackFragment trackFragment;
    private AlbumFragment albumFragment;
    private NotificationManager notificationManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setIds();

        trackFragment = new TrackFragment();
        albumFragment = new AlbumFragment();

        setTabDetails();

        setMediaSession();

        customDialogListner = (CustomDialogListner)this;

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (receiver == null)
            registerReceivers();
    }


    private void registerReceivers() {
        receiver =  new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if(PLAY_ACTION.equals(action)) {
                    Log.d("MUSIC PLAYER XX", "MUSIC PLAYER XX Notification play: ");
                    if (fragment instanceof TrackFragment){
                        trackFragment.playPause();
                    }else if (fragment instanceof AlbumFragment){
                        albumFragment.playPause();
                    }

                    MusicPlayerActviity.playPause();
                    FragmentSiloMusicSongGrid.playPause();

                } else if(PAUSE_ACTION.equals(action)) {
                    Log.d("MUSIC PLAYER XX", "MUSIC PLAYER XX Notification pause: ");
                    if (fragment instanceof TrackFragment){
                        trackFragment.playPause();
                    }else if (fragment instanceof AlbumFragment){
                        albumFragment.playPause();
                    }

                    MusicPlayerActviity.playPause();
                    FragmentSiloMusicSongGrid.playPause();

                }else if (PAUSE_ACTION_FULL.equals(action)){
                   // createNotification(false);
                }else if (PLAY_ACTION_FULL.equals(action)){
                   // createNotification(true);
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PLAY_ACTION);
        intentFilter.addAction(PAUSE_ACTION);
        intentFilter.addAction(PAUSE_ACTION_FULL);
        intentFilter.addAction(PLAY_ACTION_FULL);

        registerReceiver(receiver,intentFilter);

    }

    private void setMediaSession() {
        mediaSession = new MediaSession(this,"MusicService");
        mediaSession.setCallback(new MediaSessionCallback());
        mediaSession.setFlags(MediaSession.FLAG_HANDLES_MEDIA_BUTTONS | MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);


        Context context = getApplicationContext();
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 99 /*request code*/,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mediaSession.setSessionActivity(pi);

        mSessionExtras = new Bundle();
        mediaSession.setExtras(mSessionExtras);

    }

    private void setIds() {

          // Mediaplayer
        mediaPlayer = new MediaPlayer();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabList = new ArrayList<>();

    }

    @Override
    public void onBackPressed() {
        Utility.showCustomDialoge(this,false,0,getString(R.string.close_music_message),
                customDialogListner,"musicPlayer");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.print("onDestroy");
        payerRuning = false;
        MainActivity.mediaPlayer.release();
        unregisterReceiver();
        if (notificationManager != null)
            notificationManager.cancelAll();
    }


    private void setTabDetails() {
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        //tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.player_album));
        //tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.player_artist));
        //tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.player_music));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabList.add(albumFragment);
       // tabList.add(trackFragment);
        //tabList.add(new ArtistFragment());


        PagerView adapter = new PagerView
                (getSupportFragmentManager(), tabList, tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {



        }
    }



    public void createNotification(boolean isPlaying) {
        Log.d("MUSIC PLAYER XX", "MUSIC PLAYER XX Notification start: ");

        Intent playReceive = new Intent();
        playReceive.setAction(PLAY_ACTION);
        PendingIntent pendingIntentPlay = PendingIntent.getBroadcast(this, 12345, playReceive, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent pauseReceive = new Intent();
        pauseReceive.setAction(PAUSE_ACTION);
        PendingIntent pendingIntentPause = PendingIntent.getBroadcast(this, 12345, pauseReceive, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder  = new Notification.Builder(this);
        builder.setContentTitle(getString(R.string.app_name))
                .setContentText(songTitle)
                .setSmallIcon(this.getApplicationInfo().icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setAutoCancel(false);

        if (isPlaying){
            builder.addAction(new Notification.Action(R.drawable.img_btn_pause, "PAUSE", pendingIntentPlay));
        }else {
            builder.addAction(new Notification.Action(R.drawable.img_btn_play, "PLAY", pendingIntentPause));
        }

        notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, builder.build());
        Log.d("MUSIC PLAYER XX", "MUSIC PLAYER XX Notification end: ");

    }




    @Override
    public void play() {
        Log.d("MUSIC PLAYER XX", "MUSIC PLAYER XX playYYYYYYYYYY: ");
        createNotification(true);
    }

    @Override
    public void pause() {
        Log.d("MUSIC PLAYER XX", "MUSIC PLAYER XX pauseEEEEEEEE: ");
        createNotification(false);
    }

    @Override
    public void playNext(int current) {

    }

    @Override
    public void playPrevious(int current) {

    }

    @Override
    public void setList(ArrayList<Audio_Bean> musicList) {

    }

    @Override
    public void setTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    @Override
    public void setInstance(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void unregisterReceiver() {
//        unregisterReceiver(receiver);
        if (notificationManager != null)
            notificationManager.cancelAll();
    }



    @Override
    public void onOkClick() {
        //todo
        payerRuning = false;
        MainActivity.mediaPlayer.release();

        // Starting new intent
        Intent in = new Intent(getApplicationContext(),
                MusicPlayerActviity.class);
        // Sending songIndex to PlayerActivity
        in.putExtra("songIndex", -1);
        setResult(100, in);
        // Closing PlayListView
        finish();
    }

    @Override
    public void onCancelClick() {

    }



    private class MediaSessionCallback extends MediaSession.Callback {
        @Override
        public void onPause() {
            super.onPause();
            Log.d("MUSIC PLAYER XX", "MUSIC PLAYER XX onPause: ");
            if (mediaPlayer != null)
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
        }

        @Override
        public void onPlay() {
            super.onPlay();
            Log.d("MUSIC PLAYER XX", "MUSIC PLAYER XX onPlay: ");
            if (mediaPlayer != null)
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
        }
    }





}

