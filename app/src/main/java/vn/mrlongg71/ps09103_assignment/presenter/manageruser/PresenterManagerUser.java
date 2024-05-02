package vn.mrlongg71.ps09103_assignment.presenter.manageruser;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import vn.mrlongg71.ps09103_assignment.model.manageruser.ModelManagerUser;
import vn.mrlongg71.ps09103_assignment.model.objectclass.User;
import vn.mrlongg71.ps09103_assignment.view.book.IViewBook;
import vn.mrlongg71.ps09103_assignment.view.manageruser.IViewManagerUser;

public class PresenterManagerUser implements IPresenterManagerUser {
    private IViewManagerUser iViewManagerUser;
    private ModelManagerUser modelManagerUser;
    private ArrayList<User> userArrayList = new ArrayList<>();

    public PresenterManagerUser(IViewManagerUser iViewManagerUser) {
        this.iViewManagerUser = iViewManagerUser;
        modelManagerUser = new ModelManagerUser();
    }

    @Override
    public void getListUser() {
        modelManagerUser.initDowloadUserList(this);
    }

    @Override
    public void resultListUser(User user) {
        userArrayList.add(user);
        if (userArrayList.size() != 0) {
            iViewManagerUser.displayListUser(userArrayList);
        }

    }

    @Override
    public void getAddUser(User user, String password) {
        modelManagerUser.initAddUser(user, password, this);
    }

    @Override
    public void resultAddUser(boolean success,String message) {
        if (success) {
            iViewManagerUser.displayAddUserSuccess();
        } else {
            iViewManagerUser.displayAddUserFailed(message);
        }
    }


    @Override
    public void getDeleteUser(User user) {
        modelManagerUser.initDeleteUser(user,this);
    }

    @Override
    public void resultDeleteUser(boolean success, String message) {
        if(success){
            iViewManagerUser.displayDeleteUserSuccess();
        }else {
            iViewManagerUser.displayDeleteUserFailed(message);
        }
    }
}
