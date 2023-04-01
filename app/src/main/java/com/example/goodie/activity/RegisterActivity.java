package com.example.goodie.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.goodie.function.CustomEditText;
import com.example.goodie.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private TextView loginText;
    private Button registerBtn;
    private EditText emailRegisterEditText, usernameEditText, passRegisterEditText, confirmPassEditText;
    private CheckBox termCheckBox;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
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
        radioGroup = findViewById(R.id.radioGroup);

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
            //System.out.println("Please input every request");
            Toast.makeText(RegisterActivity.this, "Please input every request", Toast.LENGTH_SHORT).show();
        } else {
            //System.out.println(isValidPassword(passRegisterEditText.getText().toString()));
            if (!isValidPassword(passRegisterEditText.getText().toString())) {
                Toast.makeText(RegisterActivity.this,
                        "Invalid password",
                        Toast.LENGTH_LONG).show();
            } else {
                if (CheckPass()) {
                    if (CheckGender() == -1) {
                        Toast.makeText(RegisterActivity.this, "No answer has been selected", Toast.LENGTH_SHORT).show();
                    } else {
                        if (CheckTerm()) {
                            radioButton = findViewById(CheckGender());
                            Toast.makeText(RegisterActivity.this, radioButton.getText() + " Account has been create successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }
    }

    private boolean CheckPass() {
        if (!passRegisterEditText.getText().toString().equals(confirmPassEditText.getText().toString())) {
            Toast.makeText(RegisterActivity.this, "The password is not match", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean CheckTerm() {
        if (!termCheckBox.isChecked()) {
            Toast.makeText(RegisterActivity.this, "Make sure you agree with our term and condition", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private int CheckGender() {
        int selectGender = radioGroup.getCheckedRadioButtonId();
        return selectGender;
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