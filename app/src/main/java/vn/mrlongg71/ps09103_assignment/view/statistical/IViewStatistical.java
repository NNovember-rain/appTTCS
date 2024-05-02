package vn.mrlongg71.ps09103_assignment.view.statistical;

import java.util.List;

import vn.mrlongg71.ps09103_assignment.model.objectclass.Bill;
import vn.mrlongg71.ps09103_assignment.model.objectclass.BillDetail;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Book;

public interface IViewStatistical {
    void onStatisticalDay(List<Bill> billListDay, List<BillDetail> billDetailList, List<Book> bookList);
    void onStatisticalYesterday(List<Bill> billListDay, List<BillDetail> billDetailList, List<Book> bookList);
    void onStatisticalMonth(List<Bill> billListDay, List<BillDetail> billDetailList, List<Book> bookList);

    void onStatisticalFailed();
}
