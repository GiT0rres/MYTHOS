package com.example.mythos.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mythos.ui.theme.MythosBackground
import com.example.mythos.ui.theme.MythosBorder
import com.example.mythos.ui.theme.MythosGold
import com.example.mythos.ui.theme.MythosIvory
import com.example.mythos.ui.theme.MythosMuted
import com.example.mythos.ui.theme.MythosSurface

/**
 * Placeholder artístico da obra.
 *
 * Basta trocar por Image(painterResource(R.drawable.zeus), ...) quando as
 * imagens das esculturas/afrescos forem adicionadas em res/drawable.
 */
@Composable
fun ArtworkFrame(
    name: String,
    modifier: Modifier = Modifier
) {
    val palette = artPalette(name)
    Box(
        modifier = modifier
            .background(Brush.verticalGradient(palette))
            .border(1.dp, MythosBorder),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = name.take(1).uppercase(),
                color = MythosGold.copy(alpha = 0.55f),
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 72.sp
            )
            Text(
                text = "REPRESENTAÇÃO ARTÍSTICA",
                color = MythosMuted.copy(alpha = 0.6f),
                fontSize = 9.sp,
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun artPalette(seed: String): List<Color> {
    val variants = listOf(
        listOf(Color(0xFF1B2C36), Color(0xFF0A1A22)),
        listOf(Color(0xFF2E2419), Color(0xFF0F1116)),
        listOf(Color(0xFF23303A), Color(0xFF07131A)),
        listOf(Color(0xFF2A1F1C), Color(0xFF0B141B))
    )
    val index = (seed.sumOf { it.code }) % variants.size
    return variants[index]
}

@Composable
fun SectionTitle(title: String, subtitle: String? = null) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MythosIvory
        )
        if (subtitle != null) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MythosMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun MythosScaffoldBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MythosBackground)
    ) { content() }
}

data class BottomItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun MythosBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        BottomItem("Início", Icons.Filled.Home, "home"),
        BottomItem("Buscar", Icons.Outlined.Search, "gallery"),
        BottomItem("Favoritos", Icons.Outlined.FavoriteBorder, "favorites"),
        BottomItem("Perfil", Icons.Outlined.Person, "profile")
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MythosSurface)
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = if (selected) MythosGold else MythosMuted,
                    modifier = Modifier
                        .size(22.dp)
                        .clickableNoRipple { onNavigate(item.route) }
                )
                Text(
                    text = item.label,
                    color = if (selected) MythosGold else MythosMuted,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Composable
fun FavoriteIcon(isFavorite: Boolean) {
    Icon(
        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
        contentDescription = "Favorito",
        tint = if (isFavorite) MythosGold else MythosIvory,
        modifier = Modifier.size(20.dp)
    )
}

@Composable
fun Divider24() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(MythosBorder)
    )
}
