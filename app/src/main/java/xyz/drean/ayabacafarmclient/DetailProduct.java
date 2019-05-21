package xyz.drean.ayabacafarmclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.chip.Chip;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class DetailProduct extends AppCompatActivity {

    private String name;
    private String description;
    private String category;
    private double price;
    private String urlImg;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_poduct);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();

        getSupportActionBar().setTitle(name);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailProduct.this, AddProduct.class);
                i.putExtra("uid", uid);
                i.putExtra("name", name);
                i.putExtra("description", description);
                i.putExtra("category", category);
                i.putExtra("price", price);
                i.putExtra("urlImg", urlImg);
                startActivity(i);
            }
        });*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
