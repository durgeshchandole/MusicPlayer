package mp3player.durgesh.com.mp3player.fragment;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import Interface.MusicPlayerInterface;
import mp3player.durgesh.com.mp3player.MainActivity;
import mp3player.durgesh.com.mp3player.MusicPlayerActviity;
import mp3player.durgesh.com.mp3player.R;
import mp3player.durgesh.com.mp3player.bean.Audio_Bean;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.content.Context.MODE_PRIVATE;



import static mp3player.durgesh.com.mp3player.MainActivity.bitmap;
import static mp3player.durgesh.com.mp3player.MainActivity.currentSongIndex;
import static mp3player.durgesh.com.mp3player.MainActivity.isRepeat;
import static mp3player.durgesh.com.mp3player.MainActivity.isShuffle;
import static mp3player.durgesh.com.mp3player.MainActivity.mediaPlayer;
import static mp3player.durgesh.com.mp3player.MainActivity.songTitle;

/**
 * Created by Rebelute User on 6/17/2016.
 */
public class TrackFragment extends Fragment implements View.OnClickListener, MediaPlayer.OnCompletionListener {

    RecyclerView recycleView,recycleView1;
    View view;
    Activity activity;
    LinearLayout openPlayer;
    ImageView imageSong, imagePlay;
    TextView textSongName;
    ImageView back, viewAllSongs;
    //ArrayList<HashMap<String, String>> songsListData;
    ArrayList<Bitmap> bimapList;
    RecyclerView.ItemDecoration itemDecoration;
    private ArrayList<Audio_Bean> movieList = new ArrayList<>();
    public ArrayList<Audio_Bean> recentsongsList = new ArrayList<Audio_Bean>();

    All_SongsAdapter adapter;
    Recent_SongsAdapter1 adapter1;
    LongOperationAudio caudAsyCh;

    ViewPager viewPagerStaff;
//    CirclePageIndicator testimonialCirclePage;
    private Realm realm;
    private ArrayList<Integer> favSongdIdList;
    private MusicPlayerInterface musicPlayerInterface;
    //String songTitle="";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_track, container, false);
        bimapList = new ArrayList<>();
        setIds(view);

//        testimonialCirclePage = (CirclePageIndicator) view.findViewById(R.id.testimonialCirclePage);
//        viewPagerStaff = (ViewPager) view.findViewById(R.id.viewpagerReview);
//        viewPagerStaff.setAdapter(new SliderAudioGallery(getActivity()));
//        testimonialCirclePage.setViewPager(viewPagerStaff);



        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recycleView = (RecyclerView) view.findViewById(R.id.recycleView);
        adapter = new All_SongsAdapter(getActivity(), movieList);
        recycleView.setLayoutManager(mLayoutManager);
        recycleView.setItemAnimator(new DefaultItemAnimator());
        //RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), LinearLayoutManager.HORIZONTAL, 10);
        //recycleView.addItemDecoration(itemDecoration);
        recycleView.setAdapter(adapter);

        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recycleView1 = (RecyclerView) view.findViewById(R.id.recycleView1);

        adapter1 = new Recent_SongsAdapter1(getActivity(), recentsongsList);
        recycleView1.setLayoutManager(mLayoutManager1);
        recycleView1.setItemAnimator(new DefaultItemAnimator());
       // RecyclerView.ItemDecoration itemDecoration1 = new DividerItemDecoration(getActivity(), LinearLayoutManager.HORIZONTAL, 10);
        //recycleView1.addItemDecoration(itemDecoration1);

        recycleView1.setAdapter(adapter1);

        Log.d("MOST PLAYED", "MOST PLAYED adapter: "+adapter1.getItemCount());



