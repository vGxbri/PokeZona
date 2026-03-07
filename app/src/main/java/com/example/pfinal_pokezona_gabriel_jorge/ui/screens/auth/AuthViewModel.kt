package com.example.pfinal_pokezona_gabriel_jorge.ui.screens.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val uid: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    init {
        checkUserSession()
    }

    // Comprobar si ya hay una sesión iniciada
    fun checkUserSession() {
        val user = auth.currentUser
        if (user != null) {
            _authState.value = AuthState.Success(user.uid)
        }
    }

    // Cerrar sesión
    fun signOut(context: Context) {
        // 1. Cerramos sesión en Firebase
        auth.signOut()

        // 2. Cerramos sesión en el cliente de Google del móvil
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        GoogleSignIn.getClient(context, gso).signOut().addOnCompleteListener {
            // 3. Cuando termine de borrar la memoria de Google, volvemos a la pantalla de Login
            _authState.value = AuthState.Idle
        }
    }

    // Registro de usuario
    fun register(email: String, password: String, displayName: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            Log.d("AuthViewModel", "Iniciando registro para $email")
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val uid = result.user?.uid

                if (uid != null) {
                    Log.d(
                            "AuthViewModel",
                            "Auth exitoso, UID: $uid. Intentando guardar en Firestore..."
                    )

                    try {
                        val userMap = hashMapOf("nombre" to displayName, "correo" to email)
                        db.collection("usuarios").document(uid).set(userMap).await()
                        Log.d("AuthViewModel", "Firestore guardado exitosamente")
                    } catch (firestoreError: Exception) {
                        Log.e(
                                "AuthViewModel",
                                "Error al guardar en Firestore (continuando...): ${firestoreError.message}"
                        )
                    }

                    _authState.value = AuthState.Success(uid)
                } else {
                    _authState.value = AuthState.Error("No se pudo obtener el ID de usuario")
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error en registro: ${e.message}", e)
                _authState.value = AuthState.Error(e.message ?: "Error en el registro")
            }
        }
    }

    // Inicio de sesión
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            Log.d("AuthViewModel", "Iniciando login para $email")
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                val uid = result.user?.uid
                if (uid != null) {
                    Log.d("AuthViewModel", "Login exitoso, UID: $uid")
                    _authState.value = AuthState.Success(uid)
                } else {
                    _authState.value = AuthState.Error("No se pudo obtener el ID de usuario")
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error en login: ${e.message}", e)
                _authState.value = AuthState.Error(e.message ?: "Error de inicio de sesión")
            }
        }
    }

    // Inicio de sesión con Google
    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            Log.d("AuthViewModel", "Iniciando sesión con Google")
            try {
                val credential =
                        com.google.firebase.auth.GoogleAuthProvider.getCredential(idToken, null)
                val result = auth.signInWithCredential(credential).await()
                val uid = result.user?.uid

                if (uid != null) {
                    Log.d(
                            "AuthViewModel",
                            "Google Auth exitoso, UID: $uid. Intentando actualizar Firestore..."
                    )

                    try {
                        val user = result.user
                        val userMap =
                                hashMapOf(
                                        "nombre" to (user?.displayName ?: "Google User"),
                                        "correo" to (user?.email ?: "")
                                )
                        db.collection("usuarios").document(uid).set(userMap).await()
                        Log.d("AuthViewModel", "Firestore actualizado")
                    } catch (firestoreError: Exception) {
                        Log.e(
                                "AuthViewModel",
                                "Error al actualizar Firestore (continuando...): ${firestoreError.message}"
                        )
                    }

                    _authState.value = AuthState.Success(uid)
                } else {
                    _authState.value =
                            AuthState.Error("No se pudo obtener el ID de usuario de Google")
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error en Google Login: ${e.message}", e)
                _authState.value = AuthState.Error(e.message ?: "Error al conectar con Google")
            }
        }
    }
}
