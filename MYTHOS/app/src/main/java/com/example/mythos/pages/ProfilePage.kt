package com.example.mythos.pages

import com.example.mythos.components.*

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.mythos.ui.theme.MythosBorder
import com.example.mythos.ui.theme.MythosGold
import com.example.mythos.ui.theme.MythosIvory
import com.example.mythos.ui.theme.MythosMuted
import com.example.mythos.ui.theme.MythosSurface

@Composable
fun ProfilePage(
    userName: String,
    userEmail: String,
    favoritesCount: Int,
    artworksCount: Int,
    onSignout: () -> Unit
) {
    MythosScaffoldBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SectionTitle("PERFIL", "Visitante do Museu dos Deuses")
            Spacer(Modifier.height(28.dp))
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .background(MythosSurface)
                    .border(1.dp, MythosGold, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.Person,
                    contentDescription = "Avatar",
                    tint = MythosGold,
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(userName, color = MythosIvory, style = MaterialTheme.typography.titleMedium)
            Text(userEmail, color = MythosMuted, style = MaterialTheme.typography.bodyMedium)

            Spacer(Modifier.height(24.dp))
            InfoRow("Obras no acervo", artworksCount.toString())
            Spacer(Modifier.height(10.dp))
            InfoRow("Favoritos", favoritesCount.toString())
            Spacer(Modifier.height(10.dp))
            InfoRow("Autenticação", "Firebase Authentication")

            Spacer(Modifier.height(32.dp))
            GoldButton("SAIR", onClick = onSignout)
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MythosSurface)
            .border(1.dp, MythosBorder, RoundedCornerShape(10.dp))
            .padding(14.dp)
    ) {
        Text(label, color = MythosMuted, style = MaterialTheme.typography.bodyMedium)
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                value,
                color = MythosGold,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}
