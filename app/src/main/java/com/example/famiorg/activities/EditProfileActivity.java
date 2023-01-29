package com.example.famiorg.activities;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.famiorg.DataManager;
import com.example.famiorg.GoogleLoginAssets;
import com.example.famiorg.R;
import com.example.famiorg.adapters.Adapter_ChooseIcon;
import com.example.famiorg.callbacks.Callback_DataManager;
import com.example.famiorg.logic.MemberInvitation;
import com.example.famiorg.logic.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;

import jp.wasabeef.blurry.Blurry;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editProfile_EDIT_name;
    private ExtendedFloatingActionButton editProfile_IMG_BTN_checkEditName;
    private RecyclerView editProfile_LST_icons;
    private EditText editProfile_EDIT_inviteMember;
    private ExtendedFloatingActionButton editProfile_IMG_BTN_inviteMemberSend;
    private MaterialButton editProfile_BTN_exitFamily;

    RecyclerView.LayoutManager layoutManager;
    private Adapter_ChooseIcon adapter_chooseIcon;

    DataManager dataManager = new DataManager();

    GoogleLoginAssets googleLoginAssets = new GoogleLoginAssets(dataManager, this);

    User user;

    Callback_DataManager<User> callback_setUser;
    Callback_DataManager<String> callback_setFamilyName;
    Callback_DataManager<Boolean> callback_createInvitation;
    Callback_DataManager<Boolean> callback_removeFamilyMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit_profile);

//        String background ="https://img.freepik.com/free-vector/paper-style-white-monochrome-background_52683-66444.jpg?w=2000";
//        Glide.with(this)
//                .load(background)
//                .centerCrop()
//                .into((AppCompatImageView) findViewById(R.id.editProfile_IMG_background));

        Blurry.with(this)
                .sampling(1)
                .async()
                .from(BitmapFactory.decodeResource(getResources(), R.drawable.pink_watercolor_texture))
                .into(findViewById(R.id.editProfile_IMG_background));

        findViews();
        initViews();
        initRecyclerView();

        initCallbacks();
        setCallbacks();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!googleLoginAssets.checkSignIn()) {
            getParent().finish();
            finish();
        }
    }

    private void findViews() {
        editProfile_EDIT_name = findViewById(R.id.editProfile_EDIT_name);
        editProfile_IMG_BTN_checkEditName = findViewById(R.id.editProfile_IMG_BTN_checkEditName);
        editProfile_LST_icons = findViewById(R.id.editProfile_LST_icons);
        editProfile_EDIT_inviteMember = findViewById(R.id.editProfile_EDIT_inviteMember);
        editProfile_IMG_BTN_inviteMemberSend = findViewById(R.id.editProfile_IMG_BTN_inviteMemberSend);
        editProfile_BTN_exitFamily = findViewById(R.id.editProfile_BTN_exitFamily);
    }

    private void initViews() {
        editProfile_IMG_BTN_checkEditName.setOnClickListener(v -> {
            dataManager.updateUserName(FirebaseAuth.getInstance().getCurrentUser().getUid(), editProfile_EDIT_name.getText().toString());
            user.setName(editProfile_EDIT_name.getText().toString());
            v.setVisibility(View.INVISIBLE);
        });

        editProfile_EDIT_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(user.getName())) {
                    editProfile_IMG_BTN_checkEditName.setVisibility(View.VISIBLE);
                } else {
                    editProfile_IMG_BTN_checkEditName.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editProfile_IMG_BTN_inviteMemberSend.setOnClickListener(v -> {
            if (!editProfile_EDIT_inviteMember.getText().toString().isBlank()) {
                if (!editProfile_EDIT_inviteMember.getText().toString().equalsIgnoreCase(user.getEmail())) {
                    editProfile_IMG_BTN_inviteMemberSend.setEnabled(false);

                    dataManager.getFamilyName(user.getFamilyId());
                } else {
                    Toast.makeText(this, "You can't send invitation to yourself", Toast.LENGTH_SHORT).show();
                }
            }
        });

        editProfile_BTN_exitFamily.setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Exit family");
            alert.setMessage("Are you sure you want to exit family?");
            alert.setPositiveButton("Yes", (dialog, which) -> {
                dataManager.removeMemberFromFamily(FirebaseAuth.getInstance().getCurrentUser().getUid(), user.getFamilyId());
                dialog.dismiss();
            });

            alert.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

            alert.show();
        });
    }

    private void initRecyclerView() {
        layoutManager = new GridLayoutManager(this, 4);
        editProfile_LST_icons.setLayoutManager(layoutManager);
    }

    private void initCallbacks() {
        callback_setUser = (Callback_DataManager) object -> {
            user = (User) object;

            setEditText();

            if (adapter_chooseIcon == null) {
                adapter_chooseIcon = new Adapter_ChooseIcon(this, user);
                editProfile_LST_icons.setAdapter(adapter_chooseIcon);
            }
        };

        callback_setFamilyName = (Callback_DataManager) object -> {
            MemberInvitation memberInvitation = new MemberInvitation()
                    .setFamId(user.getFamilyId())
                    .setFamName((String) object)
                    .setUserSentEmail(user.getEmail())
                    .setUserRecvEmail(editProfile_EDIT_inviteMember.getText().toString())
                    .setDate(new Date());

            dataManager.createInvitation(memberInvitation);
        };

        callback_createInvitation = (Callback_DataManager) object -> {
            editProfile_IMG_BTN_inviteMemberSend.setEnabled(true);
            editProfile_EDIT_inviteMember.setText("");

            if (!(Boolean) object) {
                Toast.makeText(this, "No User with this email found", Toast.LENGTH_SHORT);
            }
        };

        callback_removeFamilyMember = (Callback_DataManager) object -> finish();
    }

    private void setCallbacks() {
        dataManager.setCallBack_setUserProtocol(callback_setUser);
        dataManager.setCallBack_setFamilyName(callback_setFamilyName);
        dataManager.setCallback_createInvitation(callback_createInvitation);
    }

    private void setEditText() {
        editProfile_EDIT_name.setText(user.getName());
    }
}