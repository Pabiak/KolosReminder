package me.pabiak.kolosreminder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String subject;
    private String description;
    private String date;

    public Reminder() {
    }

    public Reminder(String type, String subject, String description, String date) {
        this.type = type;
        this.subject = subject;
        this.description = description;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getSubject() {
        return subject;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

}