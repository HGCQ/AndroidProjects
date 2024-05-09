package hgcq.photobook.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
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

    @Lob
    private byte[] image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="event_id")
    private Event event;

    public Photo(String imageName, byte[] image, Event event) {
        this.imageName = imageName;
        this.image = image;
        this.event = event;
    }
}
