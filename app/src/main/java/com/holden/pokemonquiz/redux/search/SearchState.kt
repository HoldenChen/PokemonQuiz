package com.holden.pokemonquiz.redux.search

import com.holden.pokemonquiz.domain.model.PokemonSpecies

data class SearchState(
        val query: String = "",
        val results: List<PokemonSpecies> = emptyList(),
        val isLoading: Boolean = false,
        val totalCount: Int = 0,
        val currentOffset: Int = 0,
        val hasMore: Boolean = false,
        val error: String? = null
)
