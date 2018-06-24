package com.group13.safemarket.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.Query;
import com.group13.safemarket.MainActivity;
import com.group13.safemarket.R;
import com.group13.safemarket.fragments.ProductInformationFragment;
import com.group13.safemarket.models.GroupProduct;
import com.group13.safemarket.models.Product;

import java.util.ArrayList;


/**
 * Created by Khanh Tran-Cong on 5/11/2018.
 * Email: congkhanh197@gmail.com
 */
public class GroupProductAdapter extends
	RecyclerView.Adapter<GroupProductAdapter.GroupProductViewHolder> {

	private Context mContext;
	private ArrayList<GroupProduct> mArrayList;

	public GroupProductAdapter(Context context, ArrayList<GroupProduct> mArrayList) {
		this.mContext = context;
		this.mArrayList = mArrayList;
	}

	@Override
	public GroupProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_vertical_item, parent, false);
		return new GroupProductViewHolder(view);
	}

	@Override
	public void onBindViewHolder(GroupProductViewHolder holder, int position) {
		final GroupProduct current = mArrayList.get(position);
		final String strTitle = current.getTitle();
		holder.tvTitle.setText(strTitle);

	}

	@Override
	public int getItemCount() {
		return mArrayList.size();
	}

	public GroupProduct getProductFromPosition(int position){
		return mArrayList.get(position);
	}

	public class GroupProductViewHolder extends RecyclerView.ViewHolder {
		public TextView tvTitle;
		public RecyclerView mListProduct;
		public Button mMoreButton;

		public GroupProductViewHolder(View itemView) {
			super(itemView);
			tvTitle = itemView.findViewById(R.id.tv_title);
			mListProduct = itemView.findViewById(R.id.rv_horizontal);
			mMoreButton = itemView.findViewById(R.id.bt_more);
		}
	}
}