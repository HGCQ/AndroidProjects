package hgcq.photobook.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInvitationDTO implements Serializable {

    private EventDTO eventDTO;
    private MemberDTO memberDTO;

    public MemberInvitationDTO(EventDTO eventDTO, MemberDTO memberDTO) {
        this.eventDTO = eventDTO;
        this.memberDTO = memberDTO;
    }
}
