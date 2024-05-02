package vn.mrlongg71.ps09103_assignment.presenter.manageruser;

import java.util.List;

import vn.mrlongg71.ps09103_assignment.model.objectclass.User;

public interface IPresenterManagerUser {
    void getListUser();
    void resultListUser(User user);


    void getAddUser(User user,String password);
    void resultAddUser(boolean success,String message);

    void getDeleteUser(User user);
    void resultDeleteUser(boolean success,String message);
}
