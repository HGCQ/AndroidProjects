package hgcq.model.dto;

import java.io.Serializable;

public class MemberInvitationDTO implements Serializable {

    private EventDTO eventDTO;
    private MemberDTO memberDTO;

    public MemberInvitationDTO() {
    }

    public EventDTO getEventDTO() {
        return eventDTO;
    }

    public MemberDTO getMemberDTO() {
        return memberDTO;
    }



    public MemberInvitationDTO(EventDTO eventDTO, MemberDTO memberDTO) {
        this.eventDTO = eventDTO;
        this.memberDTO = memberDTO;
    }
}