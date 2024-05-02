package vn.mrlongg71.ps09103_assignment.view.bill;

import java.util.List;

import vn.mrlongg71.ps09103_assignment.model.objectclass.Bill;

public interface IViewPresenterBill {
    void displayListBill(List<Bill> billList);

    void displayListBillFailed();
}
