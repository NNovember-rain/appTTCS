package vn.mrlongg71.ps09103_assignment.library;

import android.app.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import vn.mrlongg71.ps09103_assignment.R;

public class ActionBarLib {
    public static  void setSupportActionBar(Activity activity, String title){
        AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        actionBar.setTitle(title);

    }
}
