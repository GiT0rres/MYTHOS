package com.example.mythos.pages

import com.example.mythos.components.*

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.filled.Whatshot
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mythos.R
import com.example.mythos.ui.theme.MythosBorder
import com.example.mythos.ui.theme.MythosGold
import com.example.mythos.ui.theme.MythosIvory
import com.example.mythos.ui.theme.MythosSurface

@Composable
fun HomePage(
    userName: String,
    onExplore: () -> Unit,
    onCategory: (String) -> Unit
) {
    MythosScaffoldBackground {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 20.dp,
                    vertical = 24.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            MythosWordmark()

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Bem-vinda, $userName. Explore os mitos e descubra as conexões entre culturas.",
                color = MythosIvory,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(20.dp))

            // IMAGEM DE DESTAQUE DO ZEUS
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
            ) {

                Image(
                    painter = painterResource(
                        id = R.drawable.zeus
                    ),
                    contentDescription = "Imagem de Zeus",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Gradiente para deixar o visual mais elegante
                // e melhorar a leitura caso tenha texto sobre a imagem.
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.65f)
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {

                    Text(
                        "ZEUS",
                        color = MythosIvory,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Text(
                        "Mitologia Grega",
                        color = MythosGold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            GoldButton(
                text = "EXPLORAR",
                onClick = onExplore
            )

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                CategoryTile(
                    label = "DEUSES",
                    icon = Icons.Filled.AccountBalance,
                    modifier = Modifier.weight(1f)
                ) {
                    onCategory("Todas")
                }

                CategoryTile(
                    label = "HERÓIS",
                    icon = Icons.Filled.Whatshot,
                    modifier = Modifier.weight(1f)
                ) {
                    onCategory("Grega")
                }

                CategoryTile(
                    label = "CRIADORES",
                    icon = Icons.Filled.WorkspacePremium,
                    modifier = Modifier.weight(1f)
                ) {
                    onCategory("Egípcia")
                }

                CategoryTile(
                    label = "PODERES",
                    icon = Icons.Filled.Bolt,
                    modifier = Modifier.weight(1f)
                ) {
                    onCategory("Nórdica")
                }
            }
        }
    }
}

@Composable
private fun CategoryTile(
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MythosSurface)
            .border(
                1.dp,
                MythosBorder,
                RoundedCornerShape(12.dp)
            )
            .clickableNoRipple(onClick)
            .padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            icon,
            contentDescription = label,
            tint = MythosGold,
            modifier = Modifier.size(22.dp)
        )

        Spacer(Modifier.height(6.dp))

        Text(
            label,
            color = MythosGold,
            style = MaterialTheme.typography.labelSmall
        )
    }
}