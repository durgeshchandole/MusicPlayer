package Interface;

import android.support.v4.app.Fragment;

import mp3player.durgesh.com.mp3player.bean.Audio_Bean;

import java.util.ArrayList;

/**
 * Created by Rebelute User on 12/29/2017.
 */

public interface MusicPlayerInterface {
    void play();
    void pause();
    void playNext(int current);
    void playPrevious(int current);
    void setList(ArrayList<Audio_Bean> musicList);


    void setTitle(String songTitle);

    void setInstance(Fragment fragment);

    void unregisterReceiver();
}
