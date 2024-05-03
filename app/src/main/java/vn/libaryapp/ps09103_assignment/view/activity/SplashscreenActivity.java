package vn.libaryapp.ps09103_assignment.view.activity;

import android.content.Intent;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import vn.libaryapp.ps09103_assignment.R;
import vn.libaryapp.ps09103_assignment.view.login.LoginActivity;

public class SplashscreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        Animation animProgresBar = AnimationUtils.loadAnimation(this, R.anim.custom_progressbar);
        findViewById(R.id.progress_barSplash).startAnimation(animProgresBar);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashscreenActivity.this, LoginActivity.class));
                finish();
            }
        }, 3000);

    }





}
