package hgcq.photobook.service;

import hgcq.photobook.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
}
