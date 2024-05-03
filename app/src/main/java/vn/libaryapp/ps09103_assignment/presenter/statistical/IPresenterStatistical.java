package vn.libaryapp.ps09103_assignment.presenter.statistical;

import java.util.List;

import vn.libaryapp.ps09103_assignment.model.objectclass.Bill;
import vn.libaryapp.ps09103_assignment.model.objectclass.Book;

public interface IPresenterStatistical {
    void getListBill(int i,int month);
    void resultBillSuccess(List<Bill> billListDow , List<Book> bookListDow);
    void resultBillFailed();
}
