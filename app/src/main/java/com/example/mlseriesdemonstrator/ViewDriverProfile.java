package com.example.mlseriesdemonstrator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.util.Objects;

class user_data {
    String DoB, email, photo, fName, lName, flagged, role, gender;
}

public class ViewDriverProfile extends AppCompatActivity {
    String user_email;
    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    user_data user;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        String currentUser = mAuth.getUid();
        Log.d("TAG", "onCreate: " + currentUser);
        if (currentUser == null) {
            Intent intent = new Intent(ViewDriverProfile.this, LoginDriverActivity.class);
            startActivity(intent);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_driver_profile);

        user_email = mAuth.getCurrentUser().getEmail();

        get_user();
    }


    private void get_user() {
        Log.d("TAG", "get_user: ");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    Log.d("TAG", "onDataChange: FOR");
                    String key = child.getKey();
                    String value = child.getValue().toString();

                    String email = (String) child.child("email").getValue();
                    String role = (String) child.child("role").getValue();

//                    Log.d("TAG", "onDataChange: GOT EMAIL");

                    if (Objects.equals(email, user_email) && Objects.equals(role, "driver")) {
                        Log.d("TAG", "onDataChange: INSIDE");
                        Log.d("TAG", "onDataChange: KEY: " + child.getKey().toString() + " " + child.getValue().toString());
                        //get the data here
                        user = new user_data();
                        user.email = (String) child.child("email").getValue();
                        user.DoB = (String) child.child("dateOfBirth").getValue();
                        user.flagged = (String) child.child("isFlagged").getValue().toString();
                        user.fName = (String) child.child("firstName").getValue();
                        user.lName = (String) child.child("lastName").getValue();
                        user.role = (String) child.child("role").getValue();
                        user.gender = (String) child.child("gender").getValue();
                        user.photo = (String) child.child("image").getValue();

                        Log.d("TAG", "onDataChange: Boutto Change DATA");
                        TextView text = (TextView) findViewById(R.id.txt_first_name);
                        text.setText(user.fName);
                        text = (TextView) findViewById(R.id.txt_last_name);
                        text.setText(user.lName);
//                        text = (TextView) findViewById(R.id.txt_email);
//                        text.setText(user.email);
                        text = (TextView) findViewById(R.id.txt_dob);
                        text.setText(user.DoB);
                        text = (TextView) findViewById(R.id.txt_flag);
                        text.setText(user.flagged);

                        ImageView imageView = (ImageView) findViewById(R.id.imageView2);


                        StorageReference storageRef = FirebaseStorage.getInstance().getReference("profilePictures");

                        storageRef.child(child.getKey()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Got the download URL for 'users/me/profile.png'
                                Log.d("TAG", "onSuccess: " + uri.toString());
                                Glide.with(ViewDriverProfile.this)
                                        .load(uri.toString())
                                        .into(imageView);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });

                        return;
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

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
            Toast.makeText(getApplicationContext(), "Please wait, it may take a few minute...", Toast.LENGTH_SHORT).show();
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.d("TAG", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }


}