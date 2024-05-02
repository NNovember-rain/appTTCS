package vn.mrlongg71.ps09103_assignment.view.manageruser;


import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;

import com.developer.kalert.KAlertDialog;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import vn.mrlongg71.ps09103_assignment.R;
import vn.mrlongg71.ps09103_assignment.adapter.RecyclerViewUserAdapter;
import vn.mrlongg71.ps09103_assignment.library.ActionBarLib;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Book;
import vn.mrlongg71.ps09103_assignment.model.objectclass.User;
import vn.mrlongg71.ps09103_assignment.presenter.manageruser.IPresenterManagerUserAdapter;
import vn.mrlongg71.ps09103_assignment.presenter.manageruser.PresenterManagerUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManagerUserFragment extends Fragment implements View.OnClickListener, IViewManagerUser, IPresenterManagerUserAdapter {

    private EditText edtEmailNew, edtPassNew, edtNameNew, edtPhoneNew, edtDateNew;
    CircleImageView circleImageUserAdd;
    private Button btnAddUserNew;
    private PresenterManagerUser presenterManagerUser;
    private RecyclerViewUserAdapter recyclerViewUserAdapter;
    private RecyclerView recyclerUser;
    private Dialog dialogAddUser;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private final int CAMERA_PIC_REQUEST = 123;
    private final int FILE_PIC_REQUEST = 456;
    private Uri uriFile, uriCamera;
    private  boolean manager;

    private Calendar calendar = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    private String currentTime = sdf.format(calendar.getTime());
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manager_user, container, false);
        ActionBarLib.setSupportActionBar(getActivity(), getString(R.string.manager));
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("manager", Context.MODE_PRIVATE);
        manager = sharedPreferences.getBoolean("admin", false);
        if(manager){
            setHasOptionsMenu(true);
        }

        progressDialog = new ProgressDialog(getActivity());
        vn.mrlongg71.ps09103_assignment.library.Dialog.DialogLoading(progressDialog, true);
        presenterManagerUser = new PresenterManagerUser(this);
        recyclerUser = view.findViewById(R.id.recyclerUser);
        presenterManagerUser.getListUser();
        return view;
    }

    private void initCreateUser() {

        if (edtEmailNew.getText().toString().trim().length() > 0 && edtPassNew.getText().toString().trim().length() > 0 && edtPhoneNew.getText().toString().trim().length() > 0 && edtNameNew.getText().toString().trim().length() > 0) {
            User user = new User();
            user.setEmail(edtEmailNew.getText().toString().trim());
            user.setPhone(edtPhoneNew.getText().toString().trim());
            user.setName(edtNameNew.getText().toString().trim());
            user.setDate(currentTime);

            if (uriFile != null) {
                user.setImage(uriFile.getLastPathSegment());
                vn.mrlongg71.ps09103_assignment.library.Dialog.DialogLoading(progressDialog, true);
                presenterManagerUser.getAddUser(user, edtPassNew.getText().toString().trim());

            } else {
                Toasty.error(getActivity(), getString(R.string.error), Toasty.LENGTH_LONG).show();
            }

        } else {
            Toasty.error(getActivity(), getString(R.string.error), Toasty.LENGTH_LONG).show();
        }

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.custom_menu_add, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_type:
                dialogAddUser = new Dialog(getActivity());
                dialogAddUser.setContentView(R.layout.custom_dialog_add_user);
                circleImageUserAdd = dialogAddUser.findViewById(R.id.imageUserAdd);
                edtEmailNew = dialogAddUser.findViewById(R.id.edtEmailAddnew);
                edtPassNew = dialogAddUser.findViewById(R.id.edtPassAddnew);
                edtPhoneNew = dialogAddUser.findViewById(R.id.edtPhoneAddnew);
                edtNameNew = dialogAddUser.findViewById(R.id.edtNameAddnew);
                edtDateNew = dialogAddUser.findViewById(R.id.edtDateAddnew);
                edtDateNew.setText(currentTime);
                btnAddUserNew = dialogAddUser.findViewById(R.id.btnAddUserNewDialog);
                btnAddUserNew.setOnClickListener(this);
                circleImageUserAdd.setOnClickListener(this);
                dialogAddUser.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddUserNewDialog:
                initCreateUser();
                break;
            case R.id.imageUserAdd:
                openFileImage();
                break;
        }
    }

    @Override
    public void displayListUser(ArrayList<User> userList) {
        recyclerViewUserAdapter = new RecyclerViewUserAdapter(getActivity(), userList, R.layout.custom_list_user,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerUser.setLayoutManager(layoutManager);
        recyclerUser.setAdapter(recyclerViewUserAdapter);
        recyclerViewUserAdapter.notifyDataSetChanged();
        vn.mrlongg71.ps09103_assignment.library.Dialog.DialogLoading(progressDialog, false);
    }

    @Override
    public void displayAddUserSuccess() {
        vn.mrlongg71.ps09103_assignment.library.Dialog.DialogLoading(progressDialog, false);
        dialogAddUser.dismiss();
        Toasty.success(getActivity(), getString(R.string.success)).show();
    }

    @Override
    public void displayAddUserFailed(String message) {
        vn.mrlongg71.ps09103_assignment.library.Dialog.DialogLoading(progressDialog, false);
        Toasty.error(getActivity(), getString(R.string.error) + " " + message).show();
    }



    @Override
    public void displayDeleteUserSuccess() {
        recyclerViewUserAdapter.notifyDataSetChanged();
        Toasty.success(getActivity(),getString(R.string.success),Toasty.LENGTH_LONG).show();
    }

    @Override
    public void displayDeleteUserFailed(String message) {
        Toasty.success(getActivity(),message,Toasty.LENGTH_LONG).show();
    }

    private void openFileImage() {
        PopupMenu popupMenu = new PopupMenu(getActivity(), circleImageUserAdd);
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

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_PIC_REQUEST:
                if (resultCode == getActivity().RESULT_OK && data != null) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    circleImageUserAdd.setImageBitmap(bitmap);
                    uploadImageUser();
                }
                break;
            case FILE_PIC_REQUEST:
                if (resultCode == getActivity().RESULT_OK && data != null) {
                    uriFile = data.getData();
                    circleImageUserAdd.setImageURI(uriFile);
                    uploadImageUser();
                }
                break;
        }

    }

    private void uploadImageUser() {

        storageReference.child("user/" + uriFile.getLastPathSegment()).putFile(uriFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });
    }

    @Override
    public void onEventDeleteItemClickListenerBook(int position, final ArrayList<User> userList) {
        final User user = userList.get(position);
        new KAlertDialog(getActivity())
                .setContentText(getString(R.string.wantDelete))
                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                    @Override
                    public void onClick(KAlertDialog kAlertDialog) {
                        presenterManagerUser.getDeleteUser(user);
                        kAlertDialog.dismissWithAnimation();
                        userList.clear();
                    }
                })
                .show();

    }

}
