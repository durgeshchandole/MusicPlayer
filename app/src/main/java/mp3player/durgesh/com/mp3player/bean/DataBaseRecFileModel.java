package mp3player.durgesh.com.mp3player.bean;

/**
 * Created by ketan.parmar on 11/4/2014.
 */
public class DataBaseRecFileModel {
    private int id;
    private String title;
    private String filePath;

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    private String fileType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String desc) {
        this.filePath = desc;
    }


}
