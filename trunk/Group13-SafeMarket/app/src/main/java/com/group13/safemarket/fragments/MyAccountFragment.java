package com.group13.safemarket.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group13.safemarket.MainActivity;
import com.group13.safemarket.R;
import com.group13.safemarket.models.User;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Khanh Tran-Cong on 5/11/2018.
 * Email: congkhanh197@gmail.com
 */
public class MyAccountFragment extends BaseFragment {
	private static String TAG = "MyAccountFragment";

	private CircleImageView mImageView;
	private TextView mNameTextView, mEmailTextView, mAddressTextView, mPhoneTextView, mCardTextView;
	private Button mSignOutButton, mDeleteButton, mBackButton;
	private FloatingActionButton mFABModify;
	private RatingBar mRatingBar;
	@Override
	public boolean onActivityBackPress() {
		return false;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_my_account,container,false);
	}

	@Override
	public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

		DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().
			child(MainActivity.USER).child(user.getUid());

		mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				//Log.e(TAG, dataSnapshot.getValue(User.class).getName());
				User user = dataSnapshot.getValue(User.class);
				mImageView = view.findViewById(R.id.im_profile);
				if(user.getPhotoUrl()!= null)
					Glide.with(view)
						.load(user.getPhotoUrl())
						.into(mImageView);
				mNameTextView = view.findViewById(R.id.et_name);
				mNameTextView.setText(user.getName());
				mEmailTextView = view.findViewById(R.id.et_email);
				mEmailTextView.setText(user.getMail());
				mPhoneTextView = view.findViewById(R.id.et_phone);
				mPhoneTextView.setText(user.getPhone());
				mCardTextView = view.findViewById(R.id.et_card);
				mCardTextView.setText(user.getIdentifyCard());
				mAddressTextView = view.findViewById(R.id.et_address);
				mAddressTextView.setText(user.getAddress());
				mRatingBar = view.findViewById(R.id.rating_bar);
				mRatingBar.setIsIndicator(false);
				mRatingBar.setRating(Float.parseFloat(user.getReviewStar()));
			}
			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});

		mSignOutButton = view.findViewById(R.id.bt_sign_out);
		mSignOutButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.e(TAG, "Logout");
				AuthUI.getInstance()
					.signOut(getContext())
					.addOnCompleteListener(new OnCompleteListener<Void>() {
						public void onComplete(@NonNull Task<Void> task) {
							if (task.isSuccessful()) {
								Toast.makeText(getContext(),"Sign out successful! Please sign in to use!", Toast.LENGTH_LONG).show();
								Intent i = getActivity().getBaseContext().getPackageManager()
									.getLaunchIntentForPackage(  getActivity().getBaseContext().getPackageName() );
								i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(i);
							} else {
								Toast.makeText(getContext(),"Sign out failed!", Toast.LENGTH_LONG).show();
							}
						}
					});
				}
		});
		mDeleteButton = view.findViewById(R.id.bt_delete_account);
		mDeleteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			AuthUI.getInstance()
				.delete(getContext())
				.addOnCompleteListener(new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
					if (task.isSuccessful()) {
						Toast.makeText(getContext(),"Delete successful!", Toast.LENGTH_LONG).show();
						// TODO delete all product and data of user;
						Intent i = getActivity().getBaseContext().getPackageManager()
							.getLaunchIntentForPackage(  getActivity().getBaseContext().getPackageName() );
						i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(i);
					} else {
						Toast.makeText(getContext(),"Delete failed!", Toast.LENGTH_LONG).show();
					}
					}
				});
			}
		});

		mBackButton = view.findViewById(R.id.btn_back);
		mBackButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});

		mFABModify = view.findViewById(R.id.fab);
		mFABModify.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				replaceFragment(new ModifyAccountFragment());
			}
		});
	}
}
