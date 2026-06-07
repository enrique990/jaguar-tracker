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
import ni.edu.uam.jaguar_tracker.ui.profilesetup.ProfileScreen
import ni.edu.uam.jaguar_tracker.ui.profilesetup.ProfileSetupScreen
import ni.edu.uam.jaguar_tracker.ui.routine.NewRoutineScreen
import ni.edu.uam.jaguar_tracker.ui.session.WorkoutSessionScreen
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
            LoginScreen(
                onProfileClick = { navController.navigate("profile") },
                onLoginSuccess = {
                    navController.navigate("profile_setup") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("profile_setup") {
            ProfileSetupScreen(
                onSave = { _, _ ->
                    navController.navigate("home") {
                        popUpTo("profile_setup") { inclusive = true }
                    }
                },
                onSkip = {
                    navController.navigate("home") {
                        popUpTo("profile_setup") { inclusive = true }
                    }
                }
            )
        }
        composable("home") {
            HomeScreen(
                onNewRoutineClick = { navController.navigate("new_routine") },
                onStartWorkoutClick = { navController.navigate("workout_session") },
                onProfileClick = { navController.navigate("profile") }
            )
        }
        composable("profile") {
            ProfileScreen(
                onNavigateToHome = { navController.navigate("home") }
            )
        }
        composable("new_routine") {
            NewRoutineScreen {
                navController.popBackStack()
            }
        }
        composable("workout_session") {
            WorkoutSessionScreen(
                onBackClick = { navController.popBackStack() },
            )
        }
    }
}