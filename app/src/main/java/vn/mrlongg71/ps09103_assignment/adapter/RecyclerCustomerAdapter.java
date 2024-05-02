package vn.mrlongg71.ps09103_assignment.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.mrlongg71.ps09103_assignment.R;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Book;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Customer;
import vn.mrlongg71.ps09103_assignment.presenter.bill.IPresenterAddBillAdapter;
import vn.mrlongg71.ps09103_assignment.view.bill.ChooseCustomerActivity;

import static android.app.Activity.RESULT_OK;

public class RecyclerCustomerAdapter extends RecyclerView.Adapter<RecyclerCustomerAdapter.ViewHolderCustomer> {
    private ChooseCustomerActivity context;
    private int resource;
    private List<Customer> customerList;

    public RecyclerCustomerAdapter(ChooseCustomerActivity context, int resource, List<Customer> customerList  ) {
        this.context = context;
        this.resource = resource;
        this.customerList = customerList;

    }


    public class ViewHolderCustomer extends RecyclerView.ViewHolder {
        TextView txtName,txtEmail;
        CardView cardCustomer;
        public ViewHolderCustomer(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            cardCustomer = itemView.findViewById(R.id.cardUser);
        }
    }

    @NonNull
    @Override
    public RecyclerCustomerAdapter.ViewHolderCustomer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        RecyclerCustomerAdapter.ViewHolderCustomer ViewHolderCustomer = new RecyclerCustomerAdapter.ViewHolderCustomer(view);
        return ViewHolderCustomer;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull RecyclerCustomerAdapter.ViewHolderCustomer holder, int position) {
        final Customer customer = customerList.get(position);
        holder.txtName.setText(customer.getName());
        holder.txtEmail.setText(customer.getEmail());
        holder.cardCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelable("customer", customer);
                intent.putExtras(bundle);
                context.setResult(RESULT_OK, intent);
                context.finish();
            }
        });
    }




    @Override
    public int getItemCount() {
        return customerList.size();
    }


}

