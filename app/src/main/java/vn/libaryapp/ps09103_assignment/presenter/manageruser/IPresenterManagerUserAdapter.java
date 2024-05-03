package vn.libaryapp.ps09103_assignment.presenter.manageruser;

import java.util.ArrayList;

import vn.libaryapp.ps09103_assignment.model.objectclass.User;

public interface IPresenterManagerUserAdapter {
    void onEventDeleteItemClickListenerBook(int position, ArrayList<User> userList);

}
