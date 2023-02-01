package com.example.mlseriesdemonstrator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginDriverActivity extends AppCompatActivity {

    EditText usernameEditText, passwordEditText;
    DatabaseReference databaseReference;
    String enteredPassword, enteredUsername;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        usernameEditText = findViewById(R.id.inputusername);
        passwordEditText = findViewById(R.id.inputpassword);

        mAuth = FirebaseAuth.getInstance();

        String currentUser = mAuth.getUid();
        if (currentUser != null)
        {
            Intent intent = new Intent(LoginDriverActivity.this, DriverDashboard.class);
            startActivity(intent);
        }

    }

    public void attempt_admin_login(View view) {
        Log.d("TAG", "attempt_admin_login: ");
        enteredUsername = usernameEditText.getText().toString();
        enteredPassword = passwordEditText.getText().toString();

        redirectAdmin(enteredUsername);
    }
    private void redirectAdmin(String username)
    {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child: snapshot.getChildren())
                {
                    Log.d("TAG", "onDataChange: ");
                    String key = child.getKey();
                    String value = child.getValue().toString();
//                    Log.d("TAG", "onDataChange: key: " + key + " value: " + value);

                    String email = (String) child.child("email").getValue();
                    String role = (String) child.child("role").getValue();

                    if (Objects.equals(email, enteredUsername) && Objects.equals(role, "driver"))
                    {
                        // do authentication code here

                        assert email != null;
                        Log.d("TAG", "Authentication: " + email + " pass: " + enteredPassword.toString());
                        mAuth.signInWithEmailAndPassword(email, enteredPassword)
                                .addOnCompleteListener(
                                        new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(
                                                    @NonNull Task<AuthResult> task)
                                            {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getApplicationContext(),
                                                                    "Login successful!!",
                                                                    Toast.LENGTH_LONG)
                                                            .show();

                                                    // if sign-in is successful
                                                    Intent intent = new Intent(LoginDriverActivity.this, DriverDashboard.class);
                                                    startActivity(intent);
                                                }

                                                else {
                                                    Toast.makeText(getApplicationContext(),
                                                                    "Login Unsuccessful!!",
                                                                    Toast.LENGTH_LONG)
                                                            .show();
                                                    // sign-in failed
                                                    Log.d("TAG", "onComplete: Login Failed");
                                                }
                                            }
                                        });

                    }
//                    else
//                        Log.d("TAG", "onDataChange: " + "Invalid username or password");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", "onCancelled: Cancelled bhai");
            }
        });

    }
}