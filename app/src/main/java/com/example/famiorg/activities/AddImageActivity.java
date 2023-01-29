package com.example.famiorg.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.example.famiorg.DataManager;
import com.example.famiorg.GoogleLoginAssets;
import com.example.famiorg.R;
import com.example.famiorg.callbacks.Callback_DataManager;
import com.example.famiorg.logic.ImagePost;
import com.example.famiorg.logic.User;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

public class AddImageActivity extends AppCompatActivity {

    private User user;
    private ImagePost imagePost = new ImagePost();
    private ImageView addImage_IMG_image;
    Uri selectedImageUri;
    private AppCompatEditText addImage_EDITTEXT_description;
    private MaterialButton addImage_BTN_submit;

    Callback_DataManager callback_setUser;

    DataManager dataManager = new DataManager();
    GoogleLoginAssets googleLoginAssets = new GoogleLoginAssets(dataManager, this);

    int SELECT_PICTURE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_image);

        String background = "https://img.freepik.com/free-vector/hand-drawn-minimal-background_23-2149022113.jpg?w=2000";
        Glide.with(this)
                .load(background)
                .centerCrop()
                .into((AppCompatImageView) findViewById(R.id.addImage_IMG_background));

        if (!googleLoginAssets.checkSignIn()) {
            finish();
        }

        findViews();
        initViews();

        initCallbacks();
        setCallbacks();
    }

    private void findViews() {
        addImage_IMG_image = findViewById(R.id.addImage_IMG_image);
        addImage_EDITTEXT_description = findViewById(R.id.addImage_EDITTEXT_description);
        addImage_BTN_submit = findViewById(R.id.addImage_BTN_submit);
    }

    private void initViews() {
        addImage_IMG_image.setOnClickListener(v -> imageChooser());

        addImage_BTN_submit.setOnClickListener(v -> {
            if (selectedImageUri != null &&
                    !addImage_EDITTEXT_description.getText().toString().isBlank()) {

                imagePost.setDescription(addImage_EDITTEXT_description.getText().toString())
                        .setPublishDate(new Date())
                        .setEmail(user.getEmail())
                        .setUserName(user.getName())
                        .setIcon(user.getIcon());

                StorageReference storageRef = FirebaseStorage.getInstance().getReference();

                StorageReference riversRef = storageRef.child("images/" + selectedImageUri.getLastPathSegment());
                UploadTask uploadTask = riversRef.putFile(selectedImageUri);

                uploadTask.addOnFailureListener(exception ->
                        addImage_IMG_image.setImageResource(R.drawable.choose_img)).addOnSuccessListener(taskSnapshot ->
                        storageRef.child("images/" + selectedImageUri.getLastPathSegment()).getDownloadUrl().addOnSuccessListener(uri -> {
                            // Got the download URL for 'users/me/profile.png'
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri1 -> {
                                imagePost.setImageLink(uri1.toString());

                                dataManager.addImage(user.getFamilyId(), imagePost);
                                finish();
                            });
                        }).addOnFailureListener(exception -> Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show()));
            }
        });
    }

    private void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    private void initCallbacks() {
        callback_setUser = (Callback_DataManager) object -> user = (User) object;
    }

    private void setCallbacks() {
        dataManager.setCallBack_setUserProtocol(callback_setUser);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                selectedImageUri = data.getData();

                // update the preview image in the layout
                addImage_IMG_image.setImageURI(selectedImageUri);
            }
        }
    }
}