package com.example.mlseriesdemonstrator;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mlseriesdemonstrator.object.DriverDrowsinessDetectionActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DriverDashboard extends AppCompatActivity {
    private FirebaseAuth mAuth;
    String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getUid();
        if (currentUser == null)
        {
            Intent intent = new Intent(this, LoginDriverActivity.class);
            startActivity(intent);
        }

        setContentView(R.layout.activity_driver_dashboard);
        FirebaseUser user = mAuth.getCurrentUser();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getUid();
        if (currentUser == null)
        {
            Intent intent = new Intent(this, LoginDriverActivity.class);
            startActivity(intent);
        }
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public void OnDrowsinessClick(View view) {
        Intent intent = new Intent(this, DriverDrowsinessDetectionActivity.class);
        intent.putExtra("name", "Driver Drowsiness Detector");
        startActivity(intent);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginDriverActivity.class);
        startActivity(intent);
    }

    public void Report_Bud_Clicked(View view) {
        Intent intent = new Intent(this, ReportBug.class);
        startActivity(intent);
    }

    public void view_driver_profile(View view) {
        Intent intent = new Intent(this, ViewDriverProfile.class);
        startActivity(intent);
    }
}