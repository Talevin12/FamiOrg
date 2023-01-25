package com.example.famiorg.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.famiorg.DataManager;
import com.example.famiorg.GoogleLoginAssets;
import com.example.famiorg.R;
import com.example.famiorg.callbacks.Callback_DataManager;
import com.example.famiorg.logic.Family;
import com.example.famiorg.logic.MemberInvitation;
import com.example.famiorg.logic.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;

public class CreateNewFamilyActivity extends AppCompatActivity {

    private EditText createFam_EDIT_famName;
    private EditText createFam_EDIT_inviteMember;

    private LinearLayout createFam_invitations;
    private ExtendedFloatingActionButton createFam_IMG_BTN_inviteMemberSend;
    private MaterialButton createFam_BTN_submit;

    private User user = new User();

    Family newFam;

    DataManager dataManager = new DataManager();

    Callback_DataManager<User> callback_setUser;
    Callback_DataManager<String> callback_createFamily;
    Callback_DataManager<Object> callback_setUserFamily;
    Callback_DataManager<Boolean> callback_createInvitation;

    GoogleLoginAssets googleLoginAssets = new GoogleLoginAssets(dataManager, this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_family);

        findViews();
        initViews();

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
        createFam_EDIT_famName = findViewById(R.id.createFam_EDIT_famName);
        createFam_EDIT_inviteMember = findViewById(R.id.createFam_EDIT_inviteMember);
        createFam_invitations = findViewById(R.id.createFam_invitations);
        createFam_IMG_BTN_inviteMemberSend = findViewById(R.id.createFam_IMG_BTN_inviteMemberSend);
        createFam_BTN_submit = findViewById(R.id.createFam_BTN_submit);
    }

    private void initViews() {
        createFam_BTN_submit.setOnClickListener(v -> {
            if(createFam_invitations.getVisibility() == View.INVISIBLE) {
                if (!createFam_EDIT_famName.getText().toString().isBlank()) {
                    newFam = new Family()
                            .setFamilyName(createFam_EDIT_famName.getText().toString());
                    newFam.addMember(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    dataManager.createFamily(newFam);
                } else {
                    Toast.makeText(this, "Family name is empty", Toast.LENGTH_SHORT);
                }
            } else {
                openMainActivity();
                finish();
            }
        });

        createFam_IMG_BTN_inviteMemberSend.setOnClickListener(v -> {
            if (!createFam_EDIT_inviteMember.getText().toString().isBlank()) {
                if(!createFam_EDIT_inviteMember.getText().toString().equalsIgnoreCase(user.getEmail())) {
                    createFam_IMG_BTN_inviteMemberSend.setEnabled(false);

                    MemberInvitation memberInvitation = new MemberInvitation()
                            .setFamId(user.getFamilyId())
                            .setFamName(newFam.getFamilyName())
                            .setUserSentEmail(user.getEmail())
                            .setUserRecvEmail(createFam_EDIT_inviteMember.getText().toString())
                            .setDate(new Date());

                    dataManager.createInvitation(memberInvitation);
                } else {
                    Toast.makeText(this, "You can't send invitation to yourself", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private void initCallbacks() {
        callback_setUser = (Callback_DataManager) object -> user = (User) object;

        callback_createFamily = (Callback_DataManager) object -> {
            user.setFamilyId((String) object);
            dataManager.updateUserFamilyAndAddToFamily(FirebaseAuth.getInstance().getCurrentUser().getUid(), (String) object, true);
        };

        callback_setUserFamily = (Callback_DataManager) object -> {
            createFam_EDIT_famName.setVisibility(View.INVISIBLE);
            createFam_invitations.setVisibility(View.VISIBLE);
            createFam_BTN_submit.setText("Finish");
        };

        callback_createInvitation = (Callback_DataManager) object -> {
            createFam_IMG_BTN_inviteMemberSend.setEnabled(true);
            createFam_EDIT_inviteMember.setText("");

            if (!(Boolean) object) {
                Toast.makeText(this, "No User with this email found", Toast.LENGTH_SHORT);
            }
        };
    }

    private void setCallbacks() {
        dataManager.setCallBack_setUserProtocol(callback_setUser);
        dataManager.setCallback_createFamily(callback_createFamily);
        dataManager.setCallback_setUserFamily(callback_setUserFamily);
        dataManager.setCallback_createInvitation(callback_createInvitation);
    }

    private void openMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}