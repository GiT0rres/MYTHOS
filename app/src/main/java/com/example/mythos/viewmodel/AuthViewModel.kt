package com.example.mythos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {

    // FirebaseAuth.getInstance() lança IllegalStateException se o FirebaseApp nunca foi
    // inicializado — é exatamente o que acontece no sandbox do Compose Preview (Layoutlib),
    // que não roda o ContentProvider de auto-init do Firebase. Guardando com try/catch, o
    // ViewModel continua instanciável nesse ambiente, apenas operando como "visitante".
    private val auth: FirebaseAuth? = try {
        FirebaseAuth.getInstance()
    } catch (e: IllegalStateException) {
        null
    }

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        _authState.value = if (auth?.currentUser == null) {
            AuthState.Unauthenticated
        } else {
            AuthState.Authenticated
        }
    }

    val userEmail: String
        get() = auth?.currentUser?.email ?: "visitante"

    val userName: String
        get() = auth?.currentUser?.displayName?.takeIf { it.isNotBlank() }
            ?: userEmail.substringBefore("@")

    fun login(email: String, password: String) {
        val firebaseAuth = auth ?: run {
            _authState.value = AuthState.Error("Firebase indisponível neste ambiente.")
            return
        }
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Preencha e-mail e senha.")
            return
        }
        _authState.value = AuthState.Loading
        firebaseAuth.signInWithEmailAndPassword(email.trim(), password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value =
                        AuthState.Error(task.exception?.message ?: "Não foi possível entrar.")
                }
            }
    }

    fun signup(name : String, email: String, password: String, confirmPassword: String) {
        val firebaseAuth = auth ?: run {
            _authState.value = AuthState.Error("Firebase indisponível neste ambiente.")
            return
        }
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Preencha todos os campos.")
            return
        }
        if (password != confirmPassword) {
            _authState.value = AuthState.Error("As senhas não coincidem.")
            return
        }
        if (password.length < 6) {
            _authState.value = AuthState.Error("A senha deve ter no mínimo 6 caracteres.")
            return
        }
        _authState.value = AuthState.Loading
        firebaseAuth.createUserWithEmailAndPassword(email.trim(), password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val request = com.google.firebase.auth.UserProfileChangeRequest.Builder()
                        .setDisplayName(name.trim())
                        .build()
                    firebaseAuth.currentUser?.updateProfile(request)
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value =
                        AuthState.Error(task.exception?.message ?: "Não foi possível cadastrar.")
                }
            }
    }

    fun resetPassword(email: String) {
        val firebaseAuth = auth ?: run {
            _authState.value = AuthState.Error("Firebase indisponível neste ambiente.")
            return
        }
        if (email.isBlank()) {
            _authState.value = AuthState.Error("Informe o e-mail para recuperar a senha.")
            return
        }
        firebaseAuth.sendPasswordResetEmail(email.trim())
            .addOnCompleteListener { task ->
                _authState.value = if (task.isSuccessful) {
                    AuthState.Message("E-mail de recuperação enviado.")
                } else {
                    AuthState.Error(task.exception?.message ?: "Falha ao enviar o e-mail.")
                }
            }
    }

    fun signout() {
        auth?.signOut()
        _authState.value = AuthState.Unauthenticated
    }
}

sealed class AuthState {
    data object Authenticated : AuthState()
    data object Unauthenticated : AuthState()
    data object Loading : AuthState()
    data class Error(val message: String) : AuthState()
    data class Message(val message: String) : AuthState()
}