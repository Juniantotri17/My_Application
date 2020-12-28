package com.example.myapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.batoulapps.adhan.CalculationMethod;
import com.batoulapps.adhan.CalculationParameters;
import com.batoulapps.adhan.Coordinates;
import com.batoulapps.adhan.PrayerTimes;
import com.batoulapps.adhan.data.DateComponents;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;



/**
 * A simple {@link Fragment} subclass.
 */
public class JadwalFragment extends Fragment {

    TextView Subuh,Dhuhur,Ashar,Maghrib,Isha;
    FusedLocationProviderClient fusedLocationProviderClient;
    ProgressDialog progressDialog;
    private float Longitude;
    private float Latitude;
    TextView Hari;
    public JadwalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_jadwal, container, false);

        Calendar calendar = Calendar.getInstance();
        String currentdate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        Hari = (TextView) rootview.findViewById(R.id.hari);
        Hari.setText(currentdate);

        Subuh = (TextView) rootview.findViewById(R.id.subuh_value);
        Dhuhur = (TextView) rootview.findViewById(R.id.dhuhur_value);
        Ashar = (TextView) rootview.findViewById(R.id.ashar_value);
        Maghrib = (TextView) rootview.findViewById(R.id.maghrib_value);
        Isha = (TextView) rootview.findViewById(R.id.isha_value);


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Mohon Tunggu");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        CheckinternetGPS();

        return rootview;
    }

    private void CheckinternetGPS() {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (null != networkInfo) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI || networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Toast.makeText(getActivity(), "Nyalakan GPS anda", Toast.LENGTH_SHORT).show();
                } else {
                    GetLocation();
                }
            }
        }else{
            Toast.makeText(getActivity(),"No Internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void GetLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions((getActivity()), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
//            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 44);
//        } else {
//            JadwalShalat();
            return;
        }
        progressDialog.show();
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if(location !=null){
                    Geocoder geocoder = new Geocoder (getActivity() , Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(),location.getLongitude(),1
                        );

                        Longitude = (float) addresses.get(0).getLongitude();
                        Latitude = (float) addresses.get(0).getLatitude();

                        JadwalShalat();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void JadwalShalat() {

        final DateComponents dateComponents = DateComponents.from(new Date());
        final Coordinates coordinates = new Coordinates(Latitude,Longitude);
        final CalculationParameters parameters = CalculationMethod.SINGAPORE.getParameters();

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        formatter.setTimeZone(TimeZone.getDefault());

        PrayerTimes prayerTimes = new PrayerTimes(coordinates,dateComponents,parameters);

        Subuh.setText(Html.fromHtml("" + formatter.format(prayerTimes.fajr)));
        Dhuhur.setText(Html.fromHtml("" + formatter.format(prayerTimes.dhuhr)));
        Ashar.setText(Html.fromHtml("" + formatter.format(prayerTimes.asr)));
        Maghrib.setText(Html.fromHtml("" + formatter.format(prayerTimes.maghrib)));
        Isha.setText(Html.fromHtml("" + formatter.format(prayerTimes.isha)));
        progressDialog.dismiss();
    }
}
