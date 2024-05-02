package vn.mrlongg71.ps09103_assignment.view.bill;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.renderscript.Sampler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import es.dmoral.toasty.Toasty;
import vn.mrlongg71.ps09103_assignment.R;
import vn.mrlongg71.ps09103_assignment.adapter.RecyclerItemBookBillAdapter;
import vn.mrlongg71.ps09103_assignment.library.PopBack;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Bill;
import vn.mrlongg71.ps09103_assignment.model.objectclass.BillDetail;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Book;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Customer;
import vn.mrlongg71.ps09103_assignment.model.objectclass.User;
import vn.mrlongg71.ps09103_assignment.presenter.bill.IPresenterAddBillAdapter;
import vn.mrlongg71.ps09103_assignment.presenter.bill.PresenterAddBill;
import vn.mrlongg71.ps09103_assignment.view.activity.HomeActivity;


public class AddBillFragment extends Fragment implements View.OnClickListener, IViewBillAdd, IPresenterAddBillAdapter {


    private int REQUES_CODE = 123;
    private int REQUES_CODE_CUSTOMER = 456;
    private List<BillDetail> billDetailList;
    private CardView cartChooseBook, cartTotal;
    private ArrayList<Book> bookArrayListItem;
    private RecyclerView recyclerItemBookBill;
    private RecyclerItemBookBillAdapter recyclerItemBookBillAdapter;
    private Button btnCreateBill;
    private TextView txtTotalBook, txtTotalMoney, txtTotal,txtAddDetail,txtAddCustomer;
    private PresenterAddBill presenterAddBill;
    private String noteDetailsBill = "";
    private String codeUser;
    private String billCode;
    private String currentTime;
    private Customer customer;
    private double totalMoney = 0;
    private FragmentManager fragmentManager;
    private ProgressDialog progressDialog;
    private Bill bill = new Bill();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_bill, container, false);
        initView(view);
        setHasOptionsMenu(true);
        PopBack.callBack(view, fragmentManager);
        return view;
    }

    private void initView(View view) {
        cartChooseBook = view.findViewById(R.id.cardChooseProducts);
//        cartTotal = view.findViewById(R.id.cartTotal);
        txtTotalBook = view.findViewById(R.id.txtTotalBook);
        txtTotalMoney = view.findViewById(R.id.txtTotalMoney);
        txtTotal = view.findViewById(R.id.txtTotal);
        txtAddDetail = view.findViewById(R.id.txtAddDetail);
        txtAddCustomer = view.findViewById(R.id.txtAddCustomer);
        recyclerItemBookBill = view.findViewById(R.id.recyclerItemBookBill);
        btnCreateBill = view.findViewById(R.id.btnCreateBill);
        progressDialog = new ProgressDialog(getActivity());
        presenterAddBill = new PresenterAddBill(this);
        fragmentManager = getActivity().getSupportFragmentManager();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("manager", Context.MODE_PRIVATE);
         codeUser = sharedPreferences.getString("id", "");
        Random random = new Random();
        int detail = random.nextInt();
         billCode = "DH" + detail;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        currentTime = sdf.format(calendar.getTime());
        initEvent();

    }


    private void initEvent() {
        cartChooseBook.setOnClickListener(this);
        btnCreateBill.setOnClickListener(this);
        txtAddDetail.setOnClickListener(this);
        txtAddCustomer.setOnClickListener(this);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.custom_menu_add, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_search) {
            startActivityForResult(new Intent(getActivity(), SearchBookActivity.class), REQUES_CODE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUES_CODE) {
            if (resultCode == getActivity().RESULT_OK && data != null) {
                Bundle bundle = data.getExtras();
                bookArrayListItem = new ArrayList<>();
                bookArrayListItem = bundle.getParcelableArrayList("billitem");
                initShowItemBookChoose();
            }
        }
        if (requestCode == REQUES_CODE_CUSTOMER) {
            if (resultCode == getActivity().RESULT_OK && data != null) {
                Bundle bundle = data.getExtras();
                customer = new Customer();
                customer = bundle.getParcelable("customer");
                txtAddCustomer.setText("Customer: "+ customer.getName());
            }
        }
    }

    private void initShowItemBookChoose() {
        if (bookArrayListItem.size() != 0) {
            cartChooseBook.setVisibility(View.GONE);
            recyclerItemBookBill.setVisibility(View.VISIBLE);
            recyclerItemBookBillAdapter = new RecyclerItemBookBillAdapter(getActivity(), R.layout.custom_item_bill_book, bookArrayListItem, this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
            recyclerItemBookBill.setLayoutManager(layoutManager);

            recyclerItemBookBill.setAdapter(recyclerItemBookBillAdapter);
            billDetailList = new ArrayList<>();
            for (Book valueBook : bookArrayListItem) {
                billDetailList.add(new BillDetail("", valueBook.getBookcode(), 1));
            }
            initSetTotal();

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cardChooseProducts:
                startActivityForResult(new Intent(getActivity(), SearchBookActivity.class), REQUES_CODE);
                break;
            case R.id.btnCreateBill:
                vn.mrlongg71.ps09103_assignment.library.Dialog.DialogLoading(progressDialog,true);

                initCreateBill();
                break;
            case R.id.txtAddDetail:
                initAddDetail();
                break;
            case R.id.txtAddCustomer:
                initChooseCustomer();
                break;
        }
    }

    private void initChooseCustomer() {
        startActivityForResult(new Intent(getActivity(),ChooseCustomerActivity.class),REQUES_CODE_CUSTOMER);
    }

    private void initAddDetail() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_note_bill);
        TextView btnAddDetails,btnCloseDetails;
        final EditText edtNoteBill;
        btnAddDetails = dialog.findViewById(R.id.btnAddDetails);
        btnCloseDetails = dialog.findViewById(R.id.btnCloseDetails);
        edtNoteBill = dialog.findViewById(R.id.edtNoteBill);
        btnCloseDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnAddDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtNoteBill.getText().toString().length() > 0){
                    noteDetailsBill = edtNoteBill.getText().toString().trim();
                    txtAddDetail.setText(noteDetailsBill);
                    dialog.dismiss();
                }else {
                    Toasty.error(getActivity(), getString(R.string.error),Toasty.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();

    }

    private void initCreateBill() {

        if (billDetailList.size() != 0) {

            bill.setCode(billCode);
            bill.setCodeUser(codeUser);
            bill.setTotalPrice(totalMoney);
            if(customer == null){
                customer = new Customer();
                bill.setCodeCustomer("-Lr88nUKi4D8JNMMAIBO");
            }else {
                bill.setCodeCustomer(customer.getCode());
            }
            if(noteDetailsBill.length() >0){
                bill.setNote(noteDetailsBill);
            }else {
                bill.setNote("");
            }


            bill.setDateCreate(currentTime);
            presenterAddBill.getAddBill(bill, billDetailList);

        }
    }

    @Override
    public void onAddSucces() {
        vn.mrlongg71.ps09103_assignment.library.Dialog.DialogLoading(progressDialog,false);

        Toasty.success(getActivity(), getString(R.string.success), Toasty.LENGTH_LONG).show();
       fragmentManager.popBackStack();
    }

    @Override
    public void onAddFailed() {
        Toasty.error(getActivity(), getString(R.string.error), Toasty.LENGTH_LONG).show();

    }

    @Override
    public void initReduction(final int i, final TextView txtReduction, final TextView txtAmountBill, final TextView txtNoAmount, final TextView txtIncreasing) {

        txtReduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (billDetailList.get(i).getAmountBuy() == 1) {
                    txtReduction.setVisibility(View.INVISIBLE);
                }else {
                    txtNoAmount.setVisibility(View.GONE);
                    txtIncreasing.setVisibility(View.VISIBLE);
                    int amountNew = (int) billDetailList.get(i).getAmountBuy() - 1;
                    billDetailList.get(i).setAmountBuy(amountNew);
                    txtAmountBill.setText(billDetailList.get(i).getAmountBuy() + "");
                    initSetTotal();
                }

            }
        });
    }

    @Override
    public void initIncreasing(final int i, final TextView txtIncreasing, final TextView txtAmountBill, final TextView txtNoAmount, final TextView txtReduction) {
        txtIncreasing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int amountCurret = Integer.parseInt(bookArrayListItem.get(i).getAmount());
                if (billDetailList.get(i).getAmountBuy() < amountCurret) {
                    txtReduction.setVisibility(View.VISIBLE);
                    int amountNew = (int) billDetailList.get(i).getAmountBuy() + 1;
                    billDetailList.get(i).setAmountBuy(amountNew);
                    txtAmountBill.setText(billDetailList.get(i).getAmountBuy() + "");
                    initSetTotal();

                } else {
                    txtIncreasing.setVisibility(View.INVISIBLE);
                    txtNoAmount.setVisibility(View.VISIBLE);
                    txtNoAmount.setText("Not enough quantity!!");
                }

            }
        });
    }

    private void initSetTotal() {
        if (billDetailList.size() != 0) {
            int totalBook = 0;
            totalMoney = 0;
            for (int i = 0; i < billDetailList.size(); i++) {
                totalBook += billDetailList.get(i).getAmountBuy();
                totalMoney += bookArrayListItem.get(i).getPrice() * billDetailList.get(i).getAmountBuy();

            }
            txtTotalBook.setText(totalBook + "");
            txtTotalMoney.setText(totalMoney + "");
            txtTotal.setText(totalMoney + "");
        }
    }
}
