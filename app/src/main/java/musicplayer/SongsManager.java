package musicplayer;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

public class SongsManager {
    // SDCard Path
    final String MEDIA_PATH = new String("/");
    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

    // Constructor
    public SongsManager() {

    }

    /**
     * Function to read all mp3 files from sdcard
     * and store the details in ArrayList
     */
    public ArrayList<HashMap<String, String>> getPlayList(Activity activity) {
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

            cursor = activity.managedQuery(
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

                HashMap<String, String> song = new HashMap<String, String>();
                song.put("songTitle", displayName.substring(0, (displayName.length() - 4)));
                song.put("songPath", path);
                song.put("displayPic",ulbumImg);

                // Adding each song to SongList
                songsList.add(song);

            }

        } catch (Exception e) {
            System.out.print("");
        }

        /*File home = new File(MEDIA_PATH);

        try {
            if (home.listFiles(new FileExtensionFilter()).length > 0) {
                for (File file : home.listFiles(new FileExtensionFilter())) {
                    HashMap<String, String> song = new HashMap<String, String>();
                    song.put("songTitle", file.getName().substring(0, (file.getName().length() - 4)));
                    song.put("songPath", file.getPath());

                    // Adding each song to SongList
                    songsList.add(song);
                }
            }
        } catch (Exception e) {
            Log.e("Exception",""+e);
        }*/

        // return songs list array
        return songsList;
    }

    /**
     * Class to filter files which are having .mp3 extension
     */
    class FileExtensionFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return (name.endsWith(".mp3") || name.endsWith(".MP3"));
        }
    }
}
