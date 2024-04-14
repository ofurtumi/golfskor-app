package hugbo.golfskor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import hugbo.golfskor.data.UserInfoDataStoreService
import hugbo.golfskor.service.GPSLocation
import hugbo.golfskor.ui.theme.GolfskorTheme

class MainActivity : ComponentActivity() {

    private val gpsLocation = GPSLocation()

    /**
     * Initializes the activity, setting up necessary services and the user interface.
     *
     * This method is called when the activity is starting. It performs several key initializations and setups:
     * - Calls the superclass's onCreate method with the saved instance state, ensuring proper lifecycle management.
     * - Initializes the UserInfoDataStoreService to manage user data storage within the application context.
     * - Starts location updates to fetch and monitor geographical data.
     * - Sets up the Compose UI framework by defining the theme and primary surface where the navigation UI will be embedded.
     *
     * The primary components set up in the UI are defined within the `GolfskorTheme` and `Surface`, which include:
     * - `GolfskorTheme`: Applies a consistent visual style across the application.
     * - `Surface`: Serves as the container for the navigation system of the app, filling the maximum size of the application
     *   window and setting the background color from the theme's color scheme.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle
     *                           contains the data it most recently supplied in onSaveInstanceState(Bundle). Otherwise, it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UserInfoDataStoreService.init(this)
        gpsLocation.startLocationUpdates(this)


        setContent {
            GolfskorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Nav()
                }
            }
        }
    }

    /**
     * Handles actions to perform when the activity is no longer visible to the user.
     *
     * This method is called when the activity is about to be stopped and is no longer visible to the user.
     * It extends the base class behavior by stopping the location updates to conserve system resources such
     * as battery and GPS usage when the activity does not require real-time location data.
     *
     * - Calls {@code super.onStop()} to ensure that any system-level operations handled by the Android framework
     *   are executed properly.
     * - Stops the location updates by calling {@code gpsLocation.stopLocationUpdates()} to prevent unnecessary
     *   location data processing and battery drainage when the user is not actively interacting with the app.
     */
    override fun onStop() {
        super.onStop()
        gpsLocation.stopLocationUpdates()
    }
}

