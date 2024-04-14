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
import hugbo.golfskor.network.GolfSkorApi
import hugbo.golfskor.ui.viewModels.ProfileUiState

class GPSLocation {

    private var contextRef: WeakReference<Context>? = null
    private lateinit var locationManager: LocationManager
    private var latitude: Double = 8.3
    private var longitude: Double = 14.0

    /**
     * Defines a location listener that updates the current latitude and longitude whenever a new location is received.
     *
     * This listener is assigned to listen for location updates and acts upon the reception of new location data.
     * When a new location is found, it updates the {@code latitude} and {@code longitude} fields of this class
     * with the new geographic coordinates provided by the location update.
     */
    private val locationListener = LocationListener { location ->
        latitude = location.latitude
        longitude = location.longitude
    }

    /**
     * Starts location updates by requesting updates from the GPS provider.
     *
     * This method sets up a weak reference to the provided {@code context} to avoid memory leaks,
     * and retrieves the system's location service. If location services are enabled on the device,
     * it requests updates at the highest frequency and with no minimum distance change required
     * between updates. If location permissions have not been granted, it displays a toast message
     * informing the user that the permissions have not been granted.
     *
     * @param context The context used for accessing system services and displaying the toast.
     *                It should be the application context or an activity context.
     *                Must not be null.
     * @throws SecurityException if the app does not have the necessary location permissions.
     *                           This exception is caught internally and a toast message is shown.
     */
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
                showToast(context, R.string.permission_not_granted)
            }
        }
    }
    /**
     * Stops receiving location updates from the GPS provider.
     *
     * This method checks if the {@code locationManager} has been previously initialized by
     * {@code startLocationUpdates}. If it has, it calls the {@code removeUpdates} method on
     * the {@code locationManager} to stop receiving location updates.
     */
    fun stopLocationUpdates() {
        if (::locationManager.isInitialized) {
            locationManager.removeUpdates(locationListener)
        }
    }
    /**
     * Checks if location services are enabled on the device.
     *
     * This method determines the availability of location services using two approaches depending
     * on the API level of the device. For devices running Android P (API level 28) or higher,
     * it directly checks the {@code isLocationEnabled} property of the {@code locationManager}.
     * For older devices, it checks whether either the GPS or Network providers are enabled.
     *
     * @return {@code true} if location services are enabled; {@code false} otherwise.
     */
    fun checkIfLocationIsEnabled(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            locationManager.isLocationEnabled
        } else {
            val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            isGPSEnabled || isNetworkEnabled
        }
    }

    private fun showToast(context: Context, message: Int) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Converts a shorthand wind direction code into its full textual representation.
     *
     * This function takes a single-character string representing the cardinal direction ('A', 'V', 'S', 'N')
     * and returns the full name of the direction in Icelandic. If the input does not match any of the expected
     * codes, it returns the input as is.
     *
     * - 'A' returns "Austur" (East)
     * - 'V' returns "Vestur" (West)
     * - 'S' returns "Suður" (South)
     * - 'N' returns "Norður" (North)
     *
     * @param wind The shorthand code for the wind direction, expected to be a single uppercase letter.
     * @return The full Icelandic name of the direction, or the input string if no match is found.
     */
    fun getDirection(wind: String): String {
        return when (wind) {
            "A" -> "Austur"
            "V" -> "Vestur"
            "S" -> "Suður"
            "N" -> "Norður"
            else -> wind
        }
    }

    fun getLatitude(): Double = latitude

    fun getLongitude(): Double = longitude
}
