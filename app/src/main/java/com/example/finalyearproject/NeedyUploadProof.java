package com.example.finalyearproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class NeedyUploadProof extends AppCompatActivity {

    private Button upload, choose, skip;
    ArrayList<Uri> filePath = new ArrayList<Uri>();
    TextView Count;
    Uri imageuri;

    //FirebaseStorage storage;
    //StorageReference storageReference;

    private final int PICK_IMAGE_REQUEST = 22;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_needy_upload_proof);

        choose = findViewById(R.id.choose);
        upload = findViewById(R.id.upload);
        skip = findViewById(R.id.skip);
        Count = findViewById(R.id.count);
        progressDialog = new ProgressDialog(NeedyUploadProof.this);

        //storage = FirebaseStorage.getInstance();
        //storageReference = storage.getReference();

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //uploadFile();
                progressDialog.setTitle("Uploading...");
                progressDialog.show();

                StorageReference Image = FirebaseStorage.getInstance().getReference().child("Image");
                for(int uploadcount = 0; uploadcount < filePath.size(); uploadcount++){
                    Uri Singleimage = filePath.get(uploadcount);
                    StorageReference Imagename = Image.child("Image"+Singleimage.getLastPathSegment());

                    Imagename.putFile(Singleimage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = String.valueOf(uri);
                                    Storelink(url);
                                }
                            });
                        }
                    });
                }


            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(com.example.finalyearproject.NeedyUploadProof.this,
                        NeedyWall.class);
                startActivity(intent);
            }
        });
    }

    private void Storelink(String url) {
        DatabaseReference Dbref = FirebaseDatabase.getInstance().getReference().child("Proofs");
        HashMap<String, String> map = new HashMap<>();

        Dbref.push().setValue(map);
        progressDialog.dismiss();
        Count.setText("Proofs upload Successfully");
        upload.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data.getClipData() != null) {
            int count = data.getClipData().getItemCount();
            int currentcount = 0;

            while(currentcount < count){
                imageuri = data.getClipData().getItemAt(currentcount).getUri();
                filePath.add(imageuri);
                currentcount = currentcount + 1;
            }
            Count.setVisibility(View.VISIBLE);
            Count.setText(currentcount + " Images are selected.");
        } else {
            Toast.makeText(this, "Please select images", Toast.LENGTH_SHORT).show();
        }
    }

    /*private void uploadFile() {
        if (filePath != null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference imgref = storageReference.child("image/" + UUID.randomUUID().toString());

            imgref.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(com.example.fyp.UploadProof.this, "File Uploaded!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(com.example.fyp.UploadProof.this, NeedyWall.class));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(com.example.fyp.UploadProof.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded" + (int) progress + "%");
                }
            });
        }
    }*/
}