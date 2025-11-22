package quo.vadis.readio.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import quo.vadis.navigation.Route
import quo.vadis.readio.presentation.ui.AppNavHost
import quo.vadis.readio.presentation.ui.theme.ReadioTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ReadioTheme {
                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }

                AppNavHost(
                    navController = navController,
                    startDestination = Route.Auth.route,
                    snackBarHostState = snackbarHostState,
                    firebaseAuth = firebaseAuth
                )
            }
        }
    }
}