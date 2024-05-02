package vn.mrlongg71.ps09103_assignment.model.bill;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import vn.mrlongg71.ps09103_assignment.model.objectclass.Bill;
import vn.mrlongg71.ps09103_assignment.presenter.bill.PresenterBill;

public class ModelBill {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference noteBill = FirebaseDatabase.getInstance().getReference();
    public void initGetListBill(final PresenterBill presenterBill){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               DataSnapshot dataBill = dataSnapshot.child("Bill");
               if(!dataBill.exists()){
                  presenterBill.resultListBillExits();
               }
                for(DataSnapshot valueBill : dataBill.getChildren()){
                    Bill bill = valueBill.getValue(Bill.class);
                    presenterBill.resultListBill(bill);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        noteBill.addListenerForSingleValueEvent(valueEventListener);
    }
}
