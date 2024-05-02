package vn.mrlongg71.ps09103_assignment.presenter.bill;

import java.util.List;

import vn.mrlongg71.ps09103_assignment.model.objectclass.Bill;
import vn.mrlongg71.ps09103_assignment.model.objectclass.BillDetail;

public interface IPresenterAddBill {

    void getAddBill(Bill bill, List<BillDetail> billDetailList);
    void resultAddbill(boolean success);
}
