package vn.mrlongg71.ps09103_assignment.model.book;

import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vn.mrlongg71.ps09103_assignment.model.objectclass.Book;
import vn.mrlongg71.ps09103_assignment.model.objectclass.TypeBook;
import vn.mrlongg71.ps09103_assignment.presenter.book.PresenterBook;

public class ModelBook {
    boolean upload = false;

    DatabaseReference noteRoot = FirebaseDatabase.getInstance().getReference();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    public void dowloadListBook(final PresenterBook presenterBook) {

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot dataBook = dataSnapshot.child("Book");
                if (!dataBook.exists()) {
                    presenterBook.resultgetBookFailed();
                }
                for (DataSnapshot valueBook : dataBook.getChildren()) {
                    final Book book = valueBook.getValue(Book.class);

                    DataSnapshot dataImagesBook = dataSnapshot.child("ImagesBookList").child(book.getBookcode());
                    List<String> arrImagesBook = new ArrayList<>();
                    for (DataSnapshot valueImagesBook : dataImagesBook.getChildren()) {
                        arrImagesBook.add(valueImagesBook.getValue(String.class));
                    }
                    book.setArrImagesBook(arrImagesBook);

                    DataSnapshot dataType = dataSnapshot.child("TypeBook").child(book.getTypecode());
                    TypeBook typeBook = dataType.getValue(TypeBook.class);
                    presenterBook.resultgetBook(book, typeBook);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };


        noteRoot.addListenerForSingleValueEvent(valueEventListener);
    }


    public void dowloadListTypeBook(final PresenterBook presenterBook) {
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                TypeBook typeBook = dataSnapshot.getValue(TypeBook.class);
                presenterBook.resultgetTypeBook(typeBook);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        noteRoot.child("TypeBook").addChildEventListener(childEventListener);
    }

    public void initAddBook(final Book book, final List<String> uriList, final PresenterBook presenterBook) {
        final String key = noteRoot.child("Book").push().getKey();
        book.setBookcode(key);

        noteRoot.child("Book").child(key).setValue(book).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    uploadImages(book, uriList);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            presenterBook.resultAddBook(true);
                        }
                    },3000);

                    Log.d("kiemtra456 ", upload + "");
//                    if (upload == true) {
//                        presenterBook.resultAddBook(true);
//                    }
////                    } else {
////                        presenterBook.resultAddBook(false);
////                    }

                } else {
                    presenterBook.resultAddBook(false);
                }
            }
        });

    }

    public void initDeleteBook(String key, final PresenterBook presenterBook) {
        noteRoot.child("Book").child(key).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    presenterBook.resultDeleteTypeBook(true);
                } else {
                    presenterBook.resultDeleteTypeBook(false);
                }
            }
        });
    }

    public void initEditBook(String key, final Book book, final PresenterBook presenterBook, final List<String> uriList) {
        noteRoot.child("Book").child(key).setValue(book).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (uriList != null) {
                        noteRoot.child("ImagesBookList").child(book.getBookcode()).setValue(null);
                        uploadImages(book, uriList);
                        presenterBook.resultEditBook(true);
                    }
                    presenterBook.resultEditBook(true);
                } else {
                    presenterBook.resultEditBook(false);
                }
            }
        });
    }

    public void uploadImages(Book book, List<String> uriList) {

        for (final String valueUri : uriList) {
            Uri uri = Uri.fromFile(new File(valueUri));
            storageReference.child("book/" + uri.getLastPathSegment()).putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    upload = true;
                    Log.d("kiemtra456", taskSnapshot.getMetadata().getPath());
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            upload = false;
                        }
                    });
        }
        for (String nameImages : uriList) {
            noteRoot.child("ImagesBookList").child(book.getBookcode()).push().setValue(Uri.parse(nameImages).getLastPathSegment());
        }

    }
}
