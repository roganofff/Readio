package quo.vadis.books.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun PlaceholderBooksScreen(onBookClick: (String, String) -> Unit) {
    Column {
        Text("BOOK (placeholder)")
        Button(onClick = { onBookClick }) {
            Text("Log Out")
        }
    }
}