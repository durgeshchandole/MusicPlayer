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
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
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
import mp3player.durgesh.com.mp3player.MusicPlayerActviity;
import mp3player.durgesh.com.mp3player.R;
import mp3player.durgesh.com.mp3player.bean.Audio_Bean;
import mp3player.durgesh.com.mp3player.bean.CartItemsListBean;
import musicplayer.SongsManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Rebelute User on 6/17/2016.
 */



import static mp3player.durgesh.com.mp3player.MainActivity.bitmap;
import static mp3player.durgesh.com.mp3player.MainActivity.currentSongIndex;
import static mp3player.durgesh.com.mp3player.MainActivity.isRepeat;
import static mp3player.durgesh.com.mp3player.MainActivity.isShuffle;
import static mp3player.durgesh.com.mp3player.MainActivity.mediaPlayer;
import static mp3player.durgesh.com.mp3player.MainActivity.songTitle;



public class AlbumFragment extends Fragment implements View.OnClickListener , MediaPlayer.OnCompletionListener {

    MusicPlayerInterface musicPlayerInterface;

    //ArrayList<HashMap<String, String>> songsListData;
    ArrayList<Bitmap> bimapList;

//    CirclePageIndicator testimonialCirclePage;

        private Handler mHandler = new Handler();
        private LongOperationAudio caudAsyCh;
        private SongsManager songManager;

        ImageView back;
        Activity mActivity;
        RecyclerView AlbumRecyclerView;
        private AlbumList_Adapter adapter;
        DividerItemDecoration itemDecoration;
        private ArrayList<HashMap<String, String>> AlbumSongData = new ArrayList<HashMap<String, String>>();
        private ArrayList<Audio_Bean> movieList = new ArrayList<Audio_Bean>();
        public ArrayList<Audio_Bean> recentsongsList = new ArrayList<Audio_Bean>();

        LinearLayout openPlayer;
        ImageView imageSong, imagePlay;
        TextView textSongName;

        View view;
        Activity activity;
        //String songTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_blank, container, false);

        setIds(view);

        // Getting all songs list
        //AlbumSongData = songManager.getPlayList(getActivity());


        caudAsyCh = new LongOperationAudio();
        caudAsyCh.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        for (int i=0;i<movieList.size();i++)
        {

            Log.d("AlbumSongDataDAta=",""+movieList.get(i).getAudioName().toString());
        }


