package vn.mrlongg71.ps09103_assignment.presenter.bill;

import java.util.ArrayList;
import java.util.List;

import vn.mrlongg71.ps09103_assignment.model.bill.ModelBillSearch;
import vn.mrlongg71.ps09103_assignment.model.book.ModelBook;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Book;
import vn.mrlongg71.ps09103_assignment.model.objectclass.TypeBook;
import vn.mrlongg71.ps09103_assignment.view.bill.IViewBillSearch;

public class PresenterBillSearch implements IPresenterBillSearch {
    IViewBillSearch iViewBillSearch;
    ModelBillSearch modelBillSearch;
    ArrayList<Book> bookList = new ArrayList<>();
    ArrayList<TypeBook> typeBookListItem = new ArrayList<>();

    public PresenterBillSearch(IViewBillSearch iViewBillSearch) {
        this.iViewBillSearch = iViewBillSearch;
        modelBillSearch = new ModelBillSearch();
    }

    @Override
    public void getBook() {
        modelBillSearch.dowloadListBook(this);
    }

    @Override
    public void resultgetBook(Book book, TypeBook typeBook) {
        bookList.add(book);
        typeBookListItem.add(typeBook);
        if (bookList.size() != 0 && typeBookListItem.size() != 0) {
            iViewBillSearch.displayListBook(bookList, typeBookListItem);
        }

    }


}
