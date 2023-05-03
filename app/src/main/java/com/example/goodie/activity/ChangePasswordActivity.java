package com.example.goodie.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.goodie.R;
import com.example.goodie.function.CustomEditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangePasswordActivity extends AppCompatActivity {
    private Toolbar toolBar;
    private ImageView backIcon;
    private EditText currentPass, newPassEditText, conPassEditText;
    private TextView textView13;
    private FirebaseAuth authProfile;
    private Button confirmBtn, authBtn;
    private ProgressBar progressBar;
    private String userCurrentPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        toolBar = findViewById(R.id.changePassToolBar);
        backIcon = findViewById(R.id.addProductBackIcon);
        currentPass = findViewById(R.id.currentPassEditText);
        newPassEditText = findViewById(R.id.newPassEditText);
        conPassEditText = findViewById(R.id.conPassEditText);
        authBtn = findViewById(R.id.authBtn);
        confirmBtn = findViewById(R.id.changePassBtn);
        textView13 = findViewById(R.id.textView13);
        progressBar = findViewById(R.id.changePassProgressBar);
        new CustomEditText(currentPass);
        new CustomEditText(newPassEditText);
        new CustomEditText(conPassEditText);
        newPassEditText.setEnabled(false);
        conPassEditText.setEnabled(false);
        confirmBtn.setEnabled(false);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePasswordActivity.this.finish();
            }
        });

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        if (firebaseUser.equals("")) {
            Toast.makeText(ChangePasswordActivity.this, "Something went wrong! Please try again later.", Toast.LENGTH_LONG).show();
            ChangePasswordActivity.this.finish();
        } else {
            ReAuthenticateUser(firebaseUser);
        }
    }

    private void ReAuthenticateUser(FirebaseUser firebaseUser) {
        authBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userCurrentPass = currentPass.getText().toString();

                if (TextUtils.isEmpty(userCurrentPass)) {
                    Toast.makeText(ChangePasswordActivity.this, "Please input your current password.", Toast.LENGTH_SHORT).show();
                    currentPass.setError("Input your current password.");
                    currentPass.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);

                    AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), userCurrentPass);

                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                currentPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                currentPass.setEnabled(false);
                                newPassEditText.setEnabled(true);
                                conPassEditText.setEnabled(true);

                                authBtn.setEnabled(false);
                                authBtn.setBackgroundResource(R.drawable.round_button_second);
                                confirmBtn.setEnabled(true);
                                textView13.setVisibility(View.VISIBLE);
                                Toast.makeText(ChangePasswordActivity.this,
                                        "Password has been verified. You can change new password now.",
                                        Toast.LENGTH_LONG).show();

                                confirmBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        ChangePassword(firebaseUser);
                                    }
                                });
                            } else {
                                try {
                                    throw task.getException();
                                } catch (Exception e){
                                    Toast.makeText(ChangePasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    currentPass.setError(e.getMessage());
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });
    }

    private void ChangePassword(FirebaseUser firebaseUser) {
        String newPass = newPassEditText.getText().toString();
        String conPass = conPassEditText.getText().toString();

        if (TextUtils.isEmpty(newPass)){
            Toast.makeText(this, "Please input new password", Toast.LENGTH_LONG).show();
            newPassEditText.setError("Input new password");
            newPassEditText.requestFocus();
        } else if (TextUtils.isEmpty(conPass)){
            Toast.makeText(this, "Please input confirm password", Toast.LENGTH_LONG).show();
            conPassEditText.setError("Input confirm password");
            conPassEditText.requestFocus();
        } else if (!isValidPassword(newPass)){
            Toast.makeText(this, "Password is weak.", Toast.LENGTH_LONG).show();
            newPassEditText.setError("Password is weak");
            newPassEditText.requestFocus();
        } else if (!newPass.matches(conPass)){
            Toast.makeText(this, "Password not match.", Toast.LENGTH_LONG).show();
            newPassEditText.setError("Password not match");
            newPassEditText.requestFocus();
            conPassEditText.setError("Password not match");
            conPassEditText.requestFocus();
        } else if (userCurrentPass.matches(newPass)) {
            Toast.makeText(this, "New password can't be the same as old password.", Toast.LENGTH_LONG).show();
            newPassEditText.setError("New password can't be the same as old password");
            newPassEditText.requestFocus();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            firebaseUser.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(ChangePasswordActivity.this, "Password has been changed.", Toast.LENGTH_LONG).show();
                        ChangePasswordActivity.this.finish();
                    } else {
                        try {
                            throw task.getException();
                        } catch (Exception e){
                            Toast.makeText(ChangePasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    public boolean isValidPassword(String password) {

        // Regex to check valid password.
        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=.])"
                + "(?=\\S+$).{8,20}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the password is empty
        // return false
        if (password == null) {
            return false;
        }
        // Pattern class contains matcher() method
        // to find matching between given password
        // and regular expression.
        Matcher m = p.matcher(password);
        // Return if the password
        // matched the ReGex
        return m.matches();
    }
}