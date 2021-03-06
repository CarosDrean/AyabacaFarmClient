package xyz.drean.ayabacafarmclient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import xyz.drean.ayabacafarmclient.pojo.Profile;

public class InputProfile extends AppCompatActivity {

    private EditText name;
    private EditText address;
    private EditText cel;

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickSave();
            }
        });

        ActionBar actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setTitle("Datos de usuario");

        iconActionBar(actionBar);

        if(getIntent().getStringExtra("name") != null) {
            edit(actionBar);
        }
    }

    private void iconActionBar(ActionBar actionBar) {
        final Drawable menuIcon = getResources().getDrawable(R.drawable.ic_baseline_close_24px, null);
        menuIcon.setColorFilter(getResources().getColor(R.color.blanco), PorterDuff.Mode.SRC_ATOP);
        actionBar.setHomeAsUpIndicator(menuIcon);
    }

    private void clickSave() {
        if(!name.getText().toString().equals("") || !address.getText().toString().equals("") || !cel.getText().toString().equals("")){
            saveProfile();
            Intent i = new Intent(InputProfile.this, MainActivity.class);
            startActivity(i);
        } else {
            Toast.makeText(InputProfile.this, getResources().getString(R.string.fill_fields), Toast.LENGTH_SHORT).show();
        }
    }

    private void edit(ActionBar actionBar) {
        actionBar.setTitle("Editar Usuario");
        uid = getIntent().getStringExtra("uid");
        name.setText(getIntent().getStringExtra("name"));
        address.setText(getIntent().getStringExtra("address"));
        cel.setText(getIntent().getStringExtra("cel"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(getIntent().getStringExtra("name") != null) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, getResources().getString(R.string.fill_fields), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveProfile() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = String.valueOf(System.currentTimeMillis());

        Profile p = new Profile(
                uid,
                name.getText().toString(),
                address.getText().toString(),
                cel.getText().toString()
        );

        if(getIntent().getStringExtra("name") != null) {
            uid = this.uid;
            p.setUid(uid);
            Toast.makeText(this, getResources().getString(R.string.data_updated), Toast.LENGTH_SHORT).show();
        } else {
            saveIdProfile(uid);
        }

        db.collection("profiles").document(uid).set(p);
    }

    private void init() {
        name = findViewById(R.id.name_profile);
        address = findViewById(R.id.address_profile);
        cel = findViewById(R.id.cel_profile);
    }

    private void saveIdProfile(String uid) {
        SharedPreferences prefs = getSharedPreferences("DatosUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("uid", uid);
        editor.apply();
        editor.commit();
    }

}
