package vn.libaryapp.ps09103_assignment.view.bill;

import java.util.ArrayList;

import vn.libaryapp.ps09103_assignment.model.objectclass.Book;
import vn.libaryapp.ps09103_assignment.model.objectclass.TypeBook;

public interface IViewBillSearch {
    void displayListBook(ArrayList<Book> bookList, ArrayList<TypeBook> typeBookList);

}
