package xyz.drean.ayabacafarmclient.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import xyz.drean.ayabacafarmclient.DetailProduct;
import xyz.drean.ayabacafarmclient.R;
import xyz.drean.ayabacafarmclient.pojo.Product;

public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.CategoriaViewHolder> {

    private ArrayList<Product> productos;
    private Activity activity;

    public AdapterCategory(ArrayList<Product> productos, Activity activity) {
        this.productos = productos;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new AdapterCategory.CategoriaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoriaViewHolder holder, int position) {
        final Product item = productos.get(position);
        holder.nombre.setText(item.getName());
        holder.precio.setText("" + item.getPrice());

        StorageReference str = FirebaseStorage.getInstance().getReference()
                .child("img")
                .child(item.getUrlImg());

        try {
            final File localFile = File.createTempFile("images", "jpg");
            str.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Glide.with(activity).load(localFile).into(holder.imagen);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        holder.imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, DetailProduct.class);
                i.putExtra("uid", item.getUid());
                i.putExtra("name", item.getName());
                i.putExtra("description", item.getDescription());
                i.putExtra("category", item.getCategory());
                i.putExtra("price", item.getPrice());
                i.putExtra("urlImg", item.getUrlImg());
                activity.startActivity(i, ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity, v, activity.getString(R.string.trancicionFoto)).toBundle());
            }
        });

    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public static class CategoriaViewHolder extends RecyclerView.ViewHolder{

        private ImageView imagen;
        private TextView precio;
        private TextView nombre;

        public CategoriaViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre_prodcuto);
            precio = itemView.findViewById(R.id.precio_producto);
            imagen = itemView.findViewById(R.id.img_product);
        }
    }
}
