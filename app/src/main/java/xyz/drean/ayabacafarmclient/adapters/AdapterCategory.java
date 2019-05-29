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
import java.util.ArrayList;

import xyz.drean.ayabacafarmclient.DetailProduct;
import xyz.drean.ayabacafarmclient.R;
import xyz.drean.ayabacafarmclient.abstraction.General;
import xyz.drean.ayabacafarmclient.pojo.Product;

public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.CategoryViewHolder> {

    private ArrayList<Product> products;
    private Activity activity;

    public AdapterCategory(ArrayList<Product> products, Activity activity) {
        this.products = products;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryViewHolder holder, int position) {
        final Product item = products.get(position);
        holder.name.setText(item.getName());
        holder.price.setText(String.valueOf(item.getPrice()));

        General general = new General();
        general.loadImage(item.getUrlImg(), holder.image, activity);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goDetail(item, activity, v);
            }
        });

    }

    private void goDetail(Product model, Activity activity, View v) {
        Intent i = new Intent(activity, DetailProduct.class);
        i.putExtra("uid", model.getUid());
        i.putExtra("name", model.getName());
        i.putExtra("description", model.getDescription());
        i.putExtra("category", model.getCategory());
        i.putExtra("price", model.getPrice());
        i.putExtra("urlImg", model.getUrlImg());
        activity.startActivity(i, ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity, v, activity.getString(R.string.trancicionFoto)).toBundle());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder{

        private ImageView image;
        private TextView price;
        private TextView name;

        CategoryViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nombre_prodcuto);
            price = itemView.findViewById(R.id.precio_producto);
            image = itemView.findViewById(R.id.img_product);
        }
    }
}
