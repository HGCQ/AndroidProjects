package hgcq.model.dto;

import java.io.Serializable;

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

    public MemberDTO(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "MemberDTO{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'';
    }
}

