package hgcq.photobook.service;

import hgcq.photobook.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepository;
}
