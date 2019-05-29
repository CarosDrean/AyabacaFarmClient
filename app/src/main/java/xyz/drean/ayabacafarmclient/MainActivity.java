package xyz.drean.ayabacafarmclient;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import xyz.drean.ayabacafarmclient.fragments.Home;
import xyz.drean.ayabacafarmclient.fragments.Orders;
import xyz.drean.ayabacafarmclient.fragments.Products;
import xyz.drean.ayabacafarmclient.fragments.Profiles;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);

        dataHome(navigationView.getHeaderView(0));

        onNavigationItemSelected(navigationView.getMenu().getItem(0));

        if(getUseApp() == 0 || getIdProfile().equals("")){
            simpleAlert(
                    getResources().getString(R.string.important),
                    getResources().getString(R.string.message_important),
                    getResources().getString(R.string.list)
            );
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        signInAnonymously();
    }

    private void signInAnonymously() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void simpleAlert(String title, String message, String positiveButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setCancelable(false)
                .setMessage(message);
        builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            simpleAlert(
                    getResources().getString(R.string.action_abaut),
                    getResources().getString(R.string.text_abaut),
                    getResources().getString(R.string.list)
            );
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment  = null;
        boolean fragmentManager = false;

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fragmentManager = true;
            fragment = new Home();
        } else if (id == R.id.nav_products) {
            fragmentManager = true;
            fragment = new Products();
        } else if (id == R.id.nav_orders) {
            fragmentManager = true;
            fragment = new Orders();
        } else if (id == R.id.nav_profile) {
            fragmentManager = true;
            fragment = new Profiles();
        }

        if(fragmentManager){
            ActionBar actionBar = getSupportActionBar();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();
            item.setChecked(true);
            assert actionBar != null;
            actionBar.setTitle(item.getTitle());
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private String getIdProfile() {
        SharedPreferences prefs = getSharedPreferences("DatosUser",Context.MODE_PRIVATE);
        return prefs.getString("uid", "");
    }

    private void dataHome(View v){
        ImageView fondo = v.findViewById(R.id.fondo_cabecera);
        Glide.with(this).load(getDrawable(R.drawable.logo)).into(fondo);
    }

    private int getUseApp() {
        SharedPreferences sp = getSharedPreferences("MYAPP", 0);
        int result, currentVersionCode = BuildConfig.VERSION_CODE;
        int lastVersionCode = sp.getInt("FIRSTTIMERUN", -1);

        if (lastVersionCode == -1) {
            result = 0;
        } else{
            result = (lastVersionCode == currentVersionCode) ? 1 : 2;
        }

        sp.edit().putInt("FIRSTTIMERUN", currentVersionCode).apply();
        return result;
    }
}
