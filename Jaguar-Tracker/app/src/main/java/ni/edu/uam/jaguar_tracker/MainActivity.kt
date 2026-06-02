package ni.edu.uam.jaguar_tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ni.edu.uam.jaguar_tracker.ui.home.HomeScreen
import ni.edu.uam.jaguar_tracker.ui.login.LoginScreen
import ni.edu.uam.jaguar_tracker.ui.theme.JaguarTrackerTheme

// Herencia -> MainActivity hereda de ComponentActivity, fundamental en Android
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JaguarTrackerTheme {
                // Composición -> Inyectamos la navegación principal
                JaguarTrackerNavHost()
            }
        }
    }
}

@Composable
fun JaguarTrackerNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login",
    ) {
        composable("login") {
            LoginScreen {
                navController.navigate("home") {
                    // Limpiamos el stack para que no pueda volver al login con atrás
                    popUpTo("login") { inclusive = true }
                }
            }
        }
        composable("home") {
            HomeScreen()
        }
    }
}