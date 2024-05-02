package vn.mrlongg71.ps09103_assignment.view.login;

public interface IViewLogin {
    void onSuccess();
    void onFailed();

    void onResetPasswordSuccess();
    void onResetPasswordFailed(String error);
}
