package com.example.famiorg.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.famiorg.DataManager;
import com.example.famiorg.GoogleLoginAssets;
import com.example.famiorg.R;
import com.example.famiorg.adapters.Adapter_Invitations;
import com.example.famiorg.callbacks.Callback_DataManager;
import com.example.famiorg.logic.MemberInvitation;
import com.example.famiorg.logic.User;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class ChooseFamilyActivity extends AppCompatActivity {

    private RecyclerView chooseFam_LST_invitations;
    private MaterialButton chooseFam_BTN_CreateNewFam;
    private MaterialButton chooseFam_BTN_Signout;

    Adapter_Invitations adapter_invitations;
    ArrayList<MemberInvitation> invitations;

    User user;
    DataManager dataManager = new DataManager();
    Callback_DataManager<User> callback_setUser;
    Callback_DataManager<ArrayList<MemberInvitation>> callback_getUserInvitations;
    GoogleLoginAssets googleLoginAssets = new GoogleLoginAssets(dataManager, this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_family);

        findViews();
        initViews();

        initCallbacks();
        setCallbacks();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!googleLoginAssets.checkSignIn()) {
            finish();
        }
    }

    private void findViews() {
        chooseFam_LST_invitations = findViewById(R.id.chooseFam_LST_invitations);
        chooseFam_BTN_CreateNewFam = findViewById(R.id.chooseFam_BTN_CreateNewFam);
        chooseFam_BTN_Signout = findViewById(R.id.chooseFam_BTN_Signout);
    }

    private void initViews() {
        chooseFam_BTN_CreateNewFam.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateNewFamilyActivity.class);
            startActivity(intent);
            finish();
        });

        chooseFam_BTN_Signout.setOnClickListener(v -> {
            googleLoginAssets.SignOut();
            finish();
        });
    }

    private void initRecyclerView() {
        adapter_invitations = new Adapter_Invitations(this, invitations, dataManager);
        chooseFam_LST_invitations.setLayoutManager(new LinearLayoutManager(this));

        chooseFam_LST_invitations.setAdapter(adapter_invitations);
    }

    private void initCallbacks() {
        callback_setUser = (Callback_DataManager) object -> {
            user = (User) object;

            dataManager.getUserInvitations(user.getEmail());
        };

        callback_getUserInvitations = (Callback_DataManager) object -> {
          invitations = (ArrayList<MemberInvitation>) object;

          initRecyclerView();
        };
    }

    private void setCallbacks() {
        dataManager.setCallBack_setUserProtocol(callback_setUser);
        dataManager.setCallback_getUserInvitations(callback_getUserInvitations);
    }
}