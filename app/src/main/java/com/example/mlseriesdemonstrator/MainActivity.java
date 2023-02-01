package com.example.mlseriesdemonstrator;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mlseriesdemonstrator.object.DriverDrowsinessDetectionActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = findViewById(R.id.imageView3);
        Drawable myDrawable = getResources().getDrawable(R.drawable.ghost);
        imageView.setImageDrawable(myDrawable);

        Log.d("TAG", "onCreate: written to DB: ");
        Intent intent = new Intent(this, LoginDriverActivity.class);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
            }
        }, 3000);
    }

    public void GoToNextScreen(View view) {
        Intent intent = new Intent(this, LoginDriverActivity.class);
        startActivity(intent);
    }
}