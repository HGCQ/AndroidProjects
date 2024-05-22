package hgcq.model.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class EventDTO implements Serializable {

    private String name;
    private String date;
    private String content;

    public EventDTO() {
    }

    public EventDTO(String name, String date) {
        this.name = name;
        this.date = date;
    }

    public EventDTO(String name, String date, String content) {
        this.name = name;
        this.date = date;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "EventDTO{" +
                "name='" + name + '\'' +
                ", date=" + date +
                '}';
    }
}