package vn.mrlongg71.ps09103_assignment.model.objectclass;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Bill implements Parcelable {
    private String code, note, codeUser, codeBillDetail, dateCreate,codeCustomer;
    private double totalPrice;

    public Bill(String code, String note, String codeUser, String codeBillDetail, String dateCreate, String codeCustomer, double totalPrice) {
        this.code = code;
        this.note = note;
        this.codeUser = codeUser;
        this.codeBillDetail = codeBillDetail;
        this.dateCreate = dateCreate;
        this.codeCustomer = codeCustomer;
        this.totalPrice = totalPrice;
    }

    public Bill() {
    }

    protected Bill(Parcel in) {
        code = in.readString();
        note = in.readString();
        codeUser = in.readString();
        codeBillDetail = in.readString();
        dateCreate = in.readString();
        codeCustomer = in.readString();
        totalPrice = in.readDouble();
    }

    public static final Creator<Bill> CREATOR = new Creator<Bill>() {
        @Override
        public Bill createFromParcel(Parcel in) {
            return new Bill(in);
        }

        @Override
        public Bill[] newArray(int size) {
            return new Bill[size];
        }
    };

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCodeUser() {
        return codeUser;
    }

    public void setCodeUser(String codeUser) {
        this.codeUser = codeUser;
    }

    public String getCodeBillDetail() {
        return codeBillDetail;
    }

    public void setCodeBillDetail(String codeBillDetail) {
        this.codeBillDetail = codeBillDetail;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getCodeCustomer() {
        return codeCustomer;
    }

    public void setCodeCustomer(String codeCustomer) {
        this.codeCustomer = codeCustomer;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(note);
        dest.writeString(codeUser);
        dest.writeString(codeBillDetail);
        dest.writeString(dateCreate);
        dest.writeString(codeCustomer);
        dest.writeDouble(totalPrice);
    }
}