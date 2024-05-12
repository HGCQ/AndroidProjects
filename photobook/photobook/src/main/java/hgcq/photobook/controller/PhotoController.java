package hgcq.photobook.controller;

import hgcq.photobook.domain.Event;
import hgcq.photobook.domain.Photo;
import hgcq.photobook.dto.PhotoDTO;
import hgcq.photobook.service.EventService;
import hgcq.photobook.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/photo")
public class PhotoController {

    private final PhotoService photoService;
    private final EventService eventService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadPhoto(@RequestBody PhotoDTO photoDTO) {
        Event event = eventService.getEvent(LocalDate.parse(photoDTO.getDate()));
        if (event != null) {
            Photo photo = new Photo(photoDTO.getImageName(), photoDTO.getImage(), event);
            photoService.uploadPhoto(photo);
            return ResponseEntity.ok().body("Photo Upload Success");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Photo Upload Fail");
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deletePhoto(@RequestBody PhotoDTO photoDTO) {
        Event event = eventService.getEvent(LocalDate.parse(photoDTO.getDate()));
        if (event != null) {
            Photo findPhoto = photoService.getPhoto(photoDTO.getImageName(), event);
            if (findPhoto != null) {
                photoService.deletePhoto(findPhoto.getImageName(), event);
                return ResponseEntity.ok().body("Photo Upload Success");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Photo Delete Fail");
    }

    @PostMapping("/photos")
    public ResponseEntity<?> getPhotos(@RequestBody String eventDate) {
        Event event = eventService.getEvent(LocalDate.parse(eventDate));
        if (event != null) {
            List<Photo> photos = photoService.photoList(event);
            List<PhotoDTO> photoDTOs = new ArrayList<>(photos.size());
            for (Photo photo : photos) {
                PhotoDTO photoDTO = new PhotoDTO(photo.getImageName(), photo.getImage(), eventDate);
                photoDTOs.add(photoDTO);
            }
            return ResponseEntity.ok().body(photoDTOs);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Photos Not Found");
    }
}
