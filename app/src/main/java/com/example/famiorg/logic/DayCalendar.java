package com.example.famiorg.logic;

import java.time.LocalDate;
import java.util.ArrayList;

public class DayCalendar {
    private LocalDate date;
    private ArrayList<DailyEvent> events = new ArrayList<>();

    public LocalDate getDate() {
        return date;
    }

    public DayCalendar setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public ArrayList<DailyEvent> getEvents() {
        return events;
    }

    public DayCalendar setEvents(ArrayList<DailyEvent> events) {
        this.events = events;
        return this;
    }
}
