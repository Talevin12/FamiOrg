package com.example.famiorg.activities;

import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.famiorg.R;
import com.example.famiorg.adapters.Adapater_ImagePosts;
import com.example.famiorg.assets.GoogleLoginAssets;
import com.example.famiorg.assets.ImageUtils;
import com.example.famiorg.assets.IntentUtils;
import com.example.famiorg.callbacks.Callback_DataManager;
import com.example.famiorg.dataManagers.FamilyDataManager;
import com.example.famiorg.dataManagers.ImageDataManager;
import com.example.famiorg.dataManagers.UserDataManager;
import com.example.famiorg.logic.ImagePost;
import com.example.famiorg.logic.User;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;

public class ImageCatalogActivity extends AppCompatActivity {

    private RecyclerView imageCatalog_LST_images;
    private ExtendedFloatingActionButton imageCatalog_BTN_addImage;

    private Adapater_ImagePosts adapter_imagesPosts;

    private User user;

    Callback_DataManager<User> callback_setUser;
    Callback_DataManager<ImagePost> callback_getImages;
    Callback_DataManager<User> callback_getFamilyMembers;

    ArrayList<ImagePost> imagePosts;
    UserDataManager userDataManager = new UserDataManager();
    FamilyDataManager familyDataManager = new FamilyDataManager();
    ImageDataManager imageDataManager = new ImageDataManager();
    GoogleLoginAssets googleLoginAssets = new GoogleLoginAssets(userDataManager, this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_image_catalog);

//        String background ="https://img.freepik.com/free-photo/abstract-grunge-decorative-relief-navy-blue-stucco-wall-texture-wide-angle-rough-colored-background_1258-28311.jpg?w=2000";
//        ImageUtils.getInstance().load(this, background, findViewById(R.id.imageCatalog_IMG_background));

        ImageUtils.getInstance().loadBlurry(this, getResources(), R.drawable.image_catalog_background, findViewById(R.id.imageCatalog_IMG_background), 5);

        findViews();
        initViews();

//        initRecyclerView();

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
        imageCatalog_LST_images = findViewById(R.id.imageCatalog_LST_images);
        imageCatalog_BTN_addImage = findViewById(R.id.imageCatalog_BTN_addImage);
    }

    private void initViews() {
        imageCatalog_BTN_addImage.setOnClickListener(v -> IntentUtils.getInstance().openPage(this, AddImageActivity.class));
    }

    private void initRecyclerView() {
        adapter_imagesPosts = new Adapater_ImagePosts(this, imagePosts);
        imageCatalog_LST_images.setLayoutManager(new LinearLayoutManager(this));

        imageCatalog_LST_images.setAdapter(adapter_imagesPosts);
    }

    private void initCallbacks() {
        callback_setUser = (Callback_DataManager) object -> {
            user = (User) object;
            imageDataManager.getImages(user.getFamilyId());
        };

        callback_getImages = (Callback_DataManager) object -> {
            imagePosts = (ArrayList<ImagePost>) object;
            familyDataManager.getFamilyMembers(user.getFamilyId());
        };

        callback_getFamilyMembers = (Callback_DataManager) object -> {
            ArrayList<User> famMembers = (ArrayList<User>) object;

            for (User member : famMembers) {
                for (ImagePost img : imagePosts) {
                    if (img.getEmail().equals(member.getEmail())) {
                        img.setUserName(member.getName());
                        img.setIcon(member.getIcon());
                    }
                }
            }

            initRecyclerView();
        };
    }

    private void setCallbacks() {
        userDataManager.setCallBack_setUserProtocol(callback_setUser);
        imageDataManager.setCallback_getImagePosts(callback_getImages);
        familyDataManager.setCallback_getFamilyMembers(callback_getFamilyMembers);
    }
}