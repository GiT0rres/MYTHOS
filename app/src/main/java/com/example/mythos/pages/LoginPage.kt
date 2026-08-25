package com.example.mythos.pages

import com.example.mythos.components.*

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mythos.ui.theme.MythosGold
import com.example.mythos.ui.theme.MythosIvory
import com.example.mythos.ui.theme.MythosMuted
import com.example.mythos.viewmodel.AuthState
import com.example.mythos.viewmodel.AuthViewModel

@Composable
fun MythosWordmark() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "MYTHOS",
            color = MythosIvory,
            fontFamily = FontFamily.Serif,
            fontSize = 42.sp,
            letterSpacing = 8.sp
        )
        Text(
            text = "— MUSEU DOS DEUSES —",
            color = MythosGold,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
fun MythosField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MythosGold,
            unfocusedBorderColor = MythosMuted.copy(alpha = 0.4f),
            focusedLabelColor = MythosGold,
            unfocusedLabelColor = MythosMuted,
            focusedTextColor = MythosIvory,
            unfocusedTextColor = MythosIvory,
            cursorColor = MythosGold
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun GoldButton(text: String, enabled: Boolean = true, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MythosGold,
            contentColor = androidx.compose.ui.graphics.Color(0xFF06141C)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        Text(text = text, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun LoginPage(
    authViewModel: AuthViewModel,
    onNavigateToSignup: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by authViewModel.authState.observeAsStateCompat()

    MythosScaffoldBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MythosWordmark()
            Spacer(Modifier.height(36.dp))
            MythosField(email, { email = it }, "E-mail")
            Spacer(Modifier.height(14.dp))
            MythosField(password, { password = it }, "Senha", isPassword = true)
            Spacer(Modifier.height(24.dp))
            GoldButton(
                text = "ENTRAR",
                enabled = authState !is AuthState.Loading
            ) { authViewModel.login(email, password) }

            TextButton(onClick = { authViewModel.resetPassword(email) }) {
                Text("Esqueceu a senha?", color = MythosMuted)
            }

            Spacer(Modifier.height(8.dp))
            Text(
                text = "Ainda não possui conta?",
                color = MythosMuted,
                style = MaterialTheme.typography.bodyMedium
            )
            TextButton(onClick = onNavigateToSignup) {
                Text("CADASTRE-SE", color = MythosGold, style = MaterialTheme.typography.labelLarge)
            }

            val state = authState
            if (state is AuthState.Error) {
                Text(
                    text = state.message,
                    color = MythosGold,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
            if (state is AuthState.Message) {
                Text(
                    text = state.message,
                    color = MythosGold,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }
    }
}
