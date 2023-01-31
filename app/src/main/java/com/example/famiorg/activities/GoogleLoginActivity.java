package com.example.famiorg.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.famiorg.R;
import com.example.famiorg.callbacks.Callback_DataManager;
import com.example.famiorg.dataManagers.UserDataManager;
import com.example.famiorg.logic.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GoogleLoginActivity extends AppCompatActivity {

    private SignInButton signInButton;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;

    UserDataManager userDataManager = new UserDataManager();
    Callback_DataManager<User> callback_setUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_google_login);

        signInButton = findViewById(R.id.sign_in_button);

        callback_setUser = (Callback_DataManager) object -> {
            User user = (User) object;

            if(user.getFamilyId() != null) {
                startActivity(new Intent(GoogleLoginActivity.this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            } else {
                startActivity(new Intent(GoogleLoginActivity.this, ChooseFamilyActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        };
        userDataManager.setCallBack_setUserProtocol(callback_setUser);
        // Initialize sign in options
        // the client-id is copied form
        // google-services.json file
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken("414697405996-5av2hirkhpalteoikvf36bbl7dmd413n.apps.googleusercontent.com")
                .requestEmail()
                .build();

        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(GoogleLoginActivity.this, googleSignInOptions);

        signInButton.setOnClickListener(view -> {
            // Initialize sign in intent
            Intent intent = googleSignInClient.getSignInIntent();
            // Start activity for result
            startActivityForResult(intent, 100);
        });

        // Initialize firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

//        firebaseAuth.signOut();

        // Initialize firebase user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        // Check condition
        if (firebaseUser != null) {
            // When user already sign in
            // redirect to profile activity
            userDataManager.getUser(firebaseUser.getUid(), false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check condition
        if (requestCode == 100) {
            // When request code is equal to 100
            // Initialize task
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn
                    .getSignedInAccountFromIntent(data);

            // check condition
            if (signInAccountTask.isSuccessful()) {
                // When google sign in successful
                // Initialize sign in account
                try {
                    // Initialize sign in account
                    GoogleSignInAccount googleSignInAccount = signInAccountTask
                            .getResult(ApiException.class);
                    // Check condition
                    if (googleSignInAccount != null) {
                        // When sign in account is not equal to null
                        // Initialize auth credential
                        AuthCredential authCredential = GoogleAuthProvider
                                .getCredential(googleSignInAccount.getIdToken()
                                        , null);
                        // Check credential
                        firebaseAuth.signInWithCredential(authCredential)
                                .addOnCompleteListener(this, task -> {
                                    // Check condition
                                    if (task.isSuccessful()) {
                                        // When task is successful
                                        FirebaseDatabase db = FirebaseDatabase.getInstance();

                                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                                        db.getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(!snapshot.hasChild(firebaseUser.getUid())){
                                                    User newUser = new User()
                                                            .setName(firebaseUser.getDisplayName().split(" ")[0])
                                                            .setEmail(firebaseUser.getEmail());

                                                    db.getReference("Users")
                                                            .child(firebaseUser.getUid())
                                                            .setValue(newUser);

                                                }

                                                userDataManager.getUser(firebaseUser.getUid(), false);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    } else {
                                        // When task is unsuccessful
                                        // Display Toast
                                        displayToast("Authentication Failed :" + task.getException()
                                                .getMessage());
                                    }
                                });

                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void displayToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}