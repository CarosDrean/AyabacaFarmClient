package xyz.drean.ayabacafarmclient;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.chip.Chip;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import xyz.drean.ayabacafarmclient.abstraction.General;
import xyz.drean.ayabacafarmclient.pojo.Order;

import static java.lang.String.*;

public class DetailProduct extends AppCompatActivity {

    private String name;
    private double price;
    private String urlImg;

    private TextView igv;
    private TextView price_ui;
    private TextView total;
    private TextView quantity_ui;
    private boolean enable = false;

    private String nameUser;
    private String addressUser;
    private String celUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_poduct);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        init();
        getProfile(getIdProfile());

        actionBar.setTitle(name);

        FloatingActionButton fab = findViewById(R.id.fab_detail);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertOrder();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void alertOrder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.alert_order, null);
        dataOrder(v);
        builder.setView(v);
        builder.setPositiveButton("Pedir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(enable) {
                    saveOrder();
                    finish();
                } else {
                    Toast.makeText(DetailProduct.this, getResources().getString(R.string.asign_count), Toast.LENGTH_SHORT).show();
                }
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

    private String getIdProfile() {
        SharedPreferences prefs = getSharedPreferences("DatosUser",Context.MODE_PRIVATE);
        return prefs.getString("uid", "");
    }

    private void saveOrder() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("orders").add(createOrder());
        Toast.makeText(this, "¡Pedido registrado!", Toast.LENGTH_SHORT).show();
    }

    private Order createOrder() {
        return new Order(
                valueOf(System.currentTimeMillis()),
                getIdProfile(),
                addressUser,
                name,
                urlImg,
                nameUser,
                celUser,
                quantity_ui.getText().toString(),
                Double.parseDouble(price_ui.getText().toString()),
                Double.parseDouble(igv.getText().toString()),
                getDate(),
                Double.parseDouble(total.getText().toString())
        );
    }

    public String getDate() {
        GregorianCalendar calendar = new GregorianCalendar();
        return formatDate (calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.YEAR));
    }

    public String formatDate(int dia, int mes, int ano) {
        return String.format(Locale.getDefault(),"%d/%d/%d", dia, (mes + 1), ano);
    }

    private void getProfile(String uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("profiles").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    nameUser = document.getString("name");
                    addressUser = document.getString("address");
                    celUser = document.getString("cel");
                }
            }
        });
    }

    private void dataOrder(View v){
        igv = v.findViewById(R.id.igv_order);
        price_ui = v.findViewById(R.id.price_unit_order);
        quantity_ui = v.findViewById(R.id.cantidad_order);
        total = v.findViewById(R.id.total_order);
        Button mas = v.findViewById(R.id.btn_mas);
        Button menos = v.findViewById(R.id.btn_menos);

        price_ui.setText(String.valueOf(price));

        menos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cant = quantity_ui.getText().toString();
                if(Integer.parseInt(cant) > 1) {
                    quantity_ui.setText((Integer.parseInt(cant) - 1));
                    calculate();
                }
            }
        });

        mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cant = quantity_ui.getText().toString();
                quantity_ui.setText((Integer.parseInt(cant) + 1));
                calculate();
                enable = true;
            }
        });
    }

    public void calculate() {
        double precio = price;

        String cant = quantity_ui.getText().toString();
        int cantidad = Integer.parseInt(cant);
        double subtotal = precio * cantidad;
        double igv = subtotal * 0.18;
        double total = (precio * cantidad) + igv;

        this.igv.setText(String.valueOf(igv));
        this.total.setText(String.valueOf(total));
    }

    private void init() {
        TextView name_u = findViewById(R.id.name_detail);
        TextView description_u = findViewById(R.id.descrption_detail);
        Chip category_u = findViewById(R.id.category_detail);
        Chip price_u = findViewById(R.id.price_detail);
        ImageView img = findViewById(R.id.img_detail);

        name = getIntent().getStringExtra("name");
        String description = getIntent().getStringExtra("description");
        String category = getIntent().getStringExtra("category");
        price = getIntent().getDoubleExtra("price", 0.0);
        urlImg = getIntent().getStringExtra("urlImg");

        name_u.setText(name);
        description_u.setText(description);
        category_u.setText(category);
        price_u.setText(String.valueOf(price));

        General general = new General();
        general.loadImage(urlImg, img, DetailProduct.this);
    }
}
