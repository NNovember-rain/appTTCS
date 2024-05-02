package vn.mrlongg71.ps09103_assignment.view.login;

public interface IViewLoginAdmin {
    void onSendSuccess();
    void onSendFaile(String error);

    void onVerifyOTPSuccess();
    void onVerifyOTPFaile(String error);
}
