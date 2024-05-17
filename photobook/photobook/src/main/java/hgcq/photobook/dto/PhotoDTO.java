package hgcq.photobook.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Getter
public class PhotoDTO implements Serializable {

    private String date;
    private String imageName;

    private MultipartFile image;

    public PhotoDTO() {
    }

    public PhotoDTO(String date, String imageName) {
        this.date = date;
        this.imageName = imageName;
    }

    public PhotoDTO(String date, MultipartFile image) {
        this.date = date;
        this.image = image;
    }

    @Override
    public String toString() {
        return "PhotoDTO{" +
                "image=" + image +
                ", date='" + date + '\'' +
                '}';
    }
}
