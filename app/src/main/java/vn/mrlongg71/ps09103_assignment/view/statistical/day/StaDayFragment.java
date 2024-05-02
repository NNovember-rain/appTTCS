package vn.mrlongg71.ps09103_assignment.view.statistical.day;


import android.app.ProgressDialog;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import vn.mrlongg71.ps09103_assignment.R;
import vn.mrlongg71.ps09103_assignment.library.Chart;
import vn.mrlongg71.ps09103_assignment.library.Dialog;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Bill;
import vn.mrlongg71.ps09103_assignment.model.objectclass.BillDetail;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Book;
import vn.mrlongg71.ps09103_assignment.presenter.statistical.PresenterStatistical;
import vn.mrlongg71.ps09103_assignment.view.statistical.IViewStatistical;

/**
 * A simple {@link Fragment} subclass.
 */
public class StaDayFragment extends Fragment implements IViewStatistical {
    private PresenterStatistical presenterStatistical;

    private TextView txtDayCurrent, txtThuDay, txtChiDay;
    private String dayCurrent = "";
    private Calendar calendar;
    private PieChart chartDay;
    private ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        presenterStatistical = new PresenterStatistical(this);
        presenterStatistical.getListBill(1,0);
//        presenterStatistical.initStatisticalDay();
        View view = inflater.inflate(R.layout.fragment_sta_day, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        txtDayCurrent = view.findViewById(R.id.txtDayCurrent);
        txtChiDay = view.findViewById(R.id.txtChiDay);
        txtThuDay = view.findViewById(R.id.txtThuDay);
        calendar = calendar.getInstance();
        progressDialog = new ProgressDialog(getActivity());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dayCurrent = simpleDateFormat.format(calendar.getTime());
        chartDay = view.findViewById(R.id.chartDay);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onStatisticalDay(List<Bill> billListDay, List<BillDetail> billDetailList, List<Book> bookList) {
        int totalThu = 0;
        int totalChi = 0;
        for (Bill bill : billListDay) {
            totalThu += bill.getTotalPrice();
        }
        for (Book book : bookList) {
            totalChi += book.getPrice();
        }
       android.icu.text.NumberFormat numberFormat = NumberFormat.getCurrencyInstance();

        txtDayCurrent.setText(dayCurrent);
        txtThuDay.setText(numberFormat.format(totalThu) +"");
        txtChiDay.setText(numberFormat.format(totalChi) + "");
        Chart.addDataSet(chartDay,totalThu,totalChi,1);

    }

    @Override
    public void onStatisticalYesterday(List<Bill> billListDay, List<BillDetail> billDetailList, List<Book> bookList) {

    }

    @Override
    public void onStatisticalMonth(List<Bill> billListDay, List<BillDetail> billDetailList, List<Book> bookList) {

    }

    @Override
    public void onStatisticalFailed() {
        Chart.addDataSet(chartDay,0,0,00);
        Dialog.DialogLoading(progressDialog,false);

    }
}
