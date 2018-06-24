package com.group13.safemarket.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group13.safemarket.MainActivity;
import com.group13.safemarket.R;
import com.group13.safemarket.models.Review;
import com.group13.safemarket.models.User;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Khanh Tran-Cong on 5/14/2018.
 * Email: congkhanh197@gmail.com
 */
public class AccountFragment extends BaseFragment {
	private static String TAG = "AccountFragment";
	private String mUserID;
	private CircleImageView mProfileImage;
	private TextView mNameTextView, mEmailTextView, mAddressTextView, mIdetifyCardTextView;
	private Button mButtonCall;
	private RatingBar mRatingBar;
	@Override
	public boolean onActivityBackPress() {
		return false;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mUserID = getArguments().getString(MainActivity.USER_ID);
		return inflater.inflate(R.layout.fragment_account, container,false);
	}

	@Override
	public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mProfileImage = view.findViewById(R.id.im_profile);
		mNameTextView = view.findViewById(R.id.et_name);
		mEmailTextView = view.findViewById(R.id.et_email);
		mAddressTextView = view.findViewById(R.id.et_address);
		mIdetifyCardTextView = view.findViewById(R.id.et_card);
		mButtonCall = view.findViewById(R.id.bt_call);


		DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().
			child(MainActivity.USER).child(mUserID);
		mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				final User user = dataSnapshot.getValue(User.class);
				if(!user.getPhotoUrl().equals(""))
					Glide.with(view).load(user.getPhotoUrl()).into(mProfileImage);
				mNameTextView.setText(user.getName());
				mEmailTextView.setText(user.getMail());
				mAddressTextView.setText(user.getAddress());
				mIdetifyCardTextView.setText(user.getIdentifyCard());
				if(!user.getPhone().equals("")){
					mButtonCall.setText("Call to "+user.getPhone());
					mButtonCall.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent callIntent = new Intent(Intent.ACTION_CALL);
							callIntent.setData(Uri.parse("tel:"+user.getPhone()));
							startActivity(callIntent);
						}
					});
				} else mButtonCall.setVisibility(View.GONE);
				mRatingBar.setRating(Float.parseFloat(user.getReviewStar()));
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});
		mRatingBar = view.findViewById(R.id.rb_rating);
		mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				if(fromUser){
					FirebaseDatabase.getInstance().getReference().child(MainActivity.REVIEW).push()
						.setValue(new Review(
							FirebaseAuth.getInstance().getUid(),
							mUserID,
							String.valueOf(rating)
						));
				}
			}
		});

	}
}
