package vn.mrlongg71.ps09103_assignment.presenter.login;

import vn.mrlongg71.ps09103_assignment.model.login.ModelLogin;
import vn.mrlongg71.ps09103_assignment.view.login.IViewLogin;

public class PresenterLogin  implements IPresenterLogin{
    IViewLogin iViewLogin;
    ModelLogin modelLogin;
    public PresenterLogin(IViewLogin iViewLogin) {
        this.iViewLogin = iViewLogin;
        modelLogin = new ModelLogin();
    }

    @Override
    public void getEmailandPass(String email, String pass) {
        modelLogin.loginWithEmail(email,pass,this);
    }

    @Override
    public void loginStatus(boolean status) {
        if(status){
            iViewLogin.onSuccess();
        }else {
            iViewLogin.onFailed();
        }
    }

    @Override
    public void getEmailResetPassword(String email) {
        modelLogin.checkEmailExits(email,this);
    }

    @Override
    public void resultResetPassword(boolean success,String message) {
        if(success){
            iViewLogin.onResetPasswordSuccess();
        }else {
            iViewLogin.onResetPasswordFailed(message);
        }
    }
}
