package com.example.famiorg.logic;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class DailyEvent implements Serializable {
    private String eventId = String.valueOf(UUID.randomUUID());
    private String title;
    private String description;
    private Date dateTimeStart;
    private Date dateTimeEnd;
    private ArrayList<String> familyMembersParticipatingEmails;

    public String getEventId() {
        return eventId;
    }

    public DailyEvent setEventId(String eventId) {
        this.eventId = eventId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public DailyEvent setTitle(String title) {
        this.title = title;
        return this;
    }

    @Exclude
    public LocalDateTime getLocalDateTimeStart() {
        return dateTimeStart.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public Date getDateTimeStart() {
        return dateTimeStart;
    }

    @Exclude
    public DailyEvent setLocalDateTimeStart(LocalDateTime dateTimeStart) {
        this.dateTimeStart = java.util.Date
                .from(dateTimeStart.atZone(ZoneId.systemDefault())
                        .toInstant());
        return this;
    }

    public DailyEvent setDateTimeStart(Date dateTimeStart) {
        this.dateTimeStart = dateTimeStart;

        return this;
    }

    @Exclude
    public LocalDateTime getLocalDateTimeEnd() {
        return dateTimeEnd.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public Date getDateTimeEnd() {
        return dateTimeEnd;
    }

    @Exclude
    public DailyEvent setLocalDateTimeEnd(LocalDateTime dateTimeEnd) {
        this.dateTimeEnd = java.util.Date
                .from(dateTimeEnd.atZone(ZoneId.systemDefault())
                        .toInstant());
        return this;
    }

    public DailyEvent setDateTimeEnd(Date dateTimeEnd) {
        this.dateTimeEnd = dateTimeEnd;

        return this;
    }

    public String getDescription() {
        return description;
    }

    public DailyEvent setDescription(String description) {
        this.description = description;
        return this;
    }

    public ArrayList<String> getFamilyMembersParticipatingEmails() {
        return familyMembersParticipatingEmails;
    }

    public DailyEvent setFamilyMembersParticipatingEmails(ArrayList<String> familyMembersParticipatingEmails) {
        this.familyMembersParticipatingEmails = familyMembersParticipatingEmails;
        return this;
    }
}
