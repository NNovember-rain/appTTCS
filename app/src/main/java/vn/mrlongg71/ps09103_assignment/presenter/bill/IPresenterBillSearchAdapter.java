package vn.mrlongg71.ps09103_assignment.presenter.bill;

import android.widget.CheckBox;

import java.util.List;

import vn.mrlongg71.ps09103_assignment.model.objectclass.Book;

public interface IPresenterBillSearchAdapter {
    void initEventCheckboxChooseBook(List<Book> bookList, CheckBox checkBox, int i);

}
