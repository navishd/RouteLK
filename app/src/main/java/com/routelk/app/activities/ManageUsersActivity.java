package com.routelk.app.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.routelk.app.R;
import com.routelk.app.adapters.UserAdapter;
import com.routelk.app.models.User;
import com.routelk.app.services.UserService;

import java.util.ArrayList;
import java.util.List;

public class ManageUsersActivity extends AppCompatActivity {

    RecyclerView usersRecyclerView;
    List<User> userList;
    UserAdapter adapter;
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        userService = new UserService();
        usersRecyclerView = findViewById(R.id.usersRecyclerView);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();
        adapter = new UserAdapter(this, userList);
        usersRecyclerView.setAdapter(adapter);

        loadUsers();
    }

    private void loadUsers() {
        userService.getAllUsers(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                userList.clear();
                for (DocumentSnapshot document : task.getResult().getDocuments()) {
                    User user = document.toObject(User.class);
                    if (user != null) {
                        userList.add(user);
                    }
                }
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Failed to load users", Toast.LENGTH_SHORT).show();
            }
        });
    }
}