package vn.mrlongg71.ps09103_assignment.presenter.statistical;

import java.util.List;

import vn.mrlongg71.ps09103_assignment.model.objectclass.Bill;
import vn.mrlongg71.ps09103_assignment.model.objectclass.BillDetail;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Book;

public interface IPresenterStatistical {
    void getListBill(int i,int month);
    void resultBillSuccess(List<Bill> billListDow , List<Book> bookListDow);
    void resultBillFailed();
}
