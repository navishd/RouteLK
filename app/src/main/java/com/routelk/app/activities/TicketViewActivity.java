package com.routelk.app.activities;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import com.routelk.app.R;
import com.routelk.app.adapters.TicketPagerAdapter;
import com.routelk.app.models.Booking;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class TicketViewActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TextView tvTicketCounter;
    private ImageView btnPrevTicket;
    private ImageView btnNextTicket;
    private TabLayout tabIndicator;
    private Button btnDownload;

    private FirebaseFirestore db;
    private ArrayList<Booking> tickets = new ArrayList<>();
    private String bookingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_view);

        db = FirebaseFirestore.getInstance();
        initializeViews();

        bookingId = getIntent().getStringExtra("BOOKING_ID");

        if (bookingId == null) {
            Toast.makeText(this, "Booking ID missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadTicket();

        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
    }

    private void initializeViews() {
        viewPager = findViewById(R.id.viewPagerTickets);
        tvTicketCounter = findViewById(R.id.tvTicketCounter);
        btnPrevTicket = findViewById(R.id.btnPrevTicket);
        btnNextTicket = findViewById(R.id.btnNextTicket);
        tabIndicator = findViewById(R.id.tabIndicator);
        btnDownload = findViewById(R.id.btnDownload);

        if (btnDownload != null) {
            btnDownload.setEnabled(false);
            btnDownload.setOnClickListener(v -> downloadTicketsAsPDF());
        }
    }

    private void loadTicket() {
        db.collection("bookings")
                .whereGreaterThanOrEqualTo("id", bookingId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    tickets.clear();
                    for (QueryDocumentSnapshot doc : snapshot) {
                        String id = doc.getString("id");
                        if (id != null && id.startsWith(bookingId)) {
                            Booking booking = doc.toObject(Booking.class);
                            tickets.add(booking);
                        }
                    }

                    if (tickets.isEmpty()) {
                        Toast.makeText(this, "Ticket not found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (btnDownload != null) {
                        btnDownload.setEnabled(true);
                    }

                    setupPager();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Loading failed", Toast.LENGTH_SHORT).show();
                });
    }

    private void setupPager() {
        TicketPagerAdapter adapter = new TicketPagerAdapter(tickets);
        viewPager.setAdapter(adapter);

        tvTicketCounter.setText("1/" + tickets.size());

        new TabLayoutMediator(tabIndicator, viewPager, (tab, position) -> {
        }).attach();

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tvTicketCounter.setText((position + 1) + "/" + tickets.size());
            }
        });

        btnPrevTicket.setOnClickListener(v -> {
            int current = viewPager.getCurrentItem();
            if (current > 0) {
                viewPager.setCurrentItem(current - 1);
            }
        });

        btnNextTicket.setOnClickListener(v -> {
            int current = viewPager.getCurrentItem();
            if (current < tickets.size() - 1) {
                viewPager.setCurrentItem(current + 1);
            }
        });
    }

    private void downloadTicketsAsPDF() {
        if (tickets.isEmpty()) {
            Toast.makeText(this, "No tickets to download", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                return;
            }
        }

        PdfDocument pdfDocument = new PdfDocument();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < tickets.size(); i++) {
            Booking booking = tickets.get(i);
            View view = inflater.inflate(R.layout.item_ticket_card, null);

            // Bind data to view
            TextView tvBookingCode = view.findViewById(R.id.tvBookingCode);
            TextView tvFromCity = view.findViewById(R.id.tvFromCity);
            TextView tvToCity = view.findViewById(R.id.tvToCity);
            TextView tvDateDetail = view.findViewById(R.id.tvDateDetail);
            TextView tvSeatDetail = view.findViewById(R.id.tvSeatDetail);
            TextView tvBusDetail = view.findViewById(R.id.tvBusDetail);
            TextView tvFromCode = view.findViewById(R.id.tvFromCode);
            TextView tvToCode = view.findViewById(R.id.tvToCode);
            TextView tvStartTime = view.findViewById(R.id.tvStartTime);
            TextView tvBoardingTime = view.findViewById(R.id.tvBoardingTime);
            TextView tvPassengerDetail = view.findViewById(R.id.tvPassengerDetail);

            tvBookingCode.setText(booking.getId());
            tvFromCity.setText(booking.getFrom().toUpperCase());
            tvFromCode.setText(booking.getFrom().substring(0, Math.min(booking.getFrom().length(), 3)).toUpperCase());
            tvToCity.setText(booking.getTo().toUpperCase());
            tvToCode.setText(booking.getTo().substring(0, Math.min(booking.getTo().length(), 3)).toUpperCase());
            tvDateDetail.setText(booking.getDate().toUpperCase());
            tvSeatDetail.setText(booking.getSeatNo());
            tvBusDetail.setText(booking.getBusName());
            
            if (tvPassengerDetail != null && booking.getPassengerName() != null) {
                tvPassengerDetail.setText(booking.getPassengerName().toUpperCase());
            }

            if (booking.getTime() != null && !booking.getTime().equals("N/A")) {
                tvStartTime.setText(booking.getTime());
                tvBoardingTime.setText(booking.getTime());
            }

            // Measure and layout the view
            int width = 1080;
            int height = 1920;
            view.measure(
                    View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST)
            );
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(view.getMeasuredWidth(), view.getMeasuredHeight(), i + 1).create();
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);

            Canvas canvas = page.getCanvas();
            view.draw(canvas);
            pdfDocument.finishPage(page);
        }

        String fileName = "RouteLK_Ticket_" + bookingId + ".pdf";

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

                Uri uri = getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);
                if (uri != null) {
                    OutputStream outputStream = getContentResolver().openOutputStream(uri);
                    if (outputStream != null) {
                        pdfDocument.writeTo(outputStream);
                        outputStream.close();
                        Toast.makeText(this, "Ticket saved to Downloads", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
                pdfDocument.writeTo(new FileOutputStream(file));
                Toast.makeText(this, "Ticket saved to " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving ticket: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            pdfDocument.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadTicketsAsPDF();
            } else {
                Toast.makeText(this, "Permission denied to write to storage", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
