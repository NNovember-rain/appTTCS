package vn.mrlongg71.ps09103_assignment.model.bill;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import vn.mrlongg71.ps09103_assignment.model.objectclass.Bill;
import vn.mrlongg71.ps09103_assignment.model.objectclass.BillDetail;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Book;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Customer;
import vn.mrlongg71.ps09103_assignment.model.objectclass.User;
import vn.mrlongg71.ps09103_assignment.presenter.bill.PresenterBillDetails;

public class ModelBillDetails {
    DatabaseReference noteRoot = FirebaseDatabase.getInstance().getReference();

    public void initGetDetailsBill(final Bill bill, final PresenterBillDetails presenterBillDetails){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot dataDetailsBill = dataSnapshot.child("BillDetails").child(bill.getCodeBillDetail());
                BillDetail billDetail;
                List<Book> bookList = new ArrayList<>();
                for(DataSnapshot valueBillDetails : dataDetailsBill.getChildren()){

                    billDetail = valueBillDetails.getValue(BillDetail.class);
                    DataSnapshot dataCustomer = dataSnapshot.child("Customer").child(bill.getCodeCustomer());
                    Customer customer = dataCustomer.getValue(Customer.class);

                    DataSnapshot dataUser = dataSnapshot.child("User").child(bill.getCodeUser());
                    User user = dataUser.getValue(User.class);

                    DataSnapshot dataBook = dataSnapshot.child("Book").child(billDetail.getBookCode());
                    Book book = dataBook.getValue(Book.class);

                    DataSnapshot dataImagesBook = dataSnapshot.child("ImagesBookList").child(book.getBookcode());
                    List<String> arrImagesBook = new ArrayList<>();
                    for(DataSnapshot valueImagesBook : dataImagesBook.getChildren()){
                        arrImagesBook.add(valueImagesBook.getValue(String.class));
                    }
                    book.setArrImagesBook(arrImagesBook);
                    bookList.add(book);

//
                    presenterBillDetails.resultGetBillDetails(billDetail,customer,user,bookList);

                }





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        noteRoot.addValueEventListener(valueEventListener);



    }

    public void initDeleteBill(final Bill bill, final PresenterBillDetails presenterBillDetails){
        noteRoot.child("Bill").child(bill.getCodeBillDetail()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    noteRoot.child("BillDetails").child(bill.getCodeBillDetail()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                presenterBillDetails.resultDeleteBill(true);
                            }else{
                                presenterBillDetails.resultDeleteBill(false);
                            }
                        }
                    });
                }
            }
        });
    }
}
