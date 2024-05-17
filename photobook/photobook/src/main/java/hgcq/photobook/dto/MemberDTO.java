package hgcq.photobook.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
public class MemberDTO implements Serializable {

    private String name;
    private String email;
    private String password;

    public MemberDTO() {
    }

    public MemberDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "MemberDTO{" +
                "name='" + name + '\'' +
                ", email='" + email;
    }
}
