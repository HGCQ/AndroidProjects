package hgcq.photobook.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private String email;

    private String password;

    public Member(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @OneToMany(mappedBy="member")
    private List<Friend> friends = new ArrayList<>();

    @OneToMany(mappedBy="member")
    private List<EventMember> eventMembers = new ArrayList<>();
}
