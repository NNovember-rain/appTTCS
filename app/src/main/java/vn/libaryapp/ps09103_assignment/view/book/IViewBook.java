package vn.libaryapp.ps09103_assignment.view.book;

import java.util.ArrayList;
import java.util.List;

import vn.libaryapp.ps09103_assignment.model.objectclass.Book;
import vn.libaryapp.ps09103_assignment.model.objectclass.TypeBook;

public interface IViewBook {
    void displayListBook(ArrayList<Book> bookList, List<TypeBook> typeBookList);
    void displayListBookFailed();
    void displayListTypeBookSpiner(TypeBook typeBook);

    void displayAddBookSucces();
    void displayAddBookFailed();

    void displayDeleteItemBookSuccess();
    void displayDeleteItemBookFailed();


    void displayEditItemBookSuccess();
    void displayEditItemBookFailed();

}

