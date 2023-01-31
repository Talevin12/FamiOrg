package com.example.famiorg.dataManagers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.famiorg.callbacks.Callback_DataManager;
import com.example.famiorg.logic.Family;
import com.example.famiorg.logic.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FamilyDataManager {
    private Callback_DataManager callback_addOrUpdateFamilyMember;
    private Callback_DataManager callback_removeFamilyMember;
    private Callback_DataManager callback_setFamilyName;
    private Callback_DataManager callback_getFamilyMembers;
    private Callback_DataManager callback_createFamily;
    private Callback_DataManager callback_setUserFamily;

    public void setCallBack_addOrUpdateFamilyMemberProtocol(Callback_DataManager callback_addOrUpdateFamilyMember) {
        this.callback_addOrUpdateFamilyMember = callback_addOrUpdateFamilyMember;
    }

    public void setCallBack_removeFamilyMemberProtocol(Callback_DataManager callback_removeFamilyMember) {
        this.callback_removeFamilyMember = callback_removeFamilyMember;
    }

    public void setCallBack_setFamilyName(Callback_DataManager callback_setFamilyName) {
        this.callback_setFamilyName = callback_setFamilyName;
    }

    public void setCallback_getFamilyMembers(Callback_DataManager callback_getFamilyMembers) {
        this.callback_getFamilyMembers = callback_getFamilyMembers;
    }

    public void setCallback_createFamily(Callback_DataManager callback_createFamily) {
        this.callback_createFamily = callback_createFamily;
    }

    public void setCallback_setUserFamily(Callback_DataManager callback_setUserFamily) {
        this.callback_setUserFamily = callback_setUserFamily;
    }

    public void getFamilyMembersRT(String famId) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refUsers = db.getReference("Users");
        DatabaseReference refFamilY = db.getReference("Families").child(famId).child("familyMembersIDs");

        refFamilY.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                refUsers.child(snapshot.getValue(String.class)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        callback_addOrUpdateFamilyMember.getObject(snapshot.getValue(User.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                refUsers.child(snapshot.getValue(String.class)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        callback_removeFamilyMember.getObject(snapshot.getValue(User.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void getFamilyMembers(String famId) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refUsers = db.getReference("Users");
        DatabaseReference refFamilY = db.getReference("Families").child(famId).child("familyMembersIDs");

        refFamilY.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                ArrayList<User> famMembers = new ArrayList<>();

                for (DataSnapshot child : snapshot1.getChildren()) {
                    refUsers.child(child.getValue(String.class)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot2) {
                            famMembers.add(snapshot2.getValue(User.class));

                            if (snapshot1.getChildrenCount() == famMembers.size()) {
                                callback_getFamilyMembers.getObject(famMembers);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getFamilyName(String famId) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refFamilyName = db.getReference("Families").child(famId).child("familyName");

        refFamilyName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback_setFamilyName.getObject(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void createFamily(Family newFam) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refFamily = db.getReference("Families").child(newFam.getFamilyId());

        refFamily.child("familyMembersIDs").setValue(newFam.getFamilyMembersIDs());
        refFamily.child("familyName").setValue(newFam.getFamilyName());

        callback_createFamily.getObject(newFam.getFamilyId());
    }

    public void updateUserFamilyAndAddToFamily(String userId, String famId, boolean isCreator) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refUserFamily = db.getReference("Users").child(userId).child("familyId");

        refUserFamily.setValue(famId);

        if (!isCreator) {
            DatabaseReference refFamilyMembers = db.getReference("Families").child(famId).child("familyMembersIDs");
            refFamilyMembers.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<>() {
                    };
                    ArrayList<String> members = snapshot.getValue(t);
                    members.add(userId);
                    refFamilyMembers.setValue(members);


                    callback_setUserFamily.getObject(null);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            callback_setUserFamily.getObject(null);
        }
    }

    public void removeMemberFromFamily(String userId, String familyId) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refUserFamily = db.getReference("Users").child(userId).child("familyId");
        DatabaseReference refFamilyMembers = db.getReference("Families").child(familyId).child("familyMembersIDs");

        refUserFamily.removeValue();

        refFamilyMembers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<>() {};
                ArrayList<String> members = snapshot.getValue(t);

                members.remove(userId);
                refFamilyMembers.removeValue();
                refFamilyMembers.setValue(members);

                callback_removeFamilyMember.getObject(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
