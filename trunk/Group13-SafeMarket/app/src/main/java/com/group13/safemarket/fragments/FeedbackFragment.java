package com.group13.safemarket.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.group13.safemarket.MainActivity;
import com.group13.safemarket.R;
import com.group13.safemarket.models.Feedback;

/**
 * Created by Khanh Tran-Cong on 5/10/2018.
 * Email: congkhanh197@gmail.com
 */
public class FeedbackFragment extends BaseFragment {
	private EditText nameEditText, detailEditText;
	private Button sendButton;

	@Override
	public boolean onActivityBackPress() {
		return false;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_feedback, container,false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		nameEditText = view.findViewById(R.id.et_feedback_name);
		detailEditText = view.findViewById(R.id.et_feedback_detail);
		sendButton = view.findViewById(R.id.bt_send_feedback);
		sendButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if((nameEditText.getText()!= null) &&(detailEditText.getText()!= null)){
					FirebaseDatabase.getInstance().getReference().child(MainActivity.FEEDBACK)
						.push().setValue(new Feedback(nameEditText.getText().toString(),
						detailEditText.getText().toString(),
						FirebaseAuth.getInstance().getUid()));
					getActivity().getSupportFragmentManager().popBackStack();
					Toast.makeText(getContext(),"Push feedback successful!",Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
