package com.routelk.app.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.routelk.app.R;
import com.routelk.app.models.User;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context context;
    private ArrayList<User> userList;
    private FirebaseFirestore db;

    public UserAdapter(Context context, ArrayList<User> userList) {
        this.context = context;
        this.userList = userList;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.user_item, parent, false);

        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

        User user = userList.get(position);

        holder.userName.setText(user.getFullName());
        holder.userEmail.setText(user.getEmail());
        holder.userPhone.setText(user.getPhone());

        holder.deleteBtn.setOnClickListener(v -> {

            new AlertDialog.Builder(context)
                    .setTitle("Delete User")
                    .setMessage("Are you sure you want to delete this user?")
                    .setPositiveButton("Delete", (dialog, which) -> {

                        db.collection("users")
                                .document(user.getId())                                .delete()
                                .addOnSuccessListener(unused -> {

                                    Toast.makeText(
                                            context,
                                            "User Deleted Successfully",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                })
                                .addOnFailureListener(e -> {

                                    Toast.makeText(
                                            context,
                                            e.getMessage(),
                                            Toast.LENGTH_SHORT
                                    ).show();

                                });
                    })
                    .setNegativeButton("Cancel", null)
                    .show();

        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        TextView userName, userEmail, userPhone;
        Button deleteBtn;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.userName);
            userEmail = itemView.findViewById(R.id.userEmail);
            userPhone = itemView.findViewById(R.id.userPhone);

            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }

}