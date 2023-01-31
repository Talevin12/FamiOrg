package com.example.famiorg.dataManagers;

import androidx.annotation.NonNull;

import com.example.famiorg.callbacks.Callback_DataManager;
import com.example.famiorg.logic.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDataManager {
    private Callback_DataManager callback_setUser;

    public void setCallBack_setUserProtocol(Callback_DataManager callback_setUser) {
        this.callback_setUser = callback_setUser;
    }

    public void getUser(String userId, boolean isRT) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refUser = db.getReference("Users").child(userId);

        if (isRT) {
            refUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    callback_setUser.getObject(snapshot.getValue(User.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        } else {
            refUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    callback_setUser.getObject(snapshot.getValue(User.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    public void updateUserName(String userId, String newName) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refUserName = db.getReference("Users").child(userId).child("name");

        refUserName.setValue(newName);
    }

    public void updateUserIcon(String userId, Integer newIcon) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refUserName = db.getReference("Users").child(userId).child("icon");

        refUserName.setValue(newIcon);
    }
}
