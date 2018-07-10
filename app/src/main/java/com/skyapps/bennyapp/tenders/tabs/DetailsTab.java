package com.skyapps.bennyapp.tenders.tabs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.skyapps.bennyapp.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class DetailsTab extends Fragment {

    private EditText editMqt, editName, editAddress, editContact, editPhone, editEmail, editCredit, editMaam, editDhifot, editHovala;
    private ImageButton uploadImage;
    private ImageView image;
    private Button btn;
    private String name;
    private ProgressDialog mProgressDialog;
///////////////////// PDF ////////////////
  /*  private ImageButton uploadPdfBtn ;
    private ImageButton selectPdfBtn ;
    private TextView filePdfName ;
    Uri pdfUri ;*/
/////////////////////////////////////////
    private Uri mImageUri;
    DatabaseReference dRef;
    String url;
    static EditText editComments, editAddressForSend;


   // final int PICKFILE_REQUEST_CODE = 99 ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details_tab, container, false);
        editMqt = view.findViewById(R.id.editMqt);
        editName = view.findViewById(R.id.editName);
        editAddress = view.findViewById(R.id.editAddress);
        editContact = view.findViewById(R.id.editContact);
        editPhone = view.findViewById(R.id.editPhone);
        editEmail = view.findViewById(R.id.editEmail);
        editCredit = view.findViewById(R.id.editCredit);
        editMaam = view.findViewById(R.id.editMaam);
        editDhifot = view.findViewById(R.id.editDhifot);
        editHovala = view.findViewById(R.id.editHovala);
        editComments = view.findViewById(R.id.editComments);
        editAddressForSend = view.findViewById(R.id.editAddressForSend);
        image = view.findViewById(R.id.image);

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("אנא המתן...");
        mProgressDialog.show();
        //name = getActivity().getIntent().getStringExtra("name");

        /////////////////////    PDF    ///////////////////////////////

       /* selectPdfBtn = view.findViewById(R.id.selectPdf);
        uploadPdfBtn = view.findViewById(R.id.pdfBtn);
        filePdfName = view.findViewById(R.id.pdfFileName);

        selectPdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*if(ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)== );*//*
                Intent pdfIntent = new Intent(Intent.ACTION_GET_CONTENT);
                pdfIntent.setType("pdf/*");
                startActivityForResult(pdfIntent, 17);

            }
        });
        uploadPdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pdfUri != null){
                    uploadPdf(pdfUri);
                }
                else{
                    Toast.makeText(getContext(),"Pleas choose a File",Toast.LENGTH_SHORT).show();
                }
            }
        });
*/


        ///////////////////////////////////////////////////////////////
        /// set the company name  ////
        name = getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company", "");

        Firebase.setAndroidContext(getContext());//FireBase , Upload Data from fireBase to EditTexts...
        final Firebase myFirebaseRef = new Firebase("https://tenders-83c71.firebaseio.com/");
        final Firebase ref = myFirebaseRef.child("Tenders/" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category","") + "/");
        final Firebase ref2 = myFirebaseRef.child("users");

        ((EditText)view.findViewById(R.id.category)).setText(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category",""));


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (postSnapshot.getKey().equals(name)) {

                        editMqt.setText(postSnapshot.child("mqt").getValue() + "");
                        editName.setText(postSnapshot.child("name").getValue() + "");
                        editAddress.setText(postSnapshot.child("address").getValue() + "");
                        editContact.setText(postSnapshot.child("contact").getValue() + "");
                        editPhone.setText(postSnapshot.child("phone").getValue() + "");
                        editEmail.setText(postSnapshot.child("mail").getValue() + "");
                        editCredit.setText(postSnapshot.child("credit").getValue() + "");
                        editMaam.setText(postSnapshot.child("maam").getValue() + "");
                        editDhifot.setText(postSnapshot.child("dhifot").getValue() + "");
                        editHovala.setText(postSnapshot.child("transit").getValue() + "");
                        editAddressForSend.setText(postSnapshot.child("addressforsend").getValue() + "");
                        editComments.setText(postSnapshot.child("comments").getValue() + "");


                       // Glide.with(getContext()).load(postSnapshot.child(name+"Image").getValue()).into(image);

                        break;
                    }
                }

                mProgressDialog.dismiss();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //Toast.makeText(getContext(), postSnapshot.getKey() + "", Toast.LENGTH_SHORT).show();
                    Log.e( "כמ:" , postSnapshot.getKey());
                    try {
                        if (postSnapshot.getKey().equals(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username", ""))) {
                            url = postSnapshot.child(name).child(name + "Image").getValue()+"";
                            //Glide.with(getContext()).load(postSnapshot.child(name + "Image").getValue()).into(image);
                            Glide.with(getContext()).load(postSnapshot.child(name).child(name + "Image").getValue()).into(image);
                        }
                    } catch (Exception e){
                        //Toast.makeText(getContext(), "ישנה שגיאה במערכת, נסה לשמור שנית בבקשה.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        uploadImage = view.findViewById(R.id.uploadImageFromGallery);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent openGallery = new Intent(Intent.ACTION_GET_CONTENT);
                openGallery.setType("image/*"); // Intent that opens the gallery
                startActivityForResult(openGallery, 2);


               /////// Intent that get permission to all files ///////
             //   Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
             //   intent.setType("*/*");
             //   startActivityForResult(intent, PICKFILE_REQUEST_CODE);

            }
        });

        view.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabsActivity.viewPager.setCurrentItem(1);
            }
        });
