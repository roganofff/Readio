package quo.vadis.books

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import quo.vadis.auth.data.FirebaseAuthRepository
import javax.inject.Inject

@HiltViewModel
class BooksViewModel @Inject constructor(
    private val repo: FirebaseAuthRepository
) : ViewModel() {

}