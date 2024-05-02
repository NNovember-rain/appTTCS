package vn.mrlongg71.ps09103_assignment.library;

import android.view.KeyEvent;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import vn.mrlongg71.ps09103_assignment.R;
import vn.mrlongg71.ps09103_assignment.view.statistical.yesterday.StaYesterdayFragment;

public class CallBackFragment {
    public static void Callback(View view, final FragmentManager fragmentManager){
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        StaYesterdayFragment fragment = new StaYesterdayFragment();
                        fragmentTransaction.replace(R.id.fram, fragment, fragment.getTag()).commit();

                        return true;
                    }
                }
                return false;
            }
        });
    }
}
