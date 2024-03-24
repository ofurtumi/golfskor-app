package hugbo.golfskor.service

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.location.Location
import android.os.Build
import android.widget.Toast
import hugbo.golfskor.R

class GPSLocation() {

    private lateinit var context: Context
    private lateinit var locationManager: LocationManager
    private var latitude: Double = 10.0
    private var longitude: Double = 14.0
    private var direction: String = "w"

    fun init(context: Context) {
        this.context = context
        this.locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (checkIfLocationIsEnabled()) {
            try {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0L,
                    0f,
                    { location: Location ->
                        latitude = location.latitude
                        longitude = location.longitude
                    },
                    null
                )
            } catch (ex: SecurityException) {
                showToast(R.string.permission_not_granted)
            }
        } else {
            showToast(R.string.location_not_enabled)
        }
    }

    private fun checkIfLocationIsEnabled(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            locationManager.isLocationEnabled
        } else {
            val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            isGPSEnabled || isNetworkEnabled
        }
    }

    private fun showToast(message: Int) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun getLatitude(): Double {
        return latitude
    }

    fun getLongitude(): Double {
        return longitude
    }
    fun getDirection(): String{
        return direction
    }
}
