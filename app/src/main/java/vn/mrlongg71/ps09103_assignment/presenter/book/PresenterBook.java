package vn.mrlongg71.ps09103_assignment.presenter.book;

import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import vn.mrlongg71.ps09103_assignment.model.book.ModelBook;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Book;
import vn.mrlongg71.ps09103_assignment.model.objectclass.TypeBook;
import vn.mrlongg71.ps09103_assignment.view.book.IViewBook;

public class PresenterBook implements IPresenterBook{
    private IViewBook iViewBook;
    private ModelBook modelBook;
    final ArrayList<Book> bookList = new ArrayList<>();
    final List<TypeBook> typeBookListItem = new ArrayList<>();
    List<TypeBook> typeBookList= new ArrayList<>();
    public PresenterBook(IViewBook iViewBook){
        this.iViewBook = iViewBook;
        modelBook = new ModelBook();

    }
    @Override
    public void getBook() {
        modelBook.dowloadListBook(this);
    }

    @Override
    public void resultgetBook(Book book,TypeBook typeBook) {
        bookList.add(book);
        typeBookListItem.add(typeBook);
        if(bookList.size() != 0 && typeBookListItem.size() != 0){
        iViewBook.displayListBook(bookList,typeBookListItem);
    }
}

    @Override
    public void resultgetBookFailed() {
        iViewBook.displayListBookFailed();
    }

    @Override
    public void getTypeBook() {
        modelBook.dowloadListTypeBook(this);
    }

    @Override
    public void resultgetTypeBook(TypeBook typeBook) {

        iViewBook.displayListTypeBookSpiner(typeBook);
//        typeBookList.add(typeBook);
//        Log.d("type" , typeBookList.size() + " ");
//
//        if(typeBookList.size() > 0){
//            iViewBook.displayListTypeBookSpiner(typeBookList);
//        }
    }


    @Override
    public void getAddBook(Book book, List<String> uriList) {
        modelBook.initAddBook(book,uriList,this);
    }

    @Override
    public void resultAddBook(boolean success) {
        if(success){
            iViewBook.displayAddBookSucces();
        }else {
            iViewBook.displayAddBookFailed();
        }
    }

    @Override
    public void getItemDelete(String key) {
        modelBook.initDeleteBook(key,this);
    }

    @Override
    public void resultDeleteTypeBook(boolean success) {
        if(success){
            iViewBook.displayDeleteItemBookSuccess();
        }else {
            iViewBook.displayDeleteItemBookFailed();
        }
    }

    @Override
    public void getItemEditBook(String key,Book book,List<String> uriList) {
        modelBook.initEditBook(key,book,this,uriList);
    }

    @Override
    public void resultEditBook(boolean success) {
        if(success){
            iViewBook.displayEditItemBookSuccess();
        }else {
            iViewBook.displayEditItemBookFailed();
        }
    }
}
