package hgcq.model.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class PhotoDTO implements Serializable {

    private String imageName;

    private String date;

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

}
