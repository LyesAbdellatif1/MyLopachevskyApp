package com.example.mylopachevskyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import de.hdodenhof.circleimageview.CircleImageView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class setupAccount extends AppCompatActivity {

    private CircleImageView setUpImage;
    private Uri mainImageURI =null;

    private EditText setupName;
    private Button setupButton;

    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_account);

        Toolbar setupToolbar = findViewById(R.id.setup_Toolbar);
        setSupportActionBar(setupToolbar);
        getSupportActionBar().setTitle(" Account Setup ");

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();


        setUpImage = findViewById(R.id.profile_image);
        setupName = findViewById(R.id.setup_name);
        setupButton =findViewById(R.id.setup_btn);

        setupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user_name = setupName.getText().toString();

                if(!TextUtils.isEmpty(user_name)&& mainImageURI != null){

                    String user_id = firebaseAuth.getCurrentUser().getUid();
                    StorageReference image_path = storageReference.child("profile_images").child(user_id+".jpj");

                    image_path.putFile(mainImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){

                                Uri download_uri = task.getResult().getUploadSessionUri();
                            }else{
                                String error=task.getException().getMessage();
                                Toast.makeText(setupAccount.this,"Error:"+error,Toast.LENGTH_LONG).show();
                            }
                        }
                    });







                }

            }
        });





        setUpImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION. SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(setupAccount.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                        ActivityCompat.requestPermissions( setupAccount. this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1);

                        Toast.makeText ( setupAccount.this,"Permission Denied", Toast. LENGTH_LONG).show();

                    }else{
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1,1)
                                .start(setupAccount.this);

                    }
                }



            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mainImageURI = result.getUri();
                setUpImage.setImageURI(mainImageURI);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }







}