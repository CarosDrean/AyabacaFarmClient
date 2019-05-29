package xyz.drean.ayabacafarmclient.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

import xyz.drean.ayabacafarmclient.R;
import xyz.drean.ayabacafarmclient.adapters.AdapterCategory;
import xyz.drean.ayabacafarmclient.pojo.Product;

/**
 * A simple {@link Fragment} subclass.
 */
public class Categories extends Fragment {

    private FirebaseFirestore db;
    GridLayoutManager gridLayoutManager;
    RecyclerView categoryList;
    ArrayList<Product> products;
    AdapterCategory adapter;

    public Categories() {
        // Required empty public constructor
    }

    public static Categories newInstance(int indicesSection) {
        Categories fragment = new Categories();
        Bundle args = new Bundle();
        args.putInt("key", indicesSection);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_categorys, container, false);
        categoryList = v.findViewById(R.id.reciclador);
        products = new ArrayList<>();
        init();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
        initAdapter();
    }

    private void initAdapter() {
        adapter = new AdapterCategory(products, getActivity());
        categoryList.setAdapter(adapter);
    }

    private void init(){
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        categoryList.setLayoutManager(gridLayoutManager);
        db = FirebaseFirestore.getInstance();
    }

    private void loadData() {
        assert getArguments() != null;
        int indicesSection = getArguments().getInt("key");

        switch (indicesSection) {
            case 0:
                getData("Pastillas");
                init();
                break;
            case 1:
                getData("Jarabes");
                init();
                break;
            case 2:
                getData("Inyectables");
                init();
                break;
        }
    }

    private void getData(String category) {
        products.clear();
        db.collection("products")
                .whereEqualTo("category", category)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        assert value != null;
                        for(QueryDocumentSnapshot doc: value) {
                            Product p = doc.toObject(Product.class);
                            p.setUid(doc.getId());
                            products.add(p);
                            initAdapter();
                        }
                    }
                });
    }
}
