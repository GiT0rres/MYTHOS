package com.example.mythos.pages

import com.example.mythos.components.*

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mythos.model.Deity
import com.example.mythos.ui.theme.MythosBorder
import com.example.mythos.ui.theme.MythosGold
import com.example.mythos.ui.theme.MythosIvory
import com.example.mythos.ui.theme.MythosMuted
import com.example.mythos.ui.theme.MythosSurface

@Composable
fun DetailPage(
    deity: Deity,
    isFavorite: Boolean,
    onBack: () -> Unit,
    onToggleFavorite: () -> Unit,
    onCompare: () -> Unit
) {
    MythosScaffoldBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {

                ArtworkFrame(
                    name = deity.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(360.dp)
                )

                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = MythosIvory,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                        .size(24.dp)
                        .clickableNoRipple(onBack)
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .clickableNoRipple(onToggleFavorite)
                ) {
                    FavoriteIcon(isFavorite)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    deity.name,
                    color = MythosIvory,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    "Mitologia ${deity.culture}",
                    color = MythosGoldOrIvory(),
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    deity.period,
                    color = MythosMuted,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .border(
                            1.dp,
                            MythosGold,
                            RoundedCornerShape(50)
                        )
                        .padding(
                            horizontal = 12.dp,
                            vertical = 6.dp
                        )
                ) {
                    Text(
                        deity.epithet,
                        color = MythosGold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    deity.description,
                    color = MythosIvory,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(Modifier.height(20.dp))

                Divider24()

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    AttributeItem(
                        "PODER",
                        deity.power,
                        Icons.Filled.Bolt,
                        Modifier.weight(1f)
                    )

                    AttributeItem(
                        "DOMÍNIO",
                        deity.domain,
                        Icons.Filled.AccountBalance,
                        Modifier.weight(1f)
                    )

                    AttributeItem(
                        "SÍMBOLO",
                        deity.symbol,
                        Icons.Filled.Shield,
                        Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MythosSurface)
                        .border(
                            1.dp,
                            MythosBorder,
                            RoundedCornerShape(10.dp)
                        )
                        .padding(14.dp)
                ) {
                    Column {
                        Text(
                            "REPRESENTAÇÃO ARTÍSTICA",
                            color = MythosGold,
                            style = MaterialTheme.typography.labelSmall
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            deity.artwork,
                            color = MythosIvory,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                GoldButton(
                    "COMPARAR",
                    onClick = onCompare
                )

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun MythosGoldOrIvory() = MythosGold

@Composable
private fun AttributeItem(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
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

        Text(
            value,
            color = MythosIvory,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}