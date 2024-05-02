package vn.mrlongg71.ps09103_assignment.view.bill;


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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import vn.mrlongg71.ps09103_assignment.R;
import vn.mrlongg71.ps09103_assignment.adapter.RecyclerViewBillAdapter;
import vn.mrlongg71.ps09103_assignment.library.ActionBarLib;
import vn.mrlongg71.ps09103_assignment.library.CallBackFragment;
import vn.mrlongg71.ps09103_assignment.library.Dialog;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Bill;
import vn.mrlongg71.ps09103_assignment.presenter.bill.IPresenterBillAdapter;
import vn.mrlongg71.ps09103_assignment.presenter.bill.PresenterBill;
import vn.mrlongg71.ps09103_assignment.service.NetworkReceiver;

/**
 * A simple {@link Fragment} subclass.
 */
public class BillFragment extends Fragment implements IViewPresenterBill, IPresenterBillAdapter {

    private BroadcastReceiver broadcastReceiver;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerBill;
    private PresenterBill presenterBill;
    private SearchView searchView;
    private RecyclerViewBillAdapter recyclerViewBillAdapter;
    private TextView txtNoData;
    private FragmentManager fragmentManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bill, container, false);
        setActionBar();
        initView(view);
        CallBackFragment.Callback(view,fragmentManager);
        presenterBill.getListBill();
        setHasOptionsMenu(true);
        return view;


    }

    private void initView(View view) {
        broadcastReceiver = new NetworkReceiver();
        presenterBill = new PresenterBill(this);
        progressDialog = new ProgressDialog(getActivity());
        recyclerBill = view.findViewById(R.id.recyclerBill);
        txtNoData = view.findViewById(R.id.txtNoData);
        fragmentManager = getActivity().getSupportFragmentManager();
    }

    private void setActionBar() {
        ActionBarLib.setSupportActionBar(getActivity(), getString(R.string.menu_bill));
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
                recyclerViewBillAdapter.search(newText);
                recyclerViewBillAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_add_type){
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fram, new AddBillFragment()).addToBackStack(null).commit();
        }


        return super.onOptionsItemSelected(item);
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
            setHasOptionsMenu(true);

        }else {
            Dialog.DialogLoading(progressDialog,false);

        }
    };

    @Override
    public void displayListBill(List<Bill> billList) {
        recyclerViewBillAdapter = new RecyclerViewBillAdapter(getActivity(),R.layout.custom_list_bill, billList,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        recyclerBill.setLayoutManager(layoutManager);
        recyclerBill.setAdapter(recyclerViewBillAdapter);
        recyclerViewBillAdapter.notifyDataSetChanged();
        Dialog.DialogLoading(progressDialog,false);
    }

    @Override
    public void displayListBillFailed() {
        Dialog.DialogLoading(progressDialog,false);
        txtNoData.setVisibility(View.VISIBLE);
    }

    @Override
    public void initEventClickDetails(int i, Bill bill) {
        if(bill != null){
            Bundle bundle = new Bundle();
            bundle.putParcelable("billdetails", bill);
            DetailsBillFragment detailsBillFragment = new DetailsBillFragment();
            detailsBillFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fram,detailsBillFragment).addToBackStack(null).commit();
        }
    }
}
