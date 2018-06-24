package com.group13.safemarket.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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
public class ModifyAccountFragment extends BaseFragment {

	private EditText mNameET, mEmailET, mAddressET, mPhoneET, mCardET;
	private Button mBackBT, mChangeBT, mDeleteBT;
	private CircleImageView mImageView;
	@Override
	public boolean onActivityBackPress() {
		return false;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_modify_account,container,false);
	}

	@Override
	public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		FirebaseDatabase.getInstance().getReference().child(MainActivity.USER).child(FirebaseAuth.getInstance().getUid())
			.addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot dataSnapshot) {
					User user = dataSnapshot.getValue(User.class);
					mImageView = view.findViewById(R.id.im_profile);
					if(user.getPhotoUrl()!= null)
						Glide.with(view)
							.load(user.getPhotoUrl())
							.into(mImageView);
					mNameET = view.findViewById(R.id.et_name);
					mNameET.setText(user.getName());
					mEmailET = view.findViewById(R.id.et_email);
					mEmailET.setText(user.getMail());
					mAddressET = view.findViewById(R.id.et_address);
					mAddressET.setText(user.getAddress());
					mPhoneET = view.findViewById(R.id.et_phone);
					mPhoneET.setText(user.getPhone());
					mCardET = view.findViewById(R.id.et_card);
					mCardET.setText(user.getIdentifyCard());
				}
				@Override
				public void onCancelled(DatabaseError databaseError) {	}
			});
		mBackBT = view.findViewById(R.id.btn_back);
		mBackBT.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		mChangeBT = view.findViewById(R.id.bt_change);
		mChangeBT.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FirebaseDatabase.getInstance().getReference().child(MainActivity.USER).child(FirebaseAuth.getInstance().getUid())
					.addListenerForSingleValueEvent(new ValueEventListener() {
						@Override
						public void onDataChange(DataSnapshot dataSnapshot) {
							User user = dataSnapshot.getValue(User.class);
							FirebaseDatabase.getInstance().getReference().child(MainActivity.USER)
								.child(FirebaseAuth.getInstance().getUid()).setValue(new User(
								mNameET.getText().toString(),
								mEmailET.getText().toString(),
								user.getPhotoUrl(),
								mPhoneET.getText().toString(),
								mCardET.getText().toString(),
								mAddressET.getText().toString(), user.getReviewStar()
							));
							Toast.makeText(getContext(),"Change successfull!", Toast.LENGTH_SHORT).show();

						}
						@Override
						public void onCancelled(DatabaseError databaseError) {	}
					});
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		mDeleteBT = view.findViewById(R.id.bt_delete_account);
		mDeleteBT.setOnClickListener(new View.OnClickListener() {
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
	}
}
