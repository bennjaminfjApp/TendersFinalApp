package com.skyapps.bennyapp.tenders.tabs;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;
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
import com.skyapps.bennyapp.SelectPhotoDialog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;


public class DetailsTab extends Fragment implements SelectPhotoDialog.OnPhotoSelectedListener {

    private EditText editMqt, editName, editAddress, editContact, editPhone, editEmail, editCredit, editMaam, editDhifot, editHovala;
    private ImageButton uploadFromCam;
    private ImageButton uploadFromGallery;
    private Button uploadImage;

    Context context = this.getContext();

    private static final int CAMERA_REQUEST_CODE = 69 ;
    private static final int GALLERY_REQUEST_CODE = 70 ;
    private static final int PICK_FILE_REQUEST_CODE = 71 ;

    private ImageView image; /// now using this
    private Button btn;
    private String name;
    private ProgressDialog mProgressDialog;


    private Uri mImageUri;
    DatabaseReference dRef;
    String url;
    static EditText editComments, editAddressForSend;

    @Override
    public void getImagePath(Uri path) {

    }

    @Override
    public void getImageBitmap(Bitmap bitmap) {

    }


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



        /// set the company name  ////
        name = getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company", "");

        Firebase.setAndroidContext(getContext());//FireBase , Upload Data from fireBase to EditTexts...
        final Firebase myFirebaseRef = new Firebase("https://tenders-83c71.firebaseio.com/");
        final Firebase ref = myFirebaseRef.child("Tenders/" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE)
                .getString("category","") + "/");
        final Firebase ref2 = myFirebaseRef.child("users");

        ((EditText)view.findViewById(R.id.category)).setText(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE)
                .getString("category",""));


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (postSnapshot.getKey().equals(name)) {

                        editMqt.setText(postSnapshot.child("mqt").getValue() + "");
                        editName.setText(name + "");
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




                        break;
                    }
                }

                mProgressDialog.dismiss();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
/////////////////////////////// retrive image url from fireBase //////////////////////////////////////////
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.e( "כמ:" , postSnapshot.getKey());
                    try {
                        if (postSnapshot.getKey().equals(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE)
                                .getString("username", ""))) {
                            url = postSnapshot.child(name).child(name + "Image").getValue()+"";
                            Glide.with(getContext()).load(postSnapshot.child(name).child(name + "Image").getValue()).into(image);
                        }
                    } catch (Exception e){

                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


//////////////////////////// chose from gallery //////////////////////////////////////
        uploadFromGallery = view.findViewById(R.id.uploadImageFromGallery);
        uploadFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGallery = new Intent(Intent.ACTION_GET_CONTENT);
                openGallery.setType("image/*"); // Intent that opens the gallery
                startActivityForResult(openGallery, GALLERY_REQUEST_CODE);
            }
        });
//////////////////////// chose from camera ///////////////////////////////////////////
        uploadFromCam = view.findViewById(R.id.cam);
        uploadFromCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if ( PermissionChecker.checkSelfPermission( getContext(), Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED ) {

                    ActivityCompat.requestPermissions((Activity) getContext(), new String[] {  Manifest.permission.CAMERA  },CAMERA_REQUEST_CODE );
                }
                else {
                    invokeCamera();
                }
            }
        });

//////////////////////////////////////////////////////////////////////////////////////
        uploadImage = view.findViewById(R.id.uploadImage);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectPhotoDialog dialog = new SelectPhotoDialog() ;
                dialog.show(getFragmentManager(),getString(R.string.dialog_select_photo));
                dialog.setTargetFragment( DetailsTab.this ,36);

            }

        });
//////////////////////////////////////////////////////////////////////////////
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
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });


        return view;
    }

    private void invokeCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent,CAMERA_REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_REQUEST_CODE){
            if(grantResults[0] ==   PackageManager.PERMISSION_GRANTED){
                invokeCamera();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle("אנא המתן...");
        mProgressDialog.setMessage("מעלה את התמונה שלך ושומר אותה...");
        mProgressDialog.show();

        if( (requestCode == CAMERA_REQUEST_CODE &&  resultCode == RESULT_OK) 
                || (requestCode == GALLERY_REQUEST_CODE &&  resultCode == RESULT_OK)  ){

            Boolean b;
            try {
                data.getData();
                b = true;
            } catch (Exception e){
                b = false;
            }
            if(b) {
                Uri imageUri = data.getData();
                if (imageUri != null) {
                    String imagePath = imageUri.getPath();
                    image.setImageURI(imageUri);
                    try {
                        Bitmap bitmapdata = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmapdata.compress(Bitmap.CompressFormat.PNG, 0, baos);


                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReferenceFromUrl("gs://tenders-83c71.appspot.com/");
                        final StorageReference ref = storageRef.child("Pictures/" +
                                getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("name", "") + "/" + getContext()
                                .getSharedPreferences("BennyApp", Context.MODE_PRIVATE)
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
                                mProgressDialog.dismiss();
                                dRef = FirebaseDatabase.getInstance().getReference().child("users");

                                dRef.child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username", ""))
                                        .child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company", ""))
                                        .child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company", "") + "Image")
                                        .setValue(downloadUri.toString());

                                Glide.with(getContext()).load(downloadUri.toString()).into(image);
                            }
                        });
                    }catch (IOException e) {
                        Toast.makeText(getContext(), "ישנה שגיאה, נסה שנית", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else {
            mProgressDialog.dismiss();
        }

    }


////////////////////////////////////////////////////////////////////////////////////////////////////
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
