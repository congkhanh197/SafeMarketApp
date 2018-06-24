package com.group13.safemarket.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group13.safemarket.MainActivity;
import com.group13.safemarket.R;
import com.group13.safemarket.models.Product;
import com.group13.safemarket.models.User;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Khanh Tran-Cong on 5/11/2018.
 * Email: congkhanh197@gmail.com
 */
public class ProductInformationFragment extends BaseFragment {

	private TextView mNameProductTV, mPriceTV, mAddressTV, mUserNameTV, mTypeTV, mDetailTV;
	private ImageView imageView;
	private CircleImageView circleImageView;
	private Button mConnectBT, mBackBT;
	private String mProductID;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mProductID = getArguments().getString(MainActivity.PRODUCT);
		return inflater.inflate(R.layout.fragment_product_information,container, false);
	}

	@Override
	public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mNameProductTV = view.findViewById(R.id.product_name);
		mPriceTV = view.findViewById(R.id.product_price);
		mAddressTV = view.findViewById(R.id.product_address);
		mUserNameTV = view.findViewById(R.id.product_user_name);
		mTypeTV = view.findViewById(R.id.product_type);
		mDetailTV = view.findViewById(R.id.product_detail);
		imageView = view.findViewById(R.id.product_image);
		circleImageView = view.findViewById(R.id.product_user_image);
		FirebaseDatabase.getInstance().getReference().child(MainActivity.PRODUCT)
			.child(mProductID).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				Product product = dataSnapshot.getValue(Product.class);
				Glide.with(view).load(product.getPictureUrl()).into(imageView);
				mNameProductTV.setText(product.getName());
				String price = product.getPrice()+"đ";
				mPriceTV.setText(price);
				mAddressTV.setText(product.getAddress());
				mTypeTV.setText(product.getType());
				mDetailTV.setText(product.getDetail());
				FirebaseDatabase.getInstance().getReference().child(MainActivity.USER)
					.child(product.getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot) {
						User user = dataSnapshot.getValue(User.class);
						Glide.with(view).load(user.getPhotoUrl()).into(circleImageView);
						mUserNameTV.setText(user.getName());
					}

					@Override
					public void onCancelled(DatabaseError databaseError) {}
				});
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});
		mConnectBT = view.findViewById(R.id.bt_connect);
		FirebaseDatabase.getInstance().getReference().child(MainActivity.PRODUCT)
			.child(mProductID).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				final Product product = dataSnapshot.getValue(Product.class);
				if(product.getUserID() == FirebaseAuth.getInstance().getUid()){
					mConnectBT.setVisibility(View.GONE);
				} else{
					mConnectBT.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Bundle bundle = new Bundle();
							bundle.putString(MainActivity.USER_ID,product.getUserID());
							AccountFragment accountFragment = new AccountFragment();
							accountFragment.setArguments(bundle);
							replaceFragment(accountFragment);
						}
					});
				}

			}
			@Override
			public void onCancelled(DatabaseError databaseError) {
			}
		});
		mBackBT = view.findViewById(R.id.btn_back);
		mBackBT.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		//mCallButton = view.findViewById(R.id.bt_call);
	}

	@Override
	public boolean onActivityBackPress() {

		return false;
	}
}
