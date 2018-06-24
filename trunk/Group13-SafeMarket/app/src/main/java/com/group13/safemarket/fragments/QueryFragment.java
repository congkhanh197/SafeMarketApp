package com.group13.safemarket.fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.group13.safemarket.MainActivity;
import com.group13.safemarket.R;
import com.group13.safemarket.adapter.QueryAdapter;
import com.group13.safemarket.models.Product;
import com.group13.safemarket.models.User;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Khanh Tran-Cong on 5/8/2018.
 * Email: congkhanh197@gmail.com
 */
public class QueryFragment extends BaseFragment {
	private static final String TAG = "QueryFragment";

	private FloatingSearchView mSearchView;

	private RecyclerView mSearchResultsList;
	private QueryAdapter mQueryAdapter;
	private Query query;

	private String mLastQuery;
	// "product" "group" "search"
	private String mScreenType;

	public QueryFragment() {
		// Required empty public constructor
	}
	private String mUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		mLastQuery = getArguments().getString(MainActivity.LAST_QUERY);
		mScreenType = getArguments().getString(MainActivity.TYPE_SCREEN);
		return inflater.inflate(R.layout.fragment_query, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		FloatingActionButton fab = view.findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				FirebaseDatabase.getInstance().getReference().child(MainActivity.USER).child(mUserID)
					.addListenerForSingleValueEvent(new ValueEventListener() {
						@Override
						public void onDataChange(DataSnapshot dataSnapshot) {
							User user = dataSnapshot.getValue(User.class);
							if((user.getPhone()!= null) && (user.getIdentifyCard()!= null)){
								AddProductFragment mAddProductFragment = new AddProductFragment();
								replaceFragment(mAddProductFragment);
							}else {
								Toast.makeText(getContext(),
									"Please add your phone number and identify card number!",
									Toast.LENGTH_LONG).show();
							}
						}
						@Override
						public void onCancelled(DatabaseError databaseError) {	}
					});
			}
		});
		if(!mScreenType.equals("product")){
			fab.setVisibility(View.GONE);
		}
		mSearchView = view.findViewById(R.id.floating_search_view);
		mSearchResultsList = view.findViewById(R.id.search_results_list);

		setupFloatingSearch();
		setupResultsList();
		setupDrawer();
	}

	private void setupFloatingSearch() {
		mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
			@Override
			public void onSearchTextChanged(String oldQuery, final String newQuery) {
				Log.d(TAG, "onSearchTextChanged()");
			}
		});

		mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
			@Override
			public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {
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
				Log.d(TAG, "onFocus()");
			}

			@Override
			public void onFocusCleared() {
				mSearchView.setSearchBarTitle(mLastQuery);
				Log.d(TAG, "onFocusCleared()");
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
				mSearchResultsList.setTranslationY(newHeight);
			}
		});

		mSearchView.setOnClearSearchActionListener(new FloatingSearchView.OnClearSearchActionListener() {
			@Override
			public void onClearSearchClicked() {
				Log.d(TAG, "onClearSearchClicked()");
			}
		});
	}

	private void setupResultsList() {
		switch (mScreenType){
			case "product":
				query =  FirebaseDatabase.getInstance().getReference()
					.child(MainActivity.PRODUCT).orderByChild("userID").equalTo(mUserID);
				break;
			case "search":
				query =  FirebaseDatabase.getInstance().getReference()
					.child(MainActivity.PRODUCT).orderByChild("name").startAt(mLastQuery);
				break;
			case "group":
				query = FirebaseDatabase.getInstance().getReference()
					.child(MainActivity.PRODUCT).orderByChild("type").equalTo(mLastQuery);
				break;
			default:
				break;
		}
		mQueryAdapter = new QueryAdapter(query){
			@Override
			public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
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
		mSearchResultsList.setAdapter(mQueryAdapter);
		mSearchResultsList.setLayoutManager(new LinearLayoutManager(getContext()));
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == MainActivity.REQ_CODE_SPEECH_INPUT){
			if (resultCode == Activity.RESULT_OK && null != data) {
				ArrayList<String> result = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				Bundle bundle = new Bundle();
				bundle.putString(MainActivity.LAST_QUERY, result.get(0));
				bundle.putString(MainActivity.TYPE_SCREEN, "search");
				QueryFragment mQueryFragment = new QueryFragment();
				mQueryFragment.setArguments(bundle);
				replaceFragment(mQueryFragment);
				Log.e(TAG, "onSearchAction()" + result.get(0));
			}
		}
	}

	@Override
	public boolean onActivityBackPress() {
		if (!mSearchView.setSearchFocused(false)) {
			return false;
		}
		return true;
	}

	private void setupDrawer() {
		attachSearchViewActivityDrawer(mSearchView);
	}

}