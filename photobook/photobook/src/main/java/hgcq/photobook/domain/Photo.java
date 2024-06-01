package hgcq.photobook.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Photo {

    @Id @GeneratedValue
    @Column(name = "photo_id")
    private Long id;

    private String imageName;

    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="event_id")
    private Event event;

    public Photo(String imageName, String path, Event event) {
        this.imageName = imageName;
        this.path = path;
        this.event = event;
    }
}
