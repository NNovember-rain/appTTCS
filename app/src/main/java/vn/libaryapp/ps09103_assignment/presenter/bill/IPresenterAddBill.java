package vn.libaryapp.ps09103_assignment.presenter.bill;

import java.util.List;

import vn.libaryapp.ps09103_assignment.model.objectclass.Bill;
import vn.libaryapp.ps09103_assignment.model.objectclass.BillDetail;

public interface IPresenterAddBill {

    void getAddBill(Bill bill, List<BillDetail> billDetailList);
    void resultAddbill(boolean success);
}
