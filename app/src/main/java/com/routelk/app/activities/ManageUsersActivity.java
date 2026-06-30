package com.routelk.app.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.routelk.app.R;
import com.routelk.app.adapters.UserAdapter;
import com.routelk.app.models.User;

import java.util.ArrayList;
import java.util.List;

public class ManageUsersActivity extends AppCompatActivity {

    RecyclerView usersRecyclerView;

    List<User> userList;
    UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        usersRecyclerView =
                findViewById(R.id.usersRecyclerView);

        usersRecyclerView.setLayoutManager(
                new LinearLayoutManager(this));

        userList = new ArrayList<>();

        adapter =
                new UserAdapter(this, userList);

        usersRecyclerView.setAdapter(adapter);

        loadUsers();
    }

    private void loadUsers() {

        FirebaseFirestore.getInstance()
                .collection("users")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    userList.clear();

                    for (var document : queryDocumentSnapshots) {

                        User user =
                                document.toObject(User.class);

                        userList.add(user);
                    }

                    adapter.notifyDataSetChanged();
                });
    }
}