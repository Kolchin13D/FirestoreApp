package com.example.firestoreapp;

import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText nameET, emailET;
    Button getBtn, setBtn, updBtn, delBtn;
    TextView textView;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private DocumentReference databaseReference = database.collection("Users")
            .document("Friends");

    private CollectionReference collectionReference = database.collection("Users");

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
                //SaveDataToFirestore();

                SaveDataToNewDocument();
            }
        });

        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GetDataFromFirestore();
                //Toast.makeText(getApplicationContext(), "Function off", Toast.LENGTH_SHORT).show();

                GetDocuments();
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

    private void GetDocuments() {
        collectionReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        String data = "";

                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            User user = snapshot.toObject(User.class);
                            data += "Name: " + user.getName() + " Email: " + user.getEmail() + "\n";
                        }

                        textView.setText(data);
                    }
                });
    }

    private void SaveDataToNewDocument() {

        String name = nameET.getText().toString();
        String email = emailET.getText().toString();

        User user = new User(name, email);

        collectionReference.add(user);

    }

    private void DeleteData() {

        databaseReference.delete();
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

        GetDocuments();

//        databaseReference.addSnapshotListener(this,
//                new EventListener<DocumentSnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//
//                        if (error != null) {
//                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
//                        }
//                        if (value != null && value.exists()) {
//
////                            String friend_name = value.getString(KEY_NAME);
////                            String friend_email = value.getString(KEY_EMAIL);
//
//                            // get user
//                            User user = value.toObject(User.class);
//
//                            textView.setText("Name: " + user.getName() + "\nEmail: " + user.getEmail());
//                        }
//                    }
//                });
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
                });
    }

    private void SaveDataToFirestore() {

        String name = nameET.getText().toString().trim();
        String email = emailET.getText().toString().trim();

        User user = new User();
        user.setName(name);
        user.setEmail(email);

//        Map<String, Object> data = new HashMap<>();
//        data.put(KEY_NAME, name);
//        data.put(KEY_EMAIL, email);

        database.collection("Users")
                .document("Friends")
                .set(user)
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