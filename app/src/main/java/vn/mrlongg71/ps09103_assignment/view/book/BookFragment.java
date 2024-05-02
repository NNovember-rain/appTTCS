package vn.mrlongg71.ps09103_assignment.view.book;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.developer.kalert.KAlertDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import vn.mrlongg71.ps09103_assignment.R;
import vn.mrlongg71.ps09103_assignment.adapter.RecyclerViewBookAdapter;
import vn.mrlongg71.ps09103_assignment.library.ActionBarLib;
import vn.mrlongg71.ps09103_assignment.library.CallBackFragment;
import vn.mrlongg71.ps09103_assignment.library.Dialog;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Book;
import vn.mrlongg71.ps09103_assignment.model.objectclass.TypeBook;
import vn.mrlongg71.ps09103_assignment.presenter.book.IPresenterBookAdapter;
import vn.mrlongg71.ps09103_assignment.presenter.book.PresenterBook;
import vn.mrlongg71.ps09103_assignment.service.NetworkReceiver;
import vn.mrlongg71.ps09103_assignment.view.login.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookFragment extends Fragment implements IViewBook, IPresenterBookAdapter {
    private PresenterBook presenterBook;
    private RecyclerViewBookAdapter recyclerViewBookAdapter;
    private RecyclerView recyclerViewBook;
    private ProgressDialog progressDialog;
    private BroadcastReceiver broadcastReceiver;
    private SearchView searchView;
    private TextView txtNoData;
    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_book, container, false);
        setActionBar();
        initView(view);
        setHasOptionsMenu(true);
        CallBackFragment.Callback(view,fragmentManager);

        presenterBook = new PresenterBook(this);

        presenterBook.getBook();

        return view;
    }

    private void initView(View view) {
        recyclerViewBook = view.findViewById(R.id.recyclerBook);
        progressDialog = new ProgressDialog(getActivity());

        broadcastReceiver = new NetworkReceiver();
        txtNoData = view.findViewById(R.id.txtNoData);
        fragmentManager = getActivity().getSupportFragmentManager();
        Dialog.DialogLoading(progressDialog,true);
    }

    private void setActionBar() {
        ActionBarLib.setSupportActionBar(getActivity(), getString(R.string.menu_book));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.custom_menu_add, menu);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerViewBookAdapter.search(newText);
                recyclerViewBookAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_type:
                AddBookFragment addBookFragment = new AddBookFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fram, addBookFragment).addToBackStack(null).commit();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void displayListBook(ArrayList<Book> bookList,List<TypeBook> typeBookList) {
        Dialog.DialogLoading(progressDialog,false);
        recyclerViewBookAdapter = new RecyclerViewBookAdapter(getActivity(), R.layout.custom_book, bookList, typeBookList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewBook.setLayoutManager(layoutManager);
        recyclerViewBook.setAdapter(recyclerViewBookAdapter);
    }

    @Override
    public void displayListBookFailed() {
        Dialog.DialogLoading(progressDialog,false);
        txtNoData.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayListTypeBookSpiner(TypeBook typeBookList) {

    }


    @Override
    public void displayAddBookSucces() {
        Dialog.DialogLoading(progressDialog, false);
    }

    @Override
    public void displayAddBookFailed() {

    }

    @Override
    public void displayDeleteItemBookSuccess() {
        Dialog.DialogLoading(progressDialog,false);

        recyclerViewBookAdapter.notifyDataSetChanged();
        presenterBook.getBook();
        Toasty.success(getActivity(),getString(R.string.success),Toasty.LENGTH_LONG).show();
    }

    @Override
    public void displayDeleteItemBookFailed() {
        Toasty.error(getActivity(),getString(R.string.error),Toasty.LENGTH_LONG).show();
    }

    @Override
    public void displayEditItemBookSuccess() {
        Dialog.DialogLoading(progressDialog,false);

        recyclerViewBookAdapter.notifyDataSetChanged();
        Toasty.success(getActivity(),getString(R.string.success),Toasty.LENGTH_LONG).show();
    }

    @Override
    public void displayEditItemBookFailed() {
        Toasty.error(getActivity(),getString(R.string.error),Toasty.LENGTH_LONG).show();

    }


    @Override
    public void onEventDeleteItemClickListenerBook(final int position, final ArrayList<Book> bookList) {
        final Book book = bookList.get(position);
        new KAlertDialog(getActivity(), KAlertDialog.ERROR_TYPE)
                .setContentText(getActivity().getString(R.string.wantDelete) + " sách " + book.getBookname())
                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                    @Override
                    public void onClick(KAlertDialog kAlertDialog) {
                        Dialog.DialogLoading(progressDialog,true);

                        presenterBook.getItemDelete(book.getBookcode());
                        bookList.clear();
                        recyclerViewBookAdapter.notifyDataSetChanged();
                        kAlertDialog.dismissWithAnimation();
                    }
                })
                .setCancelText("No")
                .setCancelClickListener(new KAlertDialog.KAlertClickListener() {
                    @Override
                    public void onClick(KAlertDialog kAlertDialog) {
                        kAlertDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    @Override
    public void onEventEditItemClickListenerBook(int position, ArrayList<Book> bookList) {
        Bundle bundle = new Bundle();
        Book book = bookList.get(position);
        bundle.putParcelable("book" ,  book);
        AddBookFragment addBookFragment = new AddBookFragment();
        addBookFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fram,addBookFragment).addToBackStack(null).commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }



    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NetworkReceiver.EventConnect event) {

        if(event.isConnect() == true){
            Dialog.DialogLoading(progressDialog,true);
            presenterBook.getBook();
        }else {
            Dialog.DialogLoading(progressDialog,false);

        }
    };
}
