package hgcq.photobook.dto;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class PhotoDTO implements Serializable {

    private String imageName;

    private byte[] image;

    private String date;

    public PhotoDTO() {
    }

    public PhotoDTO(String imageName, byte[] image, String date) {
        this.imageName = imageName;
        this.image = image;
        this.date = date;
    }

    @Override
    public String toString() {
        return "PhotoDTO{" +
                "imageName='" + imageName + '\'' +
                ", date=" + date +
                '}';
    }
}
