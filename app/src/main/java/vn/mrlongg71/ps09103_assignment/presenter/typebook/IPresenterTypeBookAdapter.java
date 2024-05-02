package vn.mrlongg71.ps09103_assignment.presenter.typebook;

import java.util.List;

import vn.mrlongg71.ps09103_assignment.model.objectclass.TypeBook;

public interface IPresenterTypeBookAdapter {
    void onEventDeleteItemClickListenerTypeBook(int position, List<TypeBook> typeBookList);
    void onEventEditItemClickListenerTypeBook(int position, List<TypeBook> typeBookList);

}
