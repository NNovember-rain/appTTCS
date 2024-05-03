package vn.libaryapp.ps09103_assignment.presenter.bill;

import java.util.List;

import vn.libaryapp.ps09103_assignment.model.objectclass.Bill;
import vn.libaryapp.ps09103_assignment.model.objectclass.BillDetail;
import vn.libaryapp.ps09103_assignment.model.objectclass.Book;
import vn.libaryapp.ps09103_assignment.model.objectclass.Customer;
import vn.libaryapp.ps09103_assignment.model.objectclass.User;

public interface IPresenterBillDetails {
    void getBillDetails(Bill bill);

    void resultGetBillDetails(BillDetail billDetail, Customer customer, User user, List<Book> bookList);

    void getDeleteBill(Bill bill);
    void resultDeleteBill(boolean success);
}
