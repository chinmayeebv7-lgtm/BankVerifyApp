package com.example.bankverifyapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.security.SecureRandom;

public class OtpActivity extends AppCompatActivity {

    private static final int MAX_ATTEMPTS = 3;

    private String generatedOtp;
    private int attemptsLeft = MAX_ATTEMPTS;

    private TextInputLayout tilOtp;
    private TextInputEditText etOtp;
    private Button btnConfirm;
    private View layoutOtp, layoutResult;
    private TextView tvDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_otp);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tilOtp = findViewById(R.id.tilOtp);
        etOtp = findViewById(R.id.etOtp);
        btnConfirm = findViewById(R.id.btnConfirm);
        layoutOtp = findViewById(R.id.layoutOtp);
        layoutResult = findViewById(R.id.layoutResult);
        tvDetails = findViewById(R.id.tvDetails);
        Button btnDone = findViewById(R.id.btnDone);

        // Read the data sent from MainActivity
        String name = getIntent().getStringExtra("name");
        String maskedAadhaar = getIntent().getStringExtra("aadhaar");
        String pan = getIntent().getStringExtra("pan");

        tvDetails.setText("Name: " + name
                + "\nAadhaar: " + maskedAadhaar
                + "\nPAN: " + pan);

        // Simulate sending an SMS: generate a 6-digit OTP and show it
        generatedOtp = String.format("%06d", new SecureRandom().nextInt(1000000));
        Toast.makeText(this, "Demo OTP: " + generatedOtp, Toast.LENGTH_LONG).show();

        btnConfirm.setOnClickListener(v -> checkOtp());
        btnDone.setOnClickListener(v -> finish());
    }

    private void checkOtp() {
        String entered = etOtp.getText().toString().trim();
        tilOtp.setError(null);

        if (entered.equals(generatedOtp)) {
            // Success: hide the OTP section, show the result section
            layoutOtp.setVisibility(View.GONE);
            layoutResult.setVisibility(View.VISIBLE);
        } else {
            attemptsLeft--;
            if (attemptsLeft == 0) {
                tilOtp.setError("Too many wrong attempts. Go back and try again.");
                btnConfirm.setEnabled(false);
            } else {
                tilOtp.setError("Wrong OTP. Attempts left: " + attemptsLeft);
            }
        }
    }
}