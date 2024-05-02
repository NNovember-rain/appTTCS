package vn.mrlongg71.ps09103_assignment.view.bill;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import es.dmoral.toasty.Toasty;
import vn.mrlongg71.ps09103_assignment.R;
import vn.mrlongg71.ps09103_assignment.adapter.RecyclerViewBillSearchAdapter;
import vn.mrlongg71.ps09103_assignment.adapter.RecyclerViewBookAdapter;
import vn.mrlongg71.ps09103_assignment.adapter.SpinerAdapter;
import vn.mrlongg71.ps09103_assignment.library.Dialog;
import vn.mrlongg71.ps09103_assignment.model.objectclass.BillDetail;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Book;
import vn.mrlongg71.ps09103_assignment.model.objectclass.TypeBook;
import vn.mrlongg71.ps09103_assignment.presenter.bill.IPresenterBillSearchAdapter;
import vn.mrlongg71.ps09103_assignment.presenter.bill.PresenterBillSearch;
import vn.mrlongg71.ps09103_assignment.service.NetworkReceiver;

public class SearchBookActivity extends AppCompatActivity implements IViewBillSearch, View.OnClickListener, IPresenterBillSearchAdapter {

    private Toolbar toolbar;
    private SpinerAdapter spinerAdapter;
    private List<TypeBook> typeBookList = new ArrayList<>();
    private ArrayList<Book>  bookArrayListItem;
    private androidx.appcompat.widget.SearchView searchView;
    private PresenterBillSearch presenterBillSearch;
    private BroadcastReceiver broadcastReceiver;
    private ProgressDialog progressDialog;
    private RecyclerViewBillSearchAdapter recyclerViewBillSearchAdapter;
    private RecyclerView recyclerSearchBook;
    private Button btnChooseDoneBookBillSearch, btnChooseAgainBookBillSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);
        initView();
        setActionToolbar();
        presenterBillSearch.getBook();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                bookArrayListItem.clear();
                recyclerViewBillSearchAdapter.search(newText);
                recyclerViewBillSearchAdapter.notifyDataSetChanged();
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void setActionToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_clear_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setTitle("");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initView() {
        presenterBillSearch = new PresenterBillSearch(this);
//        broadcastReceiver = new NetworkReceiver();
        progressDialog = new ProgressDialog(this);
        toolbar = findViewById(R.id.toolbar_Search);
        recyclerSearchBook = findViewById(R.id.recyclerBookSearch);
        btnChooseDoneBookBillSearch = findViewById(R.id.btnDoneChooseBookBillSearch);
        btnChooseAgainBookBillSearch = findViewById(R.id.btnChooseAgainBillSearch);
        typeBookList.add(new TypeBook("key", "code", "All item"));
        bookArrayListItem = new ArrayList<>();
        initEvent();

    }

    private void initEvent() {
    btnChooseDoneBookBillSearch.setOnClickListener(this);
    }

//
//    @Override
//    public void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//    }
//
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        EventBus.getDefault().unregister(this);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(NetworkReceiver.EventConnect event) {
//
//        if (event.isConnect() == true) {
//            Dialog.DialogLoading(progressDialog, true);
//            presenterBillSearch.getBook();
//        } else {
//            Dialog.DialogLoading(progressDialog, false);
//
//        }
//    };

    @Override
    public void displayListBook(ArrayList<Book> bookList, ArrayList<TypeBook> typeBookList) {
        Dialog.DialogLoading(progressDialog, false);
        recyclerViewBillSearchAdapter = new RecyclerViewBillSearchAdapter(SearchBookActivity.this, R.layout.custom_book, bookList, typeBookList,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SearchBookActivity.this);
        recyclerSearchBook.setLayoutManager(layoutManager);
        recyclerSearchBook.setAdapter(recyclerViewBillSearchAdapter);
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDoneChooseBookBillSearch:
                if(bookArrayListItem.size() != 0){
                    final Intent data = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("billitem" , bookArrayListItem);
                    data.putExtras(bundle);
                    setResult(RESULT_OK, data);
                    finish();
                }else {
                    Toasty.error(getApplicationContext(),"Don't choose book!",Toasty.LENGTH_SHORT).show();
                }

                break;
            case R.id.btnChooseAgainBillSearch:
//                initAgainBillSearch();
                break;

        }
    }

    @Override
    public void initEventCheckboxChooseBook(final List<Book> bookList, CheckBox checkBox,final int i) {
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book book = bookList.get(i);
                bookArrayListItem.add(book);

            }
        });

    }
}
