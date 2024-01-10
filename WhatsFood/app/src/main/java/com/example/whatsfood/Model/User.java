package com.example.whatsfood.Model;

import android.media.Image;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.whatsfood.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User implements Serializable {
    public String username;
    public String avatarUrl;
    public  User() { super();  }
    public User(String username, String avatarUrl ) {
        this.username = username;
        this.avatarUrl = avatarUrl;
    }

    public static String UploadImage(Uri uri, String folder) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String filename = uri.getLastPathSegment();
        String full_path = folder + "/" + UUID.randomUUID(); // + filename.substring(filename.lastIndexOf("."));
        StorageReference imgRef = storageRef.child(full_path);
        UploadTask uploadTask = imgRef.putFile(uri);
        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });
        return full_path;
    }
}