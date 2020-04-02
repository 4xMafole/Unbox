package com.example.unbox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class KitStarter extends AppCompatActivity
{
    private View basicsProgrammingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.starter_kit_layout);

        basicsProgrammingLayout = findViewById(R.id.basics_of_pro_layout);
        basicsProgrammingLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getBaseContext(), VideoPlayer.class);
                startActivity(intent);
            }
        });
    }
}
