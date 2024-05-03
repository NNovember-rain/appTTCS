package vn.libaryapp.ps09103_assignment.view.manageruser;

import java.util.ArrayList;

import vn.libaryapp.ps09103_assignment.model.objectclass.User;

public interface IViewManagerUser {
    void displayListUser(ArrayList<User> userList);

    void displayAddUserSuccess();
    void displayAddUserFailed(String message);


    void displayDeleteUserSuccess();
    void displayDeleteUserFailed(String message);
}
