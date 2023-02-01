package com.example.mlseriesdemonstrator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

class Bug_Data{
    String email, subject, body;
    Bug_Data(String email, String subject, String body)
    {
        this.email = email;
        this.subject = subject;
        this.body = body;
    }
}

public class ReportBug extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText ocUsernameEditText, ocPasswordEditText;
    DatabaseReference reference;
    String email;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_a_bug);
        ocUsernameEditText = findViewById(R.id.addmentorusername);
        ocPasswordEditText = findViewById(R.id.addmentorpassword);


        mAuth = FirebaseAuth.getInstance();
        email = mAuth.getCurrentUser().getEmail();
    }



    public void Report_Bug_Clicked(View view) {
        String username = ocUsernameEditText.getText().toString();
        String password = ocPasswordEditText.getText().toString();

        reference = FirebaseDatabase.
                getInstance().
                getReference("Bugs");

        reference.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    reference = FirebaseDatabase.getInstance()
                            .getReference()
                            .child("Bugs");

//                    String pushRef = reference.push().getKey();
//                    Log.d("TAG", "onComplete: " + pushRef + " reference is: " + reference.toString());
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("email", email);
                    hashMap.put("subject", username);
                    hashMap.put("body", password);
                    hashMap.put("Status", "False");

//                    assert pushRef != null;
                    reference.push().setValue(hashMap);


                    Log.d("TAG", "onComplete: Bug Reported");

                    Toast.makeText(ReportBug.this, "Bug Reported", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(ReportBug.this, "Error connecting to database MentorAddOCActivity-ln58", Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "onComplete: Error connecting to database MentorAddOCActivity-ln58");
                }
            }
        });
        Snackbar.make(view, "Bug Reported", Snackbar.LENGTH_LONG);
        finish();
    }
}