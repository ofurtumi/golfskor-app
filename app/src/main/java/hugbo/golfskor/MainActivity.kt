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

    override fun onStop() {
        super.onStop()
        gpsLocation.stopLocationUpdates()
    }
}

