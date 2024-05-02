package vn.mrlongg71.ps09103_assignment.view.typebook;

import java.util.List;

import vn.mrlongg71.ps09103_assignment.model.objectclass.TypeBook;

public interface IViewTypeBook {
    void displayListType(List<TypeBook> typeBookList);

    void displayAddTypeSucces();
    void displayAddTypeFailed();

    void displayDeleteItemTypeBookSuccess();
    void displayDeleteItemTypeBookFailed();

    void displayEditItemTypeBookSuccess();
    void displayEditItemTypeBookFailed();

}
