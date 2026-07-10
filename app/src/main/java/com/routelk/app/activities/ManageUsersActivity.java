package com.routelk.app.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.routelk.app.R;
import com.routelk.app.adapters.UserAdapter;
import com.routelk.app.models.User;

import java.util.ArrayList;

public class ManageUsersActivity extends AppCompatActivity {

    private RecyclerView usersRecyclerView;

    private ArrayList<User> userList;

    private UserAdapter adapter;

    private FirebaseFirestore db;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);


        usersRecyclerView = findViewById(R.id.usersRecyclerView);

        usersRecyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );


        userList = new ArrayList<>();

        adapter = new UserAdapter(
                this,
                userList
        );

        usersRecyclerView.setAdapter(adapter);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Users...");
        progressDialog.setCancelable(false);


        db = FirebaseFirestore.getInstance();


        loadUsers();

    }



    private void loadUsers(){

        progressDialog.show();


        db.collection("users")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {


                    progressDialog.dismiss();


                    userList.clear();


                    for(QueryDocumentSnapshot document : queryDocumentSnapshots){


                        User user = document.toObject(User.class);


                        if(user != null){

                            // Save Firestore document ID
                            user.setDocumentId(
                                    document.getId()
                            );


                            userList.add(user);

                        }

                    }


                    adapter.notifyDataSetChanged();



                    if(userList.isEmpty()){

                        Toast.makeText(
                                this,
                                "No Users Found",
                                Toast.LENGTH_SHORT
                        ).show();

                    }


                })
                .addOnFailureListener(e -> {


                    progressDialog.dismiss();


                    Toast.makeText(
                            this,
                            "Failed : " + e.getMessage(),
                            Toast.LENGTH_SHORT
                    ).show();


                });

    }

}