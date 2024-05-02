package vn.mrlongg71.ps09103_assignment.model.login;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import vn.mrlongg71.ps09103_assignment.presenter.login.PresenterLogin;

public class ModelLogin {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    public void loginWithEmail(final String email, String pass, final PresenterLogin presenterLogin){
        auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    presenterLogin.loginStatus(true);
                }else{
                    presenterLogin.loginStatus(false);
                }
            }
        });

    }

    public void initResetPassword(String email, final PresenterLogin presenterLogin){
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            presenterLogin.resultResetPassword(true,"");
                        }else {
                            presenterLogin.resultResetPassword(false,"Error");
                        }
                    }
                });
    }
    public void checkEmailExits(final String email, final PresenterLogin presenterLogin){
        auth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
               if(task.getResult().getSignInMethods().size() ==0){
                   presenterLogin.resultResetPassword(false,"Email not exits!");
               }else {
                   initResetPassword(email,presenterLogin);
               }
            }
        });

    }
}
