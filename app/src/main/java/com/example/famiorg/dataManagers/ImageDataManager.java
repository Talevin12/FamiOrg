package com.example.famiorg.dataManagers;

import androidx.annotation.NonNull;

import com.example.famiorg.callbacks.Callback_DataManager;
import com.example.famiorg.logic.ImagePost;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ImageDataManager {
    private Callback_DataManager callback_getImagePosts;

    public void setCallback_getImagePosts(Callback_DataManager callback_getImagePosts) {
        this.callback_getImagePosts = callback_getImagePosts;
    }

    public void addImage(String famId, ImagePost newImagePost) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refImagePosts = db.getReference("Families").child(famId).child("imagePosts");

        refImagePosts.child(String.valueOf(newImagePost.getId())).setValue(newImagePost);
    }

    public void getImages(String famId) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refImagePosts = db.getReference("Families").child(famId).child("imagePosts");

        refImagePosts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<ImagePost> imagePosts = new ArrayList<>();

                for (DataSnapshot child : snapshot.getChildren()) {
                    imagePosts.add(child.getValue(ImagePost.class));
                }

                callback_getImagePosts.getObject(imagePosts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
