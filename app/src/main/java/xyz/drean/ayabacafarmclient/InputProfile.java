package xyz.drean.ayabacafarmclient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
                if(!name.getText().toString().equals("") || !address.getText().toString().equals("") || !cel.getText().toString().equals("")){
                    saveProfile();
                    Intent i = new Intent(InputProfile.this, MainActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(InputProfile.this, "¡Llene todos los campos!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Datos de usuario");

        ActionBar actionBar = getSupportActionBar();
        final Drawable menuIcon = getResources().getDrawable(R.drawable.ic_baseline_close_24px);
        menuIcon.setColorFilter(getResources().getColor(R.color.blanco), PorterDuff.Mode.SRC_ATOP);
        actionBar.setHomeAsUpIndicator(menuIcon);
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
        Toast.makeText(this, "¡Debe llenar los campos!", Toast.LENGTH_SHORT).show();
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

        saveIdProfile(uid);
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
