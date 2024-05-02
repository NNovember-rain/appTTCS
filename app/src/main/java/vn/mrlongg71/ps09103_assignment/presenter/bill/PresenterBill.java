package vn.mrlongg71.ps09103_assignment.presenter.bill;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import vn.mrlongg71.ps09103_assignment.model.bill.ModelBill;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Bill;
import vn.mrlongg71.ps09103_assignment.view.bill.IViewPresenterBill;

public class PresenterBill implements IPresenterBill {
    IViewPresenterBill iViewPresenterBill;
    ModelBill modelBill;
    List<Bill> billList = new ArrayList<>();
    public PresenterBill(IViewPresenterBill iViewPresenterBill) {
        this.iViewPresenterBill = iViewPresenterBill;
        modelBill = new ModelBill();
    }

    @Override
    public void getListBill() {
        modelBill.initGetListBill(this);
    }

    @Override
    public void resultListBill(Bill bill) {
        if(bill != null){
            billList.add(bill);
            iViewPresenterBill.displayListBill(billList);
        }
    }

    @Override
    public void resultListBillExits() {
        iViewPresenterBill.displayListBillFailed();
    }
}
