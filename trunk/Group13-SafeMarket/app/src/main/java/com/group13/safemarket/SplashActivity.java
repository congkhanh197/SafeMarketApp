package com.group13.safemarket;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Khanh Tran-Cong on 5/8/2018.
 * Email: congkhanh197@gmail.com
 */
public class SplashActivity extends AppCompatActivity {
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		setTheme(R.style.AppTheme_NoActionBar_SplashLauncher);
		super.onCreate(savedInstanceState);
		startActivity(new Intent(SplashActivity.this, MainActivity.class));
		// TODO splashscreen
	}
}
