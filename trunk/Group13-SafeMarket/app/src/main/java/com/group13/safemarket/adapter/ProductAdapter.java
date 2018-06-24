package com.group13.safemarket.adapter;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.Query;
import com.group13.safemarket.MainActivity;
import com.group13.safemarket.R;
import com.group13.safemarket.fragments.BaseFragment;
import com.group13.safemarket.fragments.ProductInformationFragment;
import com.group13.safemarket.fragments.QueryFragment;
import com.group13.safemarket.models.Product;

/**
 * Created by Khanh Tran-Cong on 5/11/2018.
 * Email: congkhanh197@gmail.com
 */
public class ProductAdapter extends
	RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

	private FirebaseArray mSnapshots;

	public ProductAdapter(Query ref) {
		mSnapshots = new FirebaseArray(ref);
		mSnapshots.setOnChangedListener(new FirebaseArray.OnChangedListener() {
			@Override
			public void onChanged(EventType type, int index, int oldIndex) {
			switch (type) {
				case Added:
					notifyItemInserted(index);
					break;
				case Removed:
					notifyItemRemoved(index);
					break;
				case Changed:
					notifyItemChanged(index);
					break;
				case Moved:
					notifyItemMoved(oldIndex, index);
					break;
				default:
					throw new IllegalStateException("Incomplete case statement");
			}
			}
		});
	}

	@Override
	public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
			.inflate(R.layout.home_horizontal_item, parent, false);
		return new ProductViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ProductViewHolder holder, int position) {
		final Product product = getItem(position);
		if(product!=null){
			Glide.with(holder.itemView)
				.load(Uri.parse(product.getPictureUrl()))
				.into(holder.mProfileImageView);
			holder.mTitleTextView.setText(product.getName());
			String price = product.getPrice()+"đ";
			holder.mPriceTextView.setText(price);
		}
	}

	public Product getItem(int position) {
		final Product product = mSnapshots.getItem(position).getValue(Product.class);
		String key = mSnapshots.getItem(position).getKey();
		product.setId(key);
		return product;
	}
	public void cleanup() {
		mSnapshots.cleanup();
	}

	@Override
	public int getItemCount() {
		return mSnapshots.getCount();
	}

	public class ProductViewHolder extends RecyclerView.ViewHolder {
		public ImageView mProfileImageView;
		public TextView mTitleTextView;
		public TextView mPriceTextView;

		public ProductViewHolder(View itemView) {
			super(itemView);
			mProfileImageView = itemView.findViewById(R.id.iv_thumb);
			mTitleTextView = itemView.findViewById(R.id.tv_title);
			mPriceTextView = itemView.findViewById(R.id.tv_price);
		}
	}
}

