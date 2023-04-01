package com.example.goodie.function;

import android.annotation.SuppressLint;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.example.goodie.R;

public class CustomEditText{
    private int check = 0;
    private EditText editText;

    @SuppressLint("ClickableViewAccessibility")
    public CustomEditText(EditText editText) {
        this.check = check;
        this.editText = editText;
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[2].getBounds().width())) {
                        if (check == 0) {
                            check = 1;
                            editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_lock, 0, R.drawable.baseline_password, 0);
                            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        } else {
                            check = 0;
                            editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_lock, 0, R.drawable.baseline_remove_red_eye_24, 0);
                            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }
}
