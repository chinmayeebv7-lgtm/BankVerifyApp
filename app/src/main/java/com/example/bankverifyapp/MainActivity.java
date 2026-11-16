package com.example.bankverifyapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    // Declarations: these were missing in your version
    private TextInputLayout tilName, tilAadhaar, tilPan;
    private TextInputEditText etName, etAadhaar, etPan;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tilName = findViewById(R.id.tilName);
        tilAadhaar = findViewById(R.id.tilAadhaar);
        tilPan = findViewById(R.id.tilPan);
        etName = findViewById(R.id.etName);
        etAadhaar = findViewById(R.id.etAadhaar);
        etPan = findViewById(R.id.etPan);
        tvResult = findViewById(R.id.tvResult);
        Button btnVerify = findViewById(R.id.btnVerify);

        // When Verify is tapped, run verify()
        btnVerify.setOnClickListener(v -> verify());
    }

    private void verify() {
        String name = etName.getText().toString().trim();
        String aadhaar = etAadhaar.getText().toString().trim();
        String pan = etPan.getText().toString().trim().toUpperCase();

        // Clear old errors and result
        tilName.setError(null);
        tilAadhaar.setError(null);
        tilPan.setError(null);
        tvResult.setText("");

        boolean allValid = true;

        if (name.isEmpty()) {
            tilName.setError("Please enter your name");
            allValid = false;
        }
        if (!Validator.isValidAadhaar(aadhaar)) {
            tilAadhaar.setError("Invalid Aadhaar number");
            allValid = false;
        }
        if (!Validator.isValidPan(pan)) {
            tilPan.setError("Invalid PAN (format: ABCPE1234F)");
            allValid = false;
        }

        if (allValid) {
            Intent intent = new Intent(this, OtpActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("aadhaar", Validator.maskAadhaar(aadhaar));
            intent.putExtra("pan", pan);
            startActivity(intent);
        }
    }
}