package xyz.drean.ayabacafarmclient;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.chip.Chip;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import xyz.drean.ayabacafarmclient.pojo.Order;
import xyz.drean.ayabacafarmclient.pojo.Profile;

public class DetailProduct extends AppCompatActivity {

    private String name;
    private String description;
    private String category;
    private double price;
    private String urlImg;
    private String uid;

    private TextView igv;
    private TextView precio;
    private TextView total;
    private TextView cantidad;
    private boolean habilitar = false;

    private String nameUser;
    private String addresUser;
    private String celUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_poduct);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        getProfile(getIdProfile());

        getSupportActionBar().setTitle(name);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_detail);
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
                if(habilitar) {
                    saveOrder();
                    finish();
                } else {
                    Toast.makeText(DetailProduct.this, "¡Asigne una cantidad mayor a 0!", Toast.LENGTH_SHORT).show();
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
                String.valueOf(System.currentTimeMillis()),
                getIdProfile(),
                addresUser,
                name,
                urlImg,
                nameUser,
                celUser,
                cantidad.getText().toString(),
                Double.parseDouble(precio.getText().toString()),
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
        return (String.format("%02d", dia)
                + "/" + String.format("%02d", mes + 1)
                + "/" + ano);
    }

    private void getProfile(String uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("profiles").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    nameUser = document.getString("name");
                    addresUser = document.getString("address");
                    celUser = document.getString("cel");
                }
            }
        });
    }

    private void dataOrder(View v){
        igv = v.findViewById(R.id.igv_order);
        precio = v.findViewById(R.id.price_unit_order);
        cantidad = v.findViewById(R.id.cantidad_order);
        total = v.findViewById(R.id.total_order);
        Button mas = v.findViewById(R.id.btn_mas);
        Button menos = v.findViewById(R.id.btn_menos);

        precio.setText("" + price);

        menos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cant = cantidad.getText().toString();
                if(Integer.parseInt(cant) > 1) {
                    cantidad.setText("" + (Integer.parseInt(cant) - 1));
                    calculate();
                }
            }
        });

        mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cant = cantidad.getText().toString();
                cantidad.setText("" + (Integer.parseInt(cant) + 1));
                calculate();
                habilitar = true;
            }
        });
    }

    public void calculate() {
        double precio = price;

        String cant = cantidad.getText().toString();
        int cantidad = Integer.parseInt(cant);
        double subtotal = precio * cantidad;
        double igv = subtotal * 0.18;
        double total = (precio * cantidad) + igv;

        this.igv.setText("" + igv);
        this.total.setText("" + total);
    }

    private void init() {
        TextView name_u = findViewById(R.id.name_detail);
        TextView description_u = findViewById(R.id.descrption_detail);
        Chip category_u = findViewById(R.id.category_detail);
        Chip price_u = findViewById(R.id.price_detail);
        ImageView img = findViewById(R.id.img_detail);

        name = getIntent().getStringExtra("name");
        description = getIntent().getStringExtra("description");
        category = getIntent().getStringExtra("category");
        price = getIntent().getDoubleExtra("price", 0.0);
        urlImg = getIntent().getStringExtra("urlImg");
        uid = getIntent().getStringExtra("uid");

        name_u.setText(name);
        description_u.setText(description);
        category_u.setText(category);
        price_u.setText("S/. " + price);

        loadImg(urlImg, img);
    }

    private void loadImg(String urlImg, final ImageView img) {
        StorageReference str = FirebaseStorage.getInstance().getReference()
                .child("img")
                .child(urlImg);

        try {
            final File localFile = File.createTempFile("images", "jpg");
            str.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Glide.with(DetailProduct.this).load(localFile).into(img);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
