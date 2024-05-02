package vn.mrlongg71.ps09103_assignment.view.bill;

import java.util.List;

import vn.mrlongg71.ps09103_assignment.model.objectclass.BillDetail;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Book;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Customer;
import vn.mrlongg71.ps09103_assignment.model.objectclass.User;

public interface IViewBillDetails {
    void displayBillDetails(List<BillDetail> billDetailList, Customer customer, User user, List<Book> bookList);

    void onDeleteSuccess();

    void onDeleteFailed();
}
