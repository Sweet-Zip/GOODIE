package com.example.goodie.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.goodie.function.CustomEditText;
import com.example.goodie.R;
import com.example.goodie.model.ReadWriteUserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthActionCodeException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private TextView loginText;
    private Button registerBtn;
    private EditText emailRegisterEditText, usernameEditText, passRegisterEditText, confirmPassEditText;
    private CheckBox termCheckBox;
    private static final String TAG = "RegisterActivity";
    private ProgressBar progressBar;
    private String timeStamp;

    int img;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        registerBtn = findViewById(R.id.registerBtn);
        loginText = findViewById(R.id.loginText);
        emailRegisterEditText = findViewById(R.id.emailRegisterEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        passRegisterEditText = findViewById(R.id.passRegisterEditText);
        confirmPassEditText = findViewById(R.id.confirmPassEditText);
        termCheckBox = findViewById(R.id.termCheckBox);
        progressBar = findViewById(R.id.progressBarReg);
        timeStamp = new SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime());
        System.out.println(timeStamp + "///////////////////////");

        new CustomEditText(passRegisterEditText);

        new CustomEditText(confirmPassEditText);

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckInput();
            }
        });
    }

    public void DrawableClicker(EditText editText) {

    }


    private void CheckInput() {

        if (usernameEditText.getText().toString().isEmpty() ||
                emailRegisterEditText.getText().toString().isEmpty() ||
                passRegisterEditText.getText().toString().isEmpty() ||
                confirmPassEditText.getText().toString().isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Please input every request", Toast.LENGTH_SHORT).show();
            if (usernameEditText.getText().toString().isEmpty()) {
                usernameEditText.setError("Please Input Username");
                usernameEditText.requestFocus();
            } else if (emailRegisterEditText.getText().toString().isEmpty()) {
                emailRegisterEditText.setError("Please Input email");
                emailRegisterEditText.requestFocus();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(emailRegisterEditText.getText().toString()).matches()) {
                emailRegisterEditText.setError("Email is invalid");
                emailRegisterEditText.requestFocus();
            } else if (passRegisterEditText.getText().toString().isEmpty()) {
                passRegisterEditText.setError("Please Input valid password");
                passRegisterEditText.requestFocus();
            } else if (confirmPassEditText.getText().toString().isEmpty()) {
                confirmPassEditText.setError("Please Input valid password");
                confirmPassEditText.requestFocus();
            }
        } else if (!isValidPassword(passRegisterEditText.getText().toString())) {
            Toast.makeText(RegisterActivity.this, "Invalid password", Toast.LENGTH_LONG).show();
            passRegisterEditText.setError("Password is weak");
            passRegisterEditText.requestFocus();
        } else if (!CheckPass()) {
            Toast.makeText(RegisterActivity.this, "The password is not match", Toast.LENGTH_SHORT).show();
            passRegisterEditText.setError("Password is not match");
            passRegisterEditText.requestFocus();
            confirmPassEditText.setError("Password is not match");
            confirmPassEditText.requestFocus();
        } else if (!CheckTerm()) {
            Toast.makeText(RegisterActivity.this, "Make sure you agree with our term and condition", Toast.LENGTH_SHORT).show();
            termCheckBox.setError("Make sure you agree with our term and condition");
            termCheckBox.requestFocus();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            RegisterUser(usernameEditText.getText().toString(),
                    emailRegisterEditText.getText().toString(),
                    passRegisterEditText.getText().toString(), "customer",
                    timeStamp);
        }
    }

    private boolean CheckPass() {
        if (!passRegisterEditText.getText().toString().equals(confirmPassEditText.getText().toString())) {
            return false;
        } else {
            return true;
        }
    }

    private boolean CheckTerm() {
        if (!termCheckBox.isChecked()) {
            return false;
        } else {
            return true;
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

    private void RegisterUser(String username, String email, String password, String type, String timeStamp) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Register Successful", Toast.LENGTH_LONG).show();
                            FirebaseUser firebaseUser = auth.getCurrentUser();

                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                            firebaseUser.updateProfile(profileChangeRequest);

                            ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(email, password, type, timeStamp);

                            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered users");
                            referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //send verification email
                                        firebaseUser.sendEmailVerification();
                                        //Open Login page
                                        Toast.makeText(RegisterActivity.this, "Account create successfully.", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Something went wrong! Please try again later.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                emailRegisterEditText.setError("Email is invalid or already used. Please re-enter invalid email");
                                emailRegisterEditText.requestFocus();
                            } catch (FirebaseAuthUserCollisionException e) {
                                usernameEditText.setError("Username already in use.");
                                usernameEditText.requestFocus();
                            } catch (Exception e){
                                Log.e(TAG, e.getMessage());
                                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

}