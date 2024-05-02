package vn.mrlongg71.ps09103_assignment.model.typebook;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import vn.mrlongg71.ps09103_assignment.model.objectclass.TypeBook;
import vn.mrlongg71.ps09103_assignment.presenter.typebook.PresenterTypeBook;

public class ModelTypeBook  {
    DatabaseReference databaseType = FirebaseDatabase.getInstance().getReference().child("TypeBook");
    public void dowloadListTypeBook(final PresenterTypeBook presenterTypeBook){

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
               TypeBook typeBook = dataSnapshot.getValue(TypeBook.class);
                presenterTypeBook.resultgetTypeBook(typeBook);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                databaseType.removeEventListener(this);
                databaseType.addChildEventListener(this);

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.d("test" , "remove");

                databaseType.removeEventListener(this);
                databaseType.addChildEventListener(this);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseType.addChildEventListener(childEventListener);
    }

    public void initAddTypeBook(TypeBook typeBook, final PresenterTypeBook presenterTypeBook){
        String keyType = databaseType.push().getKey();
        typeBook.setKey(keyType);
        databaseType.child(keyType).setValue(typeBook).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    presenterTypeBook.resultAddTypeBook(true);
                }else{
                    presenterTypeBook.resultAddTypeBook(false);
                }
            }
        });
    }

    public void initDeleteTypeBook(String key, final PresenterTypeBook presenterTypeBook){
        databaseType.child(key).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    presenterTypeBook.resultDeleteTypeBook(true);
                }else {
                    presenterTypeBook.resultDeleteTypeBook(false);
                }
            }
        });

    }
    public void initEditTypeBook(String key, TypeBook typeBook, final PresenterTypeBook presenterTypeBook){
        databaseType.child(key).setValue(typeBook).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    presenterTypeBook.resultEditTypeBook(true);
                }else{
                    presenterTypeBook.resultEditTypeBook(false);
                }
            }
        });
    }

}
