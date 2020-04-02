package com.example.unbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity
{
    private CardView card;
    public AuthActivity authUser;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        //cardview
        card = findViewById(R.id.cp_course);
        card.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(v.getContext(), KitStarter.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == R.id.sign_out_opt)
        {
            signOut();
            return true;
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }

//    //FirebaseUI provides convenience methods to sign out of Firebase Authenticationn as well as all social identity providers.
    public void signOut()
    {

        //Firebase sign out
        firebaseAuth.signOut();

        //Google sign out
        AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {

                //Create an action when the user has signed out of an application.
                signOutUser();
            }
        });
    }

    private void signOutUser()
    {
        Intent intent =  new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }
}
