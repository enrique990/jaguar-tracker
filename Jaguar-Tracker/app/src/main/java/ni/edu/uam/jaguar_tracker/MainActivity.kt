package ni.edu.uam.jaguar_tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ni.edu.uam.jaguar_tracker.ui.login.LoginScreen
import ni.edu.uam.jaguar_tracker.ui.theme.JaguarTrackerTheme

// Herencia -> MainActivity hereda de ComponentActivity, fundamental en Android
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JaguarTrackerTheme {
                // Composición -> Inyectamos la pantalla principal
                LoginScreen()
            }
        }
    }
}