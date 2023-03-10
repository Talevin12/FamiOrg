package com.example.famiorg.assets;

import android.content.Context;
import android.widget.Toast;

import com.example.famiorg.dataManagers.UserDataManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GoogleLoginAssets {

    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;

    UserDataManager userDataManager;
    Context context;

    public GoogleLoginAssets(UserDataManager userDataManager, Context context) {
        this.userDataManager = userDataManager;
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
            userDataManager.getUser(getUserId(), true);
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
