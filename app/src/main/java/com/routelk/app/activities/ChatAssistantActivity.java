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
        String inputLower = input.toLowerCase();

        if (inputLower.contains("timetable") || inputLower.contains("bus to") || inputLower.contains("buses to") || inputLower.contains("go to")) {
            String to = "";
            String from = "";

            // Try to extract "from X to Y" or "to Y from X"
            if (inputLower.contains("from") && inputLower.contains("to")) {
                if (inputLower.indexOf("from") < inputLower.indexOf("to")) {
                    // "from [from] to [to]"
                    from = inputLower.substring(inputLower.indexOf("from") + 4, inputLower.indexOf("to")).trim();
                    to = inputLower.substring(inputLower.indexOf("to") + 2).trim();
                } else {
                    // "to [to] from [from]"
                    to = inputLower.substring(inputLower.indexOf("to") + 2, inputLower.indexOf("from")).trim();
                    from = inputLower.substring(inputLower.indexOf("from") + 4).trim();
                }
            } else if (inputLower.contains("to")) {
                to = inputLower.substring(inputLower.indexOf("to") + 2).trim();
            } else if (inputLower.contains("for")) {
                to = inputLower.substring(inputLower.indexOf("for") + 3).trim();
            }

            if (!to.isEmpty()) {
                if (from.isEmpty()) {
                    addBotMessage("I found you want to go to " + capitalize(to) + ". Where are you traveling from? (e.g., 'from Colombo')");
                } else {
                    showBusTimetable(from, to);
                }
                return;
            }
        }

        if (inputLower.startsWith("from ") && inputLower.length() > 5) {
            String from = inputLower.substring(5).trim();
            // Try to find the last mentioned "to" destination
            String lastTo = findLastDestination();
            if (!lastTo.isEmpty()) {
                showBusTimetable(from, lastTo);
                return;
            }
        }

        if (inputLower.contains("find") || inputLower.contains("search") || inputLower.contains("bus") || inputLower.contains("book")) {
            addBotMessage("I can help you find a bus. Please tell me your 'from' and 'to' locations, for example: 'Bus from Colombo to Kandy'");
        } else if (inputLower.contains("ticket") || inputLower.contains("my booking")) {
            addBotMessage("Sure, let's look at your tickets.");
            startActivity(new Intent(this, TicketsActivity.class));
        } else if (inputLower.contains("profile") || inputLower.contains("account") || inputLower.contains("my info")) {
            addBotMessage("Opening your account details.");
            startActivity(new Intent(this, ProfileActivity.class));
        } else if (inputLower.contains("activity") || inputLower.contains("history")) {
            addBotMessage("Showing your recent travel activity.");
            startActivity(new Intent(this, MyActivitiesActivity.class));
        } else if (inputLower.contains("hello") || inputLower.contains("hi") || inputLower.contains("hey")) {
            addBotMessage("Hi there! How can I assist your journey today?");
        } else if (inputLower.contains("help") || inputLower.contains("faq") || inputLower.contains("what can you do")) {
            addBotMessage("I can help you with:\n1. Finding and booking buses\n2. Viewing your tickets\n3. Managing your profile\n4. Checking travel history\nJust type what you need!");
        } else if (inputLower.contains("price") || inputLower.contains("cost")) {
            addBotMessage("Bus prices vary by route and bus type. You can see the exact price when you search for a bus.");
        } else if (inputLower.contains("payment") || inputLower.contains("pay")) {
            addBotMessage("We currently support credit/debit card payments during the checkout process.");
        } else {
            addBotMessage("I'm still learning! You can say things like 'show my tickets', 'find a bus', or 'open my profile'.");
        }
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private String findLastDestination() {
        for (int i = messages.size() - 1; i >= 0; i--) {
            ChatMessage msg = messages.get(i);
            if (!msg.isUser() && msg.getMessage().contains("want to go to ")) {
                String text = msg.getMessage();
                return text.substring(text.indexOf("go to ") + 6, text.indexOf(".")).trim();
            }
        }
        return "";
    }

    private void showBusTimetable(String from, String to) {
        addBotMessage("Finding the best buses from " + capitalize(from) + " to " + capitalize(to) + "...");
        
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        String today = sdf.format(new java.util.Date());

        Intent intent = new Intent(this, BusListScreen.class);
        intent.putExtra("FROM", capitalize(from));
        intent.putExtra("TO", capitalize(to));
        intent.putExtra("DATE", today);
        intent.putExtra("TIME", "Any Time");
        intent.putExtra("PASSENGERS", "1 Adult");
        intent.putExtra("IS_FOR_OTHERS", false);
        
        rvChat.postDelayed(() -> startActivity(intent), 1500);
    }
}
