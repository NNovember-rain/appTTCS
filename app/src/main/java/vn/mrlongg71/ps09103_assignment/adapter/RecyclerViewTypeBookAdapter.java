package vn.mrlongg71.ps09103_assignment.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.kalert.KAlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import vn.mrlongg71.ps09103_assignment.R;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Book;
import vn.mrlongg71.ps09103_assignment.model.objectclass.TypeBook;
import vn.mrlongg71.ps09103_assignment.model.typebook.ModelTypeBook;
import vn.mrlongg71.ps09103_assignment.presenter.typebook.IPresenterTypeBook;
import vn.mrlongg71.ps09103_assignment.presenter.typebook.IPresenterTypeBookAdapter;
import vn.mrlongg71.ps09103_assignment.presenter.typebook.PresenterTypeBook;
import vn.mrlongg71.ps09103_assignment.view.typebook.IViewTypeBook;
import vn.mrlongg71.ps09103_assignment.view.typebook.TypebookFragment;

public class RecyclerViewTypeBookAdapter extends RecyclerView.Adapter<RecyclerViewTypeBookAdapter.ViewHolder> {
    private Context context;
    private int resource;
    private List<TypeBook> typeBookList;
    private IPresenterTypeBookAdapter iPresenterTypeBookAdapter;
    private List<TypeBook> typeBookListCopy;

    public RecyclerViewTypeBookAdapter(Context context, int resource, List<TypeBook> typeBookList, IPresenterTypeBookAdapter iPresenterTypeBookAdapter) {
        this.context = context;
        this.resource = resource;
        this.typeBookList = typeBookList;
        this.iPresenterTypeBookAdapter = iPresenterTypeBookAdapter;

        typeBookListCopy = new ArrayList<>();
        typeBookListCopy.addAll(typeBookList);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgTypeBook;
        TextView txtTypeCode, txtTypeName;
        CardView cardType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgTypeBook = itemView.findViewById(R.id.imgTypeBook);
            txtTypeCode = itemView.findViewById(R.id.txtTypeCode);
            txtTypeName = itemView.findViewById(R.id.txtTypeName);
            cardType = itemView.findViewById(R.id.cartType);
        }
    }

    @NonNull
    @Override
    public RecyclerViewTypeBookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewTypeBookAdapter.ViewHolder holder, final int position) {
        TypeBook typeBook = typeBookList.get(position);
        holder.txtTypeCode.setText(typeBook.getTypecode());
        holder.txtTypeName.setText(typeBook.getTypename());
        eventCartType(holder, position);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.cusstom_right_left);
        holder.itemView.startAnimation(animation);

    }

    private void eventCartType(final ViewHolder holder, final int position) {
        holder.cardType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(context, holder.cardType);
                popupMenu.getMenuInflater().inflate(R.menu.poupup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.editType) {
                            iPresenterTypeBookAdapter.onEventEditItemClickListenerTypeBook(position, typeBookList);
                        }
                        if (item.getItemId() == R.id.deleteType) {
                            iPresenterTypeBookAdapter.onEventDeleteItemClickListenerTypeBook(position, typeBookList);

                        }

                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return typeBookList.size();
    }

    public void search(String text) {
        text = text.toLowerCase();
        if (text.length() == 0) {
            typeBookList.clear();

            typeBookList.addAll(typeBookListCopy);
            notifyDataSetChanged();
        } else {
            typeBookList.clear();

            for (int i = 0; i < typeBookListCopy.size(); i++) {
                TypeBook typeBook = typeBookListCopy.get(i);
                if (typeBook.getTypename().toLowerCase().contains(text)) {
                    typeBookList.add(typeBook);
                    notifyDataSetChanged();
                }
                if (typeBook.getTypecode().toLowerCase().contains(text)) {
                    typeBookList.add(typeBook);
                    notifyDataSetChanged();
                }
            }
        }
    }

}