        return view;
    }

    private void setIds(View view) {
        songManager = new SongsManager();

        activity = getActivity();
        openPlayer = (LinearLayout) view.findViewById(R.id.openPlayer);
        imageSong = (ImageView) view.findViewById(R.id.imageSong);
        imagePlay = (ImageView) view.findViewById(R.id.imagePlay);
        //songsListData = new ArrayList<HashMap<String, String>>();
        textSongName = (TextView) view.findViewById(R.id.textSongName);
         // Mediaplayer
        mediaPlayer = new MediaPlayer();

        imagePlay.setOnClickListener(this);
        openPlayer.setOnClickListener(this);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        AlbumRecyclerView = (RecyclerView) view.findViewById(R.id.AlbumList);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(AlbumRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        AlbumRecyclerView.setLayoutManager(mLayoutManager);
        //cartList.setItemAnimator(new DefaultItemAnimator());
        AlbumRecyclerView.addItemDecoration(itemDecoration);


        adapter = new AlbumList_Adapter(getActivity(),movieList);
        AlbumRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }





//    private ArrayList<Audio_Bean> getAlbumSongsList() {
//
//        ArrayList<Audio_Bean> itemList = new ArrayList<Audio_Bean>();
//
//        Cursor cursor = getActivity().managedQuery(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
//                new String[] {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
//                MediaStore.Audio.Albums._ID+ "=?",
//                new String[] {String.valueOf(albumId)},
//                null);
//
//        if (cursor.moveToFirst()) {
//            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
//            // do whatever you need to do
//        }
//
//        return itemList;
//    }

    @Override
    public void onResume() {
        super.onResume();


        if (currentSongIndex != -1) {
            if (movieList != null) {

                if (movieList.size() > 0) {
                    textSongName.setText(songTitle);



                    SharedPreferences pre =getActivity().getSharedPreferences("SILOSD", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pre.edit();
                    int pos = pre.getInt("isSong",0);

//                    try {
//                       //mediaPlayer.reset();
//                        mediaPlayer.setDataSource(movieList.get(pos).getAudioPath());
//                        mediaPlayer.prepare();
//                        mediaPlayer.start();


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

                    songTitle = movieList.get(currentSongIndex).getAudioName();//songsList.get(currentSongIndex).get("songTitle");
                    textSongName.setText(songTitle);

                    // Changing Button Image to pause image
                    if (mediaPlayer != null) {
                        if (mediaPlayer.isPlaying() ) {
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

//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }

                }
            }



        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        musicPlayerInterface = (MusicPlayerInterface)context;
       // Toast.makeText(context, "Artist Fragment", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

       // Toast.makeText(getActivity(), "2nd", Toast.LENGTH_SHORT).show();
       // String songTitle = movieList.get(currentSongIndex).getAudioName();//songsList.get(currentSongIndex).get("songTitle");

        if (isVisibleToUser) {
            onResume();
//            TextView textSongName = (TextView) view.findViewById(R.id.textSongName);
//            textSongName.setText(songTitle);
        }

        if (!isVisibleToUser)
        {
           // mediaPlayer.pause();
          //  mediaPlayer.prepare();
        }



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
                if (mediaPlayer.isPlaying()) {
                    if (mediaPlayer != null) {
                        mediaPlayer.pause();
                        musicPlayerInterface.pause();
                        // Changing button image to play button
                        imagePlay.setImageResource(R.drawable.btn_play);
                    }
                } else {
                    // Resume song
                    if (mediaPlayer != null) {
                        mediaPlayer.start();
                        musicPlayerInterface.play();
                        // Changing button image to pause button
                        imagePlay.setImageResource(R.drawable.btn_pause);
                    }
                }
                break;


        }
    }

    public void playPause() {
        imagePlay.performClick();
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
                uri = MediaStore.Audio.Artists.INTERNAL_CONTENT_URI;
                String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
                String[] projection = {MediaStore.Audio.Artists._ID,
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

    // Adapter call.....
    private class AlbumList_Adapter extends RecyclerView.Adapter<AlbumList_Adapter.ViewHolder> {

        Context mContext;
        View itemLayoutView;
        ArrayList<Audio_Bean> song;
        private ArrayList<CartItemsListBean.CartListBean> ArrayCartList;

        public AlbumList_Adapter(Context mContext, ArrayList<Audio_Bean> movieList) {

            this.mContext = mContext;
            this.song = movieList;
        }

        @Override
        public AlbumList_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.playlist_album_item, parent, false);

            // create ViewHolder
            AlbumList_Adapter.ViewHolder viewHolder = new AlbumList_Adapter.ViewHolder(itemLayoutView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(AlbumList_Adapter.ViewHolder holder, final int position) {

            final Audio_Bean bin = song.get(position);
            final int Positions = position;
            int po= Positions+1;

            holder.name.setText(bin.getAudioName());
Log.d("getAudioName=",""+bin.getAudioName()+"\tIMAges="+bin.getAudioBitmapImg());
            holder.imgThumb.setImageBitmap(bin.getAudioBitmapImg());

            holder.mainLay.setTag(position);
            holder.mainLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (Integer) v.getTag();
                    //song.get(pos);
                    if (pos != -1) {
                        currentSongIndex = pos;
                        playSong(pos);

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


        public class ViewHolder extends RecyclerView.ViewHolder {

            public ImageView imgThumb;
            public FrameLayout listClick;
            private TextView name, vidTime;
            private LinearLayout mainLay;


            public ViewHolder(View view) {
                super(view);


                imgThumb = (ImageView) itemLayoutView.findViewById(R.id.imgThumb);
                name = (TextView) itemLayoutView.findViewById(R.id.songTitle);
                mainLay = (LinearLayout) itemLayoutView.findViewById(R.id.mainLay);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                        Intent in = new Intent(Activity_Cart.this, ActivityProductDetail.class);
//                        in.putExtra("image",AppConst.BASE_IMAGE_URL+ArrayCartList1.get(getAdapterPosition()).getProduct_image_path());
//                        //                       in.putExtra("from",ArrayCartList.get(getAdapterPosition()).getFrom());
//                        in.putExtra("prod_id",ArrayCartList1.get(getAdapterPosition()).getProduct_id());
//                        in.putExtra("onlyShow","1");
//                        startActivity(in);

//                        startActivity(in);
                    }
                });

            }

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
        Log.d("PAth=",""+movieList.get(songIndex).getAudioPath());

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(movieList.get(songIndex).getAudioPath());
            mediaPlayer.prepare();
            mediaPlayer.start();



            // Displaying Song title
            songTitle = movieList.get(songIndex).getAudioName();
            textSongName.setText(songTitle);
            musicPlayerInterface.play();
            musicPlayerInterface.setTitle(songTitle);
            musicPlayerInterface.setInstance(this);

            Audio_Bean bin = new Audio_Bean();
            bin.setAudioName(movieList.get(songIndex).getAudioName());
            bin.setAudDuration(movieList.get(songIndex).getAudDuration());
            bin.setAudioPath(movieList.get(songIndex).getAudioPath());
            bin.setSongID(movieList.get(songIndex).getSongID());

//            TrackFragment tf = new TrackFragment();
//            tf.recentsongsList.add(bin);

            int albumId = Integer.valueOf(movieList.get(songIndex).getSongID());

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
            imagePlay.setImageResource(R.drawable.btn_pause);


        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
