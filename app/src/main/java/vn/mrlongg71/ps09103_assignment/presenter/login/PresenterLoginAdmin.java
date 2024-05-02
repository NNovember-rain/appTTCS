package vn.mrlongg71.ps09103_assignment.presenter.login;

import android.app.Activity;
import android.widget.TextView;

import vn.mrlongg71.ps09103_assignment.model.login.ModelLoginAdmin;
import vn.mrlongg71.ps09103_assignment.view.login.IViewLoginAdmin;

public class PresenterLoginAdmin implements IPresenterLoginAdmin {
    IViewLoginAdmin iViewLoginAdmin;
    ModelLoginAdmin modelLoginAdmin;

    public PresenterLoginAdmin(IViewLoginAdmin iViewLoginAdmin) {
        this.iViewLoginAdmin = iViewLoginAdmin;
        modelLoginAdmin = new ModelLoginAdmin();
    }

    @Override
    public void getPhoneNumber(String phone, TextView txtResend, Activity activity) {
        modelLoginAdmin.doSendSMS(phone,txtResend,activity,this);
    }

    @Override
    public void resultSendOTP(boolean success, String message) {
        if(success){
            iViewLoginAdmin.onSendSuccess();
        }else {
            iViewLoginAdmin.onSendFaile(message);
        }
    }

    @Override
    public void getOTPCode(String verify, Activity activity) {
        modelLoginAdmin.initCheckVerifyOTP(verify,activity,this);
    }

    @Override
    public void resultVerifyOTP(boolean success, String message) {
        if(success){
            iViewLoginAdmin.onVerifyOTPSuccess();
        }else {
            iViewLoginAdmin.onVerifyOTPFaile(message);
        }
    }
}
