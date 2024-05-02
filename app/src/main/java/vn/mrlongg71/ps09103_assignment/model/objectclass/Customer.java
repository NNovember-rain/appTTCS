package vn.mrlongg71.ps09103_assignment.model.objectclass;

import android.os.Parcel;
import android.os.Parcelable;

public class Customer  implements Parcelable {
    private String code,name,phone,email,place;

    public Customer(String code, String name, String phone, String email, String place) {
        this.code = code;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.place = place;
    }

    public Customer() {
    }

    protected Customer(Parcel in) {
        code = in.readString();
        name = in.readString();
        phone = in.readString();
        email = in.readString();
        place = in.readString();
    }

    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(place);
    }
}
