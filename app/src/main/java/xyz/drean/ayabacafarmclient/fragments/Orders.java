package xyz.drean.ayabacafarmclient.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import xyz.drean.ayabacafarmclient.R;
import xyz.drean.ayabacafarmclient.adapters.AdapterOrder;
import xyz.drean.ayabacafarmclient.pojo.Order;

/**
 * A simple {@link Fragment} subclass.
 */
public class Orders extends Fragment {

    private FirebaseFirestore db;
    private CollectionReference collOrders;
    private AdapterOrder adapter;
    LinearLayoutManager llm;
    RecyclerView orderList;

    private final int CODE_PERMISSION_CALL = 0;
    int hasCallPermission;

    public Orders() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_orders, container, false);

        init(v);
        accessPermission(getActivity());
        getData();
        return v;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CODE_PERMISSION_CALL) {
            if (!(grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(getContext(), getResources().getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void accessPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hasCallPermission = activity.checkSelfPermission(Manifest.permission.CALL_PHONE);
            if(hasCallPermission != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{ Manifest.permission.CALL_PHONE }, CODE_PERMISSION_CALL);
            }
        }
    }

    private void init(View v){
        orderList = v.findViewById(R.id.recycler_order);

        llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        orderList.setLayoutManager(llm);

        db = FirebaseFirestore.getInstance();
        collOrders = db.collection("orders");
    }

    private String getIdProfile(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences("DatosUser", Context.MODE_PRIVATE);
        return prefs.getString("uid", "");
    }

    private void getData() {
        final Activity activity = getActivity();
        assert activity != null;
        Query query = collOrders
                .whereEqualTo("uidClient", getIdProfile(getActivity()));

        FirestoreRecyclerOptions<Order> options = new FirestoreRecyclerOptions.Builder<Order>()
                .setQuery(query, Order.class)
                .build();

        adapter = new AdapterOrder(options, getActivity());
        orderList.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                adapter.removeItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(orderList);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
