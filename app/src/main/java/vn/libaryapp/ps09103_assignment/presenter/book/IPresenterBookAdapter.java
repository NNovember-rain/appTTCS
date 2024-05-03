package vn.libaryapp.ps09103_assignment.presenter.book;

import java.util.ArrayList;

import vn.libaryapp.ps09103_assignment.model.objectclass.Book;

public interface IPresenterBookAdapter {
    void onEventDeleteItemClickListenerBook(int position, ArrayList<Book> bookList);
    void onEventEditItemClickListenerBook(int position, ArrayList<Book> bookList);

}
