package vn.mrlongg71.ps09103_assignment.library;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.developer.kalert.KAlertDialog;

import vn.mrlongg71.ps09103_assignment.R;

public class Dialog {
    public static void DialogExit(final Activity activity, String title, String context) {
        new KAlertDialog(activity, KAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText(context)
                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                    @Override
                    public void onClick(KAlertDialog kAlertDialog) {
                        activity.finish();
                        SharedPreferences sharedPreferences = activity.getSharedPreferences("manager", Context.MODE_PRIVATE);
                        sharedPreferences.edit().clear().commit();

                    }
                })
                .setCancelText("No")
                .cancelButtonColor(R.drawable.custom_button)
                .confirmButtonColor(R.drawable.custom_button)
                .setCancelClickListener(new KAlertDialog.KAlertClickListener() {
                    @Override
                    public void onClick(KAlertDialog kAlertDialog) {
                        kAlertDialog.dismissWithAnimation();
                    }
                })
                .show();

    }



    public static void DialogLoading(ProgressDialog progressDialog,boolean loading) {
        progressDialog.setMessage("Loading");
        if(loading){
            progressDialog.show();
        }else {
            progressDialog.dismiss();
        }
    }


}
