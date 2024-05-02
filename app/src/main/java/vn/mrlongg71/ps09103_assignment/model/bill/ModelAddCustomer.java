package vn.mrlongg71.ps09103_assignment.model.bill;

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

import vn.mrlongg71.ps09103_assignment.model.objectclass.Customer;
import vn.mrlongg71.ps09103_assignment.presenter.bill.IPresenterChooseCustomer;
import vn.mrlongg71.ps09103_assignment.presenter.bill.PresenterChooseCustomer;

public class ModelAddCustomer {
    DatabaseReference dataCustomer = FirebaseDatabase.getInstance().getReference();

    public void initGetListCustomer(final IPresenterChooseCustomer iPresenterChooseCustomer) {
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Customer customer = dataSnapshot.getValue(Customer.class);
                iPresenterChooseCustomer.resultListCustomer(customer);
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
        dataCustomer.child("Customer").addChildEventListener(childEventListener);
    }

    public void initAddCustomer(Customer customer, final PresenterChooseCustomer presenterChooseCustomer) {
        String code = dataCustomer.push().getKey();
        customer.setCode(code);
        dataCustomer.child("Customer").child(code).setValue(customer).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    presenterChooseCustomer.resultAddCustomer(true);
                } else {
                    presenterChooseCustomer.resultAddCustomer(false);
                }
            }
        });
    }
}
