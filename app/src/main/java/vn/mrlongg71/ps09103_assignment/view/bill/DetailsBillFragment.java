package vn.mrlongg71.ps09103_assignment.view.bill;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developer.kalert.KAlertDialog;

import java.util.List;

import es.dmoral.toasty.Toasty;
import vn.mrlongg71.ps09103_assignment.R;
import vn.mrlongg71.ps09103_assignment.adapter.RecyclerViewBillDetailsAdapter;
import vn.mrlongg71.ps09103_assignment.library.ActionBarLib;
import vn.mrlongg71.ps09103_assignment.library.Dialog;
import vn.mrlongg71.ps09103_assignment.library.PopBack;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Bill;
import vn.mrlongg71.ps09103_assignment.model.objectclass.BillDetail;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Book;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Customer;
import vn.mrlongg71.ps09103_assignment.model.objectclass.User;
import vn.mrlongg71.ps09103_assignment.presenter.bill.PresenterBillDetails;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsBillFragment extends Fragment implements IViewBillDetails {
    private PresenterBillDetails presenterBillDetails;
    private TextView txtDateDetailsBill, txtCreateDetailsBill, txtCustomerDetailsBill
            , txtTotalDetailsBill, txtCodeDetailsBill,txtCustomerPhoneDetailsBill,txtProducts;
    private RecyclerView recyclerDetailsBill;
    private Bill bill;
    private ProgressDialog progressDialog;
    private FragmentManager fragmentManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =
                inflater.inflate(R.layout.fragment_details_bill, container, false);
        initView(view);
        setHasOptionsMenu(true);
        setActionToolbar();
        initGetBill();
        PopBack.callBack(view,fragmentManager);
        return view;
    }

    private void setActionToolbar() {
        ActionBarLib.setSupportActionBar(getActivity(),"Details Bill " );
    }

    private void initGetBill() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            Dialog.DialogLoading(progressDialog, true);
            bill = bundle.getParcelable("billdetails");
            presenterBillDetails.getBillDetails(bill);
        }
    }

    private void initView(View view) {
        presenterBillDetails = new PresenterBillDetails(this);
        txtDateDetailsBill = view.findViewById(R.id.txtDateDetailsBill);
        txtCodeDetailsBill = view.findViewById(R.id.txtCodeDetailsBill);
        txtCreateDetailsBill = view.findViewById(R.id.txtCreateDetailsBill);
        txtCustomerDetailsBill = view.findViewById(R.id.txtCustomerDetailsBill);
        txtTotalDetailsBill = view.findViewById(R.id.txtTotalDetailsBill);
        txtProducts = view.findViewById(R.id.txtProducts);
        txtCustomerPhoneDetailsBill = view.findViewById(R.id.txtCustomerPhoneDetailsBill);
        recyclerDetailsBill = view.findViewById(R.id.recyclerDetailsBill);
        progressDialog = new ProgressDialog(getActivity());
        fragmentManager = getActivity().getSupportFragmentManager();
    }


    @Override
    public void displayBillDetails(List<BillDetail> billDetailList, Customer customer, User user, List<Book> bookList) {
        ActionBarLib.setSupportActionBar(getActivity(),"Details Bill " + bill.getCode());
        txtCodeDetailsBill.setText(bill.getCode());
        txtDateDetailsBill.setText("Date: " + bill.getDateCreate());
        txtProducts.setText("Products (" + billDetailList.size()+")");
        txtCreateDetailsBill.setText("Create Bill by: " + user.getName());
        txtCustomerDetailsBill.setText(customer.getName());
        txtCustomerPhoneDetailsBill.setText(customer.getPhone());
        txtTotalDetailsBill.setText(bill.getTotalPrice() + "đ");
        RecyclerViewBillDetailsAdapter recyclerViewBillDetailsAdapter = new RecyclerViewBillDetailsAdapter(getActivity(), R.layout.custom_book, bookList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerDetailsBill.setLayoutManager(layoutManager);
        recyclerDetailsBill.setAdapter(recyclerViewBillDetailsAdapter);
        recyclerViewBillDetailsAdapter.notifyDataSetChanged();
        Dialog.DialogLoading(progressDialog, false);

    }



    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
            inflater.inflate(R.menu.custom_menu_delete,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_delete){
            new KAlertDialog(getActivity())
                    .setTitleText(getString(R.string.wantDelete))
                    .setContentText(bill.getCode())
                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                        @Override
                        public void onClick(KAlertDialog kAlertDialog) {
                            presenterBillDetails.getDeleteBill(bill);
                            kAlertDialog.dismissWithAnimation();
                        }
                    })
                    .show();

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onDeleteSuccess() {
        Toasty.success(getActivity(),getString(R.string.success),Toasty.LENGTH_SHORT).show();
        fragmentManager.popBackStack();
    }

    @Override
    public void onDeleteFailed() {
        Toasty.error(getActivity(),getString(R.string.error),Toasty.LENGTH_SHORT).show();

    }


}
