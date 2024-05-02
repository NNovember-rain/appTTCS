package vn.mrlongg71.ps09103_assignment.model.bill;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import vn.mrlongg71.ps09103_assignment.model.objectclass.Book;
import vn.mrlongg71.ps09103_assignment.model.objectclass.TypeBook;
import vn.mrlongg71.ps09103_assignment.presenter.bill.PresenterBillSearch;
import vn.mrlongg71.ps09103_assignment.presenter.book.PresenterBook;

public class ModelBillSearch {
    DatabaseReference noteRoot = FirebaseDatabase.getInstance().getReference();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    public void dowloadListBook(final PresenterBillSearch presenterBillSearch) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot dataBook = dataSnapshot.child("Book");
                for(DataSnapshot valueBook : dataBook.getChildren()){
                    final Book book = valueBook.getValue(Book.class);

                    DataSnapshot dataImagesBook = dataSnapshot.child("ImagesBookList").child(book.getBookcode());
                    List<String> arrImagesBook = new ArrayList<>();
                    for(DataSnapshot valueImagesBook : dataImagesBook.getChildren()){
                        arrImagesBook.add(valueImagesBook.getValue(String.class));
                    }
                    book.setArrImagesBook(arrImagesBook);

                    DataSnapshot dataType =  dataSnapshot.child("TypeBook").child(book.getTypecode());
                    TypeBook typeBook = dataType.getValue(TypeBook.class);
                    presenterBillSearch.resultgetBook(book,typeBook);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };


        noteRoot.addListenerForSingleValueEvent(valueEventListener);

    }


}
