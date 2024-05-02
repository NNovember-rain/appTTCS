package vn.mrlongg71.ps09103_assignment.view.manageruser;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import vn.mrlongg71.ps09103_assignment.R;
import vn.mrlongg71.ps09103_assignment.library.ActionBarLib;
import vn.mrlongg71.ps09103_assignment.library.Dialog;
import vn.mrlongg71.ps09103_assignment.model.objectclass.User;

public class InfoFragment extends Fragment implements View.OnClickListener {


    private TextView txtManagerUser, txtNameUser, txtEmailUser, txtChangePass, txtExits;
    private CircleImageView imageUserInfo;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private DatabaseReference databaseReference;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private User user = new User();
    private final int CAMERA_PIC_REQUEST = 123;
    private final int FILE_PIC_REQUEST = 456;
    private Uri uriCamera, uriFile;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        setActionBar();
        initView(view);
        return view;
    }

    private void setActionBar() {
        ActionBarLib.setSupportActionBar(getActivity(), getString(R.string.menu_info));
    }

    private void initView(View view) {
        progressDialog = new ProgressDialog(getContext());
        Dialog.DialogLoading(progressDialog,true);
        imageUserInfo = view.findViewById(R.id.imgUserInfo);
        txtNameUser = view.findViewById(R.id.txtNameUser);
        txtEmailUser = view.findViewById(R.id.txtEmailUser);
        txtChangePass = view.findViewById(R.id.txtChangePass);
        txtExits = view.findViewById(R.id.txtExit);
        txtManagerUser = view.findViewById(R.id.txtManagerUser);
        initEvent();
        if (auth.getCurrentUser().getUid() != null) {
            initDowloadInfoUser();
        } else {
            Toasty.error(getActivity(), getString(R.string.error), Toasty.LENGTH_LONG).show();
            System.exit(0);
        }

    }

    private void initDowloadInfoUser() {

        databaseReference = FirebaseDatabase.getInstance().getReference();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                txtNameUser.setText(user.getName());
                txtEmailUser.setText(user.getEmail());
                loadImageUser();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("manager", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("id","gRoXFjKkGQNbtjGgr1uJIzrQawp1");
        databaseReference.child("User").child(id).addValueEventListener(valueEventListener);
    }

    private void loadImageUser() {
        storageReference.child("user").child(user.getImage()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Glide.
                            with(getActivity())
                            .load(task.getResult().toString())
                            .error(R.drawable.error_center_x)
                            .into(imageUserInfo);
                    Dialog.DialogLoading(progressDialog,false);
                }


            }
        });

    }

    private void initEvent() {
        txtManagerUser.setOnClickListener(this);
        imageUserInfo.setOnClickListener(this);
        txtChangePass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtManagerUser:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fram, new ManagerUserFragment()).commit();
                break;
            case R.id.imgUserInfo:
                openFileImage();
                break;
            case R.id.txtChangePass:
                changePassUser();
                break;

        }
    }

    @SuppressLint("RestrictedApi")
    private void openFileImage() {
        PopupMenu popupMenu = new PopupMenu(getActivity(), imageUserInfo);
        popupMenu.getMenuInflater().inflate(R.menu.custom_menu_camera, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_camera) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PIC_REQUEST);
                    } else {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                    }
                }
                if (item.getItemId() == R.id.menu_file) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PIC_REQUEST);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, FILE_PIC_REQUEST);
                    }
                }


                return false;
            }
        });
        popupMenu.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_PIC_REQUEST:
                if (resultCode == getActivity().RESULT_OK && data != null) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    imageUserInfo.setImageBitmap(bitmap);
                    uploadImageUser();
                }
                break;
            case FILE_PIC_REQUEST:
                if (resultCode == getActivity().RESULT_OK && data != null) {
                    uriFile = data.getData();
                    imageUserInfo.setImageURI(uriFile);
                    uploadImageUser();
                }
                break;
        }

    }

    private void uploadImageUser() {
        Dialog.DialogLoading(progressDialog,true);
        storageReference.child("user/" + uriFile.getLastPathSegment()).putFile(uriFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Dialog.DialogLoading(progressDialog,false);
                Toasty.success(getContext(), "Update Success", Toasty.LENGTH_LONG).show();
            }
        });
        databaseReference.child("User").child(auth.getCurrentUser().getUid()).child("image").setValue(uriFile.getLastPathSegment());
    }

    private void changePassUser(){
        auth.sendPasswordResetEmail(auth.getCurrentUser().getEmail())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toasty.success(getActivity(),getString(R.string.change_pass_success) + " " + auth.getCurrentUser().getEmail(),Toasty.LENGTH_LONG).show();
                        }
                    }
                });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_PIC_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                } else {
                    Toasty.error(getContext(), "Permisstion Camera Error", Toasty.LENGTH_LONG).show();
                }
                break;
            case FILE_PIC_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, FILE_PIC_REQUEST);
                } else {
                    Toasty.error(getContext(), "Permisstion File Error", Toasty.LENGTH_LONG).show();

                }
                break;
        }

    }


}
