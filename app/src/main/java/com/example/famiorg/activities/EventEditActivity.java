package com.example.famiorg.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.famiorg.CalendarUtils;
import com.example.famiorg.DataManager;
import com.example.famiorg.GoogleLoginAssets;
import com.example.famiorg.R;
import com.example.famiorg.callbacks.Callback_DataManager;
import com.example.famiorg.logic.DailyEvent;
import com.example.famiorg.logic.User;
import com.google.android.material.button.MaterialButton;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class EventEditActivity extends AppCompatActivity {

    private TextView createEvent_LBL_date;
    private EditText createEvent_ET_title;
    private EditText createEvent_ET_description;
    private MaterialButton createEvent_BTN_startTime;
    private MaterialButton createEvent_BTN_endTime;
    private TextView createEvent_MULTI_CHOICE_members;
    private MaterialButton createEvent_BTN_save;

    private User user = new User();
    DataManager dataManager = new DataManager();
    GoogleLoginAssets googleLoginAssets = new GoogleLoginAssets(dataManager, this);

    Callback_DataManager<User> callback_setUser;
    Callback_DataManager<User> callback_getFamilyMembers;

    DailyEvent dailyEvent = new DailyEvent();

    ArrayList<User> membersList = new ArrayList<>();
    boolean[] selectedMembers;
    ArrayList<String> selectedMembersEmailsList = new ArrayList<>();
    ArrayList<String> membersEmailsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);

        findViews();

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
        createEvent_LBL_date = findViewById(R.id.createEvent_LBL_date);
        createEvent_ET_title = findViewById(R.id.createEvent_ET_title);
        createEvent_ET_description = findViewById(R.id.createEvent_ET_description);
        createEvent_BTN_startTime = findViewById(R.id.createEvent_BTN_startTime);
        createEvent_BTN_endTime = findViewById(R.id.createEvent_BTN_endTime);
        createEvent_MULTI_CHOICE_members = findViewById(R.id.createEvent_MULTI_CHOICE_members);
        createEvent_BTN_save = findViewById(R.id.createEvent_BTN_save);
    }

    private void initViews() {
        createEvent_LBL_date.setText(CalendarUtils.formattedDate(CalendarUtils.getInstance(null).getSelectedDate()));

        View.OnClickListener onTimeClickListner = v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (view, hourOfDay, minute1) -> {
                        if (v == createEvent_BTN_startTime) {
                            dailyEvent.setLocalDateTimeStart(LocalDateTime.of(CalendarUtils.getInstance(null).getSelectedDate(), LocalTime.of(hourOfDay, minute1)));
                            createEvent_BTN_startTime.setText("Start Time: " + CalendarUtils.formattedTime(LocalTime.of(hourOfDay, minute1)));
                            createEvent_BTN_endTime.setEnabled(true);
                        } else {
                            if (LocalDateTime.of(CalendarUtils.getInstance(null).getSelectedDate(), LocalTime.of(hourOfDay, minute1)).isAfter(dailyEvent.getLocalDateTimeStart())) {
                                dailyEvent.setLocalDateTimeEnd(LocalDateTime.of(CalendarUtils.getInstance(null).getSelectedDate(), LocalTime.of(hourOfDay, minute1)));
                                createEvent_BTN_endTime.setText("End Time: " + CalendarUtils.formattedTime(LocalTime.of(hourOfDay, minute1)));
                            } else {
                                Toast.makeText(this, "End time must be after Start time", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    LocalTime.now().getHour(),
                    LocalTime.now().getMinute(),
                    false);

            timePickerDialog.show();
        };
        createEvent_BTN_startTime.setOnClickListener(onTimeClickListner);
        createEvent_BTN_endTime.setOnClickListener(onTimeClickListner);

        selectedMembers = new boolean[membersEmailsList.size()];
        createEvent_MULTI_CHOICE_members.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Select Family Members Related");

            builder.setCancelable(false);

            String[] membersEmailsArray = new String[membersEmailsList.size()];
            membersEmailsArray = membersEmailsList.toArray(membersEmailsArray);

            builder.setMultiChoiceItems(membersEmailsArray, selectedMembers, (dialogInterface, i, b) -> {
                if (b) {
                    selectedMembersEmailsList.add(membersEmailsList.get(i));
                } else {
                    selectedMembersEmailsList.remove(membersEmailsList.get(i));
                }
            });

            builder.setPositiveButton("OK", (dialogInterface, i) -> {
                StringBuilder stringBuilder = new StringBuilder();

                for (int j = 0; j < selectedMembersEmailsList.size(); j++) {
                    stringBuilder.append("* " + selectedMembersEmailsList.get(j));
                    if (j != selectedMembersEmailsList.size() - 1) {
                        stringBuilder.append("\n");
                    }
                }
                createEvent_MULTI_CHOICE_members.setText(stringBuilder.toString());
                dailyEvent.setFamilyMembersParticipatingEmails(selectedMembersEmailsList);
            });

            builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());
            builder.setNeutralButton("Clear All", (dialogInterface, i) -> {
                for (int j = 0; j < selectedMembers.length; j++) {
                    selectedMembers[j] = false;
                    selectedMembersEmailsList.clear();
                    createEvent_MULTI_CHOICE_members.setText("");
                }
            });

            builder.show();
        });
    }

    private void initCallbacks() {
        callback_setUser = (Callback_DataManager) object -> {
            user = (User) object;

            if (user.getFamilyId() == null) {
                finish();
            } else {
                dataManager.getFamilyMembers(user.getFamilyId());
            }
        };

        callback_getFamilyMembers = (Callback_DataManager) object -> {
            membersList = (ArrayList<User>) object;

            for (User member : membersList) {
                membersEmailsList.add(member.getEmail());
            }

            initViews();
        };
    }

    private void setCallbacks() {

        dataManager.setCallBack_setUserProtocol(callback_setUser);
        dataManager.setCallback_getFamilyMembers(callback_getFamilyMembers);
    }

    public void saveEventAction(View view) {
        if (!createEvent_ET_title.getText().toString().isBlank() && !createEvent_ET_description.getText().toString().isBlank()) {
            dailyEvent.setTitle(createEvent_ET_title.getText().toString());
            dailyEvent.setDescription(createEvent_ET_description.getText().toString());

            dataManager.createDailyEvent(user.getFamilyId(), dailyEvent);
            finish();
        }
    }
}