package vn.mrlongg71.ps09103_assignment.view.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.developer.kalert.KAlertDialog;
import com.google.android.material.textfield.TextInputEditText;

import es.dmoral.toasty.Toasty;
import vn.mrlongg71.ps09103_assignment.R;
import vn.mrlongg71.ps09103_assignment.library.Dialog;
import vn.mrlongg71.ps09103_assignment.presenter.login.PresenterLoginAdmin;
import vn.mrlongg71.ps09103_assignment.view.activity.HomeActivity;

public class LoginAdminActivity extends AppCompatActivity implements TextWatcher, IViewLoginAdmin {

    private TextInputEditText edtPhoneAdmin, edtOTP1, edtOTP2, edtOTP3, edtOTP4, edtOTP5, edtOTP6;
    private TextView txtResend;
    private Button btnNextSendSMS;
    private String error = "";
    private String phoneInput = "";
    private LinearLayout layoutVerifyPhone, layoutPhoneNumber;
    private PresenterLoginAdmin presenterLoginAdmin;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);
        initView();
    }

    private void initView() {
        btnNextSendSMS = findViewById(R.id.btnNextSendSMS);
        edtPhoneAdmin = findViewById(R.id.edtPhoneAdmin);
        layoutVerifyPhone = findViewById(R.id.layoutVerifyPhone);
        layoutPhoneNumber = findViewById(R.id.layoutPhoneNumber);
        progressDialog = new ProgressDialog(LoginAdminActivity.this);
        txtResend = findViewById(R.id.txtResend);
        edtOTP1 = findViewById(R.id.edtOTP1);
        edtOTP2 = findViewById(R.id.edtOTP2);
        edtOTP3 = findViewById(R.id.edtOTP3);
        edtOTP4 = findViewById(R.id.edtOTP4);
        edtOTP5 = findViewById(R.id.edtOTP5);
        edtOTP6 = findViewById(R.id.edtOTP6);

        edtPhoneAdmin.addTextChangedListener(this);
        presenterLoginAdmin = new PresenterLoginAdmin(this);
    }

    private void initEvent(String phone) {
        if (isValidMobile(phone)) {
            edtPhoneAdmin.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_phone, 0, R.drawable.ic_arrow_forward_black_24dp, 0);
            initEventClickPhone();
        } else {
            btnNextSendSMS.setVisibility(View.GONE);
            Toasty.error(getApplicationContext(), error, Toasty.LENGTH_LONG).show();
            edtPhoneAdmin.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_phone, 0, 0, 0);
        }


    }

    private void initEventClickPhone() {
        btnNextSendSMS.setVisibility(View.VISIBLE);
        btnNextSendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutPhoneNumber.setVisibility(View.GONE);
                layoutVerifyPhone.setVisibility(View.VISIBLE);
                if(edtPhoneAdmin.getText().toString().trim().length() == 10){
                    presenterLoginAdmin.getPhoneNumber("0392350814", txtResend, LoginAdminActivity.this);
                    lisenerOTP();
                }else {
                    error = "Phone nummber 10";
                    Toasty.error(getApplicationContext(), error, Toasty.LENGTH_LONG).show();
                }



            }
        });

    }

    private void lisenerOTP() {
        TextWatcher textWatcherOTP = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 1) {
                    initOTP();
                }
            }
        };
        edtOTP1.addTextChangedListener(textWatcherOTP);
        edtOTP2.addTextChangedListener(textWatcherOTP);
        edtOTP3.addTextChangedListener(textWatcherOTP);
        edtOTP4.addTextChangedListener(textWatcherOTP);
        edtOTP5.addTextChangedListener(textWatcherOTP);
        edtOTP6.addTextChangedListener(textWatcherOTP);
    }

    private void initOTP() {
        String OTP1 = edtOTP1.getText().toString().trim();
        String OTP2 = edtOTP2.getText().toString().trim();
        String OTP3 = edtOTP3.getText().toString().trim();
        String OTP4 = edtOTP4.getText().toString().trim();
        String OTP5 = edtOTP5.getText().toString().trim();
        String OTP6 = edtOTP6.getText().toString().trim();

        String OTP = OTP1 + OTP2 + OTP3 + OTP4 + OTP5 + OTP6;

        if (OTP.length() == 6) {
            Dialog.DialogLoading(progressDialog,true);
            presenterLoginAdmin.getOTPCode(OTP, LoginAdminActivity.this);
        }

    }

    private boolean isValidMobile(String phone) {
        boolean check = false;
        if (phone.equals("0392350814")) {
            check = true;
        } else {
            check = false;
            error = "You are not the admin";
        }

        return check;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        phoneInput = s.toString();
        if(phoneInput.length() == 10){
            initEvent(phoneInput);
        }

    }

    @Override
    public void onSendSuccess() {
        Toasty.success(getApplicationContext(), getString(R.string.success), Toasty.LENGTH_LONG).show();
    }

    @Override
    public void onSendFaile(String error) {
        Toasty.error(getApplicationContext(), error, Toasty.LENGTH_LONG).show();
    }

    @Override
    public void onVerifyOTPSuccess() {
        Dialog.DialogLoading(progressDialog,false);
        new KAlertDialog(LoginAdminActivity.this,KAlertDialog.SUCCESS_TYPE)
                .setTitleText("Verify Success!!")
                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                    @Override
                    public void onClick(KAlertDialog kAlertDialog) {
                        SharedPreferences sharedPreferences = getSharedPreferences("manager", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("admin",true);
                        editor.commit();
                        startActivity(new Intent(LoginAdminActivity.this, HomeActivity.class));
                        finish();
                    }
                })
                .show();

    }

    @Override
    public void onVerifyOTPFaile(String error) {
       Toasty.error(getApplicationContext(),error,Toasty.LENGTH_LONG).show();
    }
}
