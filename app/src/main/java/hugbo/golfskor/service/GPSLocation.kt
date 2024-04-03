package hugbo.golfskor.service

import android.content.Context
import android.location.LocationManager
import android.location.LocationListener
import android.os.Build
import android.widget.Toast
import hugbo.golfskor.R
import hugbo.golfskor.entities.ApiLocation
import java.lang.ref.WeakReference
import android.util.Log

class GPSLocation {

    private var contextRef: WeakReference<Context>? = null
    private lateinit var locationManager: LocationManager
    private var latitude: Double = 8.3
    private var longitude: Double = 14.0
    private var direction: String = "N"
    private var heat: Double = 0.0
    private var wind: Double = 0.0
    private var date: String = "2024-03-24"

    private val locationListener = LocationListener { location ->
        latitude = location.latitude
        longitude = location.longitude
    }

    fun updateWeatherData(apiLocation: ApiLocation) {
        Log.d("GPSLocation", "Updating weather data");
        this.heat = apiLocation.temperature
        this.wind = apiLocation.windspeed
        this.direction = apiLocation.direction
        this.date = apiLocation.date

        Log.d("GPSLocation", "New values - Heat: " + heat + ", Wind: " + wind + ", Direction: " + direction + ", Date: " + date);
    }
    fun locationUpdate() {
        if (::locationManager.isInitialized && checkIfLocationIsEnabled() && contextRef != null) {
            try {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0L,
                    0f,
                    locationListener
                )
            } catch (ex: SecurityException) {

            }
        } else {
            heat = 0.0
            wind = 0.0
            direction = "NaN"
            date = "NaN"
        }
    }

    fun startLocationUpdates(context: Context) {
        contextRef = WeakReference(context)
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (checkIfLocationIsEnabled()) {
            try {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0L,
                    0f,
                    locationListener
                )
            } catch (ex: SecurityException) {
                showToast(context,R.string.permission_not_granted)
            }
        }
    }

    fun stopLocationUpdates() {
        if (::locationManager.isInitialized) {
            locationManager.removeUpdates(locationListener)
        }
    }

    fun checkIfLocationIsEnabled(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            locationManager.isLocationEnabled
        } else {
            val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            isGPSEnabled || isNetworkEnabled
        }
    }

    private fun showToast(context: Context,message: Int) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun getLatitude(): Double = latitude

    fun getLongitude(): Double = longitude

    fun getDirection(): String {
        return when(direction) {
            "A" -> "Austur"
            "V" -> "Vestur"
            "S" -> "Suður"
            "N" -> "Norður"
            else -> direction
        }
    }

    fun getHeat(): Double = heat

    fun getWind(): Double = wind
}
