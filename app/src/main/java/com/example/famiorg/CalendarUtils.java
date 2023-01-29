package com.example.famiorg;

import com.example.famiorg.callbacks.Callback_DataManager;
import com.example.famiorg.logic.DailyEvent;
import com.example.famiorg.logic.DayCalendar;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

public class CalendarUtils {
    private static CalendarUtils single_instance = null;
    private LocalDate selectedDate;

    private ArrayList<DailyEvent> dailyEvents = new ArrayList<>();

    Callback_DataManager<DailyEvent> getCallback_getDailyEventsRT = (Callback_DataManager) object -> dailyEvents.add((DailyEvent) object);

    public CalendarUtils(DataManager dataManager, String famId) {
        selectedDate = LocalDate.now().plusMonths(1);

        dataManager.setCallback_getDailyEvents(getCallback_getDailyEventsRT);
        dataManager.getDailyEventsRT(famId);
    }

    public static CalendarUtils getInstance(String famId)
    {
        if (single_instance == null) {
            if( Optional.ofNullable(famId).isPresent()) {
                DataManager dataManager = new DataManager();
                single_instance = new CalendarUtils(dataManager, famId);
            }
        }

        return single_instance;
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
    }

    public static String formattedDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        return date.format(formatter);
    }

    public static String formattedTime(LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        return time.format(formatter);
    }

    public static String formattedDuration(Duration duration) {
        return String.format("%d:%02d",
                duration.toHours(),
                duration.toMinutes()/60);
    }

    public static String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    public static ArrayList<DayCalendar> daysInMonthArray(LocalDate date) {
        ArrayList<DayCalendar> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = CalendarUtils.getInstance(null).selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue()+1;
        dayOfWeek = dayOfWeek > 7 ? 1 : dayOfWeek; //Sunday starts the week

        for (int i = 1; i <= 42; i++) {
            if (i < dayOfWeek || i >= daysInMonth + dayOfWeek) {
                daysInMonthArray.add(new DayCalendar()
                        .setDate(null));
            } else {
                daysInMonthArray.add(new DayCalendar()
                        .setDate(LocalDate.of(CalendarUtils.getInstance(null).selectedDate.getYear(),
                                CalendarUtils.getInstance(null).selectedDate.getMonth().getValue(),
                                i - (dayOfWeek-1))));
            }
        }

        for (DailyEvent event : CalendarUtils.getInstance(null).dailyEvents) {
            if (event.getLocalDateTimeStart().getYear() == CalendarUtils.getInstance(null).selectedDate.getYear() &&
                    event.getLocalDateTimeStart().getMonth().getValue() == CalendarUtils.getInstance(null).selectedDate.getMonth().getValue()) {
                daysInMonthArray.get(event.getLocalDateTimeStart().getDayOfMonth() + (dayOfWeek-1) - 1).getEvents().add(event);
            }
        }

        return daysInMonthArray;
    }

    public static ArrayList<DayCalendar> daysInWeekArray(LocalDate selectedDate) {
        ArrayList<DayCalendar> days = new ArrayList<>();
        LocalDate startDate = sundayForDate(selectedDate);
        LocalDate current = startDate;
        LocalDate endDate = current.plusWeeks(1);

        while (current.isBefore(endDate)) {
            days.add(new DayCalendar()
                    .setDate(LocalDate.of(current.getYear(),
                            current.getMonth().getValue(),
                            current.getDayOfMonth())));

            current = current.plusDays(1);
        }

        for (DailyEvent event : CalendarUtils.getInstance(null).dailyEvents) {
            if (event.getLocalDateTimeStart().toLocalDate().isAfter(startDate.minusDays(1)) &&
                    event.getLocalDateTimeStart().toLocalDate().isBefore(endDate)) {
                int dayOfWeek = event.getLocalDateTimeStart().getDayOfWeek().getValue() + 1;
                dayOfWeek = dayOfWeek > 7 ? 1 : dayOfWeek;
                days.get(dayOfWeek-1).getEvents().add(event);
            }
        }

        return days;
    }

    private static LocalDate sundayForDate(LocalDate current) {
        LocalDate oneWeekAgo = current.minusWeeks(1);

        while (current.isAfter(oneWeekAgo)) {
            if (current.getDayOfWeek() == DayOfWeek.SUNDAY)
                return current;

            current = current.minusDays(1);
        }

        return null;
    }
}
