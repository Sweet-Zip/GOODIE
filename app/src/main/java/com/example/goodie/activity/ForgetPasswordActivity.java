package com.example.goodie.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.goodie.R;

public class ForgetPasswordActivity extends AppCompatActivity {
    private TextView forgetPassText;
    private Button forgetBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        forgetPassText = findViewById(R.id.emailEditTextForget);
        forgetPassText.setText(getIntent().getStringExtra("email"));
        forgetBtn = findViewById(R.id.forgetBtn);
        forgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (forgetPassText.getText().toString().isEmpty()){
                    forgetPassText.setError("Please input your email");
                    forgetPassText.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(forgetPassText.getText().toString()).matches()){
                    forgetPassText.setError("Email is invalid");
                    forgetPassText.requestFocus();
                }
            }
        });
    }
}