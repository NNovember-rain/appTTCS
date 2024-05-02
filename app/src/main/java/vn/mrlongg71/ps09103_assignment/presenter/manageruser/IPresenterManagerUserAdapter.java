package vn.mrlongg71.ps09103_assignment.presenter.manageruser;

import java.util.ArrayList;

import vn.mrlongg71.ps09103_assignment.model.objectclass.Book;
import vn.mrlongg71.ps09103_assignment.model.objectclass.User;

public interface IPresenterManagerUserAdapter {
    void onEventDeleteItemClickListenerBook(int position, ArrayList<User> userList);

}
