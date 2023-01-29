package com.example.famiorg;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GoogleLoginAssets {

    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;

    DataManager dataManager;
    Context context;

    public GoogleLoginAssets(DataManager dataManager, Context context) {
        this.dataManager = dataManager;
        this.context = context;
    }

    public boolean checkSignIn() {
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .build();

        googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null) {
            dataManager.getUser(firebaseUser.getUid(), true);
            return true;
        } else {
            return false;
        }
    }

    public String getUserId() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void SignOut() {
        googleSignInClient.signOut().addOnCompleteListener(task -> {
            // Check condition
            if (task.isSuccessful()) {
                // When task is successful
                // Sign out from firebase
                FirebaseAuth.getInstance().signOut();
                // Display Toast
                Toast.makeText(context, "Logout successful", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
