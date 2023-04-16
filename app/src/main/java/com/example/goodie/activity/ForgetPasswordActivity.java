package com.example.goodie.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goodie.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
    private EditText forgetPassText;
    private TextView loginForget;
    private Button forgetBtn;
    private ProgressBar progressBar;
    FirebaseAuth authProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        forgetPassText = findViewById(R.id.emailEditTextForget);
        forgetPassText.setText(getIntent().getStringExtra("email"));
        forgetBtn = findViewById(R.id.forgetBtn);
        loginForget = findViewById(R.id.loginForget);
        progressBar = findViewById(R.id.forgetProgressBar);
        loginForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForgetPasswordActivity.this.finish();
            }
        });
        forgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = forgetPassText.getText().toString();

                if (forgetPassText.getText().toString().isEmpty()){
                    forgetPassText.setError("Please input your email");
                    forgetPassText.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(forgetPassText.getText().toString()).matches()){
                    forgetPassText.setError("Email is invalid");
                    forgetPassText.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    ResetPassword(email);
                }
            }
        });
    }

    private void ResetPassword(String email) {
        authProfile = FirebaseAuth.getInstance();
        authProfile.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ForgetPasswordActivity.this, "Please check your email inbox for password reset link", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ForgetPasswordActivity.this, FirstActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ForgetPasswordActivity.this, "Something is wrong! Please try again later.", Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}