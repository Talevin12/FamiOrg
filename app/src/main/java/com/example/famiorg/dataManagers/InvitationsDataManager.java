package com.example.famiorg.dataManagers;

import androidx.annotation.NonNull;

import com.example.famiorg.callbacks.Callback_DataManager;
import com.example.famiorg.logic.MemberInvitation;
import com.example.famiorg.logic.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InvitationsDataManager {
    private Callback_DataManager callback_createInvitation;
    private Callback_DataManager callback_getUserInvitations;

    public void setCallback_createInvitation(Callback_DataManager callback_createInvitation) {
        this.callback_createInvitation = callback_createInvitation;
    }

    public void setCallback_getUserInvitations(Callback_DataManager callback_getUserInvitations) {
        this.callback_getUserInvitations = callback_getUserInvitations;
    }

    public void createInvitation(MemberInvitation memberInvitation) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refUsers = db.getReference("Users");
        DatabaseReference refInvitations = db.getReference("Invitations");

        refUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean flag = false;
                for (DataSnapshot child : snapshot.getChildren()) {
                    if (child.getValue(User.class).getEmail().equalsIgnoreCase(memberInvitation.getUserRecvEmail())) {
                        refInvitations.child(memberInvitation.getInvitationId()).setValue(memberInvitation);
                        flag = true;
                        break;
                    }
                }

                callback_createInvitation.getObject(flag);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void getUserInvitations(String email) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refInvitations = db.getReference("Invitations");

        refInvitations.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<MemberInvitation> invitations = new ArrayList<>();

                for (DataSnapshot child : snapshot.getChildren()) {
                    MemberInvitation invitation = child.getValue(MemberInvitation.class);
                    if (invitation.getUserRecvEmail().equalsIgnoreCase(email)) {
                        invitations.add(invitation);
                    }
                }

                callback_getUserInvitations.getObject(invitations);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void removeInvitation(String invitationId) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refInvitation = db.getReference("Invitations").child(invitationId);

        refInvitation.removeValue();
    }
}
