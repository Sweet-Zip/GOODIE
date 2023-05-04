package com.example.goodie.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.goodie.R;
import com.example.goodie.function.GridAdapter;
import com.example.goodie.model.Product;
import com.example.goodie.retrofit.RetrofitService;
import com.example.goodie.retrofit.ServerApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        loadProduct(rootView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int productId = (int) view.getTag();
                Toast.makeText(getActivity().getApplicationContext(), String.valueOf(productId), Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }

    private void loadProduct(View view) {

        gridView = view.findViewById(R.id.homeGridView);
        gridView.setAdapter(new GridAdapter(getActivity(), new ArrayList<>()));
        RetrofitService retrofitService = new RetrofitService();
        ServerApi productApi = retrofitService.getRetrofit().create(ServerApi.class);
        productApi.getAllData().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                List<Product> productList = response.body();
                GridAdapter gridAdapter = new GridAdapter(getActivity(), productList);
                gridView.setAdapter(gridAdapter);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "Failed to load Employee", Toast.LENGTH_SHORT).show();
            }
        });
    }



}