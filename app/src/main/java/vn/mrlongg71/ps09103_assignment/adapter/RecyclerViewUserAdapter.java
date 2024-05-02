package vn.mrlongg71.ps09103_assignment.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.mrlongg71.ps09103_assignment.R;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Book;
import vn.mrlongg71.ps09103_assignment.model.objectclass.User;
import vn.mrlongg71.ps09103_assignment.presenter.manageruser.IPresenterManagerUserAdapter;

public class RecyclerViewUserAdapter extends RecyclerView.Adapter<RecyclerViewUserAdapter.ViewHolderUser> {
    private Context context;
    private ArrayList<User> userList;
    private int layout;
    private IPresenterManagerUserAdapter iPresenterManagerUserAdapter;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    public RecyclerViewUserAdapter(Context context, ArrayList<User> userList, int layout,IPresenterManagerUserAdapter iPresenterManagerUserAdapter) {
        this.context = context;
        this.userList = userList;
        this.layout = layout;
        this.iPresenterManagerUserAdapter = iPresenterManagerUserAdapter;
    }

    @NonNull
    @Override
    public ViewHolderUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layout, parent, false);
        ViewHolderUser viewHolderUser = new ViewHolderUser(view);
        return viewHolderUser;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderUser holder, int position) {
        final User user = userList.get(position);
        storageReference.child("user").child(user.getImage()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Glide.with(context)
                        .load(task.getResult().toString())
                        .into(holder.imgUser);
            }
        });

        holder.txtName.setText(user.getName());
        holder.txtEmail.setText(user.getEmail());
        SharedPreferences sharedPreferences = context.getSharedPreferences("manager", Context.MODE_PRIVATE);
        boolean manager = sharedPreferences.getBoolean("admin", false);
        if(manager){
            eventCardClick(holder,position);
        }

    }

    private void eventCardClick(final ViewHolderUser holderUser,final int position) {
        holderUser.cardUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(context, holderUser.cardUser);
                popupMenu.getMenuInflater().inflate(R.menu.poupup_menu, popupMenu.getMenu());
                MenuItem menuItem = popupMenu.getMenu().findItem(R.id.editType);
                menuItem.setVisible(false);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getItemId() == R.id.deleteType) {
                            iPresenterManagerUserAdapter.onEventDeleteItemClickListenerBook(position,userList);
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
        return userList.size();
    }

    public class ViewHolderUser extends RecyclerView.ViewHolder {
        CardView cardUser;
        CircleImageView imgUser;
        TextView txtName,txtEmail;
        public ViewHolderUser(@NonNull View itemView) {
            super(itemView);
            cardUser = itemView.findViewById(R.id.cardUser);
            imgUser = (CircleImageView) itemView.findViewById(R.id.imgUser);
            txtName = itemView.findViewById(R.id.txtName);
            txtEmail = itemView.findViewById(R.id.txtEmail);

        }
    }
}
