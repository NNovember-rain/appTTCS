package vn.mrlongg71.ps09103_assignment.model.statistical;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import vn.mrlongg71.ps09103_assignment.model.objectclass.Bill;
import vn.mrlongg71.ps09103_assignment.model.objectclass.BillDetail;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Book;
import vn.mrlongg71.ps09103_assignment.presenter.statistical.PresenterStatistical;

public class ModelStatistical {
    public  void initGetListBill(final PresenterStatistical presenterStatistical){
        DatabaseReference noteRoot = FirebaseDatabase.getInstance().getReference();
        final List<Bill> billList = new ArrayList<>();
        final List<Book> bookList = new ArrayList<>();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot dataBill = dataSnapshot.child("Bill");
                DataSnapshot dataBook = dataSnapshot.child("Book");
                if(!dataBill.exists() || !dataBook.exists()){
                    presenterStatistical.resultBillFailed();
                }

                for(DataSnapshot valueListBill : dataBill.getChildren()){
                    Bill bill = valueListBill.getValue(Bill.class);
                    billList.add(bill);
                }

                for(DataSnapshot valueBook : dataBook.getChildren()){
                    Book book = valueBook.getValue(Book.class);
                    bookList.add(book);

                }
                presenterStatistical.resultBillSuccess(billList,bookList);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        noteRoot.addListenerForSingleValueEvent(valueEventListener);
    }


}
