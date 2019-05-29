package xyz.drean.ayabacafarmclient.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;
import xyz.drean.ayabacafarmclient.R;
import xyz.drean.ayabacafarmclient.abstraction.General;
import xyz.drean.ayabacafarmclient.pojo.Order;

public class AdapterOrder extends FirestoreRecyclerAdapter<Order, AdapterOrder.OrderHolder> {

    private Activity activity;

    public AdapterOrder(@NonNull FirestoreRecyclerOptions<Order> options, Activity activity) {
        super(options);
        this.activity = activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull OrderHolder holder, int position, @NonNull Order model) {
        holder.name.setText(model.getNameProduct());
        holder.product.setText(String.format("%s - S/.%s", model.getQuantity(), model.getTotal()));
        holder.adders.setText(model.getDate());

        holder.itemView.setTag(model.getUid());

        General general = new General();
        general.loadImage(model.getUrlImg(), holder.img, activity);
    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_order, viewGroup, false);
        return new OrderHolder(view);
    }

    public void removeItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
        Toast.makeText(activity, activity.getResources().getString(R.string.order_delete), Toast.LENGTH_SHORT).show();
    }

    class OrderHolder extends RecyclerView.ViewHolder {
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
}
