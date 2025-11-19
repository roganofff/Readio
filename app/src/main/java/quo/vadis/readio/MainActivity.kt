package quo.vadis.readio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.auth.FirebaseAuth
import quo.vadis.auth.getRememberMeFlow
import quo.vadis.readio.presentation.navigation.Screen
import quo.vadis.readio.presentation.ui.theme.ReadioTheme

class MainActivity : ComponentActivity() {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current
            val rememberFlow = context.getRememberMeFlow().collectAsState(initial = true)
            val isUserCurrentlyLogged = remember { mutableStateOf(auth.currentUser != null) }

            LaunchedEffect(rememberFlow.value) {
                if (auth.currentUser != null && !rememberFlow.value) {
                    auth.signOut()
                    isUserCurrentlyLogged.value = false
                } else {
                    isUserCurrentlyLogged.value = auth.currentUser != null
                }
            }

            val start = if (isUserCurrentlyLogged.value) Screen.Main.route else Screen.Auth.route

            AppNavHost(startDestination = start, firebaseAuth = auth)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ReadioTheme {
        Greeting("Android")
    }
}