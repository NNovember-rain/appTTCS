package vn.mrlongg71.ps09103_assignment.model.manageruser;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import vn.mrlongg71.ps09103_assignment.model.objectclass.User;
import vn.mrlongg71.ps09103_assignment.presenter.manageruser.PresenterManagerUser;

public class ModelManagerUser {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    public void initAddUser(final User user, String password, final PresenterManagerUser presenterManagerUser) {
        auth.createUserWithEmailAndPassword(user.getEmail(), password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    user.setKey(auth.getCurrentUser().getUid());
                    databaseReference.child("User").child(user.getKey()).setValue(user);
                    presenterManagerUser.resultAddUser(true, "");
                } else {
                    presenterManagerUser.resultAddUser(false, task.getException().getMessage());
                }
            }
        });
    }

    public void initDowloadUserList(final PresenterManagerUser presenterManagerUser) {
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                final User user = dataSnapshot.getValue(User.class);
                presenterManagerUser.resultListUser(user);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                databaseReference.child("User").removeEventListener(this);
                databaseReference.child("User").addChildEventListener(this);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                databaseReference.child("User").removeEventListener(this);
                databaseReference.child("User").addChildEventListener(this);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.child("User").addChildEventListener(childEventListener);


    }

    public void initDeleteUser(final User user, final PresenterManagerUser presenterManagerUser) {
        databaseReference.child("User").child(user.getKey()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                presenterManagerUser.resultDeleteUser(true, "");

                            } else {
                                presenterManagerUser.resultDeleteUser(false, task.getException().getMessage());
                            }
                        }
                    });
//        firebaseAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    databaseReference.child("User").child(user.getKey()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()) {
//                                presenterManagerUser.resultDeleteUser(true, "");
//
//                            } else {
//                                presenterManagerUser.resultDeleteUser(false, task.getException().getMessage());
//                            }
//                        }
//                    });
//                }
//            }
//        });


    }
}
