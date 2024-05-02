package vn.mrlongg71.ps09103_assignment.presenter.bill;

import java.util.ArrayList;
import java.util.List;

import vn.mrlongg71.ps09103_assignment.model.bill.ModelBillDetails;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Bill;
import vn.mrlongg71.ps09103_assignment.model.objectclass.BillDetail;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Book;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Customer;
import vn.mrlongg71.ps09103_assignment.model.objectclass.User;
import vn.mrlongg71.ps09103_assignment.view.bill.IViewBillDetails;

public class PresenterBillDetails implements IPresenterBillDetails {
    IViewBillDetails iViewBillDetails;
    ModelBillDetails modelBillDetails;
    List<BillDetail> billDetailList = new ArrayList<>();

    public PresenterBillDetails(IViewBillDetails iViewBillDetails) {
        this.iViewBillDetails = iViewBillDetails;
        modelBillDetails = new ModelBillDetails();
    }

    @Override
    public void getBillDetails(Bill bill) {
        modelBillDetails.initGetDetailsBill(bill,this);
    }

    @Override
    public void resultGetBillDetails(BillDetail billDetail, Customer customer, User user, List<Book> bookList) {
        billDetailList.add(billDetail);
        iViewBillDetails.displayBillDetails(billDetailList,customer,user,bookList);
    }

    @Override
    public void getDeleteBill(Bill bill) {
        modelBillDetails.initDeleteBill(bill,this);
    }

    @Override
    public void resultDeleteBill(boolean success) {
        if(success){
            iViewBillDetails.onDeleteSuccess();
        }else {
            iViewBillDetails.onDeleteFailed();
        }
    }


}
