package vn.mrlongg71.ps09103_assignment.view.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;

import es.dmoral.toasty.Toasty;
import vn.mrlongg71.ps09103_assignment.R;
import vn.mrlongg71.ps09103_assignment.library.Dialog;
import vn.mrlongg71.ps09103_assignment.model.objectclass.User;
import vn.mrlongg71.ps09103_assignment.presenter.login.PresenterLogin;
import vn.mrlongg71.ps09103_assignment.service.IViewConnect;
import vn.mrlongg71.ps09103_assignment.service.NetworkReceiver;
import vn.mrlongg71.ps09103_assignment.view.activity.HomeActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, IViewLogin, IViewConnect {

    private CallbackManager callbackManager;
    private Button btnLogin, btnLoginGoogle;
    private Button btnLoginFacebook, btnSendForgotPassword, btnCancelForgotPassword,btnLoginAdmin;
    private EditText edtEmail, edtPass,edtEmailGetPass;
    private TextView txtForgotPassword, txtEmailForgotPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient googleSignInClient;
    private LoginManager loginManager;
    private int RC_SIGN_IN = 123;
    private PresenterLogin presenterLogin;
    private BroadcastReceiver broadcastReceiver;
    String TAG = "bb";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        initView();

    }

    @SuppressLint("WrongViewCast")
    private void initView() {
        btnLogin = findViewById(R.id.btnLogin);
        btnLoginFacebook = findViewById(R.id.btnLoginFacebook);
        btnLoginGoogle = findViewById(R.id.btnLoginGoogle);
        btnLoginAdmin = findViewById(R.id.btnLoginAdmin);
        edtEmail = findViewById(R.id.edtEmail);
        edtPass = findViewById(R.id.edtPass);
        txtForgotPassword = findViewById(R.id.txtForgotpassword);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(LoginActivity.this);
        edtEmail.setText("abc456@gmail.com");
        edtPass.setText("123456");
        presenterLogin = new PresenterLogin(LoginActivity.this);
    }

    private void initEvent() {
            btnLogin.setOnClickListener(this);
            btnLoginGoogle.setOnClickListener(this);
            btnLoginFacebook.setOnClickListener(this);
            btnLoginAdmin.setOnClickListener(this);
            txtForgotPassword.setOnClickListener(this);



    }

    @Override
    protected void onResume() {
        super.onResume();
        broadcastReceiver = new NetworkReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiver,intentFilter);
        broadcastReceiver.onReceive(LoginActivity.this,new Intent(LoginActivity.this,NetworkReceiver.class));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver !=null){
            unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NetworkReceiver.EventConnect event) {
        if(event.isConnect() == true){
            initEvent();
        }
    };


    private void setupGoogle() {
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), googleSignInOptions);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                    String email = edtEmail.getText().toString().trim();
                    String password = edtPass.getText().toString().trim();
                    doLoginUser(email, password);
                break;
            case R.id.txtForgotpassword:
                initForgotpassword();
                break;
            case R.id.btnLoginFacebook:
                signInFacebook();
                break;
            case R.id.btnLoginGoogle:
                signInGoogle();
                break;
            case R.id.btnLoginAdmin:
                signInAdmin();
                break;
        }
    }

    private void signInAdmin() {
        startActivity(new Intent(LoginActivity.this, LoginAdminActivity.class));
    }

    private void initForgotpassword() {
        final android.app.Dialog dialogForgotpassword = new android.app.Dialog(this);
        dialogForgotpassword.setContentView(R.layout.custom_dialog_getpass);
        dialogForgotpassword.getWindow().setBackgroundDrawableResource(R.color.float_transparent);
        edtEmailGetPass = dialogForgotpassword.findViewById(R.id.edtEmailGetPass);
        txtEmailForgotPassword = dialogForgotpassword.findViewById(R.id.edtEmailGetPass);
        btnSendForgotPassword = dialogForgotpassword.findViewById(R.id.btnSendForgotPassword);
        btnCancelForgotPassword = dialogForgotpassword.findViewById(R.id.btnCancelForgotPassword);
        btnSendForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenterLogin.getEmailResetPassword(edtEmailGetPass.getText().toString());
                dialogForgotpassword.dismiss();
            }
        });
        btnCancelForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogForgotpassword.dismiss();
            }
        });
        dialogForgotpassword.show();

    }

    private void signInGoogle() {
            Dialog.DialogLoading(progressDialog, true);
            setupGoogle();
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signInFacebook() {
        loginManager = LoginManager.getInstance();
        List<String> permissionFacebook = Arrays.asList("email", "public_profile");
        loginManager.logInWithReadPermissions(this, permissionFacebook);
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                firebaseAuthWithFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void doLoginUser(String email, String password) {
        if (initCheckVailied(email, password)) {
            Dialog.DialogLoading(progressDialog, true);
            presenterLogin.getEmailandPass(email, password);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("bbb", "Google sign in failed", e);
                // ...
            }
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("bbbG", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("bbb", "signInWithCredential:success");
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            User user = new User(firebaseUser.getUid(), firebaseUser.getEmail(), firebaseUser.getDisplayName(), "", "", "user.png");
                            createUserLoginOther(user);
                            onSuccess();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("bbbb", "signInWithCredential:failure", task.getException());

                        }

                    }
                });
    }

    private void firebaseAuthWithFacebook(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            User user = new User(firebaseUser.getUid(), firebaseUser.getEmail(), firebaseUser.getDisplayName(), "", "", "user.png");
                            createUserLoginOther(user);
                            onSuccess();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                        }

                        // ...
                    }
                });
    }

    private void createUserLoginOther(User user) {
        Log.d("bbb", "creatwe");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        databaseReference.child(user.getKey()).setValue(user);
    }

    private boolean initCheckVailied(String email, String password) {
        boolean check = false;
        if (email.length() > 0 || password.length() > 0) {
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                check = true;
            } else {
                check = false;
                edtEmail.setError(getString(R.string.email_invalid));
            }
            if (password.length() < 6) {
                check = false;
                edtPass.setError(getString(R.string.weak_password));
            }

        } else {
            check = false;
            edtEmail.setError(getString(R.string.youhavenotinput));
            edtPass.setError(getString(R.string.youhavenotinput));
        }
        return check;
    }


    @Override
    public void onSuccess() {
        Dialog.DialogLoading(progressDialog, false);
        Toasty.success(LoginActivity.this, getString(R.string.success), Toast.LENGTH_SHORT, true).show();
        SharedPreferences sharedPreferences = getSharedPreferences("manager", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("admin",false);
        editor.putString("id", firebaseAuth.getCurrentUser().getUid());

        editor.commit();
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
    }


    @Override
    public void onFailed() {
        Dialog.DialogLoading(progressDialog, false);
        Toasty.error(LoginActivity.this, getString(R.string.failure), Toasty.LENGTH_LONG, true).show();
    }

    @Override
    public void onResetPasswordSuccess() {
        Toasty.success(getApplicationContext(), getString(R.string.success), Toasty.LENGTH_LONG).show();
    }

    @Override
    public void onResetPasswordFailed(String error) {
        Toasty.error(getApplicationContext(), error, Toasty.LENGTH_LONG).show();

    }

    @Override
    public void connected() {

    }
}
