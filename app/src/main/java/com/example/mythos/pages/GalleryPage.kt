package com.example.mythos.pages

import com.example.mythos.components.*

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mythos.R
import com.example.mythos.model.Culture
import com.example.mythos.model.Deity
import com.example.mythos.ui.theme.MythosBackground
import com.example.mythos.ui.theme.MythosBorder
import com.example.mythos.ui.theme.MythosGold
import com.example.mythos.ui.theme.MythosIvory
import com.example.mythos.ui.theme.MythosMuted
import com.example.mythos.ui.theme.MythosSurface

@Composable
fun GalleryPage(
    deities: List<Deity>,
    loading: Boolean,
    favorites: Set<String>,
    initialFilter: String = Culture.TODAS.label,
    onDeityClick: (String) -> Unit,
    onToggleFavorite: (String) -> Unit
) {
    var filter by remember { mutableStateOf(initialFilter) }

    val filtered = remember(filter, deities) {
        if (filter == Culture.TODAS.label) {
            deities
        } else {
            deities.filter { it.culture == filter }
        }
    }

    MythosScaffoldBackground {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 16.dp,
                    vertical = 20.dp
                )
        ) {

            SectionTitle(
                "GALERIA",
                "Explore obras e personagens de diferentes culturas"
            )

            Spacer(Modifier.height(16.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(Culture.entries.toList()) { culture ->

                    FilterChipGold(
                        label = culture.label,
                        selected = filter == culture.label
                    ) {
                        filter = culture.label
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            if (loading) {

                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MythosGold
                    )
                }

            } else {

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    items(filtered) { deity ->

                        DeityCard(
                            deity = deity,
                            isFavorite = favorites.contains(deity.id),
                            onClick = {
                                onDeityClick(deity.id)
                            },
                            onToggleFavorite = {
                                onToggleFavorite(deity.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FilterChipGold(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(
                if (selected) MythosGold
                else MythosSurface
            )
            .border(
                1.dp,
                MythosBorder,
                RoundedCornerShape(50)
            )
            .clickableNoRipple(onClick)
            .padding(
                horizontal = 14.dp,
                vertical = 8.dp
            )
    ) {
        Text(
            text = label,
            color = if (selected) {
                MythosBackground
            } else {
                MythosMuted
            },
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun DeityCard(
    deity: Deity,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.78f)
            .clip(RoundedCornerShape(12.dp))
            .clickableNoRipple(onClick)
    ) {

        // IMAGEM DO DEUS
        Image(
            painter = painterResource(
                id = getDeityImage(deity.id)
            ),
            contentDescription = "Imagem de ${deity.name}",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // GRADIENTE SOBRE A IMAGEM
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            MythosBackground.copy(alpha = 0.92f)
                        )
                    )
                )
        )

        // FAVORITO
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .clickableNoRipple(onToggleFavorite)
        ) {
            FavoriteIcon(isFavorite)
        }

        // NOME E CULTURA
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp)
        ) {

            Text(
                deity.name,
                color = MythosIvory,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                "Mitologia ${deity.culture}",
                color = MythosMuted,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

private fun getDeityImage(id: String): Int {
    return when (id.lowercase()) {

        "zeus" -> R.drawable.zeus

        "thor" -> R.drawable.thor

        "jupiter" -> R.drawable.jupiter

        "indra" -> R.drawable.indra

        "hercules" -> R.drawable.hercules

        "ra" -> R.drawable.ra

        else -> R.drawable.zeus
    }
}