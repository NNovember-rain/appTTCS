package vn.mrlongg71.ps09103_assignment.model.objectclass;

import android.os.Parcel;
import android.os.Parcelable;

public class BillDetail   {
    private String code, bookCode;
    private  int amountBuy;

    public BillDetail(String code, String bookCode, int amountBuy) {
        this.code = code;
        this.bookCode = bookCode;
        this.amountBuy = amountBuy;
    }

    public BillDetail() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBookCode() {
        return bookCode;
    }

    public void setBookCode(String bookCode) {
        this.bookCode = bookCode;
    }

    public int getAmountBuy() {
        return amountBuy;
    }

    public void setAmountBuy(int amountBuy) {
        this.amountBuy = amountBuy;
    }
}
