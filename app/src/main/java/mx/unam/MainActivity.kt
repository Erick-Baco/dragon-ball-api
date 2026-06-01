package mx.unam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.serialization.Serializable
import mx.unam.model.AuthViewModel
import mx.unam.ui.screens.HomeScreen
import mx.unam.ui.screens.LoginScreen
import mx.unam.ui.screens.RegisterScreen
import mx.unam.ui.screens.ResetPasswordScreen
import mx.unam.ui.theme.Proyecto_finalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        enableEdgeToEdge()

        setContent {
            Proyecto_finalTheme {
                MyApp()
            }
        }
    }
}

@Serializable
data object Login
@Serializable
data object Register
@Serializable
data object ResetPassword
@Serializable
data object Home

@Composable
fun MyApp() {
    val navController = rememberNavController()
    val authModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory)

    val mAuth = FirebaseAuth.getInstance()
    val currentUser = mAuth.currentUser

    val startDestination = if (currentUser == null) Login else Home

    NavHost(navController, startDestination = startDestination) {
        composable<Login> {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(route = Register) },
                onNavigateToResetPassword = { navController.navigate(route = ResetPassword) },
                onLoginSuccess = { navController.navigate(route = Home) {
                    popUpTo(Login) { inclusive = true}
                } },
            )
        }
        composable<Register> {
            RegisterScreen(
                onRegisterSuccess = { navController.navigate(route = Login) },
                onAccountAlreadyRegistered = { navController.popBackStack() }
            )
        }
        composable<Home> {
            HomeScreen(
                onLogOut = {
                    authModel.signOut()

                    navController.navigate(route = Login) {
                        popUpTo(Home) { inclusive = true }
                    }
                }
            )
        }
        composable<ResetPassword> {
            ResetPasswordScreen(
                onNavigateToLogin = { navController.navigate(route = Login) {
                    popUpTo(ResetPassword) { inclusive = true }
                } }
            )
        }
    }

}