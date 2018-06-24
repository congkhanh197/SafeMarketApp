package com.group13.safemarket.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.group13.safemarket.R;

/**
 * Created by Khanh Tran-Cong on 5/11/2018.
 * Email: congkhanh197@gmail.com
 */
public class SettingFragment extends BaseFragment {
	@Override
	public boolean onActivityBackPress() {
		return false;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_setting, container,false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}
}
