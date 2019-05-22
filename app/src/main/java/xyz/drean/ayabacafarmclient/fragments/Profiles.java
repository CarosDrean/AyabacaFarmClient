package xyz.drean.ayabacafarmclient.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.chip.Chip;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import xyz.drean.ayabacafarmclient.InputProfile;
import xyz.drean.ayabacafarmclient.R;
import xyz.drean.ayabacafarmclient.pojo.Profile;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profiles extends Fragment {

    private TextView name;
    private Chip address;
    private Chip cel;

    private String uidUser;
    private String nameUser;
    private String addressUser;
    private String celUser;


    public Profiles() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        init(v);
        getProfile(getIdProfile());

        FloatingActionButton fab = v.findViewById(R.id.fab_profile);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit();
            }
        });
        return v;
    }

    private void init(View v) {
        name = v.findViewById(R.id.name_p);
        address = v.findViewById(R.id.address_p);
        cel = v.findViewById(R.id.cel_p);
    }

    private void getProfile(final String uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("profiles").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    uidUser = uid;
                    nameUser = document.getString("name");
                    addressUser = document.getString("address");
                    celUser = document.getString("cel");
                    setProfile(nameUser, addressUser, celUser);
                }
            }
        });
    }

    private void setProfile(String name, String address, String cel) {
        this.name.setText(name);
        this.address.setText(address);
        this.cel.setText(cel);
    }

    private String getIdProfile() {
        SharedPreferences prefs = getActivity().getSharedPreferences("DatosUser", Context.MODE_PRIVATE);
        return prefs.getString("uid", "");
    }

    private void edit() {
        Intent i = new Intent(getActivity(), InputProfile.class);
        i.putExtra("uid", uidUser);
        i.putExtra("name", nameUser);
        i.putExtra("address", addressUser);
        i.putExtra("cel", celUser);
        startActivity(i);
    }

}
