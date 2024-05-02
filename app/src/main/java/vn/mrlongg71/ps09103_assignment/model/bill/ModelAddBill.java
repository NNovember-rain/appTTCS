package vn.mrlongg71.ps09103_assignment.model.bill;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import vn.mrlongg71.ps09103_assignment.model.objectclass.Bill;
import vn.mrlongg71.ps09103_assignment.model.objectclass.BillDetail;
import vn.mrlongg71.ps09103_assignment.presenter.bill.PresenterAddBill;

public class ModelAddBill {
    DatabaseReference dataBill = FirebaseDatabase.getInstance().getReference();
    public void initAddBill(Bill bill, final List<BillDetail> billDetailList, final PresenterAddBill presenterAddBill){
        final String code = dataBill.push().getKey();
        bill.setCodeBillDetail(code);
        dataBill.child("Bill").child(code).setValue(bill).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    for(BillDetail valueBillDetail : billDetailList){
                        String codeDetail = dataBill.push().getKey();
                        valueBillDetail.setCode(codeDetail);
                        dataBill.child("BillDetails").child(code).child(codeDetail).setValue(valueBillDetail);

                    }

                        presenterAddBill.resultAddbill(true);


                }else {
                    presenterAddBill.resultAddbill(false);
                }
            }
        });
    }
}
