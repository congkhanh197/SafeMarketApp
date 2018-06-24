package com.group13.safemarket.adapter;


import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.Query;
import com.group13.safemarket.models.Product;
import com.group13.safemarket.R;

/**
 * Created by Khanh Tran-Cong on 5/3/2018.
 * Email: congkhanh197@gmail.com
 */
public class QueryAdapter extends RecyclerView.Adapter<QueryAdapter.ProductViewHolder> {
	public static final String TAG = "QueryAdapter.java";
	private FirebaseArray mSnapshots;

	//DatabaseReference ref
	public QueryAdapter(Query ref){
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

	@NonNull
	@Override
	public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item,parent,false);
		return new ProductViewHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
		Product product = getItem(position);
		if(product != null){
			holder.itemView.setTag(position);
			holder.nameTextView.setText(product.getName());
			holder.addressTextView.setText(product.getAddress());
			holder.priceTextView.setText(product.getPrice());
			Glide.with(holder.itemView)
				.load(Uri.parse(product.getPictureUrl()))
				.into(holder.iconImageView);
		}


	}
	public void cleanup() {
		mSnapshots.cleanup();
	}

	public Product getItem(int position) {
		final Product product = mSnapshots.getItem(position).getValue(Product.class);
		String key = mSnapshots.getItem(position).getKey();
		product.setId(key);
		return product;

	}

	@Override
	public int getItemCount() {
		return mSnapshots.getCount();
	}

	public class ProductViewHolder extends RecyclerView.ViewHolder {
		public TextView nameTextView, addressTextView, priceTextView;
		public ImageView iconImageView;
		public ProductViewHolder(View itemView) {
			super(itemView);
			nameTextView = itemView.findViewById(R.id.tv_name_product);
			addressTextView = itemView.findViewById(R.id.tv_address_product);
			priceTextView = itemView.findViewById(R.id.tv_price_product);
			iconImageView = itemView.findViewById(R.id.im_icon);
		}
	}
}
