package vn.mrlongg71.ps09103_assignment.presenter.statistical;

import android.renderscript.Sampler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.security.KeyException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Handler;

import vn.mrlongg71.ps09103_assignment.model.objectclass.Bill;
import vn.mrlongg71.ps09103_assignment.model.objectclass.BillDetail;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Book;
import vn.mrlongg71.ps09103_assignment.model.statistical.ModelStatistical;
import vn.mrlongg71.ps09103_assignment.view.statistical.IViewStatistical;

public class PresenterStatistical implements IPresenterStatistical {
    IViewStatistical iViewStatistical;
    List<BillDetail> billDetailListItem = new ArrayList<>();
    ModelStatistical modelStatistical;
    List<Bill> billList;
    List<Book> bookList;

    public PresenterStatistical(IViewStatistical iViewStatistical) {
        this.iViewStatistical = iViewStatistical;
        modelStatistical = new ModelStatistical();
        billList = new ArrayList<>();
        bookList = new ArrayList<>();
    }

    @Override
    public void getListBill(int i, int month) {
        modelStatistical.initGetListBill(this);
    }

    private void statistical(int i, int month) {
        if (bookList != null && billList != null) {
            Log.d("kiemtra123", "result " + billList.size() + "  " + bookList.size());

            switch (i) {
                case 0:
                    initStatisticalYesterDay();
                    Log.d("kiemtra123", "yes");

                    break;
                case 1:

                    initStatisticalDay();
                    Log.d("kiemtra123", "day");

                    break;
                case 2:
                    initStatisticalMonth(month);
                    Log.d("kiemtra123", "month");

                    break;
            }
        }
    }

    @Override
    public void resultBillSuccess(List<Bill> billListDow, List<Book> bookListDow) {
        if (billListDow != null && bookListDow != null) {
            billList.addAll(billListDow);
            bookList.addAll(bookListDow);
            if(billList != null && bookList != null){
                initStatisticalYesterDay();
                initStatisticalDay();
                initStatisticalMonth(0);
            }


        }
    }

    @Override
    public void resultBillFailed() {
        iViewStatistical.onStatisticalFailed();
    }

    public void initStatisticalDay() {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        List<Bill> billListDay = new ArrayList<>();
        List<Book> bookListDay = new ArrayList<>();
        String[] dateCal = simpleDateFormat.format(calendar.getTime()).split("-");

        for (Bill bill : billList) {
            String[] dateBill = bill.getDateCreate().split("-");
            if (dateBill[0].equals(dateCal[0])) {
                billListDay.add(bill);
//
            }
            bookListDay.clear();
            for (Book book : bookList) {
                String[] dateBook = book.getDate().split("-");
                if (dateBook[0].equals(dateCal[0])) {
                    bookListDay.add(book);
                }
                iViewStatistical.onStatisticalDay(billListDay, billDetailListItem, bookListDay);
            }

        }

//


    }

    public void initStatisticalYesterDay() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        List<Bill> billListDay = new ArrayList<>();
        List<Book> bookListDay = new ArrayList<>();
        String[] dateCal = simpleDateFormat.format(calendar.getTime()).split("-");
        String yesterday = String.valueOf(Integer.parseInt(dateCal[0]) - 1);

        for (Bill bill : billList) {
            String[] dateBill = bill.getDateCreate().split("-");
            if (dateBill[0].equals(yesterday)) {
                billListDay.add(bill);
            }
            bookListDay.clear();
            for (Book book : bookList) {
                String[] dateBook = book.getDate().split("-");
                if (dateBook[0].equals(yesterday)) {
                    bookListDay.add(book);
                }
                iViewStatistical.onStatisticalYesterday(billListDay, billDetailListItem, bookListDay);
            }

        }

//


    }

    public void initStatisticalMonth(final int month) {

        String[] dateCal;
        if (month != 0) {
            dateCal = new String[2];
            if (month < 10) {
                dateCal[1] = "0" + month;
            } else {
                dateCal[1] = String.valueOf(month);

            }


        } else {

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            dateCal = simpleDateFormat.format(calendar.getTime()).split("-");
        }
        List<Bill> billListDay = new ArrayList<>();
        List<Book> bookListDay = new ArrayList<>();
        for (Bill bill : billList) {
            String[] dateBill = bill.getDateCreate().split("-");
            if (dateBill[1].equals(dateCal[1])) {
                billListDay.add(bill);
//
            }
            bookListDay.clear();
            for (Book book : bookList) {
                String[] dateBook = book.getDate().split("-");
                if (dateBook[1].equals(dateCal[1])) {
                    bookListDay.add(book);
                }
                iViewStatistical.onStatisticalMonth(billListDay, billDetailListItem, bookListDay);
            }

        }

//


    }


}
