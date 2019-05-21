package xyz.drean.ayabacafarmclient.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.chip.Chip;
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

import xyz.drean.ayabacafarmclient.R;
import xyz.drean.ayabacafarmclient.pojo.Profile;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profiles extends Fragment {

    private TextView name;
    private Chip address;
    private Chip cel;


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
        return v;
    }

    private void init(View v) {
        name = v.findViewById(R.id.name_p);
        address = v.findViewById(R.id.address_p);
        cel = v.findViewById(R.id.cel_p);
    }

    private void getProfile(String uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("profiles").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    setProfile(document.getString("name"), document.getString("address"), document.getString("cel"));
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

}
