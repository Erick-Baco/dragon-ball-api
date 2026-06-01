package mx.unam.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import mx.unam.utils.isValidEmail
import mx.unam.utils.isValidPassword

class AuthViewModel (
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
): ViewModel() {


    private var _currentUserEmail by mutableStateOf("")
    val currentUserEmail: String
        get() = _currentUserEmail
    private var _currentUserName by mutableStateOf("")
    val currentUserName: String
        get () = _currentUserName

    private var _errorMessage by mutableStateOf<String?>(null)
    val errorMessage: String?
        get() = _errorMessage

    private var _isLoading by mutableStateOf(false)
    val isLoading: Boolean
        get () = _isLoading


    fun registerManual (
        name: String,
        lastname: String,
        surname: String,
        username: String,
        email: String,
        pass: String,
        confirmPass: String,
        onSuccess: () -> Unit
    ) {
        if (name.isBlank() || lastname.isBlank()
            || surname.isBlank() || username.isBlank()) {
            _errorMessage = "Debe llenar todos los campos"
            return
        }

        if ( !isValidEmail(email)) {
            _errorMessage = "Email inválido"
            return
        }

        if ( pass != confirmPass ) {
            _errorMessage = "La contraseña debe coincidir"
            return
        }


        if ( !isValidPassword(pass) ) {
            _errorMessage = "Contraseña inválida"
            return
        }

        viewModelScope.launch {
            try {

                _isLoading = true

                val result: AuthResult = auth.createUserWithEmailAndPassword(email,pass ).await()

                val firebaseUser: FirebaseUser? = result.user

                val userMap: HashMap<String, String> = hashMapOf(
                    "name" to name,
                    "lastname" to lastname,
                    "surname" to surname,
                    "username" to username,
                    "email" to email,
                )

                db.collection("users").document(email).set(userMap).await()

                firebaseUser?.sendEmailVerification()?.await()

                auth.signOut()
                onSuccess()
            } catch ( n: FirebaseNetworkException) {
                _errorMessage = "Revisa tu conexión a internet"
            } catch (e: Exception) {
                _errorMessage = "Este usuario ya existe o hubo un error"
            } finally {
                _isLoading = false
            }
        }

    }

    fun loginManual(
        email: String,
        pass: String,
        onSuccess: () -> Unit
    ) {
        if (email.isBlank() || pass.isBlank()) {
            _errorMessage = "Completa los campos"
            return
        }

        viewModelScope.launch {
            try {
                _isLoading = true

                val result: AuthResult = auth.signInWithEmailAndPassword(email, pass).await()

                if (result.user?.isEmailVerified == true) {
                    _currentUserEmail = email

                    val document: DocumentSnapshot = db.collection("users").document(email).get().await()
                    _currentUserName = document.getString("username") ?: email
                    onSuccess()

                } else {
                    auth.signOut()
                    _errorMessage = "Verifica tu correo"
                }
            }  catch ( n: FirebaseNetworkException) {
                _errorMessage = "Revisa tu conexión a internet"
            } catch (e: Exception) {
                _errorMessage = "Usuario o contaseña inválidos"
            } finally {
                _isLoading = false
            }
        }

    }

    fun resetPassword(
        email: String,
        onSuccess: () -> Unit
    )
    {
        if (!isValidEmail(email)) {
            _errorMessage = "Email inválido"
            return
        }

        viewModelScope.launch {
            try {
                _isLoading = true
                auth.sendPasswordResetEmail(email).await()

                onSuccess()
            }  catch ( n: FirebaseNetworkException) {
                _errorMessage = "Revisa tu conexión a internet"
            } catch (e: Exception) {
                onSuccess()
            } finally {
                _isLoading = false
            }
        }

    }

    fun signOut() {
        auth.signOut()
        _currentUserName = ""
        _currentUserEmail = ""
    }

    fun clearError() {
        _errorMessage = null
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {

                val auth = FirebaseAuth.getInstance()
                val db = FirebaseFirestore.getInstance()

                return AuthViewModel(auth, db) as T
            }
        }
    }
}
