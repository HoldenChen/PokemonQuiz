package com.holden.pokemonquiz.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.holden.pokemonquiz.domain.model.Pokemon
import com.holden.pokemonquiz.domain.model.PokemonSpecies
import com.holden.pokemonquiz.redux.search.SearchAction
import com.holden.pokemonquiz.redux.search.SearchState
import com.holden.pokemonquiz.ui.mapper.PokemonColorMapper
import com.toggl.komposable.architecture.Store
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@Composable
fun SearchScreen(store: Store<SearchState, SearchAction>, onPokemonClick: (Int) -> Unit) {
    val searchState by store.state.collectAsState(initial = SearchState())

    // 重组期间保留光标位置
    var textFieldValue by remember { mutableStateOf(TextFieldValue(searchState.query)) }

    LaunchedEffect(searchState.query) {
        if (textFieldValue.text != searchState.query) {
            textFieldValue =
                TextFieldValue(
                    text = searchState.query,
                    selection = TextRange(searchState.query.length)
                )
        }
    }

    // 防抖
    LaunchedEffect(Unit) {
        store.state.map { it.query }.debounce(500L).distinctUntilChanged().collect { query ->
            store.send(SearchAction.PerformSearch(query))
        }
    }

    Scaffold(topBar = { TopAppBar(title = { Text("PokémonQuiz") }) }) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 16.dp)) {
            TextField(
                value = textFieldValue,
                onValueChange = { newValue ->
                    textFieldValue = newValue
                    store.send(SearchAction.QueryChanged(newValue.text))
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search Pokémon species...") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            when {
                searchState.isLoading && searchState.results.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                searchState.error != null && searchState.results.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Error: ${searchState.error}",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                searchState.results.isEmpty() &&
                        searchState.query.isNotBlank() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No Pokémon found for \"${searchState.query}\"",
                            color = Color.Gray
                        )
                    }
                }

                searchState.results.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Start typing to search Pokémon species", color = Color.Gray)
                    }
                }

                else -> {
                    val listState = rememberLazyListState()

                    LazyColumn(
                        state = listState,
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(items = searchState.results, key = { it.id }) { species ->
                            SpeciesCard(species = species, onPokemonClick = onPokemonClick)
                        }

                        if (searchState.hasMore) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (searchState.isLoading) {
                                        CircularProgressIndicator(modifier = Modifier.size(32.dp))
                                    } else {
                                        Button(onClick = { store.send(SearchAction.LoadMore) }) {
                                            Text("Load More")
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (searchState.totalCount > 0) {
                        Text(
                            text =
                                "Showing ${searchState.results.size} of ${searchState.totalCount} species",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SpeciesCard(species: PokemonSpecies, onPokemonClick: (Int) -> Unit) {
    val bgColor = PokemonColorMapper.getColor(species.colorName)
    val textColor = PokemonColorMapper.getTextColor(species.colorName)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Species header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = species.name.replaceFirstChar { it.uppercase() },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
                Text(
                    text = "Capture Rate: ${species.captureRate}",
                    fontSize = 13.sp,
                    color = textColor.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Pokémon list under this species
            Text(
                text = "Pokémon:",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(4.dp))

            species.pokemons.forEach { pokemon ->
                PokemonRow(
                    pokemon = pokemon,
                    textColor = textColor,
                    onClick = { onPokemonClick(pokemon.id) }
                )
            }
        }
    }
}

@Composable
fun PokemonRow(pokemon: Pokemon, textColor: Color, onClick: () -> Unit) {
    Text(
        text = "→ ${pokemon.name}",
        fontSize = 15.sp,
        color = textColor,
        fontWeight = FontWeight.Medium,
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .clickable(onClick = onClick)
                .padding(vertical = 6.dp, horizontal = 8.dp)
    )
}
