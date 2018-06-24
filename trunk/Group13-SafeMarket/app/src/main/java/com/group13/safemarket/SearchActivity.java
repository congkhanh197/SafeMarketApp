package com.group13.safemarket;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.google.firebase.auth.FirebaseUser;
import com.group13.safemarket.fragments.BaseFragment;

import java.util.List;

/**
 * Created by Khanh Tran-Cong on 5/8/2018.
 * Email: congkhanh197@gmail.com
 */
public class SearchActivity extends AppCompatActivity implements BaseFragment.BaseFragmentCallbacks, NavigationView.OnNavigationItemSelectedListener {

	private final String TAG = "MainActivity";

	private DrawerLayout mDrawerLayout;

	private String mProfileUrl;
	private FirebaseUser mFirebaseUser;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_search);
//		Intent intent = getIntent();
//		mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//		mProfileUrl = intent.getStringExtra(MainActivity.PROFILE_URL);
//
//		mDrawerLayout = findViewById(R.id.drawer_layout_search);
//		NavigationView navigationView =  findViewById(R.id.nav_view_search);
//		navigationView.setNavigationItemSelectedListener(this);
//		View header = navigationView.getHeaderView(0);
//
//		ImageView imageView = header.findViewById(R.id.imageView);
//		Glide.with(this)
//			.load(mProfileUrl)
//			.into(imageView);
//		TextView nameTextView = header.findViewById(R.id.tv_user_name);
//		nameTextView.setText(mFirebaseUser.getDisplayName());
//		TextView mailTextView = header.findViewById(R.id.tv_mail);
//		mailTextView.setText(mFirebaseUser.getEmail());
//
//
//		replaceFragment(new QueryFragment());
	}

	@Override
	public void onAttachSearchViewToDrawer(FloatingSearchView searchView) {
		searchView.attachNavigationDrawerToMenuButton(mDrawerLayout);
	}

	@Override
	public void onBackPressed() {
		List fragments = getSupportFragmentManager().getFragments();
		BaseFragment currentFragment = (BaseFragment) fragments.get(fragments.size() - 1);

		if (!currentFragment.onActivityBackPress()) {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onNavigationItemSelected(MenuItem menuItem) {
		mDrawerLayout.closeDrawer(GravityCompat.START);
		switch (menuItem.getItemId()) {
			case R.id.nav_home:
//				startActivity(new Intent(SearchActivity.this, MainActivity.class));
//				finish();
				break;
			case R.id.nav_account:
//				Intent intent = new Intent(SearchActivity.this, AccountActivity.class);
//				startActivity(intent);
//				finish();
				break;
			case R.id.nav_product:
				break;
//			case R.id.nav_bookmark:
//				break;
//			case R.id.nav_setting:
//				break;
			case R.id.nav_feedback:
				break;
			case R.id.nav_share:
				break;
			case R.id.nav_about:
				break;
			default:
				break;
		}
		return true;
	}

	private void showFragment(Fragment fragment) {
		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.fragment_container, fragment).commit();
	}
}
