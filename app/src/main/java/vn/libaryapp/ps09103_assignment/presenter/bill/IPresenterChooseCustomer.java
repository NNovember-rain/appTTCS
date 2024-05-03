package vn.libaryapp.ps09103_assignment.presenter.bill;

import vn.libaryapp.ps09103_assignment.model.objectclass.Customer;

public interface IPresenterChooseCustomer {

    void getListCustomer();

    void resultListCustomer(Customer customer);

    void getAddCustomer(Customer customer);
    void resultAddCustomer(boolean success);
}
