package vn.mrlongg71.ps09103_assignment.presenter.bill;

import android.widget.TextView;

public interface IPresenterAddBillAdapter {
    void initReduction(int i, TextView txtReduction, TextView txtAmountBill,TextView txtNoAmount,TextView txtIncreasing);
    void initIncreasing(int i,TextView txtIncreasing,TextView txtAmountBill,TextView txtNoAmount,TextView txtReduction);
}
