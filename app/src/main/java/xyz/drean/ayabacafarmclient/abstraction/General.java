package xyz.drean.ayabacafarmclient.abstraction;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import xyz.drean.ayabacafarmclient.R;

public class General {

    public General() {
    }

    public void loadImage(String urlImg, final ImageView imageView, final Activity activity) {
        StorageReference str = FirebaseStorage.getInstance().getReference()
                .child("img")
                .child(urlImg);

        try {
            final File localFile = File.createTempFile("images", "jpg");
            str.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Glide.with(activity)
                            .load(localFile)
                            .centerCrop()
                            .placeholder(R.drawable.holder)
                            .into(imageView);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
