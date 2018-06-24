package com.group13.safemarket;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.group13.safemarket.models.Product;


public class GPSActivity extends AppCompatActivity {

	Button btnShowAddress;
	TextView tv_address;
	AppLocationService appLocationService;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.fragment_add_product);

		//address
		tv_address = (TextView) findViewById(R.id.et_diachi);
		appLocationService = new AppLocationService(
				GPSActivity.this);

		btnShowAddress = (Button) findViewById(R.id.button4);
		btnShowAddress.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {

				Location location = appLocationService
						.getLocation(LocationManager.GPS_PROVIDER);

				if (location != null) {
					double latitude = location.getLatitude();
					double longitude = location.getLongitude();
					LocationAddress locationAddress = new LocationAddress();
					locationAddress.getAddressFromLocation(latitude, longitude,
							getApplicationContext(), new GPSActivity.GeocoderHandler());
				} else {
					showSettingsAlert();
				}
			}
		});
	}
	// location
	public void showSettingsAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				GPSActivity.this);
		alertDialog.setTitle("SETTINGS");
		alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						GPSActivity.this.startActivity(intent);
					}
				});
		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		alertDialog.show();
	}

	private class GeocoderHandler extends Handler {
		@Override
		public void handleMessage(Message message) {
			String locationAddress;
			switch (message.what) {
				case 1:
					Bundle bundle = message.getData();
					locationAddress = bundle.getString("address");
					break;
				default:
					locationAddress = null;
			}
			tv_address.setText(locationAddress);
		}
	}
}
