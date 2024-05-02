package vn.mrlongg71.ps09103_assignment.view.statistical.month;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class StaMonthFragment extends Fragment implements IViewStatistical {
    private TextView txtCurrentMonth,txtMonthThu,txtMonthChi;
    private PieChart chartMonth;
    private PresenterStatistical presenterStatistical;
    private Spinner spMonth;
    private List<String> arrMonth;
    private ArrayAdapter<String> arrayAdapter;
    private ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =
         inflater.inflate(R.layout.fragment_sta_month, container, false);
        initView(view);
        presenterStatistical.getListBill(2,0);
//        presenterStatistical.initStatisticalMonth(0);
        return view;
    }

    private void initView(View view) {
        txtCurrentMonth = view.findViewById(R.id.txtMonthCurrent);
        txtMonthChi = view.findViewById(R.id.txtChiMonth);
        txtMonthThu = view.findViewById(R.id.txtThuMonth);
        chartMonth = view.findViewById(R.id.chartMonth);
        progressDialog = new ProgressDialog(getActivity());
        presenterStatistical = new PresenterStatistical(this);
        spMonth = view.findViewById(R.id.spMonth);
        arrMonth = new ArrayList<>();
        for(int i = 1; i <13; i++){
            arrMonth.add("Month-"+i);
        }
        arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,arrMonth);
        spMonth.setAdapter(arrayAdapter);
        spMonth.setSelection(9);
        spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] monthStr = arrMonth.get(position).split("-");
                int month = Integer.parseInt(monthStr[1]);
                   presenterStatistical.initStatisticalMonth(month);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public void onStatisticalDay(List<Bill> billListDay, List<BillDetail> billDetailList, List<Book> bookList) {

    }

    @Override
    public void onStatisticalYesterday(List<Bill> billListDay, List<BillDetail> billDetailList, List<Book> bookList) {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onStatisticalMonth(List<Bill> billListDay, List<BillDetail> billDetailList, List<Book> bookList) {
        int totalThu = 0;
        int totalChi = 0;
        for (Bill bill : billListDay) {
            totalThu += bill.getTotalPrice();
        }
        for (Book book : bookList) {
            totalChi += book.getPrice();
        }
        android.icu.text.NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM");
        txtCurrentMonth.setText("Month "+simpleDateFormat.format(yesterday())+"");
        txtMonthThu.setText(numberFormat.format(totalThu) +"");
        txtMonthChi.setText(numberFormat.format(totalChi) + "");
        Chart.addDataSet(chartMonth,totalThu,totalChi,2);
        Dialog.DialogLoading(progressDialog,false);

    }

    @Override
    public void onStatisticalFailed() {
        Chart.addDataSet(chartMonth,0,0,00);
        Dialog.DialogLoading(progressDialog,false);

    }

    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }
}
