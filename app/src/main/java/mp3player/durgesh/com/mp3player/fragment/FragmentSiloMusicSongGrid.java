package mp3player.durgesh.com.mp3player.fragment;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

//import com.androidquery.AQuery;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import mp3player.durgesh.com.mp3player.MainActivity;
import mp3player.durgesh.com.mp3player.MusicPlayerActviity;
import mp3player.durgesh.com.mp3player.R;
import mp3player.durgesh.com.mp3player.bean.Audio_Bean;


import static mp3player.durgesh.com.mp3player.MainActivity.bitmap;
import static mp3player.durgesh.com.mp3player.MainActivity.currentSongIndex;
import static mp3player.durgesh.com.mp3player.MainActivity.mediaPlayer;



public class FragmentSiloMusicSongGrid extends AppCompatActivity implements View.OnClickListener {

    Activity mActivity;
    RecyclerView recycleViewGrid;
    AdapterSongGrid mAdapter;
    DividerItemDecoration itemDecoration;
    ArrayList<Audio_Bean> itemsData;


    LinearLayout openPlayer;
    ImageView imageSong;
    static ImageView imagePlay;
    TextView textSongName;
    private boolean isForFav;
    ArrayList<Integer> favSongdIdList;

    private static final String PLAY_ACTION_FULL = "play_full";
    private static final String PAUSE_ACTION_FULL = "pause_full";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_song_grid);

        itemsData = new ArrayList<>();

        String songTitle = getIntent().getStringExtra("SongName");
        isForFav = getIntent().getBooleanExtra("isForFav",false);
        if (isForFav)
            favSongdIdList = getIntent().getIntegerArrayListExtra("favSongdIdList");

        setIds();

        textSongName.setText(songTitle);
        imageSong.setImageBitmap(MainActivity.bitmap);

        mAdapter = new AdapterSongGrid(mActivity, itemsData);


        //itemDecoration = new DividerItemDecoration(mActivity, LinearLayoutManager.HORIZONTAL, 10);

        GridLayoutManager gLayoutManager = new GridLayoutManager(mActivity, 2); // (Context context, int spanCount)
        recycleViewGrid.setLayoutManager(gLayoutManager);
        recycleViewGrid.setItemAnimator(new DefaultItemAnimator());
        recycleViewGrid.addItemDecoration(itemDecoration);
        recycleViewGrid.setAdapter(mAdapter);


        LongOperationAudio caudAsyCh = new LongOperationAudio();
        caudAsyCh.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

       private void setIds() {
        mActivity = FragmentSiloMusicSongGrid.this;

//        TextView txtTitle = (TextView)findViewById(R.id.txtTitle);
//        if (!isForFav){
//            txtTitle.setText("My MuSIC");
//        }else {
//            txtTitle.setText("MY FAVOURITE");
//        }
        openPlayer = (LinearLayout) findViewById(R.id.openPlayer);
        imageSong = (ImageView) findViewById(R.id.imageSong);
        imagePlay = (ImageView) findViewById(R.id.imagePlay);
        textSongName = (TextView) findViewById(R.id.textSongName);
        imagePlay.setOnClickListener(this);
        openPlayer.setOnClickListener(this);

        recycleViewGrid = (RecyclerView) findViewById(R.id.recycleViewGrid);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (currentSongIndex != -1) {
            if (itemsData != null) {

                if (itemsData.size() > 0) {


                    if (MainActivity.mediaPlayer.isPlaying()) {
                        if (MainActivity.mediaPlayer != null) {
                            //ActivitySiloMusicPlayerList.mediaPlayer.pause();
                            // Changing button image to play button
                            imagePlay.setImageResource(R.drawable.btn_play);
                        }
                    } else {
                        // Resume song
                        if (MainActivity.mediaPlayer != null) {
                            //ActivitySiloMusicPlayerList.mediaPlayer.start();
                            // Changing button image to pause button
                            imagePlay.setImageResource(R.drawable.btn_pause);
                        }
                    }


                    SharedPreferences pre =getApplicationContext().getSharedPreferences("SILOSD", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pre.edit();
                    int pos = pre.getInt("isSong",0);
                    String songTitle = itemsData.get(currentSongIndex).getAudioName();//songsList.get(currentSongIndex).get("songTitle");
                    textSongName.setText(songTitle);




                    int albumId = Integer.valueOf(itemsData.get(currentSongIndex).getSongID());


                    Uri sArtworkUri = Uri
                            .parse("content://media/external/audio/albumart");
                    Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

                    System.out.print(albumArtUri.toString());

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(
                                this.getContentResolver(), albumArtUri);
                        //bitmap = Bitmap.createScaledBitmap(bitmap, 30, 30, true);
                        imageSong.setImageBitmap(bitmap);

                    } catch (FileNotFoundException exception) {
                        exception.printStackTrace();
                        bitmap = BitmapFactory.decodeResource(this.getResources(),
                                R.drawable.adele);
                        imageSong.setImageBitmap(bitmap);
                    } catch (IOException e) {

                        e.printStackTrace();
                    }

                    // Changing Button Image to pause image
                    if (mediaPlayer.isPlaying()) {
                        if (mediaPlayer != null) {
                            // Changing button image to play button
                            imagePlay.setImageResource(R.drawable.btn_pause);
                        }
                    } else {
                        // Resume song
                        if (mediaPlayer != null) {
                            // Changing button image to pause button
                            imagePlay.setImageResource(R.drawable.btn_play);
                        }
                    }


                }
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.openPlayer:

                Intent intent = new Intent(mActivity, MusicPlayerActviity.class);
                intent.putExtra("SongName", textSongName.getText().toString());
                startActivity(intent);

                break;

            case R.id.imagePlay:
                if (mediaPlayer.isPlaying()) {
                    if (mediaPlayer != null) {
                        mediaPlayer.pause();
                        // Changing button image to play button
                        imagePlay.setImageResource(R.drawable.btn_play);


                        Intent playReceive = new Intent();
                        playReceive.setAction(PAUSE_ACTION_FULL);
                        sendBroadcast(playReceive);

                    }
                } else {
                    // Resume song
                    if (mediaPlayer != null) {
                        mediaPlayer.start();
                        // Changing button image to pause button
                        imagePlay.setImageResource(R.drawable.btn_pause);

                        Intent playReceive = new Intent();
                        playReceive.setAction(PLAY_ACTION_FULL);

                        sendBroadcast(playReceive);

                    }
                }
                break;

        }
}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null)
            mediaPlayer.release();

    }

    public static void playPause() {
        try {
            if (MainActivity.mediaPlayer.isPlaying()) {
                if (MainActivity.mediaPlayer != null) {
                    //ActivitySiloMusicPlayerList.mediaPlayer.pause();
                    // Changing button image to play button
                    imagePlay.setImageResource(R.drawable.btn_pause);
                }
            } else {
                // Resume song
                if (MainActivity.mediaPlayer != null) {
                    //ActivitySiloMusicPlayerList.mediaPlayer.start();
                    // Changing button image to pause button
                    imagePlay.setImageResource(R.drawable.btn_play);
                }
            }

        }catch (Exception ex){

        }
    }

    private class LongOperationAudio extends AsyncTask<String, Audio_Bean, String> {


        @Override
        protected String doInBackground(String... params) {
            try {


                Uri uri;
                Cursor cursor;
                int column_index_data, column_index_folder_name;
                ArrayList<String> listOfAllImages = new ArrayList<String>();
                String absolutePathOfImage = null;
                uri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
                String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
                String[] projection = {MediaStore.Audio.Media._ID,
                        MediaStore.MediaColumns.DATA,
                        MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.ALBUM_ID};

                cursor = managedQuery(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        projection,
                        selection,
                        null,
                        null);

                while (cursor.moveToNext()) {
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                    String ulbumImg = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

                    Audio_Bean bin = new Audio_Bean();
                    bin.setAudioName(displayName);
                    bin.setAudDuration(duration);
                    bin.setAudioPath(path);
                    bin.setSongID(ulbumImg);
                    bin.setChecked(false);

                    int albumId = Integer.valueOf(ulbumImg);


                    Uri sArtworkUri = Uri
                            .parse("content://media/external/audio/albumart");

                    Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(
                                mActivity.getContentResolver(), albumArtUri);
                        //bitmap = Bitmap.createScaledBitmap(bitmap, 30, 30, true);

                        bin.setAudioBitmapImg(bitmap);

                    } catch (FileNotFoundException exception) {
                        exception.printStackTrace();
                        bitmap = BitmapFactory.decodeResource(mActivity.getResources(),
                                R.drawable.adele);

                        bin.setAudioBitmapImg(bitmap);
                    } catch (IOException e) {

                        e.printStackTrace();
                    }

                    publishProgress(bin);
                }
                // cursor.close();

            } catch (Exception e) {
                System.out.print("");
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            if (isForFav)
                checkForMostPlayed();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Audio_Bean... values) {
            if (values[0] != null) {
                itemsData.add(values[0]);
                System.out.println("ImagePath:" + values);
                if (itemsData != null) {
                    if (itemsData.size() > 0) {

                        mAdapter.notifyItemInserted(itemsData.size() - 1);

                    }
                }


            }

        }
    }

    public void playSong(int songIndex) {
        // Play song
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(itemsData.get(songIndex).getAudioPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            // Displaying Song title
            String songTitle = itemsData.get(songIndex).getAudioName();
            textSongName.setText(songTitle);

            int albumId = Integer.valueOf(itemsData.get(songIndex).getSongID());


            Uri sArtworkUri = Uri
                    .parse("content://media/external/audio/albumart");
            Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

            System.out.print(albumArtUri.toString());

            try {
                bitmap = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), albumArtUri);
                //bitmap = Bitmap.createScaledBitmap(bitmap, 30, 30, true);
                imageSong.setImageBitmap(bitmap);

            } catch (FileNotFoundException exception) {
                exception.printStackTrace();
                bitmap = BitmapFactory.decodeResource(getResources(),
                        R.drawable.adele);
                imageSong.setImageBitmap(bitmap);
            } catch (IOException e) {

                e.printStackTrace();
            }

            // Changing Button Image to pause image
            imagePlay.setImageResource(R.drawable.btn_pause);


        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void checkForMostPlayed() {
        Log.d("MOST PLAYED", "MOST PLAYED checkForMostPlayed: ");

        ArrayList recentsongsList = new ArrayList<Audio_Bean>();
        for (Audio_Bean audio_bean : itemsData){

            Log.d("MOST PLAYED", "MOST PLAYED XX checkForMostPlayed: "+Integer.parseInt(audio_bean.getSongID()));

            if (favSongdIdList.contains(Integer.parseInt(audio_bean.getSongID())) ){
                recentsongsList.add(audio_bean);
            }
        }

        itemsData.clear();
        itemsData.addAll(recentsongsList);

        mAdapter.notifyDataSetChanged();



        Log.d("MOST PLAYED", "MOST PLAYED checkForMostPlayed: "+recentsongsList.size());
    }



    public class AdapterSongGrid extends RecyclerView.Adapter<AdapterSongGrid.ViewHolder> {

        Activity mContext;
        ArrayList<Audio_Bean> itemsData;
        View itemLayoutView;
//        AQuery aq;

        public AdapterSongGrid(Activity mContext, ArrayList<Audio_Bean> itemsData) {
            this.mContext = mContext;
            this.itemsData = itemsData;
//            aq = new AQuery(mContext);
        }

        @Override
        public AdapterSongGrid.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_song_grid, parent, false);


            // create ViewHolder
            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(AdapterSongGrid.ViewHolder holder, int position) {

            try {
            /*File f = new File(itemsData.get(position).getAudioPath().toString());
            Picasso.with(mContext).load(f).into(holder.imgThumb1);*/
                holder.imgThumb1.setImageBitmap(itemsData.get(position).getAudioBitmapImg());

            } catch (Exception e) {
                String a;
            }
            holder.songName.setText(itemsData.get(position).getAudioName());
            holder.listClick.setTag(position);
            holder.listClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (Integer) v.getTag();
                    playSong(pos);
                }
            });

        }

        @Override
        public int getItemCount() {
            return itemsData.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public ImageView imgThumb1;
            public LinearLayout listClick;
            public TextView songName;

            public ViewHolder(View itemLayoutView) {
                super(itemLayoutView);


                imgThumb1 = (ImageView) itemLayoutView.findViewById(R.id.imgThumb1);
                songName = (TextView) itemLayoutView.findViewById(R.id.songName);
                listClick = (LinearLayout) itemLayoutView.findViewById(R.id.listClick);

            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {


                }
            }
        }

    }
}
