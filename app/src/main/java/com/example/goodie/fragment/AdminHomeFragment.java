package com.example.goodie.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.goodie.R;
import com.example.goodie.activity.AdminDetailActivity;
import com.example.goodie.function.GridAdapter;
import com.example.goodie.model.Product;
import com.example.goodie.retrofit.ServerApi;
import com.example.goodie.retrofit.RetrofitService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminHomeFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private int position;
    private GridAdapter gridAdapter;
    private GridView gridView;

    public AdminHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminHomeFragment newInstance(String param1, String param2) {
        AdminHomeFragment fragment = new AdminHomeFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_admin_home, container, false);
        loadProduct(rootView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int productId = (int) view.getTag();
                Toast.makeText(getActivity().getApplicationContext(), String.valueOf(productId), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity().getApplicationContext(), AdminDetailActivity.class);
                intent.putExtra("id", productId);
                startActivity(intent);
            }
        });

        SearchView searchView = rootView.findViewById(R.id.productSearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                gridAdapter.filter(newText);
                return true;
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
                gridAdapter = new GridAdapter(getActivity(), productList);
                gridView.setAdapter(gridAdapter);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "Failed to load Employee", Toast.LENGTH_SHORT).show();
            }
        });
    }
}