package vn.mrlongg71.ps09103_assignment.presenter.book;

import java.util.ArrayList;
import java.util.List;

import vn.mrlongg71.ps09103_assignment.model.objectclass.Book;
import vn.mrlongg71.ps09103_assignment.model.objectclass.TypeBook;

public interface IPresenterBookAdapter {
    void onEventDeleteItemClickListenerBook(int position, ArrayList<Book> bookList);
    void onEventEditItemClickListenerBook(int position, ArrayList<Book> bookList);

}
