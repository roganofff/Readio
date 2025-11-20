package quo.vadis.auth.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
private fun PlaceholderAuthScreen(onAuthSuccess: () -> Unit) {
    Column {
        Text("AUTH (placeholder)")
        Button(onClick = onAuthSuccess) {
            Text("Login")
        }
    }
}