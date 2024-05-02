package vn.mrlongg71.ps09103_assignment.presenter.login;

import android.app.Activity;
import android.widget.TextView;

public interface IPresenterLoginAdmin {
    void getPhoneNumber(String phone, TextView txtResend, Activity activity);

    void resultSendOTP(boolean success, String message);

    void getOTPCode(String verify,Activity activity);

    void resultVerifyOTP(boolean success,String message);
}
