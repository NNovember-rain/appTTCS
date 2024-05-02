package vn.mrlongg71.ps09103_assignment.presenter.bill;

import java.util.ArrayList;
import java.util.List;

import vn.mrlongg71.ps09103_assignment.model.bill.ModelAddCustomer;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Customer;
import vn.mrlongg71.ps09103_assignment.view.bill.IViewChooseCustomer;

public class PresenterChooseCustomer implements IPresenterChooseCustomer {
    IViewChooseCustomer iViewChooseCustomer;
    ModelAddCustomer modelAddCustomer;
    List<Customer>  customerList = new ArrayList<>();
    public PresenterChooseCustomer(IViewChooseCustomer iViewChooseCustomer) {
        this.iViewChooseCustomer = iViewChooseCustomer;
        modelAddCustomer = new ModelAddCustomer();
    }



    @Override
    public void getListCustomer() {
        modelAddCustomer.initGetListCustomer(this);
    }

    @Override
    public void resultListCustomer(Customer customer) {
        if(customer != null){
            customerList.add(customer);
            iViewChooseCustomer.displayListCustomer(customerList);
        }
    }

    @Override
    public void getAddCustomer(Customer customer) {
        modelAddCustomer.initAddCustomer(customer,this);
    }

    @Override
    public void resultAddCustomer(boolean success) {
        if(success){
            iViewChooseCustomer.onAddCustomerSuccess();
        }else {
            iViewChooseCustomer.onAddCustomerFailed();
        }
    }
}
