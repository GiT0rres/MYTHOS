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
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mythos.model.Deity
import com.example.mythos.ui.theme.MythosBackground
import com.example.mythos.ui.theme.MythosBorder
import com.example.mythos.ui.theme.MythosGold
import com.example.mythos.ui.theme.MythosIvory
import com.example.mythos.ui.theme.MythosMuted
import com.example.mythos.ui.theme.MythosSurface

@Composable
fun ComparePage(
    first: Deity,
    others: List<Deity>,
    second: Deity,
    curiosity: String,
    onBack: () -> Unit,
    onSelectSecond: (String) -> Unit
) {
    MythosScaffoldBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = MythosIvory,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(24.dp)
                        .clickableNoRipple(onBack)
                )
                Column(modifier = Modifier.align(Alignment.Center)) {
                    SectionTitle(
                        "COMPARAÇÃO",
                        "Veja semelhanças entre deuses de diferentes culturas"
                    )
                }
            }

            Row(modifier = Modifier.fillMaxWidth().height(220.dp)) {
                CompareHeader(first, Modifier.weight(1f))
                CompareHeader(second, Modifier.weight(1f))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(MythosBackground)
                        .border(1.dp, MythosGold, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("VS", color = MythosGold, style = MaterialTheme.typography.labelLarge)
                }
            }

            Spacer(Modifier.height(16.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                CompareRow("PODER", first.power, second.power)
                CompareRow("SÍMBOLO", first.symbol, second.symbol)
                CompareRow("DOMÍNIO", first.domain, second.domain)
                CompareRow("REPRESENTAÇÃO", first.artwork, second.artwork)

                Spacer(Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MythosSurface)
                        .border(1.dp, MythosBorder, RoundedCornerShape(10.dp))
                        .padding(16.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "CURIOSIDADE",
                            color = MythosGold,
                            style = MaterialTheme.typography.labelLarge
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            curiosity,
                            color = MythosIvory,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))
                Text(
                    "TROCAR O SEGUNDO PERSONAGEM",
                    color = MythosGold,
                    style = MaterialTheme.typography.labelSmall
                )
                Spacer(Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(others.filter { it.id != first.id }) { deity ->
                        FilterChipGold(
                            label = deity.name,
                            selected = deity.id == second.id
                        ) { onSelectSecond(deity.id) }
                    }
                }
            }
        }
    }
}

@Composable
private fun CompareHeader(deity: Deity, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth()) {
        ArtworkFrame(name = deity.name, modifier = Modifier.fillMaxSize())
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp)
        ) {
            Text(deity.name, color = MythosIvory, style = MaterialTheme.typography.titleMedium)
            Text(
                "Mitologia ${deity.culture}",
                color = MythosMuted,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun CompareRow(label: String, left: String, right: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(
            label,
            color = MythosGold,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(4.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                left,
                color = MythosIvory,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start
            )
            Text(
                right,
                color = MythosIvory,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
        }
        Spacer(Modifier.height(8.dp))
        Divider24()
    }
}
