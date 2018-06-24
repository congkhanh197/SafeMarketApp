package com.group13.safemarket.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.group13.safemarket.MainActivity;
import com.group13.safemarket.R;
import com.group13.safemarket.models.Product;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Khanh Tran-Cong on 5/9/2018.
 * Email: congkhanh197@gmail.com
 */
public class AddProductFragment extends BaseFragment {


	private static final String TAG = "AddProductFragment";
	private static final int RC_CHOOSE_PHOTO = 101;
	private static final int RC_TAKE_PHOTO = 102;
	private static final int MY_CAMERA_REQUEST_CODE = 100;

	private DatabaseReference mDatabaseReference;
	private StorageReference mImageRef;

	private EditText nameEditText, pictureEditText, addressEditText, priceEdittext, detailEditText;
	private Spinner typeSpinner;
	private Button sellButton, cameraButton, galeryButton;
	private ProgressBar progressBar;
	private String mUserID;
	private Uri mPicturePath;
	private String mProductID;

	private String mCurrentPhotoPath;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_add_product,container,false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();


		nameEditText = view.findViewById(R.id.et_tensp);
		addressEditText = view.findViewById(R.id.et_diachi);
		typeSpinner = view.findViewById(R.id.sp_danhmuc);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
			R.array.product_type, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		typeSpinner.setAdapter(adapter);
		addressEditText = view.findViewById(R.id.et_diachi);
		priceEdittext = view.findViewById(R.id.et_gia);
		detailEditText = view.findViewById(R.id.et_chitietsp);
		sellButton = view.findViewById(R.id.bt_dangban);
		cameraButton = view.findViewById(R.id.bt_camera);
		galeryButton = view.findViewById(R.id.bt_galery);
		pictureEditText = view.findViewById(R.id.et_picture);
		pictureEditText.setEnabled(false);
		progressBar = view.findViewById(R.id.progress_bar_add_product);
		progressBar.setVisibility(ProgressBar.INVISIBLE);

		sellButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			disenableUI();
			progressBar.setVisibility(ProgressBar.VISIBLE);
			mDatabaseReference = FirebaseDatabase.getInstance()
				.getReference().child(MainActivity.PRODUCT).push();
			mProductID = mDatabaseReference.getKey();
			uploadPhoto(mPicturePath);
			}
		});
		galeryButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, RC_CHOOSE_PHOTO);
			}
		});
		cameraButton.setVisibility(View.GONE);

//		if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA)
//			!= PackageManager.PERMISSION_GRANTED) {
//			requestPermissions(new String[]{Manifest.permission.CAMERA},
//				MY_CAMERA_REQUEST_CODE);
//		}else{
//			cameraButton.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//					if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
//						File photoFile = null;
//						try {
//							photoFile = createImageFile();
//						} catch (IOException ex) {
//						}
//						// Continue only if the File was successfully created
//						if (photoFile != null) {
//							Uri photoURI = FileProvider.getUriForFile(getContext(),
//								"com.example.android.fileprovider",
//								photoFile);
//							mPicturePath = photoURI;
//							takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//							startActivityForResult(takePictureIntent, RC_TAKE_PHOTO);
//						}
//					}
//				}
//			});
//		}
	}

//	@Override
//	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//		if (requestCode == MY_CAMERA_REQUEST_CODE) {
//
//			if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//				cameraButton.setOnClickListener(new View.OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//						if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
//							File photoFile = null;
//							try {
//								photoFile = createImageFile();
//							} catch (IOException ex) {
//							}
//							// Continue only if the File was successfully created
//							if (photoFile != null) {
//								Uri photoURI = FileProvider.getUriForFile(getContext(),
//									"com.example.android.fileprovider",
//									photoFile);
//								mPicturePath = photoURI;
//								takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//								startActivityForResult(takePictureIntent, RC_TAKE_PHOTO);
//							}
//						}
//					}
//				});
//			} else {
//				Toast.makeText(getContext(), "Camera permission denied", Toast.LENGTH_LONG).show();
//			}
//		}
//	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode){
			case RC_CHOOSE_PHOTO:
				if (resultCode == Activity.RESULT_OK) {
					mPicturePath = data.getData();
					pictureEditText.setText(mPicturePath.getPath());
				} else {
					Toast.makeText(getContext(), "No image chosen", Toast.LENGTH_SHORT).show();
				}
				break;
//			case RC_TAKE_PHOTO:
//				if (resultCode == Activity.RESULT_OK) {
//					Bitmap photo = (Bitmap) data.getExtras().get("data");
//				} else {
//					Toast.makeText(getContext(), "No picture took!", Toast.LENGTH_SHORT).show();
//				}
//				break;
			default:
				break;
		}
	}

//	private File createImageFile() throws IOException {
//		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//		String imageFileName = "JPEG_" + timeStamp + "_";
//		File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//		File image = File.createTempFile(
//			imageFileName,  /* prefix */
//			".jpg",         /* suffix */
//			storageDir      /* directory */
//		);
//
//		// Save a file: path for use with ACTION_VIEW intents
//		mCurrentPhotoPath = image.getAbsolutePath();
//		return image;
//	}

	private void uploadPhoto(Uri uri){
		mImageRef = FirebaseStorage.getInstance().getReference("/product/" + mProductID + "/image.jpeg");
		try {
			InputStream stream = getActivity().getContentResolver().openInputStream(uri);
			mImageRef.putStream(stream).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
				@Override
				public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
					if (!task.isSuccessful()) {
						throw task.getException();
					}
					return mImageRef.getDownloadUrl();
				}
			}).addOnCompleteListener(new OnCompleteListener<Uri>() {
				@Override
				public void onComplete(@NonNull Task<Uri> task) {
					if (task.isSuccessful()) {
						Log.e(TAG, "Num of fragment" + getActivity().getSupportFragmentManager().getBackStackEntryCount()+"");
						Uri downloadUri = task.getResult();
						mDatabaseReference.setValue(getProduct(downloadUri.toString()));
						progressBar.setVisibility(ProgressBar.INVISIBLE);
						getActivity().getSupportFragmentManager().popBackStack();
						Toast.makeText(getContext(),"Create product successful!",Toast.LENGTH_LONG).show();
					} else {
						Log.e(TAG,"Upload failure");
						Toast.makeText(getContext(),"Create product fail!",Toast.LENGTH_LONG).show();
					}
				}
			});
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	private void disenableUI(){
		pictureEditText.setEnabled(false);
		nameEditText.setEnabled(false);
		typeSpinner.setEnabled(false);
		addressEditText.setEnabled(false);
		priceEdittext.setEnabled(false);
		detailEditText.setEnabled(false);
	}

	private Product getProduct(String pictureUrl){
		return new Product(pictureUrl,
			nameEditText.getText().toString(),
			typeSpinner.getSelectedItem().toString(),
			addressEditText.getText().toString(),
			priceEdittext.getText().toString(),
			detailEditText.getText().toString(),
			mUserID, "false", "false");
	}

	@Override
	public boolean onActivityBackPress() {
		return false;
	}
}
