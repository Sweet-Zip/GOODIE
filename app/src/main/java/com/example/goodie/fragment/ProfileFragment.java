package com.example.goodie.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goodie.R;
import com.example.goodie.activity.FirstActivity;
import com.example.goodie.activity.MainActivity;
import com.example.goodie.model.ReadWriteUserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView emailTextView, passwordTextView, createDateTextView, logoutTextView, usernameTextView, idTextView;
    private String email, username, createDate;
    private FirebaseAuth authProfile;
    private Button button;
    private FirebaseUser firebaseUser;
    private ProgressBar progressBar;


    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        usernameTextView = rootView.findViewById(R.id.username);
        emailTextView = rootView.findViewById(R.id.emailTextViewEdit);
        passwordTextView = rootView.findViewById(R.id.passTextViewInfo);
        createDateTextView = rootView.findViewById(R.id.createDateEdit);
        logoutTextView = rootView.findViewById(R.id.logoutTextView);
        button = rootView.findViewById(R.id.signoutBtn);
        progressBar = rootView.findViewById(R.id.progressBarProfile);
        idTextView = rootView.findViewById(R.id.userID);
        authProfile = FirebaseAuth.getInstance();
        logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                authProfile.signOut();

                System.out.println("Log out");
                Toast.makeText(getActivity(), "Logout",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), FirstActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        firebaseUser = authProfile.getCurrentUser();
        if (firebaseUser == null) {
            Toast.makeText(getActivity().getApplicationContext(), "Something went wrong! User's details are not available at the moment", Toast.LENGTH_LONG).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            ShowUserProfile(firebaseUser);
        }
        return rootView;
    }

    private void ShowUserProfile(FirebaseUser firebaseUser) {
        String userId = firebaseUser.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered users");
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readUserDetails != null){
                    username = firebaseUser.getDisplayName();
                    email = firebaseUser.getEmail();
                    createDate = readUserDetails.date;
                    usernameTextView.setText(username);
                    emailTextView.setText(email);
                    createDateTextView.setText(createDate);
//                    idTextView.setText(firebaseUser.getUid());
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity().getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}