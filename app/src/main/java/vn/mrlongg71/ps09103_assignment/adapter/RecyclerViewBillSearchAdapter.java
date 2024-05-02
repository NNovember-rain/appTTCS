package vn.mrlongg71.ps09103_assignment.adapter;

import android.content.Context;
import android.icu.text.NumberFormat;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.mrlongg71.ps09103_assignment.R;
import vn.mrlongg71.ps09103_assignment.model.objectclass.BillDetail;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Book;
import vn.mrlongg71.ps09103_assignment.model.objectclass.TypeBook;
import vn.mrlongg71.ps09103_assignment.presenter.bill.IPresenterBillSearchAdapter;
import vn.mrlongg71.ps09103_assignment.presenter.book.IPresenterBookAdapter;
import vn.mrlongg71.ps09103_assignment.presenter.typebook.IPresenterTypeBookAdapter;

public class RecyclerViewBillSearchAdapter extends RecyclerView.Adapter<RecyclerViewBillSearchAdapter.ViewHolderBook> {
    private Context context;
    private int resource;
    private ArrayList<Book> bookList;
    private List<TypeBook> typeBookList;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private List<Book> arrayListBookCopy;
    private List<TypeBook> arrayListTypeCopy;
    private IPresenterBillSearchAdapter iPresenterBillSearchAdapter;

    public RecyclerViewBillSearchAdapter(Context context, int resource, ArrayList<Book> bookList, List<TypeBook> typeBookList, IPresenterBillSearchAdapter iPresenterBillSearchAdapter) {
        this.context = context;
        this.resource = resource;
        this.bookList = bookList;
        this.typeBookList = typeBookList;
        arrayListBookCopy = new ArrayList<>();
        arrayListBookCopy.addAll(bookList);
        arrayListTypeCopy = new ArrayList<>();
        arrayListTypeCopy.addAll(typeBookList);
        this.iPresenterBillSearchAdapter = iPresenterBillSearchAdapter;
    }


    public class ViewHolderBook extends RecyclerView.ViewHolder {
        CardView cardBook;
        RoundedImageView imageBook;
        TextView txtBookName, txtTypeBook, txtPrice, txtAmount;
        CheckBox checkBoxChooseBookSearch;

        public ViewHolderBook(@NonNull View itemView) {
            super(itemView);
            cardBook = itemView.findViewById(R.id.cardBook);
            imageBook = itemView.findViewById(R.id.imageBook);
            txtBookName = itemView.findViewById(R.id.txtBookName);
            txtTypeBook = itemView.findViewById(R.id.txtTypeBook);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            checkBoxChooseBookSearch = itemView.findViewById(R.id.checkboxChooseBookSearch);
        }
    }

    @NonNull
    @Override
    public RecyclerViewBillSearchAdapter.ViewHolderBook onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        RecyclerViewBillSearchAdapter.ViewHolderBook viewHolderBook = new RecyclerViewBillSearchAdapter.ViewHolderBook(view);
        return viewHolderBook;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewBillSearchAdapter.ViewHolderBook holder, int position) {
        Book book = bookList.get(position);
        TypeBook typeBook = typeBookList.get(position);
        holder.txtBookName.setText(book.getBookname());
        holder.txtAmount.setText("Số lượng: " + book.getAmount());
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        String price = numberFormat.format(book.getPrice());
        holder.txtPrice.setText("Giá: " + price);
        holder.txtTypeBook.setText("Loại: " + typeBook.getTypename());
        setImagesBook(holder.imageBook, book);
        holder.checkBoxChooseBookSearch.setVisibility(View.VISIBLE);
        initcheckboxChooseBook(holder.checkBoxChooseBookSearch, position);
    }

    private void initcheckboxChooseBook(CheckBox checkBox, int postion) {
        iPresenterBillSearchAdapter.initEventCheckboxChooseBook(bookList, checkBox, postion);

    }

    private void setImagesBook(final RoundedImageView imageBook, Book book) {
        if (book.getArrImagesBook().size() > 0) {
            storageReference.child("book").child(book.getArrImagesBook().get(0)).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Picasso.get()
                            .load(task.getResult().toString())
                            .placeholder(R.drawable.ic_loading)
                            .error(R.drawable.error_center_x)
                            .into(imageBook);
                }
            });

        }
    }


    @Override
    public int getItemCount() {
        return bookList.size();
    }

    //searchclass
    public void search(String text) {

        text = text.toLowerCase();

        //nếu ô tìm kiếm không có -> add lại mảng  :
        if (text.length() == 0) {
            bookList.clear();
            typeBookList.clear();

            bookList.addAll(arrayListBookCopy);
            typeBookList.addAll(arrayListTypeCopy);
        } else {
            bookList.clear();
            typeBookList.clear();

            for (int i = 0; i < arrayListBookCopy.size(); i++) {
                Book book = arrayListBookCopy.get(i);
                TypeBook typeBook = arrayListTypeCopy.get(i);
                if (book.getBookname().toLowerCase().contains(text)) {
                    bookList.add(book);
                    typeBookList.add(typeBook);
                    notifyDataSetChanged();
                }
            }
        }
    }



}
