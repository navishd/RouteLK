package com.routelk.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.routelk.app.R;
import com.routelk.app.adapters.ChatAdapter;
import com.routelk.app.models.ChatMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChatAssistantActivity extends AppCompatActivity {

    private static final int SPEECH_REQUEST_CODE = 100;
    private RecyclerView rvChat;
    private ChatAdapter adapter;
    private List<ChatMessage> messages;
    private EditText etMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_assistant);

        etMessage = findViewById(R.id.etMessage);
        rvChat = findViewById(R.id.rvChat);
        messages = new ArrayList<>();
        adapter = new ChatAdapter(messages);
        
        rvChat.setLayoutManager(new LinearLayoutManager(this));
        rvChat.setAdapter(adapter);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        findViewById(R.id.btnSend).setOnClickListener(v -> {
            String text = etMessage.getText().toString().trim();
            if (!text.isEmpty()) {
                sendMessage(text);
                etMessage.setText("");
            }
        });

        findViewById(R.id.btnVoice).setOnClickListener(v -> startVoiceInput());

        addBotMessage("Hello! I'm your RouteLK assistant. How can I help you today? You can ask me to find buses, view your tickets, or manage your profile.");
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "How can I help you?");
        try {
            startActivityForResult(intent, SPEECH_REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(this, "Voice recognition not supported on this device", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                sendMessage(result.get(0));
            }
        }
    }

    private void sendMessage(String text) {
        messages.add(new ChatMessage(text, true));
        adapter.notifyItemInserted(messages.size() - 1);
        rvChat.scrollToPosition(messages.size() - 1);
        processResponse(text.toLowerCase());
    }

    private void addBotMessage(String text) {
        messages.add(new ChatMessage(text, false));
        adapter.notifyItemInserted(messages.size() - 1);
        rvChat.scrollToPosition(messages.size() - 1);
    }

    private void processResponse(String input) {
        if (input.contains("find") || input.contains("search") || input.contains("bus") || input.contains("book")) {
            addBotMessage("I can help you find a bus. What is your destination?");
            addBotMessage("Closing assistant to let you search on the main screen...");
            rvChat.postDelayed(() -> {
                startActivity(new Intent(this, Home.class));
                finish();
            }, 2000);
        } else if (input.contains("ticket") || input.contains("my booking")) {
            addBotMessage("Sure, let's look at your tickets.");
            startActivity(new Intent(this, TicketsActivity.class));
        } else if (input.contains("profile") || input.contains("account") || input.contains("my info")) {
            addBotMessage("Opening your account details.");
            startActivity(new Intent(this, ProfileActivity.class));
        } else if (input.contains("activity") || input.contains("history")) {
            addBotMessage("Showing your recent travel activity.");
            startActivity(new Intent(this, MyActivitiesActivity.class));
        } else if (input.contains("hello") || input.contains("hi") || input.contains("hey")) {
            addBotMessage("Hi there! How can I assist your journey today?");
        } else if (input.contains("help") || input.contains("faq") || input.contains("what can you do")) {
            addBotMessage("I can help you with:\n1. Finding and booking buses\n2. Viewing your tickets\n3. Managing your profile\n4. Checking travel history\nJust type what you need!");
        } else if (input.contains("price") || input.contains("cost")) {
            addBotMessage("Bus prices vary by route and bus type. You can see the exact price when you search for a bus.");
        } else if (input.contains("payment") || input.contains("pay")) {
            addBotMessage("We currently support credit/debit card payments during the checkout process.");
        } else {
            addBotMessage("I'm still learning! You can say things like 'show my tickets', 'find a bus', or 'open my profile'.");
        }
    }
}
