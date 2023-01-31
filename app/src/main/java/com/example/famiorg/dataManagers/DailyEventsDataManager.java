package com.example.famiorg.dataManagers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.famiorg.callbacks.Callback_DataManager;
import com.example.famiorg.logic.DailyEvent;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DailyEventsDataManager {
    private Callback_DataManager callback_getDailyEvents;

    public void setCallback_getDailyEvents(Callback_DataManager callback_getDailyEvents) {
        this.callback_getDailyEvents = callback_getDailyEvents;
    }

    public void createDailyEvent(String familyId, DailyEvent dailyEvent) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refFamilyMembers = db.getReference("Families").child(familyId).child("familyEvents");

        refFamilyMembers.child(dailyEvent.getEventId()).setValue(dailyEvent);
    }

    public void getDailyEventsRT(String familyId) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refFamilyMembers = db.getReference("Families").child(familyId).child("familyEvents");

        refFamilyMembers.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                callback_getDailyEvents.getObject(snapshot.getValue(DailyEvent.class));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}
