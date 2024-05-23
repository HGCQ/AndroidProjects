package hgcq.model.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class PhotoDTO implements Serializable {

    private String imageName;

    private String date;

    private String path;

    public PhotoDTO() {
    }

    public PhotoDTO(String imageName, String date) {
        this.imageName = imageName;
        this.date = date;
    }

    public String getImageName() {
        return imageName;
    }


    public String getDate() {
        return date;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
