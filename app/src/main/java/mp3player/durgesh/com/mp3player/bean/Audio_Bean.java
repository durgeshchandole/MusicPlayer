package mp3player.durgesh.com.mp3player.bean;

import android.graphics.Bitmap;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.Index;

/**
 * Created by rebelute13 on 19/2/18.
 */

public class Audio_Bean extends RealmObject implements Serializable {

    @Index
    private int id;
    @Ignore
    private Bitmap audioBitmapImg;
    private String audioName;
    private boolean cellType;
    private String songID;
    private String audDuration;
    private String audioPath;
    @Ignore
    private boolean checked = false;



    public boolean isCellType() {
        return cellType;
    }

    public void setCellType(boolean cellType) {
        this.cellType = cellType;
    }


    public String getSongID() {
        return songID;
    }

    public void setSongID(String songID) {
        this.songID = songID;
    }


    public Bitmap getAudioBitmapImg() {
        return audioBitmapImg;
    }

    public void setAudioBitmapImg(Bitmap audioBitmapImg) {
        this.audioBitmapImg = audioBitmapImg;
    }



    public String getAudDuration() {
        return audDuration;
    }

    public void setAudDuration(String audDuration) {
        this.audDuration = audDuration;
    }


    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioLen) {
        this.audioPath = audioLen;
    }

    public String getAudioName() {
        return audioName;
    }

    public void setAudioName(String audioName) {
        this.audioName = audioName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

//    public Boolean getChecked() {
//        return checked;
//    }
//
//    public void setChecked(Boolean checked) {
//        checked = checked;
//    }


}
