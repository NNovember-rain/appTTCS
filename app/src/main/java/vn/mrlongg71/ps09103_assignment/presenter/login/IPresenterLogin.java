package vn.mrlongg71.ps09103_assignment.presenter.login;

public interface IPresenterLogin {
    void getEmailandPass(String email,String pass);
    void loginStatus(boolean status );

    //resetPassword
    void getEmailResetPassword(String email);
    void resultResetPassword(boolean success,String message);
}
