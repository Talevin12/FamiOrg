package com.example.famiorg.activities;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.famiorg.R;
import com.example.famiorg.adapters.AdapterCalendar;
import com.example.famiorg.assets.CalendarUtils;
import com.example.famiorg.assets.GoogleLoginAssets;
import com.example.famiorg.assets.IntentUtils;
import com.example.famiorg.callbacks.Callback_DataManager;
import com.example.famiorg.dataManagers.UserDataManager;
import com.example.famiorg.logic.DayCalendar;
import com.example.famiorg.logic.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalendarActivity extends AppCompatActivity implements AdapterCalendar.OnItemListener {

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;

    private User user = new User();
    UserDataManager userDataManager = new UserDataManager();
    GoogleLoginAssets googleLoginAssets = new GoogleLoginAssets(userDataManager, this);

    Callback_DataManager<User> callback_setUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_calendar);

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
    }

    private void initCallbacks() {
        callback_setUser = (Callback_DataManager) object -> {
            user = (User) object;

            if (user.getFamilyId() == null) {
                finish();
            } else {
                CalendarUtils.getInstance(user.getFamilyId());
                CalendarUtils.getInstance(null).setSelectedDate(LocalDate.now());
                setMonthView();
            }
        };
    }

    private void setCallbacks() {
        userDataManager.setCallBack_setUserProtocol(callback_setUser);
    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.getInstance(null).getSelectedDate()));
        ArrayList<DayCalendar> daysInMonth = CalendarUtils.daysInMonthArray(CalendarUtils.getInstance(null).getSelectedDate());

        AdapterCalendar calendarAdapter = new AdapterCalendar(this, daysInMonth, this, CalendarUtils.getInstance(null).getSelectedDate().getDayOfMonth());
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    public void previousMonthAction(View view) {
        CalendarUtils.getInstance(null).setSelectedDate(CalendarUtils.getInstance(null).getSelectedDate().minusMonths(1));
        setMonthView();
    }

    public void nextMonthAction(View view) {
        CalendarUtils.getInstance(null).setSelectedDate(CalendarUtils.getInstance(null).getSelectedDate().plusMonths(1));
        setMonthView();
    }

    @Override
    public void onItemClick(int position, DayCalendar dayCalendar) {
        if (dayCalendar.getDate() != null) {
            CalendarUtils.getInstance(null).setSelectedDate(dayCalendar.getDate());
            IntentUtils.getInstance().openPage(this, WeeklyCalendarActivity.class);
        }
    }

    public void weeklyAction(View view) {
        IntentUtils.getInstance().openPage(this, WeeklyCalendarActivity.class);
    }
}