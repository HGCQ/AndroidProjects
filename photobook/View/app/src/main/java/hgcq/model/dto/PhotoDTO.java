package hgcq.model.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class PhotoDTO implements Serializable {

    private String imageName;

    private byte[] image;

    private String date;

    public PhotoDTO() {
    }

    public PhotoDTO(String imageName, String date) {
        this.imageName = imageName;
        this.date = date;
    }

    public PhotoDTO(String imageName, byte[] image, String date) {
        this.imageName = imageName;
        this.image = image;
        this.date = date;
    }

    public String getImageName() {
        return imageName;
    }

    public byte[] getImage() {
        return image;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "PhotoDTO{" +
                "imageName='" + imageName + '\'' +
                ", date=" + date +
                '}';
    }
}
