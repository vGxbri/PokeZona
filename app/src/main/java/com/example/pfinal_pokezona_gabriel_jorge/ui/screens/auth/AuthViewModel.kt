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
    fun signOut() {
        auth.signOut()
        _authState.value = AuthState.Idle
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
                    Log.d("AuthViewModel", "Auth exitoso, UID: $uid. Guardando en Firestore...")

                    val userMap = hashMapOf("nombre" to displayName, "correo" to email)

                    // Usamos await() también para Firestore
                    db.collection("usuarios").document(uid).set(userMap).await()

                    Log.d("AuthViewModel", "Firestore guardado exitosamente")
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
}
