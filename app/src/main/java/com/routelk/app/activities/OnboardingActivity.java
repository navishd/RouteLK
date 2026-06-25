package com.routelk.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.routelk.app.R;

public class OnboardingActivity extends AppCompatActivity {

    private ImageView ivIllustration;
    private TextView tvTitle;
    private TextView tvSubtitle;
    private LinearLayout dotsLayout;
    private int currentStep = 0;

    private final String[] titles = {
            "Book Your Journey\nEasily & Quickly",
            "Safe and Comfortable\nTravel",
            "Real-time Tracking"
    };

    private final String[] subtitles = {
            "Search, compare and book highway bus tickets anytime.",
            "Enjoy your trip with our highly maintained luxury buses.",
            "Track your bus in real-time and never miss a ride."
    };

    // Note: Reusing the same image for demo as only one exists.
    // Replace with @drawable/onboarding_image2 etc. when available.
    private final int[] images = {
            R.drawable.onboarding_image,
            R.drawable.onboarding_image,
            R.drawable.onboarding_image
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        ivIllustration = findViewById(R.id.ivIllustration);
        tvTitle = findViewById(R.id.tvTitle);
        tvSubtitle = findViewById(R.id.tvSubtitle);
        dotsLayout = findViewById(R.id.dotsLayout);
        Button btnSkip = findViewById(R.id.btnSkip);
        ImageButton btnNext = findViewById(R.id.btnNext);

        btnSkip.setOnClickListener(v -> goToMainScreen());

        btnNext.setOnClickListener(v -> {
            if (currentStep < titles.length - 1) {
                currentStep++;
                updateUI();
            } else {
                goToMainScreen();
            }
        });

        updateUI();
    }

    private void updateUI() {
        tvTitle.setText(titles[currentStep]);
        tvSubtitle.setText(subtitles[currentStep]);
        ivIllustration.setImageResource(images[currentStep]);
        updateDots();
    }

    private void updateDots() {
        for (int i = 0; i < dotsLayout.getChildCount(); i++) {
            View dot = dotsLayout.getChildAt(i);
            if (i == currentStep) {
                dot.setBackgroundResource(R.drawable.dot_active);
            } else {
                dot.setBackgroundResource(R.drawable.dot_inactive);
            }
        }
    }

    private void goToMainScreen() {
        // Redirecting to LoginActivity as HomeActivity does not exist in the project
        Intent intent = new Intent(OnboardingActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
