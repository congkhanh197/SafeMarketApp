package com.group13.safemarket.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.group13.safemarket.R;

/**
 * Created by Khanh Tran-Cong on 5/7/2018.
 * Email: congkhanh197@gmail.com
 */
public abstract class BaseFragment extends Fragment {

	private BaseFragmentCallbacks mCallbacks;

	public interface BaseFragmentCallbacks{
		void onAttachSearchViewToDrawer(FloatingSearchView searchView);
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof BaseFragmentCallbacks) {
			mCallbacks = (BaseFragmentCallbacks) context;
		} else {
			throw new RuntimeException(context.toString()
				+ " must implement BaseExampleFragmentCallbacks");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	protected void attachSearchViewActivityDrawer(FloatingSearchView searchView){
		if(mCallbacks != null){
			mCallbacks.onAttachSearchViewToDrawer(searchView);
		}
	}

	public abstract boolean onActivityBackPress();

	protected void addFragment(Fragment fragment) {
		getActivity().getSupportFragmentManager()
			.beginTransaction()
			.add(R.id.fragment_container, fragment).addToBackStack(null).commit();
	}

	protected void replaceFragment(Fragment fragment) {
		getActivity().getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
	}

	protected void removeFragment(Fragment fragment) {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		//Fragment fragment =  fm.findFragmentById(R.id.fragment_container);
		fm.beginTransaction().remove(fragment).commit();
	}
}
