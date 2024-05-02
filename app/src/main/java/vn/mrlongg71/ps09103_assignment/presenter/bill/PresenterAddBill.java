package vn.mrlongg71.ps09103_assignment.presenter.bill;

import android.util.Log;

import java.util.List;

import vn.mrlongg71.ps09103_assignment.model.bill.ModelAddBill;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Bill;
import vn.mrlongg71.ps09103_assignment.model.objectclass.BillDetail;
import vn.mrlongg71.ps09103_assignment.view.bill.IViewBillAdd;

public class PresenterAddBill implements IPresenterAddBill {
        IViewBillAdd iViewBillAdd;
    ModelAddBill modelAddBill;

    public PresenterAddBill(IViewBillAdd iViewBillAdd) {
        this.iViewBillAdd = iViewBillAdd;
        modelAddBill = new ModelAddBill();
    }

    @Override
    public void getAddBill(Bill bill, List<BillDetail> billDetailList) {
        modelAddBill.initAddBill(bill,billDetailList,this);
    }

    @Override
    public void resultAddbill(boolean success) {
        if(success){

            iViewBillAdd.onAddSucces();
        }else {
            iViewBillAdd.onAddFailed();
        }
    }
}
