package com.example.firestoreapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText nameET, emailET;
    Button getBtn, setBtn;
    TextView textView;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private DocumentReference databaseReference = database.collection("Users")
            .document("Friends");

    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        nameET = findViewById(R.id.name);
        emailET = findViewById(R.id.email);
        setBtn = findViewById(R.id.setBtn);
        textView = findViewById(R.id.text);
        getBtn = findViewById(R.id.getBtn);

        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveDataToFirestore();
            }
        });

        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetDataFromFirestore();
            }
        });

    }

    private void GetDataFromFirestore() {

        databaseReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String friend_name = documentSnapshot.getString(KEY_NAME);
                            String friend_email = documentSnapshot.getString(KEY_EMAIL);

                            textView.setText("Name: " + friend_name + "\nEmail: " + friend_email);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "NO DATA TO GET", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void SaveDataToFirestore() {

        String name = nameET.getText().toString().trim();
        String email = emailET.getText().toString().trim();

        Map<String, Object> data = new HashMap<>();
        data.put(KEY_NAME, name);
        data.put(KEY_EMAIL, email);

        database.collection("Users")
                .document("Friends")
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "WE GOT PROBLEMS", Toast.LENGTH_SHORT);
                    }
                });

    }
}