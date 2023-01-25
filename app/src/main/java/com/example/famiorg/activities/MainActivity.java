package com.example.famiorg.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.famiorg.DataManager;
import com.example.famiorg.GoogleLoginAssets;
import com.example.famiorg.R;
import com.example.famiorg.adapters.Adapter_MyfamilyIcons;
import com.example.famiorg.callbacks.Callback_DataManager;
import com.example.famiorg.logic.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView main_LBL_name;
    private AppCompatImageView main_IMG_myIcon;
    private ExtendedFloatingActionButton main_BTN_settings;

    private AppCompatImageButton main_IMG_BTN_calendar;
    private AppCompatImageButton main_IMG_BTN_images;
    private AppCompatImageButton main_IMG_BTN_shoppingList;

    private TextView main_LBL_familyName;
    private RecyclerView main_RECYCLE_familyIcons;

    private MaterialButton main_BTN_signout;

//    GoogleSignInOptions googleSignInOptions;
//    GoogleSignInClient googleSignInClient;

    private User user = new User();

    DataManager dataManager = new DataManager();

    GoogleLoginAssets googleLoginAssets = new GoogleLoginAssets(dataManager, this);

    ArrayList<User> familyMembers = new ArrayList<>();
    private Adapter_MyfamilyIcons adapter_myfamilyIcons = new Adapter_MyfamilyIcons(this, familyMembers);

    Callback_DataManager<User> callback_addOrUpdateFamilyMember;
    Callback_DataManager<User> callback_removeFamilyMember;
    Callback_DataManager<User> callback_setUser;
    Callback_DataManager<String> callback_setFamilyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        findViews();
        initViews();

        initCallbacks();
        setCallbacks();

        setViewsImages();

        initRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!googleLoginAssets.checkSignIn()) {
            finish();
        }
    }

    private void findViews() {
        main_LBL_name = findViewById(R.id.main_LBL_name);
        main_IMG_myIcon = findViewById(R.id.main_IMG_myIcon);
        main_BTN_settings = findViewById(R.id.main_BTN_settings);

        main_IMG_BTN_calendar = findViewById(R.id.main_IMG_BTN_calendar);
        main_IMG_BTN_images = findViewById(R.id.main_IMG_BTN_images);
        main_IMG_BTN_shoppingList = findViewById(R.id.main_IMG_BTN_shoppingList);

        main_LBL_familyName = findViewById(R.id.main_LBL_familyName);
        main_RECYCLE_familyIcons = findViewById(R.id.main_LST_familyIcons);

        main_BTN_signout = findViewById(R.id.main_BTN_signout);
    }

    private void initViews() {
        main_IMG_myIcon.setOnClickListener(v -> flipIcon(main_IMG_myIcon));

        main_BTN_settings.setOnClickListener(v -> openEditProfile());

        main_IMG_BTN_shoppingList.setOnClickListener(v -> openShoppingList());
        main_IMG_BTN_images.setOnClickListener(v -> openImageCatalog());

        main_BTN_signout.setOnClickListener(v -> {
            finish();
            googleLoginAssets.SignOut();
        });
    }

    private void initCallbacks() {
        callback_addOrUpdateFamilyMember = (Callback_DataManager) object -> {
            adapter_myfamilyIcons.addOrUpdate((User)object);
        };

        callback_removeFamilyMember = (Callback_DataManager) object -> {
            adapter_myfamilyIcons.remove(user);
        };

        callback_setUser = (Callback_DataManager) object -> {
            user = (User)object;

            if(user != null) {
                setUserInUI();
                dataManager.getFamilyName(user.getFamilyId()/*, refFamilyName*/);

                dataManager.getFamilyMembersRT(user.getFamilyId());
            }
        };

        callback_setFamilyName = (Callback_DataManager) object -> {
            setFamilyNameInUI((String)object);
        };
    }

    private void setCallbacks() {
        dataManager.setCallBack_setUserProtocol(callback_setUser);

        dataManager.setCallBack_setFamilyName(callback_setFamilyName);

        dataManager.setCallBack_addOrUpdateFamilyMemberProtocol(callback_addOrUpdateFamilyMember);

        dataManager.setCallBack_removeFamilyMemberProtocol(callback_removeFamilyMember);
    }

    private void setViewsImages() {
        String background ="https://img.freepik.com/free-photo/top-view-background-beautiful-white-grey-brown-cream-blue-background_140725-72219.jpg?w=2000";
        Glide.with(this)
                .load(background)
                .centerCrop()
                .into((AppCompatImageView) findViewById(R.id.main_IMG_background));

        String calendarImg ="https://thumbs.dreamstime.com/b/red-pin-event-calendar-background-close-up-time-red-pin-event-calendar-background-close-up-time-149540392.jpg";
        Glide.with(this)
                .load(calendarImg)
                .centerCrop()
                .into(main_IMG_BTN_calendar);

        String imagesImg ="https://dvyvvujm9h0uq.cloudfront.net/com/articles/1585856768-family-2.jpg";
        Glide.with(this)
                .load(imagesImg)
                .centerCrop()
                .into(main_IMG_BTN_images);

        String shoppingListImg ="https://img.freepik.com/premium-photo/shopping-list-shopping-cart-wooden-background_165146-326.jpg?w=2000";
        Glide.with(this)
                .load(shoppingListImg)
                .centerCrop()
                .into(main_IMG_BTN_shoppingList);
    }

    private void initRecyclerView() {
        main_RECYCLE_familyIcons.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));

        main_RECYCLE_familyIcons.setAdapter(adapter_myfamilyIcons);
    }

    private void setUserInUI() {
        main_LBL_name.setText(user.getName());

        main_IMG_myIcon.setImageResource(user.getIcon());
    }

    private void setFamilyNameInUI(String familyName) {
        main_LBL_familyName.setText(familyName);
    }

    private void flipIcon(AppCompatImageView icon) {
        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.ABSOLUTE);
        anim.setDuration(700);

        // Start animating the image
        icon.startAnimation(anim);
    }

    private void openShoppingList() {
        Intent intent = new Intent(this, ShoppingListActivity.class);
        startActivity(intent);
    }

    private void openImageCatalog() {
        Intent intent = new Intent(this, ImageCatalogActivity.class);
        startActivity(intent);
    }

    private void openEditProfile() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }
}