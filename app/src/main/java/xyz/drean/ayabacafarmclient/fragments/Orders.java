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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import xyz.drean.ayabacafarmclient.R;
import xyz.drean.ayabacafarmclient.abstraction.General;
import xyz.drean.ayabacafarmclient.pojo.Order;

/**
 * A simple {@link Fragment} subclass.
 */
public class Orders extends Fragment {

    private FirebaseFirestore db;
    private FirestoreRecyclerAdapter adapter;
    LinearLayoutManager llm;
    RecyclerView orderList;
    ArrayList<Order> orders;

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
        orders = new ArrayList<>();
        llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        orderList.setLayoutManager(llm);
        db = FirebaseFirestore.getInstance();
    }

    private String getIdProfile(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences("DatosUser", Context.MODE_PRIVATE);
        return prefs.getString("uid", "");
    }

    private void getData() {
        final Activity activity = getActivity();
        assert activity != null;
        Query query = db
                .collection("orders")
                .orderBy("uid")
                .whereEqualTo("uidClient", getIdProfile(activity))
                .limit(50);

        FirestoreRecyclerOptions<Order> options = new FirestoreRecyclerOptions.Builder<Order>()
                .setQuery(query, new SnapshotParser<Order>() {
                    @NonNull
                    @Override
                    public Order parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        Order o = snapshot.toObject(Order.class);
                        assert o != null;
                        o.setUid(snapshot.getId());

                        return o;
                    }
                })
                .setLifecycleOwner(this)
                .build();

        adapter = new FirestoreRecyclerAdapter<Order, OrderHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final OrderHolder holder, final int position, @NonNull final Order model) {
                holder.name.setText(model.getNameProduct());
                holder.product.setText(String.format("%s - S/.%s", model.getQuantity(), model.getTotal()));
                holder.adders.setText(model.getDate());

                holder.itemView.setTag(model.getUid());

                General general = new General();
                general.loadImage(model.getUrlImg(), holder.img, activity);

            }

            @Override
            public int getItemCount() {
                return super.getItemCount();
            }

            @NonNull
            @Override
            public OrderHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_order, viewGroup, false);
                return new OrderHolder(view);
            }
        };

        orderList.setAdapter(adapter);
    }

    private void deleteItem(String uid, final int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("orders").document(uid)
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(), "¡Pedido eliminado!", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
                adapter.notifyItemRemoved(position);
            }
        });
    }

    public class OrderHolder extends RecyclerView.ViewHolder {
        private CircleImageView img;
        private TextView name;
        private TextView product;
        private TextView adders;
        RelativeLayout content;

        OrderHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_order);
            name = itemView.findViewById(R.id.name_order);
            product = itemView.findViewById(R.id.product_order);
            adders = itemView.findViewById(R.id.address_order);
            content = itemView.findViewById(R.id.content_item_order);
        }
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