        caudAsyCh = new LongOperationAudio();
        caudAsyCh.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        return view;
    }

    private void checkForMostPlayed() {
        Log.d("MOST PLAYED", "MOST PLAYED checkForMostPlayed: ");
        RealmResults<Audio_Bean> results = realm.where(Audio_Bean.class).findAll();

        favSongdIdList = new ArrayList<>();

        for (Audio_Bean audio_bean : results)
            favSongdIdList.add(Integer.valueOf(audio_bean.getSongID()));

        //remove all duplicates
        Set<Integer> hs = new HashSet<>();
        hs.addAll(favSongdIdList);
        favSongdIdList.clear();
        favSongdIdList.addAll(hs);

        if (favSongdIdList.size() > 1)
            recentsongsList.clear();

        for (Audio_Bean audio_bean : movieList){

            Log.d("MOST PLAYED", "MOST PLAYED XX checkForMostPlayed: "+Integer.parseInt(audio_bean.getSongID()));

            if (favSongdIdList.contains(Integer.parseInt(audio_bean.getSongID())) ){
                recentsongsList.add(audio_bean);
            }
        }

        adapter1.notifyDataSetChanged();

        if (adapter1.getItemCount() > 1)
            recycleView1.setVisibility(View.VISIBLE);


        Log.d("MOST PLAYED", "MOST PLAYED checkForMostPlayed: "+results.size());
        Log.d("MOST PLAYED", "MOST PLAYED checkForMostPlayed: "+recentsongsList.size());
    }

    @Override
    public void onResume() {
        super.onResume();

        if (currentSongIndex != -1) {
            if (movieList != null) {

                if (movieList.size() > 0) {
                    textSongName.setText(songTitle);

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


                    SharedPreferences pre =getActivity().getSharedPreferences("SILOSD", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pre.edit();
                    int pos = pre.getInt("isSong",0);


                    int albumId = Integer.valueOf(movieList.get(currentSongIndex).getSongID());


                    Uri sArtworkUri = Uri
                            .parse("content://media/external/audio/albumart");
                    Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

                    System.out.print(albumArtUri.toString());

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(
                                activity.getContentResolver(), albumArtUri);
                        //bitmap = Bitmap.createScaledBitmap(bitmap, 30, 30, true);
                        imageSong.setImageBitmap(bitmap);

                    } catch (FileNotFoundException exception) {
                        exception.printStackTrace();
                        bitmap = BitmapFactory.decodeResource(activity.getResources(),
                                R.drawable.audio_download_icon);
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
        if (movieList.size() > 0) {
            playSong(0);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        musicPlayerInterface = (MusicPlayerInterface)context;

        //Toast.makeText(context, "Tracks Fragment", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
       // textSongName.setText("");
//        mediaPlayer.pause();
    }

    private void setIds(View view) {
        activity = getActivity();
        openPlayer = (LinearLayout) view.findViewById(R.id.openPlayer);
        imageSong = (ImageView) view.findViewById(R.id.imageSong);
        imagePlay = (ImageView) view.findViewById(R.id.imagePlay);
        //songsListData = new ArrayList<HashMap<String, String>>();
        textSongName = (TextView) view.findViewById(R.id.textSongName);
        // Mediaplayer
        mediaPlayer = new MediaPlayer();
        viewAllSongs = (ImageView) view.findViewById(R.id.viewAllCategory);

        ImageView viewFavourate = (ImageView)view.findViewById(R.id.imgViewAllFavourate);
        viewFavourate.setOnClickListener(this);
        viewAllSongs.setOnClickListener(this);
        imagePlay.setOnClickListener(this);
        openPlayer.setOnClickListener(this);
        //back.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.openPlayer:

                Intent intent = new Intent(getActivity(), MusicPlayerActviity.class);
                intent.putExtra("SongName", textSongName.getText().toString());
                //intent.putExtra("SongImg", bitmap);
               /* if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(ActivitySiloMusicPlayerList.this,
                            Pair.create(v, "selectedSong")
                    ).toBundle();

                    startActivity(intent, bundle);
                } else {
                    startActivity(intent);
                }*/
                startActivity(intent);

                break;

            case R.id.imagePlay:
                try {
                    if (mediaPlayer != null){
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.pause();
                            musicPlayerInterface.pause();
                            // Changing button image to play button
                            imagePlay.setImageResource(R.drawable.btn_play);
                        } else {
                            // Resume song
                            mediaPlayer.start();
                            musicPlayerInterface.play();
                            // Changing button image to pause button
                            imagePlay.setImageResource(R.drawable.btn_pause);
                        }

                    }

                }catch (Exception ex){

                }
                break;


            case R.id.viewAllCategory:
                callSongGrid(false);
                break;
            case R.id.imgViewAllFavourate:
                callSongGrid(true);
                break;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // check for repeat is ON or OFF
        if (isRepeat) {
            // repeat is on play same song again
            playSong(currentSongIndex);
        } else if (isShuffle) {
            // shuffle is on - play a random song
            Random rand = new Random();
            currentSongIndex = rand.nextInt((movieList.size() - 1) - 0 + 1) + 0;
            playSong(currentSongIndex);
        } else {
            // no repeat or shuffle ON - play next song
            if (currentSongIndex < (movieList.size() - 1)) {
                playSong(currentSongIndex + 1);
                currentSongIndex = currentSongIndex + 1;
            } else {
                // play first song
                playSong(0);
                currentSongIndex = 0;
            }
        }

    }

    /**
     * Function to play a song
     *
     * @param songIndex - index of song
     */
    public void playSong(int songIndex) {
        // Play song
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(movieList.get(songIndex).getAudioPath());
            mediaPlayer.prepare();
            mediaPlayer.start();

            musicPlayerInterface.play();

//            Realm realm = Realm.getInstance(getActivity());
            realm.beginTransaction();
            Audio_Bean bin = realm.createObject(Audio_Bean.class);

            bin.setAudioName(movieList.get(songIndex).getAudioName());
            bin.setAudDuration(movieList.get(songIndex).getAudDuration());
            bin.setAudioPath(movieList.get(songIndex).getAudioPath());
            bin.setSongID(movieList.get(songIndex).getSongID());

            // Displaying Song title
            songTitle = movieList.get(songIndex).getAudioName();
            textSongName.setText(songTitle);
            musicPlayerInterface.play();
            musicPlayerInterface.setTitle(songTitle);
            musicPlayerInterface.setInstance(this);


            int albumId = Integer.valueOf(movieList.get(songIndex).getSongID());

            Uri sArtworkUri = Uri
                    .parse("content://media/external/audio/albumart");
            Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

            System.out.print(albumArtUri.toString());

            try {
//                bitmap = MediaStore.Images.Media.getBitmap(
//                        activity.getContentResolver(), albumArtUri);
                //bitmap = Bitmap.createScaledBitmap(bitmap, 30, 30, true);
                imageSong.setImageBitmap(movieList.get(songIndex).getAudioBitmapImg());

                bin.setAudioBitmapImg(movieList.get(songIndex).getAudioBitmapImg());

                Log.d("",""+bin.getAudioName()+"\tClick="+movieList.get(songIndex).getAudioName());

                realm.commitTransaction();

                checkForMostPlayed();

                if (recentsongsList.size()>0)
                {
                    recycleView1.setVisibility(View.VISIBLE);
                    adapter1.notifyDataSetChanged();
                }


            } catch (Exception e) {
                bitmap = BitmapFactory.decodeResource(activity.getResources(),
                        R.drawable.audio_download_icon);
                imageSong.setImageBitmap(bitmap);
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

    private void call(int l) {


    }

    public void playPause() {
        imagePlay.performClick();
    }


    public class All_SongsAdapter extends RecyclerView.Adapter<All_SongsAdapter.ViewHolder> {

        Activity mContext;
        ArrayList<Audio_Bean> song;
        View itemLayoutView;

        public All_SongsAdapter(Activity mContext, ArrayList<Audio_Bean> movieList) {
            this.mContext = mContext;
            this.song = movieList;
        }

        @Override
        public All_SongsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.playlist_item, parent, false);


            // create ViewHolder
            All_SongsAdapter.ViewHolder viewHolder = new All_SongsAdapter.ViewHolder(itemLayoutView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(All_SongsAdapter.ViewHolder holder, int position) {

            holder.name.setText(song.get(position).getAudioName());

            holder.imgThumb.setImageBitmap(song.get(position).getAudioBitmapImg());
            holder.mainLay.setTag(position);
            holder.mainLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (Integer) v.getTag();
                    //song.get(pos);
                    if (pos != -1) {
                        currentSongIndex = pos;
                        playSong(pos);
//                        ActivitySiloMusicPlayerList mlist = new ActivitySiloMusicPlayerList();
//                        mlist.playSong(pos);

                        SharedPreferences pre =mContext.getSharedPreferences("SILOSD", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pre.edit();
                        editor.putInt("isSong",pos);
                        editor.commit();
                    }
                }
            });


        }

        @Override
        public int getItemCount() {
            return song.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public ImageView imgThumb;
            public FrameLayout listClick;
            private TextView name, vidTime;
            private LinearLayout mainLay;

            public ViewHolder(View itemLayoutView) {
                super(itemLayoutView);
                imgThumb = (ImageView) itemLayoutView.findViewById(R.id.imgThumb);
                name = (TextView) itemLayoutView.findViewById(R.id.songTitle);
                mainLay = (LinearLayout) itemLayoutView.findViewById(R.id.mainLay);

            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {


                }
            }
        }


        public String getExtention(String path) {
            String pathExt = "";
            try {
                String filenameArray[] = path.split("\\.");
                pathExt = filenameArray[filenameArray.length - 1];
            } catch (Exception e) {

            }

            return pathExt;
        }

    }



    private class Recent_SongsAdapter1 extends RecyclerView.Adapter<Recent_SongsAdapter1.ViewHolder> {

        Activity mContext;
        ArrayList<Audio_Bean> song;
        View itemLayoutView;


        public Recent_SongsAdapter1(Activity mContext, ArrayList<Audio_Bean> recentsongsList) {
            this.mContext = mContext;
            this.song = recentsongsList;

        }

        @Override
        public Recent_SongsAdapter1.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.playlist_item, parent, false);
            // create ViewHolder
            Recent_SongsAdapter1.ViewHolder viewHolder = new Recent_SongsAdapter1.ViewHolder(itemLayoutView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(Recent_SongsAdapter1.ViewHolder holder, int position) {
            Log.d("MOST PLAYED", "MOST PLAYED checkForMostPlayed bind: "+position);

            holder.name.setText(song.get(position).getAudioName());

            try {

                int albumId = Integer.valueOf(song.get(position).getSongID());

                Uri sArtworkUri = Uri
                        .parse("content://media/external/audio/albumart");
                Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                        activity.getContentResolver(), albumArtUri);
                holder.imgThumb.setImageBitmap(bitmap);
            } catch (IOException e) {
                Log.d("MOST PLAYED", "MOST PLAYED checkForMostPlayed bind: "+e.getMessage());

                e.printStackTrace();
                holder.imgThumb.setImageBitmap(song.get(position).getAudioBitmapImg());
            }

            holder.mainLay.setTag(position);
            holder.mainLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (Integer) v.getTag();
                    //song.get(pos);
                    if (pos != -1) {
                        currentSongIndex = pos;
                        playSong(pos);
//                        ActivitySiloMusicPlayerList mlist = new ActivitySiloMusicPlayerList();
//                        mlist.playSong(pos);

                        SharedPreferences pre = mContext.getSharedPreferences("SILOSD", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pre.edit();
                        editor.putInt("isSong", pos);
                        editor.commit();
                    }
                }
            });

            Log.d("MOST PLAYED", "MOST PLAYED checkForMostPlayed bind end: "+position);

        }

        @Override
        public int getItemCount() {
            return song.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public ImageView imgThumb;
            public FrameLayout listClick;
            private TextView name, vidTime;
            private LinearLayout mainLay;

            public ViewHolder(View itemLayoutView) {
                super(itemLayoutView);


                imgThumb = (ImageView) itemLayoutView.findViewById(R.id.imgThumb);
                name = (TextView) itemLayoutView.findViewById(R.id.songTitle);
                mainLay = (LinearLayout) itemLayoutView.findViewById(R.id.mainLay);

            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {


                }
            }

        }


        public String getExtention(String path) {
            String pathExt = "";
            try {
                String filenameArray[] = path.split("\\.");
                pathExt = filenameArray[filenameArray.length - 1];
            } catch (Exception e) {

            }

            return pathExt;

        }

    }

    public void callSongGrid(boolean isForFav) {
        musicPlayerInterface.unregisterReceiver();
        Intent intent = new Intent(getActivity(), FragmentSiloMusicSongGrid.class);
        intent.putExtra("SongName",textSongName.getText().toString());
        intent.putExtra("isForFav",isForFav);
        if (isForFav)
            intent.putExtra("favSongdIdList",favSongdIdList);
        startActivity(intent);
    }

    private class LongOperationAudio extends AsyncTask<String, Audio_Bean, String> {


        @Override
        protected String doInBackground(String... params) {
            try {

                movieList.clear();
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

                cursor = getActivity().managedQuery(
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

                    Log.d("albumArtUri=",""+albumArtUri);

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(
                                activity.getContentResolver(), albumArtUri);
                        //bitmap = Bitmap.createScaledBitmap(bitmap, 30, 30, true);

                        bin.setAudioBitmapImg(bitmap);

                    } catch (FileNotFoundException exception) {
                        exception.printStackTrace();
                        bitmap = BitmapFactory.decodeResource(activity.getResources(),
                                R.drawable.audio_download_icon);

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
            checkForMostPlayed();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Audio_Bean... values) {
            if (values[0] != null) {
                movieList.add(values[0]);
                System.out.println("ImagePath:" + values);
                if (movieList != null) {
                    if (movieList.size() > 0) {

                        adapter.notifyItemInserted(movieList.size() - 1);

                    }
                }


            }

        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

       // Toast.makeText(getActivity(), "1st", Toast.LENGTH_SHORT).show();

        if (isVisibleToUser) {
            onResume();
//            TextView textSongName = (TextView)view.findViewById(R.id.textSongName);
//            textSongName.setText(songTitle);
        }


        if (!isVisibleToUser)
        {
           //mediaPlayer.pause();
        }
    }



}
