package com.example.mythos.pages

import com.example.mythos.components.*

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mythos.model.Deity
import com.example.mythos.ui.theme.MythosMuted

@Composable
fun FavoritesPage(
    deities: List<Deity>,
    favorites: Set<String>,
    onDeityClick: (String) -> Unit,
    onToggleFavorite: (String) -> Unit
) {
    val list = deities.filter { favorites.contains(it.id) }

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
                "FAVORITOS",
                "Suas obras salvas no acervo"
            )

            Spacer(Modifier.height(16.dp))

            if (list.isEmpty()) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        "Nenhuma obra favoritada ainda.\nToque no coração na galeria.",
                        color = MythosMuted,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }

            } else {

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    items(list) { deity ->

                        DeityCard(
                            deity = deity,
                            isFavorite = true,
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