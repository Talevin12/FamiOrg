package com.example.famiorg.activities;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.famiorg.DataManager;
import com.example.famiorg.GoogleLoginAssets;
import com.example.famiorg.R;
import com.example.famiorg.adapters.Adapter_ChooseIcon;
import com.example.famiorg.callbacks.Callback_DataManager;
import com.example.famiorg.logic.User;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import jp.wasabeef.blurry.Blurry;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editProfile_EDIT_name;
    private ExtendedFloatingActionButton editProfile_IMG_BTN_checkEditName;
    private RecyclerView editProfile_LST_icons;

    RecyclerView.LayoutManager layoutManager;
    private Adapter_ChooseIcon adapter_chooseIcon;

    DataManager dataManager = new DataManager();

    GoogleLoginAssets googleLoginAssets = new GoogleLoginAssets(dataManager, this);

    User user;

    Callback_DataManager<User> callback_setUser;

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

        if(!googleLoginAssets.checkSignIn()) {
            getParent().finish();
            finish();
        }
    }

    private void findViews() {
        editProfile_EDIT_name = findViewById(R.id.editProfile_EDIT_name);
        editProfile_IMG_BTN_checkEditName = findViewById(R.id.editProfile_IMG_BTN_checkEditName);
        editProfile_LST_icons = findViewById(R.id.editProfile_LST_icons);
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
                if(!s.toString().equals(user.getName())) {
                    editProfile_IMG_BTN_checkEditName.setVisibility(View.VISIBLE);
                }else {
                    editProfile_IMG_BTN_checkEditName.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
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

            if(adapter_chooseIcon == null) {
                adapter_chooseIcon = new Adapter_ChooseIcon(this, user);
                editProfile_LST_icons.setAdapter(adapter_chooseIcon);
            }
        };
    }

    private void setCallbacks() {
        dataManager.setCallBack_setUserProtocol(callback_setUser);
    }

    private void setEditText() {
        editProfile_EDIT_name.setText(user.getName());
    }
}