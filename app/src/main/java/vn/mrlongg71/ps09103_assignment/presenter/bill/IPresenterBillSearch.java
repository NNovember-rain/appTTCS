package vn.mrlongg71.ps09103_assignment.presenter.bill;

import vn.mrlongg71.ps09103_assignment.model.objectclass.Book;
import vn.mrlongg71.ps09103_assignment.model.objectclass.TypeBook;

public interface IPresenterBillSearch {
    void getBook();

    void resultgetBook(Book book, TypeBook typeBook);


}
