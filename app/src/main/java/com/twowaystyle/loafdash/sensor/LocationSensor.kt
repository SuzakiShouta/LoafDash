package com.twowaystyle.loafdash.sensor

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*
import com.google.firebase.firestore.GeoPoint

class LocationSensor(private val activity: Activity) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(activity)

    private var locationCallback: LocationCallback? = null

    private val _geoPoint: MutableLiveData<GeoPoint> = MutableLiveData()
    val geoPoint: LiveData<GeoPoint> = _geoPoint

    @SuppressLint("MissingPermission")
    fun start() {
        if (checkLocationPermission()) {
            val locationRequest: LocationRequest.Builder = LocationRequest.Builder(1000)
            locationRequest.setMinUpdateDistanceMeters(10.0f)
            locationRequest.build()

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    for (location in locationResult.locations) {
                        val lat = location.latitude
                        val long = location.longitude
                        _geoPoint.postValue(GeoPoint(lat, long))
                    }
                }
            }

            fusedLocationClient.requestLocationUpdates(
                locationRequest.build(),
                locationCallback as LocationCallback,
                Looper.getMainLooper()
            )
        }
    }

    fun requestPermissions() {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    fun stop() {
        locationCallback?.let {
            fusedLocationClient.removeLocationUpdates(it)
            locationCallback = null
        }
    }

    private fun checkLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}

