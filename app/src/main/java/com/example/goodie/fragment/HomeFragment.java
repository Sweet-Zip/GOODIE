package com.example.goodie.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.example.goodie.function.GridAdapter;
import com.example.goodie.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private static final String ARG_POSITION = "position";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    String[] title = {"Test", "Test1", "Test2", "Test3", "Test4", "Test5", "Test6", "Test7", "Test8"};
    int[] imageId = {R.drawable.logo, R.drawable.logo, R.drawable.logo,
            R.drawable.logo, R.drawable.logo, R.drawable.logo,
            R.drawable.logo, R.drawable.logo, R.drawable.logo};
    String[] price = {"11.00", "12.00", "13.00", "14.00", "15.00", "16.00", "17.00", "16.00", "17.00"};
    private int position;
    GridAdapter gridAdapter;
    GridView gridView;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(int position) {
        HomeFragment myFragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            if (title == null){
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT);

                FrameLayout frameLayout = new FrameLayout(getActivity());
                frameLayout.setLayoutParams(params);

                final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
                        .getDisplayMetrics());
                TextView backupTextView = new TextView(getActivity());
                params.setMargins(margin, margin, margin, margin);
                backupTextView.setLayoutParams(params);
                backupTextView.setLayoutParams(params);
                backupTextView.setGravity(Gravity.CENTER);
                backupTextView.setText("No Data");
                frameLayout.addView(backupTextView);
                return frameLayout;
            } else {
                View rootView = inflater.inflate(R.layout.fragment_home, container, false);
                gridView = (GridView) rootView.findViewById(R.id.homeGridView);
                gridAdapter = new GridAdapter(getActivity().getApplicationContext(), title, price, imageId);
                gridView.setAdapter(gridAdapter);
                return rootView;
            }

    }

}