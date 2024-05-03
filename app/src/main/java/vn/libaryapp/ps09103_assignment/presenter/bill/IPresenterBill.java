package vn.libaryapp.ps09103_assignment.presenter.bill;

import vn.libaryapp.ps09103_assignment.model.objectclass.Bill;

public interface IPresenterBill {
    void getListBill();
    void resultListBill(Bill bill);
    void resultListBillExits();
}
