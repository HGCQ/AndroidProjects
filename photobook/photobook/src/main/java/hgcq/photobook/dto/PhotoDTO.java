package hgcq.photobook.dto;

import java.io.Serializable;
import java.util.Arrays;

public class PhotoDTO implements Serializable {

    private String imageName;

    private byte[] image;

    public PhotoDTO() {
    }

    public PhotoDTO(String imageName, byte[] image) {
        this.imageName = imageName;
        this.image = image;
    }

    public String getImageName() {
        return imageName;
    }

    public byte[] getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "PhotoDTO: " +
                "imageName='" + imageName;
    }
}
