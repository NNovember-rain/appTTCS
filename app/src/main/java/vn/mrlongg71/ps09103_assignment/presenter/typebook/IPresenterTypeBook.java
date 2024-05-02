package vn.mrlongg71.ps09103_assignment.presenter.typebook;

import java.util.List;

import vn.mrlongg71.ps09103_assignment.model.objectclass.TypeBook;

public interface IPresenterTypeBook {

    //****Get List Item Type****//
    void getTypeBook();

    void resultgetTypeBook(TypeBook typeBook);

    //****Add Item Type****//
    void getAddTypeBook(TypeBook typeBook);

    void resultAddTypeBook(boolean success);

    //****Delete Item Adapter****//
    void getItemDelete(String key);

    void resultDeleteTypeBook(boolean success);
    //****Edit Item Adapter****//
    void getItemEdit(String key,TypeBook typeBook);

    void resultEditTypeBook(boolean success);
}
