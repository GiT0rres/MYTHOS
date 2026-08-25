package com.example.mythos.pages

import com.example.mythos.components.*

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mythos.ui.theme.MythosGold
import com.example.mythos.ui.theme.MythosMuted
import com.example.mythos.viewmodel.AuthState
import com.example.mythos.viewmodel.AuthViewModel

@Composable
fun SignupPage(
    authViewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    val authState by authViewModel.authState.observeAsStateCompat()

    MythosScaffoldBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SectionTitle("CRIAR CONTA", "Entre no acervo do Museu dos Deuses")
            Spacer(Modifier.height(28.dp))
            MythosField(name, { name = it }, "Nome")
            Spacer(Modifier.height(12.dp))
            MythosField(email, { email = it }, "E-mail")
            Spacer(Modifier.height(12.dp))
            MythosField(password, { password = it }, "Senha", isPassword = true)
            Spacer(Modifier.height(12.dp))
            MythosField(confirm, { confirm = it }, "Confirmar senha", isPassword = true)
            Spacer(Modifier.height(24.dp))
            GoldButton(
                text = "CADASTRAR",
                enabled = authState !is AuthState.Loading
            ) { authViewModel.signup(name, email, password, confirm) }

            Spacer(Modifier.height(12.dp))
            Text(
                text = "Já possui uma conta?",
                color = MythosMuted,
                style = MaterialTheme.typography.bodyMedium
            )
            TextButton(onClick = onNavigateToLogin) {
                Text("LOGIN", color = MythosGold, style = MaterialTheme.typography.labelLarge)
            }

            val state = authState
            if (state is AuthState.Error) {
                Text(
                    text = state.message,
                    color = MythosGold,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
