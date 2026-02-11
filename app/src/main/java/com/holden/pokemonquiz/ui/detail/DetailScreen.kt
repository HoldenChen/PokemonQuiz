package com.holden.pokemonquiz.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.holden.pokemonquiz.redux.detail.DetailAction
import com.holden.pokemonquiz.redux.detail.DetailState
import com.toggl.komposable.architecture.Store

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(pokemonId: Int, store: Store<DetailState, DetailAction>, onBack: () -> Unit) {
    val detailState by store.state.collectAsState(initial = DetailState())

    // 进入页面时，根据 ID 获取详情
    LaunchedEffect(pokemonId) { store.send(DetailAction.LoadPokemon(pokemonId)) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text =
                            detailState.pokemon?.name?.replaceFirstChar {
                                it.uppercase()
                            } ?: "Pokémon Detail"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)) {
            when {
                detailState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                detailState.error != null -> {
                    Text(
                        text = "Error: ${detailState.error}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                detailState.pokemon != null -> {
                    val pokemon = detailState.pokemon

                    Column {
                        Text(
                            text = pokemon?.name?.replaceFirstChar { it.uppercase() } ?: "",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(text = "ID: #${pokemon?.id}", fontSize = 16.sp, color = Color.Gray)

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(text = "Abilities", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(12.dp))

                        if (pokemon?.abilities?.isEmpty() == true) {
                            Text(text = "No abilities found", color = Color.Gray)
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                pokemon?.abilities?.forEach { ability ->
                                    AbilityCard(abilityName = ability)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AbilityCard(abilityName: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
    ) {
        Text(
            text = abilityName.replace("-", " ").replaceFirstChar { it.uppercase() },
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}
