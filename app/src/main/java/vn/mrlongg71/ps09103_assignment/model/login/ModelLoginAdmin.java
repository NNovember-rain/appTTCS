package vn.mrlongg71.ps09103_assignment.model.login;

import android.app.Activity;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import vn.mrlongg71.ps09103_assignment.R;
import vn.mrlongg71.ps09103_assignment.model.objectclass.User;
import vn.mrlongg71.ps09103_assignment.presenter.login.PresenterLoginAdmin;

public class ModelLoginAdmin {
    boolean REQUES_CODE_SMS = false;
    String mVerificationId;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    public void doSendSMS(String phone, TextView txtResend, Activity activity, PresenterLoginAdmin presenterLoginAdmin) {
            sentCodeSMS(phone,txtResend,activity,presenterLoginAdmin);
    }
    public void sentCodeSMS(String phone, final TextView txtResend, final Activity activity, final PresenterLoginAdmin presenterLoginAdmin) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+84" + phone,
                60,
                TimeUnit.SECONDS,
                activity,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential, activity,presenterLoginAdmin);

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        REQUES_CODE_SMS = false;
                        if (e instanceof FirebaseTooManyRequestsException) {
                            presenterLoginAdmin.resultSendOTP(false,activity.getString(R.string.number_of_allowed_times));
                        }
                    }

                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken token) {
                        REQUES_CODE_SMS = true;
                        mVerificationId = verificationId;

                        mResendCode(txtResend, activity);
                    }
                }
        );
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, final Activity activity, final PresenterLoginAdmin presenterLoginAdmin) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String key = auth.getUid();
                            User user = new User(key,"admin@gmail.com","Admin","0392350814","10/10/2019","user.png");
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                            databaseReference.child("User").child(key).setValue(user);
                            presenterLoginAdmin.resultVerifyOTP(true,"");
                        } else {
                            presenterLoginAdmin.resultVerifyOTP(false,task.getException().getMessage());
                        }
                    }
                });
    }

    public void mResendCode(final TextView txtResend, final Activity activity) {
        new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                txtResend.setText(String.valueOf(millisUntilFinished / 1000));
            }
            @Override
            public void onFinish() {
                txtResend.setText("Didn't get OTP ? Resend?");
            }
        }.start();
    }

    public void initCheckVerifyOTP(String verify, Activity activity,PresenterLoginAdmin presenterLoginAdmin){
        if(REQUES_CODE_SMS){
            PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(mVerificationId,verify);
            signInWithPhoneAuthCredential(phoneAuthCredential,activity,presenterLoginAdmin);
        }
    }
    
}
