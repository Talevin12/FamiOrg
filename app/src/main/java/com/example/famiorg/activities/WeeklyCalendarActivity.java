package com.example.famiorg.activities;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.famiorg.R;
import com.example.famiorg.adapters.AdapterCalendar;
import com.example.famiorg.adapters.AdapterEvent;
import com.example.famiorg.assets.CalendarUtils;
import com.example.famiorg.assets.GoogleLoginAssets;
import com.example.famiorg.assets.IntentUtils;
import com.example.famiorg.callbacks.Callback_DataManager;
import com.example.famiorg.dataManagers.FamilyDataManager;
import com.example.famiorg.dataManagers.UserDataManager;
import com.example.famiorg.logic.DailyEvent;
import com.example.famiorg.logic.DayCalendar;
import com.example.famiorg.logic.User;

import java.util.ArrayList;

public class WeeklyCalendarActivity extends AppCompatActivity implements AdapterCalendar.OnItemListener {

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;

    private User user = new User();
    UserDataManager userDataManager = new UserDataManager();
    FamilyDataManager familyDataManager = new FamilyDataManager();
    GoogleLoginAssets googleLoginAssets = new GoogleLoginAssets(userDataManager, this);

    ArrayList<DayCalendar> days;
    int dayOfWeek;

    Callback_DataManager<User> callback_setUser;
    Callback_DataManager<User> callback_getFamilyMembers;

    CalendarUtils.CallBack_NewCalendarEvent callBack_newCalendarEvent = () -> setWeekView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_weekly_calendar);

        findViews();

        initCallbacks();
        setCallbacks();
    }

    @Override
    protected void onResume() {
        super.onResume();

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
                CalendarUtils.getInstance(null).callBack_newCalendarEvent = callBack_newCalendarEvent;
                setWeekView();
            }
        };

        callback_getFamilyMembers = (Callback_DataManager) object -> {
            ArrayList<User> familyMembers = (ArrayList<User>) object;

            setEventAdapter(days.get(dayOfWeek - 1).getEvents(), familyMembers);
        };
    }

    private void setCallbacks() {
        userDataManager.setCallBack_setUserProtocol(callback_setUser);
        familyDataManager.setCallback_getFamilyMembers(callback_getFamilyMembers);
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

        familyDataManager.getFamilyMembers(user.getFamilyId());
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
        IntentUtils.getInstance().openPage(this, CreateEventActivity.class);
    }
}