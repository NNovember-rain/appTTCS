package vn.mrlongg71.ps09103_assignment.view.bill;

import java.util.ArrayList;
import java.util.List;

import vn.mrlongg71.ps09103_assignment.model.objectclass.Book;
import vn.mrlongg71.ps09103_assignment.model.objectclass.TypeBook;

public interface IViewBillSearch {
    void displayListBook(ArrayList<Book> bookList, ArrayList<TypeBook> typeBookList);

}
