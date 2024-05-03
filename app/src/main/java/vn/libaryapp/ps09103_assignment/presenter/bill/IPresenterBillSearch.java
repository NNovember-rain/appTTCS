package vn.libaryapp.ps09103_assignment.presenter.bill;

import vn.libaryapp.ps09103_assignment.model.objectclass.Book;
import vn.libaryapp.ps09103_assignment.model.objectclass.TypeBook;

public interface IPresenterBillSearch {
    void getBook();

    void resultgetBook(Book book, TypeBook typeBook);


}
