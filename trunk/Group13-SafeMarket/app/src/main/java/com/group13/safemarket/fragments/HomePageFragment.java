package com.group13.safemarket.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.group13.safemarket.MainActivity;
import com.group13.safemarket.R;
import com.group13.safemarket.adapter.GroupProductAdapter;
import com.group13.safemarket.adapter.ProductAdapter;
import com.group13.safemarket.models.GroupProduct;
import com.group13.safemarket.models.Product;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Khanh Tran-Cong on 5/9/2018.
 * Email: congkhanh197@gmail.com
 */
public class HomePageFragment extends BaseFragment {
	private static final String TAG = "HomeFragment";

	private ProgressBar mProgressBar;

	private GroupProductAdapter mGroupProductAdapter;
	private RecyclerView mHomePageList;
	private LinearLayoutManager mLinearLayoutManager;

	private DatabaseReference mFirebaseDatabaseReference;

	private FloatingSearchView mSearchView;

	private String mLastQuery;

	public HomePageFragment(){	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_home_page, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mProgressBar = view.findViewById(R.id.progress_bar);

		mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

		mHomePageList = view.findViewById(R.id.home_product_list);
		mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
		mHomePageList.setLayoutManager(mLinearLayoutManager);

		String[] product_type = getResources().getStringArray(R.array.product_type);
		ArrayList<GroupProduct> type = new ArrayList<>();
		for(String tittle : product_type){
			type.add(createRef(tittle));
		}
		mGroupProductAdapter = new GroupProductAdapter(getContext(),type){
			@Override
			public void onBindViewHolder(GroupProductViewHolder holder, final int position) {
				super.onBindViewHolder(holder, position);
				final GroupProduct current = getProductFromPosition(position);
				holder.mMoreButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Bundle bundle = new Bundle();
						bundle.putString(MainActivity.LAST_QUERY, current.getTitle());
						bundle.putString(MainActivity.TYPE_SCREEN, "group");
						QueryFragment mQueryFragment = new QueryFragment();
						mQueryFragment.setArguments(bundle);
						replaceFragment(mQueryFragment);
					}
				});
				Query mTypeRef = current.getRef();
				ProductAdapter itemListDataAdapter = new ProductAdapter(mTypeRef){
					@Override
					public void onBindViewHolder(ProductViewHolder holder, int position) {
						super.onBindViewHolder(holder, position);
						final Product product = getItem(position);
						holder.itemView.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								Bundle bundle = new Bundle();
								bundle.putString(MainActivity.PRODUCT, product.getId());
								ProductInformationFragment productInformationFragment = new ProductInformationFragment();
								productInformationFragment.setArguments(bundle);
								replaceFragment(productInformationFragment);
							}
						});
					}
				};
				holder.mListProduct.setHasFixedSize(true);
				holder.mListProduct.setLayoutManager(new LinearLayoutManager(getContext(),
					LinearLayoutManager.HORIZONTAL, false));
				holder.mListProduct.setAdapter(itemListDataAdapter);
				holder.mListProduct.setNestedScrollingEnabled(false);
			}
		};
		mHomePageList.setLayoutManager(mLinearLayoutManager);
		mHomePageList.setAdapter(mGroupProductAdapter);
		mProgressBar.setVisibility(ProgressBar.INVISIBLE);
		mSearchView = view.findViewById(R.id.floating_search_view);
		setupFloatingSearch();
		attachSearchViewActivityDrawer(mSearchView);
	}

	public GroupProduct createRef(String tittle){
		return new GroupProduct(tittle,
			mFirebaseDatabaseReference
				.child(MainActivity.PRODUCT)
				.orderByChild("type")
				.equalTo(tittle));
	}

	@Override
	public boolean onActivityBackPress() {
		return false;
	}

	private void setupFloatingSearch() {
		mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
			@Override
			public void onSearchTextChanged(String oldQuery, final String newQuery) {
				Log.e(TAG, "onSearchTextChanged(), Query: " + newQuery);
			}
		});

		mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
			@Override
			public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {
				Log.e(TAG, "onSuggestionClicked()" + searchSuggestion.getBody());
			}

			@Override
			public void onSearchAction(String query) {
				mLastQuery = query;
				Bundle bundle = new Bundle();
				bundle.putString(MainActivity.LAST_QUERY, query);
				bundle.putString(MainActivity.TYPE_SCREEN, "search");
				QueryFragment mQueryFragment = new QueryFragment();
				mQueryFragment.setArguments(bundle);
				replaceFragment(mQueryFragment);
				Log.e(TAG, "onSearchAction()" + query);
			}
		});

		mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
			@Override
			public void onFocus() {
				Log.e(TAG, "onFocus()");
			}

			@Override
			public void onFocusCleared() {
				mSearchView.setSearchBarTitle(mLastQuery);
				Log.e(TAG, "onFocusCleared()");
			}
		});
		mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
			@Override
			public void onActionMenuItemSelected(MenuItem item) {
			if (item.getItemId() == R.id.action_voice_rec) {
				Toast.makeText(getContext(), item.getTitle(),
					Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
					RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
				intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
					"Try saying something");
				try {
					startActivityForResult(intent, MainActivity.REQ_CODE_SPEECH_INPUT);
				} catch (ActivityNotFoundException a) {

				}
			}
			}
		});

		mSearchView.setOnSuggestionsListHeightChanged(new FloatingSearchView.OnSuggestionsListHeightChanged() {
			@Override
			public void onSuggestionsListHeightChanged(float newHeight) {
				mHomePageList.setTranslationY(newHeight);
			}
		});

		mSearchView.setOnClearSearchActionListener(new FloatingSearchView.OnClearSearchActionListener() {
			@Override
			public void onClearSearchClicked() {
				Log.e(TAG, "onClearSearchClicked()");
			}
		});
	}
}