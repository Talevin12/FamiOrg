//package com.example.famiorg.activities;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.Window;
//import android.widget.Toast;
//
//import com.example.famiorg.R;
//import com.example.famiorg.logic.User;
//import com.google.android.gms.auth.api.identity.BeginSignInRequest;
//import com.google.android.gms.auth.api.identity.SignInCredential;
//import com.google.android.gms.auth.api.signin.GoogleSignIn;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.common.SignInButton;
//import com.google.android.gms.common.api.ApiException;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//public class GoogleLoginActivity extends AppCompatActivity {
//    private SignInButton signInButton;
//
//    GoogleSignInOptions googleSignInOptions;
//    GoogleSignInClient googleSignInClient;
//
//    FirebaseAuth firebaseAuth;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.activity_google_login);
//
//        signInButton = findViewById(R.id.sign_in_button);
//        signInButton.setSize(SignInButton.SIZE_WIDE);
//
//        BeginSignInRequest signInRequest = BeginSignInRequest.builder()
//                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                        .setSupported(true)
//                        // Your server's client ID, not your Android client ID.
//                        .setServerClientId(getString(R.string.default_web_client_id))
//                        // Only show accounts previously used to sign in.
//                        .setFilterByAuthorizedAccounts(true)
//                        .build())
//                .build();
//
//        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//
//        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
//
//        googleSignInClient.signOut();
//
//        signInButton.setOnClickListener(v -> SignIn());
//    }
//
//    private void SignIn() {
//        Intent intent = googleSignInClient.getSignInIntent();
//        startActivityForResult(intent, 100);
//    }
//
////    @Override
////    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
////        super.onActivityResult(requestCode, resultCode, data);
////
////        if(requestCode == 100) {
////            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
////            try {
////                task.getResult(ApiException.class);
////                OpenMainActivity();
////            } catch (ApiException e) {
////                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
////            }
////        }
////    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode) {
//            case REQ_ONE_TAP:
//                try {
//                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
//                    String idToken = credential.getGoogleIdToken();
//                    if (idToken !=  null) {
//                        // Got an ID token from Google. Use it to authenticate
//                        // with Firebase.
//                        Log.d(TAG, "Got ID token.");
//                    }
//                } catch (ApiException e) {
//                    // ...
//                }
//                break;
//        }
//    }
//
//    private void OpenMainActivity() {
//        finish();
//
//        FirebaseDatabase db = FirebaseDatabase.getInstance();
//        DatabaseReference refUsers = db.getReference("Users");
//
//
//        refUsers.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (!snapshot.hasChild(GoogleSignIn.getLastSignedInAccount(getApplicationContext()).getId())) {
//                    User newUser = new User()
//                            .setName(GoogleSignIn.getLastSignedInAccount(getApplicationContext())
//                                    .getDisplayName()
//                                        .split(" ")[0])
//                            .setEmail(GoogleSignIn.getLastSignedInAccount(getApplicationContext())
//                                    .getEmail())
//                            .setIcon(R.drawable.ic_dino);
//
//
//                    refUsers.child(GoogleSignIn.getLastSignedInAccount(getApplicationContext()).getId())
//                            .setValue(newUser);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        startActivity(intent);
//    }
//}