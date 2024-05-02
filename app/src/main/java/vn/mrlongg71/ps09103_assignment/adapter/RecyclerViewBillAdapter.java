package vn.mrlongg71.ps09103_assignment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.mrlongg71.ps09103_assignment.R;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Bill;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Book;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Customer;
import vn.mrlongg71.ps09103_assignment.model.objectclass.TypeBook;
import vn.mrlongg71.ps09103_assignment.presenter.bill.IPresenterBillAdapter;
import vn.mrlongg71.ps09103_assignment.view.bill.BillFragment;
import vn.mrlongg71.ps09103_assignment.view.bill.ChooseCustomerActivity;

public class RecyclerViewBillAdapter extends RecyclerView.Adapter<RecyclerViewBillAdapter.ViewHolderBill> {
    private Context context;
    private int resource;
    private List<Bill> billsList;
    private List<Bill> billListCopy;
    private IPresenterBillAdapter iPresenterBillAdapter;
    public RecyclerViewBillAdapter(Context context, int resource, List<Bill> billsList, IPresenterBillAdapter iPresenterBillAdapter) {
        this.context = context;
        this.resource = resource;
        this.billsList = billsList;
        this.iPresenterBillAdapter = iPresenterBillAdapter;
        billListCopy = new ArrayList<>();
        billListCopy.addAll(billsList);

    }

    @NonNull
    @Override
    public ViewHolderBill onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        ViewHolderBill viewHolderBill = new ViewHolderBill(view);
        return viewHolderBill;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderBill holder, final int position) {
        final Bill bill = billsList.get(position);
        holder.txtCodeBill.setText("Code: "+ bill.getCode());
        holder.txtDateBill.setText("Date: " + bill.getDateCreate());
        holder.txtPriceBill.setText(bill.getTotalPrice() + "$");
        holder.cartBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iPresenterBillAdapter.initEventClickDetails(position,bill);
            }
        });
    }

    @Override
    public int getItemCount() {
        return billsList.size();
    }

    public class ViewHolderBill extends RecyclerView.ViewHolder {
        TextView txtCodeBill,txtDateBill,txtPriceBill;
        CardView cartBill;
        public ViewHolderBill(@NonNull View itemView) {
            super(itemView);
            cartBill = itemView.findViewById(R.id.cartBill);
            txtCodeBill = itemView.findViewById(R.id.txtCodeBill);
            txtDateBill = itemView.findViewById(R.id.txtDateBill);
            txtPriceBill = itemView.findViewById(R.id.txtPriceBill);

        }
    }
    public void search(String text) {
        text = text.toLowerCase();
        if (text.length() == 0) {
            billsList.clear();

            billsList.addAll(billListCopy);
            notifyDataSetChanged();
        } else {
            billsList.clear();


            for (int i = 0; i < billListCopy.size(); i++) {
                Bill bill = billListCopy.get(i);

                if (bill.getCode().toLowerCase().contains(text)) {
                    billsList.add(bill);
                    notifyDataSetChanged();
                }
                if (bill.getDateCreate().toLowerCase().contains(text)) {
                    billsList.add(bill);
                    notifyDataSetChanged();
                }
            }
        }
    }
}
