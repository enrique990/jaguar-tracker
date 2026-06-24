package ni.edu.uam.jaguar_tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ni.edu.uam.jaguar_tracker.ui.home.HomeScreen
import ni.edu.uam.jaguar_tracker.ui.history.HistoryScreen
import ni.edu.uam.jaguar_tracker.ui.Ranking.RankingScreen
import ni.edu.uam.jaguar_tracker.ui.login.LoginScreen
import ni.edu.uam.jaguar_tracker.ui.profilesetup.ProfileScreen
import ni.edu.uam.jaguar_tracker.ui.profilesetup.ProfileSetupScreen
import ni.edu.uam.jaguar_tracker.ui.routine.NewRoutineScreen
import ni.edu.uam.jaguar_tracker.ui.session.WorkoutSessionScreen
import ni.edu.uam.jaguar_tracker.ui.theme.JaguarTrackerTheme
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ni.edu.uam.jaguar_tracker.data.repository.UserSessionRepository

// Herencia -> MainActivity hereda de ComponentActivity, fundamental en Android
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        UserSessionRepository.init(applicationContext)

        lifecycleScope.launch {
            val session = withContext(Dispatchers.IO) {
                UserSessionRepository.cargarSesion()
            }

            val startDestination = if (session != null) {
                "home"
            } else {
                "login"
            }

            setContent {
                JaguarTrackerTheme {
                    JaguarTrackerNavHost(
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}

@Composable
fun JaguarTrackerNavHost(
    startDestination: String = "login"
){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("login") {
            LoginScreen(
                onProfileClick = { navController.navigate("profile") },
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                        launchSingleTop = true
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
                onProfileClick = { navController.navigate("profile") },
                onHistoryClick = { navController.navigate("history") },
                onRankingClick = { navController.navigate("ranking") }
            )
        }
        composable("profile") {
            ProfileScreen(
                onHomeClick = {
                    navController.navigate("home")
                },
                onHistoryClick = {
                    navController.navigate("history")
                },
                onLogoutSuccess = {
                    navController.navigate("login") {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                }
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
        composable("history") {
            HistoryScreen(
                onHomeClick = { navController.navigate("home") },
                onRankingClick = { navController.navigate("ranking") },
                onProfileClick = { navController.navigate("profile") }
            )
        }
        composable("ranking") {
            RankingScreen(
                onHomeClick = { navController.navigate("home") },
                onHistoryClick = { navController.navigate("history") },
                onProfileClick = { navController.navigate("profile") }
            )
        }
    }
}