package com.group13.safemarket;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group13.safemarket.fragments.AboutUsFragment;
import com.group13.safemarket.fragments.BaseFragment;
import com.group13.safemarket.fragments.FeedbackFragment;
import com.group13.safemarket.fragments.HomePageFragment;
import com.group13.safemarket.fragments.MyAccountFragment;
import com.group13.safemarket.fragments.QueryFragment;
import com.group13.safemarket.models.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,  BaseFragment.BaseFragmentCallbacks{

	private static final String TAG = "MainActivity.java";
	// Signal
	private static final int RC_SIGN_IN = 123;
	private static final int REQUEST_INVITE = 1;
	public static final int REQ_CODE_SPEECH_INPUT = 100;
	// Constant string
	public static final String USER_ID = "user_id";
	public static final String LAST_QUERY = "last_query";
	public static final String TYPE_SCREEN = "type_screen";
	public static final String PRODUCT = "product";
	public static final String USER = "user";
	public static final String REVIEW = "review";
	public static final String FEEDBACK = "feedback";
	private static final int RC_CHOOSE_PHOTO = 101;

	private FirebaseAuth mFirebaseAuth;
	private FirebaseUser mFirebaseUser;
	private DatabaseReference mFirebaseDatabaseReference;
	private FirebaseAnalytics mFirebaseAnalytics;

	private String mUsername;
	private String mPhotoUrl;
	private String mUserMail;

	private ImageView userImageView;
	private TextView nameTextView;
	private TextView mailTextView;

	private DrawerLayout mDrawerLayout;
	private NavigationView navigationView;

	private HomePageFragment mHomePageFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(!isOnline()){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("No internet connection!");
			builder.setMessage("Please change your settings and try again.");
			builder.setCancelable(false);
			builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					finish();
				}
			});
			builder.setNegativeButton("Try again", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					//dialogInterface.dismiss();
					Intent intent = getBaseContext().getPackageManager().
						getLaunchIntentForPackage(getBaseContext().getPackageName());
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
			});
			AlertDialog alertDialog = builder.create();
			alertDialog.show();
		}else{
			mFirebaseAuth = FirebaseAuth.getInstance();
			mFirebaseUser = mFirebaseAuth.getCurrentUser();
			if (mFirebaseUser == null) {
				List<AuthUI.IdpConfig> providers = new ArrayList<>();
				providers.add(new AuthUI.IdpConfig.GoogleBuilder().build());
				providers.add(new AuthUI.IdpConfig.FacebookBuilder()
					.setPermissions(Collections.singletonList("user_photos"))
					.build());
				providers.add(new AuthUI.IdpConfig.TwitterBuilder().build());
				providers.add(new AuthUI.IdpConfig.EmailBuilder()
					.setRequireName(true)
					.setAllowNewAccounts(false)
					.build());
				providers.add(new AuthUI.IdpConfig.PhoneBuilder().build());
				startActivityForResult(
					AuthUI.getInstance()
						.createSignInIntentBuilder()
						.setAvailableProviders(providers)
						.setLogo(R.drawable.icon_transparent)
						.setTheme(R.style.GreenTheme)
						.setIsSmartLockEnabled(false)
						// TODO set smartlock
						.build(), RC_SIGN_IN
				);
				return;
			} else {
				FirebaseDatabase.getInstance().getReference().child(USER).child(mFirebaseUser.getUid())
					.addListenerForSingleValueEvent(new ValueEventListener() {
						@Override
						public void onDataChange(DataSnapshot dataSnapshot) {
							User user = dataSnapshot.getValue(User.class);
							mPhotoUrl = user.getPhotoUrl() != null ? mFirebaseUser.getPhotoUrl().toString() : "";
							if(mPhotoUrl != null){
								Glide.with(MainActivity.this)
									.load(mPhotoUrl)
									.into(userImageView);
							}
							mUsername = user.getName();
							nameTextView.setText(user.getName());
							mUserMail = user.getMail();
							mailTextView.setText(user.getMail());
						}
						@Override
						public void onCancelled(DatabaseError databaseError) {	}
					});
			}
			mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
			setContentView(R.layout.activity_main);

			bindView();

			mHomePageFragment = new HomePageFragment();
			getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.fragment_container, mHomePageFragment).addToBackStack(null).commit();

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode){
			case RC_SIGN_IN:{
				IdpResponse response = IdpResponse.fromResultIntent(data);
				if (resultCode == RESULT_OK) {
					mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
					mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
					mFirebaseDatabaseReference.child(USER).child(mFirebaseUser.getUid())
						.addListenerForSingleValueEvent(new ValueEventListener() {
						@Override
						public void onDataChange(DataSnapshot dataSnapshot) {
							if(dataSnapshot.getValue() == null){
								User user = new User(mFirebaseUser.getDisplayName(), mFirebaseUser.getEmail(),
									mFirebaseUser.getPhotoUrl() != null ? mFirebaseUser.getPhotoUrl().toString() : "",
									mFirebaseUser.getPhoneNumber()!= null? mFirebaseUser.getPhoneNumber(): "",
									"", "", "");
								mFirebaseDatabaseReference.child(USER).child(mFirebaseUser.getUid()).setValue(user);
							}
						}
						@Override
						public void onCancelled(DatabaseError databaseError) {	}
					});
					Log.d(TAG, "Login successful. Token: "+response.getIdpToken());
					finish();
					startActivity(getIntent());
				} else {
					if (response == null) {
						Log.e(TAG,"Sign in cancelled!");
						AlertDialog.Builder builder = new AlertDialog.Builder(this);
						builder.setTitle("Sign in cancelled");
						builder.setMessage("Do you want to exit?");
						builder.setCancelable(false);
						builder.setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								Intent intent = getBaseContext().getPackageManager().
									getLaunchIntentForPackage(getBaseContext().getPackageName());
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent);
							}
						});
						builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								//dialogInterface.dismiss();
								finish();
							}
						});
						AlertDialog alertDialog = builder.create();
						alertDialog.show();
						return;
					}
					if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
						Log.e(TAG, "No internet connection");
						return;
					}
					Log.e(TAG, "Sign-in error: ", response.getError());
				}
			}
				break;
			case REQUEST_INVITE:
				if (resultCode == RESULT_OK) {
					Bundle payload = new Bundle();
					payload.putString(FirebaseAnalytics.Param.VALUE, "sent");
					mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE,
						payload);
					// Check how many invitations were sent and log.
					String[] ids = AppInviteInvitation.getInvitationIds(resultCode,
						data);
					Log.d(TAG, "Invitations sent: " + ids.length);
				} else {
					Bundle payload = new Bundle();
					payload.putString(FirebaseAnalytics.Param.VALUE, "not sent");
					mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE,
						payload);
					// Sending failed or it was canceled, show failure message to
					// the user
					Log.d(TAG, "Failed to send invitation.");
				}
				break;
			default :
				break;
		}
	}

	@Override
	public void onBackPressed() {
		List fragments = getSupportFragmentManager().getFragments();
		Log.e(TAG, "Back Pressed, Num of fragments: " +fragments.size());
		BaseFragment currentFragment = (BaseFragment) fragments.get(fragments.size() - 1);
		if (!currentFragment.onActivityBackPress()) {
			super.onBackPressed();
		}

	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		mDrawerLayout.closeDrawer(GravityCompat.START);
		switch (item.getItemId()) {
			case R.id.nav_home:
				navigationView.setCheckedItem(R.id.nav_home);
				replaceFragment(mHomePageFragment);
				startActivity(getIntent());
				break;
			case R.id.nav_account:
				navigationView.setCheckedItem(R.id.nav_account);
				replaceFragment(new MyAccountFragment());
				break;
			case R.id.nav_product:
				navigationView.setCheckedItem(R.id.nav_product);
				Bundle bundle = new Bundle();
				bundle.putString(MainActivity.TYPE_SCREEN,"product");
				bundle.putString(MainActivity.LAST_QUERY,"");
				QueryFragment mQueryFragment = new QueryFragment();
				mQueryFragment.setArguments(bundle);
				replaceFragment(mQueryFragment);
				break;
//			case R.id.nav_bookmark:
//				navigationView.setCheckedItem(R.id.nav_bookmark);
//				break;
			// TODO bookmark.
//			case R.id.nav_setting:
//				navigationView.setCheckedItem(R.id.nav_setting);
//				break;
			case R.id.nav_feedback:
				navigationView.setCheckedItem(R.id.nav_feedback);
				replaceFragment(new FeedbackFragment());
				break;
			case R.id.nav_share:
				navigationView.setCheckedItem(R.id.nav_share);
				Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
					.setMessage(getString(R.string.invitation_message))
					.setCallToActionText(getString(R.string.invitation_cta))
					.build();
				startActivityForResult(intent, REQUEST_INVITE);
				break;
			case R.id.nav_about:
				navigationView.setCheckedItem(R.id.nav_about);
				replaceFragment(new AboutUsFragment());
				break;
			default:
				break;
		}
		return true;
	}

	private void bindView() {
		mDrawerLayout = findViewById(R.id.drawer_layout);

		navigationView = findViewById(R.id.nav_view);
		navigationView.setCheckedItem(R.id.nav_home);
		navigationView.setNavigationItemSelectedListener(this);
		View header = navigationView.getHeaderView(0);
		userImageView = header.findViewById(R.id.imageView);

		nameTextView = header.findViewById(R.id.tv_user_name);
		nameTextView.setText("");
		mailTextView = header.findViewById(R.id.tv_mail);
		mailTextView.setText("");
	}

	public boolean isOnline() {
		ConnectivityManager cm =
			(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();
	}

	public void replaceFragment(Fragment fragment) {
		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
	}

	@Override
	public void onAttachSearchViewToDrawer(FloatingSearchView searchView) {
		searchView.attachNavigationDrawerToMenuButton(mDrawerLayout);
	}
}