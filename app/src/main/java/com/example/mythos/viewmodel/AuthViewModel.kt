package com.example.mythos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class AuthViewModel : ViewModel() {

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
        val user = auth?.currentUser

        if (user == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            // Atualiza as informações do usuário.
            // Isso é importante para saber se o e-mail foi verificado.
            user.reload().addOnCompleteListener {
                val currentUser = auth.currentUser

                if (currentUser != null && currentUser.isEmailVerified) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Unauthenticated
                }
            }
        }
    }

    val userEmail: String
        get() = auth?.currentUser?.email ?: "visitante"

    val userName: String
        get() = auth?.currentUser?.displayName?.takeIf { it.isNotBlank() }
            ?: userEmail.substringBefore("@")

    fun login(email: String, password: String) {

        val firebaseAuth = auth ?: run {
            _authState.value =
                AuthState.Error("Firebase indisponível neste ambiente.")
            return
        }

        if (email.isBlank() || password.isBlank()) {
            _authState.value =
                AuthState.Error("Preencha e-mail e senha.")
            return
        }

        _authState.value = AuthState.Loading

        firebaseAuth.signInWithEmailAndPassword(
            email.trim(),
            password
        ).addOnCompleteListener { task ->

            if (task.isSuccessful) {

                val user = firebaseAuth.currentUser

                // Recarrega o usuário para obter o estado
                // mais atualizado do e-mail.
                user?.reload()?.addOnCompleteListener {

                    val currentUser = firebaseAuth.currentUser

                    if (currentUser?.isEmailVerified == true) {

                        _authState.value =
                            AuthState.Authenticated

                    } else {

                        // Impede acesso ao aplicativo
                        // enquanto o e-mail não estiver verificado.
                        firebaseAuth.signOut()

                        _authState.value =
                            AuthState.EmailNotVerified
                    }
                }

            } else {

                _authState.value =
                    AuthState.Error(
                        task.exception?.localizedMessage
                            ?: "E-mail ou senha incorretos."
                    )
            }
        }
    }

    fun signup(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {

        val firebaseAuth = auth ?: run {
            _authState.value =
                AuthState.Error("Firebase indisponível neste ambiente.")
            return
        }

        if (
            name.isBlank() ||
            email.isBlank() ||
            password.isBlank() ||
            confirmPassword.isBlank()
        ) {
            _authState.value =
                AuthState.Error("Preencha todos os campos.")
            return
        }

        if (password != confirmPassword) {
            _authState.value =
                AuthState.Error("As senhas não coincidem.")
            return
        }

        if (password.length < 6) {
            _authState.value =
                AuthState.Error(
                    "A senha deve ter no mínimo 6 caracteres."
                )
            return
        }

        _authState.value = AuthState.Loading

        firebaseAuth.createUserWithEmailAndPassword(
            email.trim(),
            password
        ).addOnCompleteListener { task ->

            if (task.isSuccessful) {

                val user = firebaseAuth.currentUser

                // Salva o nome no perfil do usuário do Firebase.
                val profileUpdates =
                    UserProfileChangeRequest.Builder()
                        .setDisplayName(name.trim())
                        .build()

                user?.updateProfile(profileUpdates)
                    ?.addOnCompleteListener {

                        // Envia o e-mail de verificação.
                        user.sendEmailVerification()
                            .addOnCompleteListener { verificationTask ->

                                if (verificationTask.isSuccessful) {

                                    // IMPORTANTE:
                                    // NÃO marcamos como Authenticated.
                                    // O usuário ainda precisa verificar o e-mail.
                                    firebaseAuth.signOut()

                                    _authState.value =
                                        AuthState.VerificationEmailSent(
                                            email = email.trim()
                                        )

                                } else {

                                    _authState.value =
                                        AuthState.Error(
                                            verificationTask.exception
                                                ?.localizedMessage
                                                ?: "Não foi possível enviar o e-mail de verificação."
                                        )
                                }
                            }
                    }

            } else {

                _authState.value =
                    AuthState.Error(
                        task.exception?.localizedMessage
                            ?: "Não foi possível cadastrar."
                    )
            }
        }
    }

    fun resendVerificationEmail() {

        val firebaseAuth = auth ?: run {
            _authState.value =
                AuthState.Error("Firebase indisponível neste ambiente.")
            return
        }

        val user = firebaseAuth.currentUser

        if (user == null) {
            _authState.value =
                AuthState.Error(
                    "Faça login primeiro para reenviar o e-mail."
                )
            return
        }

        if (user.isEmailVerified) {
            _authState.value =
                AuthState.Message(
                    "Seu e-mail já foi verificado."
                )
            return
        }

        user.sendEmailVerification()
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    _authState.value =
                        AuthState.Message(
                            "Novo e-mail de verificação enviado."
                        )

                } else {

                    _authState.value =
                        AuthState.Error(
                            task.exception?.localizedMessage
                                ?: "Não foi possível reenviar o e-mail."
                        )
                }
            }
    }

    fun checkEmailVerification() {

        val firebaseAuth = auth ?: return

        val user = firebaseAuth.currentUser

        if (user == null) {
            _authState.value =
                AuthState.Unauthenticated
            return
        }

        user.reload().addOnCompleteListener {

            val currentUser = firebaseAuth.currentUser

            if (currentUser?.isEmailVerified == true) {

                _authState.value =
                    AuthState.Authenticated

            } else {

                _authState.value =
                    AuthState.EmailNotVerified
            }
        }
    }

    fun resetPassword(email: String) {

        val firebaseAuth = auth ?: run {
            _authState.value =
                AuthState.Error("Firebase indisponível neste ambiente.")
            return
        }

        if (email.isBlank()) {
            _authState.value =
                AuthState.Error(
                    "Informe o e-mail para recuperar a senha."
                )
            return
        }

        firebaseAuth.sendPasswordResetEmail(
            email.trim()
        ).addOnCompleteListener { task ->

            _authState.value = if (task.isSuccessful) {

                AuthState.Message(
                    "E-mail de recuperação enviado."
                )

            } else {

                AuthState.Error(
                    task.exception?.localizedMessage
                        ?: "Falha ao enviar o e-mail."
                )
            }
        }
    }

    fun signout() {

        auth?.signOut()

        _authState.value =
            AuthState.Unauthenticated
    }
}


sealed class AuthState {

    data object Authenticated : AuthState()

    data object Unauthenticated : AuthState()

    data object Loading : AuthState()

    data object EmailNotVerified : AuthState()

    data class VerificationEmailSent(
        val email: String
    ) : AuthState()

    data class Error(
        val message: String
    ) : AuthState()

    data class Message(
        val message: String
    ) : AuthState()
}