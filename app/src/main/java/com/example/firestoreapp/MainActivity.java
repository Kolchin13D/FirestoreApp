package com.example.firestoreapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText nameET, emailET;
    Button getBtn, setBtn, updBtn, delBtn;
    TextView textView;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private DocumentReference databaseReference = database.collection("Users")
            .document("Friends");

    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";

    public String old_name, old_email;

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
        updBtn = findViewById(R.id.updBtn);
        delBtn = findViewById(R.id.delBtn);

        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveDataToFirestore();
            }
        });

        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GetDataFromFirestore();
                Toast.makeText(getApplicationContext(), "Function off", Toast.LENGTH_SHORT).show();
            }
        });

        updBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateData();
            }
        });
        
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteData();
            }
        });

    }

    private void DeleteData() {

        databaseReference.update(KEY_NAME, FieldValue.delete());

    }

    private void UpdateData() {

        String name = nameET.getText().toString().trim();
        String email = emailET.getText().toString().trim();
        Map<String, Object> data = new HashMap<>();

        data.put(KEY_NAME, name);
        data.put(KEY_EMAIL, email);

        databaseReference.update(data)
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(), "Update OK", Toast.LENGTH_SHORT).show();
                            }
                        }
                ).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addSnapshotListener(this,
                new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                        if (value != null && value.exists()) {
                            String friend_name = value.getString(KEY_NAME);
                            String friend_email = value.getString(KEY_EMAIL);

                            old_name = friend_name;
                            old_email = friend_email;

                            textView.setText("Name: " + friend_name + "\nEmail: " + friend_email);
                        }
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