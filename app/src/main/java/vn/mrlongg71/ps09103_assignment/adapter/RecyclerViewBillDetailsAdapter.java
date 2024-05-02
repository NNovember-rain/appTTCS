package vn.mrlongg71.ps09103_assignment.adapter;

import android.content.Context;
import android.icu.text.NumberFormat;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.List;

import vn.mrlongg71.ps09103_assignment.R;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Book;
import vn.mrlongg71.ps09103_assignment.model.objectclass.TypeBook;

public class RecyclerViewBillDetailsAdapter extends RecyclerView.Adapter<RecyclerViewBillDetailsAdapter.ViewHolderBillDetails> {
    private Context context;
    private int resource;
    private List<Book> bookList;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();


    public RecyclerViewBillDetailsAdapter(Context context, int resource, List<Book> bookList) {
        this.context = context;
        this.resource = resource;
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public ViewHolderBillDetails onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new ViewHolderBillDetails(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ViewHolderBillDetails holder, int position) {
        Book book = bookList.get(position);
        holder.txtBookName.setText(book.getBookname());
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        String price = numberFormat.format(book.getPrice());
        holder.txtPrice.setText("Giá: " + price);
        setImagesBook(holder.imageBook, book);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class ViewHolderBillDetails extends RecyclerView.ViewHolder {
        RoundedImageView imageBook;
        TextView txtBookName, txtPrice;

        public ViewHolderBillDetails(@NonNull View itemView) {
            super(itemView);
            imageBook = itemView.findViewById(R.id.imageBook);
            txtBookName = itemView.findViewById(R.id.txtBookName);
            txtPrice = itemView.findViewById(R.id.txtPrice);

        }
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

}
