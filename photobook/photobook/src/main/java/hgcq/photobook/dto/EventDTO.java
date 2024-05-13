package hgcq.photobook.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class EventDTO implements Serializable {

    private String name;
    private LocalDate date;

    public EventDTO() {
    }

    public EventDTO(String name, LocalDate date) {
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "EventDTO{" +
                "name='" + name + '\'' +
                ", date=" + date +
                '}';
    }
}
