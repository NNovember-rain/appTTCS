package vn.mrlongg71.ps09103_assignment.adapter;

import android.content.Context;
import android.icu.text.NumberFormat;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import vn.mrlongg71.ps09103_assignment.R;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Book;
import vn.mrlongg71.ps09103_assignment.model.objectclass.TypeBook;
import vn.mrlongg71.ps09103_assignment.presenter.bill.IPresenterAddBillAdapter;
import vn.mrlongg71.ps09103_assignment.presenter.bill.IPresenterBillSearchAdapter;
import vn.mrlongg71.ps09103_assignment.presenter.book.IPresenterBookAdapter;

public class RecyclerItemBookBillAdapter extends RecyclerView.Adapter<RecyclerItemBookBillAdapter.ViewHolderBook> {
    private Context context;
    private int resource;
    private ArrayList<Book> bookList;
    private int aumout = 1;
    private IPresenterAddBillAdapter iPresenterAddBillAdapter;

    public RecyclerItemBookBillAdapter(Context context, int resource, ArrayList<Book> bookList ,IPresenterAddBillAdapter iPresenterAddBillAdapter) {
        this.context = context;
        this.resource = resource;
        this.bookList = bookList;
        this.iPresenterAddBillAdapter = iPresenterAddBillAdapter;
    }


    public class ViewHolderBook extends RecyclerView.ViewHolder {
        TextView txtNameItem,txtPriceItem,txtIncreasing,txtReduction,txtAmountBill,txtNoAmount;

        public ViewHolderBook(@NonNull View itemView) {
            super(itemView);
            txtNameItem = itemView.findViewById(R.id.txtNameItem);
            txtPriceItem = itemView.findViewById(R.id.txtPriceItem);
            txtIncreasing = itemView.findViewById(R.id.txtIncreasing);
            txtReduction = itemView.findViewById(R.id.txtReduction);
            txtAmountBill = itemView.findViewById(R.id.txtAmountBill);
            txtNoAmount = itemView.findViewById(R.id.txtNoAmount);
        }
    }

    @NonNull
    @Override
    public RecyclerItemBookBillAdapter.ViewHolderBook onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        RecyclerItemBookBillAdapter.ViewHolderBook viewHolderBook = new RecyclerItemBookBillAdapter.ViewHolderBook(view);
        return viewHolderBook;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull RecyclerItemBookBillAdapter.ViewHolderBook holder, int position) {
        Book book = bookList.get(position);
        holder.txtNameItem.setText("Name: "+book.getBookname());
        holder.txtPriceItem.setText("Price: "+book.getPrice() + "");
        holder.txtAmountBill.setText(aumout + "");
        iPresenterAddBillAdapter.initIncreasing(position,holder.txtIncreasing,holder.txtAmountBill,holder.txtNoAmount,holder.txtReduction);
        iPresenterAddBillAdapter.initReduction(position,holder.txtReduction,holder.txtAmountBill,holder.txtNoAmount,holder.txtIncreasing);

    }




    @Override
    public int getItemCount() {
        return bookList.size();
    }


}