////////////////////// Dialog that show the loaded img ////////////////////
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog5);

                ImageView img = (ImageView) dialog.findViewById(R.id.imageview);
                Glide.with(getContext()).load(url).into(img);


                Button btn = (Button) dialog.findViewById(R.id.btn);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //getActivity().finish();
                        dialog.dismiss();

                    }
                });

                dialog.show();

            }
        });


        return view;
    }

    /*private void uploadPdf(final Uri pdfUri) {
        String pdfName = System.currentTimeMillis() + "";
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://tenders-83c71.appspot.com/");
        storageReference.child("PDF").child(pdfName).putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String url = taskSnapshot.getDownloadUrl().toString();
                        dRef = FirebaseDatabase.getInstance().getReference().child("users");

                        dRef.child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE)
                                .getString("username", ""))
                                .child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE)
                                        .getString("company", "") + "PDF").setValue(url.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });
    }*/


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ////////////////  PDF  ////////////////////

     /*   if( requestCode == 17 && resultCode == RESULT_OK && data!= null){
            pdfUri = data.getData();
        }
        else{
            Toast.makeText(getContext(),"Pleas select File",Toast.LENGTH_LONG).show();
        }
*/
        //////////////////////////////////////////
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle("אנא המתן...");
        mProgressDialog.setMessage("מעלה את התמונה שלך ושומר אותה...");
        mProgressDialog.show();

        Boolean b;
        try {
            data.getData();
            b = true;
        } catch (Exception e){
            b = false;
        }
        if (b) {
            Uri selectedImageUri = data.getData(); /// code of picture
            if (null != selectedImageUri) {
                String path = selectedImageUri.getPath();
                Log.e("image path", path + "");
                image.setImageURI(selectedImageUri);

                try {
                    Bitmap bitmapdata = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImageUri);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmapdata.compress(Bitmap.CompressFormat.PNG, 0, baos);

            /*Bitmap scaledBitmap = scaleDown(bitmap, 1000, true);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] Bitmapdata = baos.toByteArray();*/
            ////////////////////////////////////////////////////////////////////////////////////////////////////

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReferenceFromUrl("gs://tenders-83c71.appspot.com/");
                    final StorageReference ref = storageRef.child("Pictures/" +
                            getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("name", "") +
                            "/" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE)
                            .getString("company", "") + "/" + System.currentTimeMillis() + ".jpg");
                    UploadTask uploadTask = ref.putBytes(baos.toByteArray());
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.e("hmmmmm filed... ", exception.toString());
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            final Uri downloadUri = taskSnapshot.getDownloadUrl();

                            // Toast.makeText(getContext(), getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username", "") , Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();
                            dRef = FirebaseDatabase.getInstance().getReference().child("users");

                            /*dRef.child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username", ""))
                                    .child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company", "") + "Image")
                                    .setValue(downloadUri.toString());*/

                             dRef.child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username", ""))
                                    .child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company", ""))
                                    .child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company", "") + "Image")
                                    .setValue(downloadUri.toString());

                            Glide.with(getContext()).load(downloadUri.toString()).into(image);
                        }
                    });
                } catch (IOException e) {
                    Toast.makeText(getContext(), "ישנה שגיאה, נסה שנית", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            //Toast.makeText(getContext(), "ישנה שגיאה, נסה להעלות בשנית", Toast.LENGTH_SHORT).show();
            mProgressDialog.dismiss();
        }

        /*Bitmap bitmap = null;
        if (requestCode == 1) {
            getActivity().getContentResolver().notifyChange(mImageUri, null);
            ContentResolver cr = getActivity().getContentResolver();

            try {
                bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, mImageUri);
            } catch (Exception e) {
                Toast.makeText(getContext(), "Failed to load", Toast.LENGTH_SHORT).show();
                Log.d("", "Failed to load", e);
            }
        } else {
            Log.i("SonaSys", "resultCode: " + resultCode);
            switch (resultCode) {
                case 0:
                    Log.i("SonaSys", "User cancelled");
                    break;
                case -1:
                    try {
                        InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());
                        bitmap = BitmapFactory.decodeStream(inputStream);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;

            }
        }
        if (bitmap == null) {


        } else {
            Bitmap scaledBitmap = scaleDown(bitmap, 1000, true);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] Bitmapdata = baos.toByteArray();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://tenders-83c71.appspot.com/");
            final StorageReference ref = storageRef.child("Pictures/" + "Test" + System.currentTimeMillis() + ".jpg");
            UploadTask uploadTask = ref.putBytes(Bitmapdata);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("hmmmmm filed... ", exception.toString());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri downloadUri = taskSnapshot.getDownloadUrl();

                    dRef = FirebaseDatabase.getInstance().getReference().child("users");
                    dRef.child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("name", ""))
                            .child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company", "")).setValue(downloadUri.toString());

                    Glide.with(getContext()).load(downloadUri.toString()).into(image);
                }
            });
        }*/
    }

  //  @Override
  //  protected void onActivityResult(int requestCode, int resultCode, Intent data)
  //  {
  //      super.onActivityResult(requestCode, resultCode, data);
  //      if(requestCode==PICKFILE_REQUEST_CODE){
  //      }
  //  }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                maxImageSize / realImage.getWidth(),
                maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, 550,
                300, filter);
        realImage.recycle();
        return newBitmap;
    }

}
