package xyz.drean.ayabacafarmclient.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import xyz.drean.ayabacafarmclient.DetailProduct;
import xyz.drean.ayabacafarmclient.R;
import xyz.drean.ayabacafarmclient.pojo.Order;
import xyz.drean.ayabacafarmclient.pojo.Product;

/**
 * A simple {@link Fragment} subclass.
 */
public class Orders extends Fragment {

    private FirebaseFirestore db;
    private FirestoreRecyclerAdapter adapter;
    LinearLayoutManager llm;
    RecyclerView orderList;
    ArrayList<Order> orders;

    public Orders() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_orders, container, false);
        orderList = v.findViewById(R.id.recycler_order);
        orders = new ArrayList<>();
        init();
        getData();
        return v;
    }

    private void init(){
        llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        orderList.setLayoutManager(llm);
        db = FirebaseFirestore.getInstance();
    }

    private void getData() {
        Query query = db
                .collection("orders")
                .orderBy("uid")
                .limit(50);

        FirestoreRecyclerOptions<Order> options = new FirestoreRecyclerOptions.Builder<Order>()
                .setQuery(query, Order.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Order, OrderHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final OrderHolder holder, int position, @NonNull final Order model) {
                holder.name.setText(model.getNameClient());
                holder.product.setText(model.getNameProduct());
                holder.adders.setText(model.getAddress());

                StorageReference str = FirebaseStorage.getInstance().getReference()
                        .child("img")
                        .child(model.getUrlImg());

                try {
                    final File localFile = File.createTempFile("images", "jpg");
                    str.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Glide.with(getActivity()).load(localFile).into(holder.img);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

                /*holder.cel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "Llamar.", Toast.LENGTH_SHORT).show();
                    }
                });*/
            }

            @NonNull
            @Override
            public OrderHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_order, viewGroup, false);
                return new OrderHolder(view);
            }
        };

        adapter.notifyDataSetChanged();
        orderList.setAdapter(adapter);
    }

    public class OrderHolder extends RecyclerView.ViewHolder {
        CircleImageView img;
        TextView name;
        TextView product;
        TextView adders;
        // ImageView cel;

        public OrderHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_order);
            name = itemView.findViewById(R.id.name_order);
            product = itemView.findViewById(R.id.product_order);
            adders = itemView.findViewById(R.id.address_order);
            // cel = itemView.findViewById(R.id.call_order);
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
