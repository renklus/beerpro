package ch.beerpro.presentation.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import ch.beerpro.R;
import ch.beerpro.presentation.MainActivity;
import ch.beerpro.domain.models.User;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG = "SplashScreenActivity";

    private static final int RC_SIGN_IN = 123;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            // redirectToLoginScreen(); // Default provided by lecturer
            provideAnonymousLogin();
        } else {
            Log.i(TAG, "User found, redirect to Home screen");
            redirectToHomeScreenActivity(currentUser);
        }
    }

    private void redirectToLoginScreen() {
        Log.i(TAG, "No user found, redirect to Login screen");
        List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build());
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setIsSmartLockEnabled(false)
                .setAvailableProviders(providers).setLogo(R.drawable.beer_glass_icon)
                .setTheme(R.style.LoginScreenTheme).build(), RC_SIGN_IN);
    }

    private void provideAnonymousLogin() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInAnonymously:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        redirectToHomeScreenActivity(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInAnonymously:failure", task.getException());
                        Toast.makeText(SplashScreenActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        redirectToHomeScreenActivity(null);
                    }
                });
    }

    private void redirectToHomeScreenActivity(FirebaseUser currentUser) {
        String uid = currentUser.getUid();
        String displayName = currentUser.getDisplayName();
        String photoUrl = ""; // currentUser.getPhotoUrl().toString();
        FirebaseFirestore.getInstance().collection(User.COLLECTION).document(uid)
                .update(User.FIELD_NAME, displayName, User.FIELD_PHOTO, photoUrl);

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                Log.i(TAG, "User signed in");
                redirectToHomeScreenActivity(firebaseUser);
            } else if (response == null) {
                Log.w(TAG, "User cancelled signing in");
            } else {
                Log.e(TAG, "Error logging in", response.getError());
            }
        }
    }

}
