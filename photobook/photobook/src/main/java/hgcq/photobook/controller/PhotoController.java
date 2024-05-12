package hgcq.photobook.controller;

import hgcq.photobook.domain.Photo;
import hgcq.photobook.service.EventService;
import hgcq.photobook.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/photo")
public class PhotoController {

    private final PhotoService photoService;
    private final EventService eventService;

    @PostMapping("/upload/{eventId}")
    public Photo uploadPhoto(@PathVariable("eventId") int eventId) {
        return null;
    }
}
