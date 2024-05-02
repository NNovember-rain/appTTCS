package vn.mrlongg71.ps09103_assignment.view.bill;

import java.util.List;

import vn.mrlongg71.ps09103_assignment.model.objectclass.Customer;

public interface IViewChooseCustomer {
    void displayListCustomer(List<Customer> customerList);

    void onAddCustomerSuccess();
    void onAddCustomerFailed();
}
