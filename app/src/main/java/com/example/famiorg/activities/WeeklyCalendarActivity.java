package com.example.famiorg.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.famiorg.CalendarUtils;
import com.example.famiorg.DataManager;
import com.example.famiorg.GoogleLoginAssets;
import com.example.famiorg.R;
import com.example.famiorg.adapters.AdapterCalendar;
import com.example.famiorg.adapters.AdapterEvent;
import com.example.famiorg.callbacks.Callback_DataManager;
import com.example.famiorg.logic.DailyEvent;
import com.example.famiorg.logic.DayCalendar;
import com.example.famiorg.logic.User;

import java.util.ArrayList;

public class WeeklyCalendarActivity extends AppCompatActivity implements AdapterCalendar.OnItemListener {

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;

    private User user = new User();
    DataManager dataManager = new DataManager();
    GoogleLoginAssets googleLoginAssets = new GoogleLoginAssets(dataManager, this);

    ArrayList<DayCalendar> days;
    int dayOfWeek;

    Callback_DataManager<User> callback_setUser;
    Callback_DataManager<User> callback_getFamilyMembers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_calendar);

        findViews();
//        setWeekView();
        initCallbacks();
        setCallbacks();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        setWeekView();
        if (!googleLoginAssets.checkSignIn()) {
            finish();
        }
    }

    private void findViews() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        eventListView = findViewById(R.id.eventListView);
    }

    private void initCallbacks() {
        callback_setUser = (Callback_DataManager) object -> {
            user = (User) object;

            if (user.getFamilyId() == null) {
                finish();
            } else {
                setWeekView();
            }
        };

        callback_getFamilyMembers = (Callback_DataManager) object -> {
            ArrayList<User> familyMembers = (ArrayList<User>) object;

            setEventAdapter(days.get(dayOfWeek - 1).getEvents(), familyMembers);
        };
    }

    private void setCallbacks() {
        dataManager.setCallBack_setUserProtocol(callback_setUser);
        dataManager.setCallback_getFamilyMembers(callback_getFamilyMembers);
    }

    private void setWeekView() {
        monthYearText.setText(CalendarUtils.monthYearFromDate(CalendarUtils.getInstance(null).getSelectedDate()));
        days = CalendarUtils.daysInWeekArray(CalendarUtils.getInstance(null).getSelectedDate());

        AdapterCalendar calendarAdapter = new AdapterCalendar(this, days, this, CalendarUtils.getInstance(null).getSelectedDate().getDayOfMonth());
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);

        dayOfWeek = CalendarUtils.getInstance(null).getSelectedDate().getDayOfWeek().getValue() + 1;
        dayOfWeek = dayOfWeek > 7 ? 1 : dayOfWeek;

        dataManager.getFamilyMembers(user.getFamilyId());
    }


    public void previousWeekAction(View view) {
        CalendarUtils.getInstance(null).setSelectedDate(CalendarUtils.getInstance(null).getSelectedDate().minusWeeks(1));
        setWeekView();
    }

    public void nextWeekAction(View view) {
        CalendarUtils.getInstance(null).setSelectedDate(CalendarUtils.getInstance(null).getSelectedDate().plusWeeks(1));
        setWeekView();
    }

    @Override
    public void onItemClick(int position, DayCalendar dayCalendar) {
        CalendarUtils.getInstance(null).setSelectedDate(dayCalendar.getDate());
        setWeekView();
    }

    private void setEventAdapter(ArrayList<DailyEvent> dailyEvents, ArrayList<User> familyMembers) {
        AdapterEvent eventAdapter = new AdapterEvent(this, dailyEvents, familyMembers);
        eventListView.setAdapter(eventAdapter);
    }

    public void newEventAction(View view) {
        startActivity(new Intent(this, EventEditActivity.class));
    }
}