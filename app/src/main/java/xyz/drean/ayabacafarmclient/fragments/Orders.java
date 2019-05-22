package xyz.drean.ayabacafarmclient.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import xyz.drean.ayabacafarmclient.R;
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

    private String getIdProfile() {
        SharedPreferences prefs = getActivity().getSharedPreferences("DatosUser", Context.MODE_PRIVATE);
        return prefs.getString("uid", "");
    }

    private void getData() {
        Query query = db
                .collection("orders")
                .orderBy("uid")
                .whereEqualTo("uidClient", getIdProfile())
                .limit(50);

        FirestoreRecyclerOptions<Order> options = new FirestoreRecyclerOptions.Builder<Order>()
                .setQuery(query, new SnapshotParser<Order>() {
                    @NonNull
                    @Override
                    public Order parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        Order o = snapshot.toObject(Order.class);
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
                holder.product.setText(model.getQuantity() + " - S/." + model.getTotal());
                holder.adders.setText(model.getDate());

                holder.itemView.setTag(model.getUid());

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

        //adapter.notifyDataSetChanged();
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
        CircleImageView img;
        TextView name;
        TextView product;
        TextView adders;
        //ImageView delete;
        public RelativeLayout content;

        public OrderHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_order);
            name = itemView.findViewById(R.id.name_order);
            product = itemView.findViewById(R.id.product_order);
            adders = itemView.findViewById(R.id.address_order);
            //delete = itemView.findViewById(R.id.call_order);
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
