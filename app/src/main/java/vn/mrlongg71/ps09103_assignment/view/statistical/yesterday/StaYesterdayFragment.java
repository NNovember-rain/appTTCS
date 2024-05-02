 package vn.mrlongg71.ps09103_assignment.view.statistical.yesterday;

import android.app.ProgressDialog;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import vn.mrlongg71.ps09103_assignment.R;
import vn.mrlongg71.ps09103_assignment.library.Chart;
import vn.mrlongg71.ps09103_assignment.library.Dialog;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Bill;
import vn.mrlongg71.ps09103_assignment.model.objectclass.BillDetail;
import vn.mrlongg71.ps09103_assignment.model.objectclass.Book;
import vn.mrlongg71.ps09103_assignment.presenter.statistical.PresenterStatistical;
import vn.mrlongg71.ps09103_assignment.view.statistical.IViewStatistical;

 public class StaYesterdayFragment extends Fragment implements IViewStatistical {
    private TextView txtYesCurrent,txtYesThu,txtYesChi;
    private PieChart chartYes;
    private PresenterStatistical presenterStatistical;
    private ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =
         inflater.inflate(R.layout.fragment_sta_yesterday, container, false);
        initView(view);
        Dialog.DialogLoading(progressDialog,true);

        presenterStatistical.getListBill(0,0);
        callBack(view);
        return view;
    }

    private void initView(View view) {
        txtYesCurrent = view.findViewById(R.id.txtYesCurrent);
        txtYesThu = view.findViewById(R.id.txtThuYes);
        txtYesChi = view.findViewById(R.id.txtChiYes);
        chartYes = view.findViewById(R.id.chartYes);
        presenterStatistical = new PresenterStatistical(this);
        progressDialog = new ProgressDialog(getActivity());
    }

     private void callBack(View view){
         view.setFocusableInTouchMode(true);
         view.requestFocus();
         view.setOnKeyListener(new View.OnKeyListener() {
             @Override
             public boolean onKey(View v, int keyCode, KeyEvent event) {
                 if (event.getAction() == KeyEvent.ACTION_DOWN) {
                     if (keyCode == KeyEvent.KEYCODE_BACK) {
                         Dialog.DialogExit(getActivity(),getString(R.string.wantExit),"");
                         return true;
                     }
                 }
                 return false;
             }
         });
     }

     @Override
     public void onStatisticalDay(List<Bill> billListDay, List<BillDetail> billDetailList, List<Book> bookList) {

     }

     @RequiresApi(api = Build.VERSION_CODES.N)
     @Override
     public void onStatisticalYesterday(List<Bill> billListDay, List<BillDetail> billDetailList, List<Book> bookList) {
        int totalThu = 0;
         int totalChi = 0;
         for (Bill bill : billListDay) {
             totalThu += bill.getTotalPrice();
         }
         for (Book book : bookList) {
             totalChi += book.getPrice();
         }
         android.icu.text.NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
         SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
         txtYesCurrent.setText(simpleDateFormat.format(yesterday())+"");
         txtYesThu.setText(numberFormat.format(totalThu) +"");
         txtYesChi.setText(numberFormat.format(totalChi) + "");
         Chart.addDataSet(chartYes,totalThu,totalChi,0);
         Dialog.DialogLoading(progressDialog,false);
     }

     @Override
     public void onStatisticalMonth(List<Bill> billListDay, List<BillDetail> billDetailList, List<Book> bookList) {

     }

     @Override
     public void onStatisticalFailed() {
         Chart.addDataSet(chartYes,0,0,00);
         Dialog.DialogLoading(progressDialog,false);

     }

     private Date yesterday() {
         final Calendar cal = Calendar.getInstance();
         cal.add(Calendar.DATE, -1);
         return cal.getTime();
     }
 }
