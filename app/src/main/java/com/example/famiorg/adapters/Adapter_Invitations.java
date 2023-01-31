package com.example.famiorg.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.famiorg.R;
import com.example.famiorg.activities.MainActivity;
import com.example.famiorg.callbacks.Callback_DataManager;
import com.example.famiorg.dataManagers.FamilyDataManager;
import com.example.famiorg.dataManagers.InvitationsDataManager;
import com.example.famiorg.logic.MemberInvitation;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Comparator;

public class Adapter_Invitations extends RecyclerView.Adapter<Adapter_Invitations.InvitationViewHolder> {

    private Context context;
    private ArrayList<MemberInvitation> invitations;
    private InvitationsDataManager invitationsDataManager;
    private FamilyDataManager familyDataManager;

    Callback_DataManager<Object> callback_setUserFamily = new Callback_DataManager<>() {
        @Override
        public void getObject(Object object) {
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);

            ((Activity)context).finish();
        }
    };


    public Adapter_Invitations(Context context, ArrayList<MemberInvitation> invitations, FamilyDataManager familyDataManager, InvitationsDataManager invitationsDataManager) {
        this.context = context;
        this.invitations = invitations;
        this.invitationsDataManager = invitationsDataManager;
        this.familyDataManager = familyDataManager;

        invitations.sort(Comparator.comparing(MemberInvitation::getDate));
        familyDataManager.setCallback_setUserFamily(callback_setUserFamily);
    }

    @NonNull
    @Override
    public InvitationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_invitation, parent, false);
        Adapter_Invitations.InvitationViewHolder invitationViewHolder = new Adapter_Invitations.InvitationViewHolder(view);
        return invitationViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull InvitationViewHolder holder, int position) {
        MemberInvitation invitation = invitations.get(position);

        holder.invitation_LBL_text.setText(invitation.getUserSentEmail()+" invite you to join "+ invitation.getFamName() +" family.");

        holder.invitation_IMG_BTN_approve.setOnClickListener(v -> {
            familyDataManager.updateUserFamilyAndAddToFamily(FirebaseAuth
                                                            .getInstance()
                                                            .getCurrentUser()
                                                            .getUid(),
                                                    invitation.getFamId(), false);

            invitationsDataManager.removeInvitation(invitation.getInvitationId());
        });

        holder.invitation_IMG_BTN_decline.setOnClickListener(v -> {
            invitations.remove(position);
            notifyDataSetChanged();

            invitationsDataManager.removeInvitation(invitation.getInvitationId());
        });
    }

    @Override
    public int getItemCount() {
        return invitations == null ? 0 : invitations.size();
    }


    class InvitationViewHolder extends RecyclerView.ViewHolder {

        private TextView invitation_LBL_text;
        private ExtendedFloatingActionButton invitation_IMG_BTN_approve;
        private ExtendedFloatingActionButton invitation_IMG_BTN_decline;

        public InvitationViewHolder(@NonNull View itemView) {
            super(itemView);

            invitation_LBL_text = itemView.findViewById(R.id.invitation_LBL_text);
            invitation_IMG_BTN_approve = itemView.findViewById(R.id.invitation_IMG_BTN_approve);
            invitation_IMG_BTN_decline = itemView.findViewById(R.id.invitation_IMG_BTN_decline);
        }
    }
}
