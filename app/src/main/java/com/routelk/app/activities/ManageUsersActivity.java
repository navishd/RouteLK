package com.routelk.app.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.routelk.app.R;
import com.routelk.app.adapters.UserAdapter;
import com.routelk.app.models.User;
import android.app.ProgressDialog;
import android.widget.Toast;

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

        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();

        adapter = new UserAdapter(this, userList);

        usersRecyclerView.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Users...");
        progressDialog.setCancelable(false);

        db = FirebaseFirestore.getInstance();

        listenUsers();
    }

    private void listenUsers(){

        progressDialog.show();

        db.collection("users")
                .addSnapshotListener((value, error) -> {

                    progressDialog.dismiss();

                    if(error != null){

                        Toast.makeText(
                                this,
                                "Failed to load users",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }

                    userList.clear();

                    if(value != null){

                        for(QueryDocumentSnapshot document : value){

                            User user = document.toObject(User.class);

                            user.setDocumentId(document.getId());

                            userList.add(user);

                        }

                        adapter.notifyDataSetChanged();

                    }

                });

    }

}