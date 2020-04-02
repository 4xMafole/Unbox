package com.example.unbox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;
import java.util.List;

public class AuthActivity extends AppCompatActivity
{

    private static final int RC_SIGN_IN = 123;
    private SignInButton signInBtn;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        firebaseAuth = FirebaseAuth.getInstance();

        signInBtn = findViewById(R.id.sign_in_btn);

        signInBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                createSignInIntent();
            }
        });

    }

    public void createSignInIntent()
    {
        //Choose authentication provider
        List<AuthUI.IdpConfig> provider = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build());

        //Create and launch the sign-in intent
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(provider).build(), RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN)
        {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if(resultCode == RESULT_OK)
            {
                //Successfully signed in
                Toast.makeText(this, "You have successfully login!", Toast.LENGTH_SHORT).show();
                
                //Get current User from the database and deal with its information.
                FirebaseUser user = firebaseAuth.getCurrentUser();
                //Open main activity
                Intent intentMain = new Intent(this, MainActivity.class);
                startActivity(intentMain);
                finish();
                //...Here you can add anything you want after authentication meaning after the user has successful login into the computer.
            }
            else
            {
                //Sign in failed. If response is null the user canceled the
                //sign-in flow using the back button. Otherwise check
                //response.getError().getErrorCode() and handle the error.
                //...
                Toast.makeText(this, "Failed sign in", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        //Check if user is signed in (non-null) and update UI accordingly
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser)
    {
        if(currentUser != null)
        {
            Intent intentMain = new Intent(this, MainActivity.class);
            startActivity(intentMain);
            finish();
        }
    }



    //You can also completely delete the user's account
    public void deleteAccount()
    {
        AuthUI.getInstance().delete(this).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                //Create or show somethinng when the user has deleted an account
            }
        });
    }
}