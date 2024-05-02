package vn.mrlongg71.ps09103_assignment.library;

import android.view.KeyEvent;
import android.view.View;

import androidx.fragment.app.FragmentManager;

public class PopBack {
    public static void callBack(View view, final FragmentManager fragmentManager){
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        fragmentManager.popBackStack();
                        return true;
                    }

                }
                return false;
            }
        });
    }
}

