package vn.mrlongg71.ps09103_assignment.presenter.bill;

import vn.mrlongg71.ps09103_assignment.model.objectclass.Customer;

public interface IPresenterChooseCustomer {

    void getListCustomer();

    void resultListCustomer(Customer customer);

    void getAddCustomer(Customer customer);
    void resultAddCustomer(boolean success);
}
