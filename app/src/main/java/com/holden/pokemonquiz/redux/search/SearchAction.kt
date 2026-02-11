package com.holden.pokemonquiz.redux.search

import com.holden.pokemonquiz.domain.model.PokemonSpecies

// 搜索页的 Intent
sealed class SearchAction {
    data class QueryChanged(val query: String) : SearchAction()

    data class PerformSearch(val query: String) : SearchAction()

    data object LoadMore : SearchAction()

    data class SearchResultsLoaded(val species: List<PokemonSpecies>, val totalCount: Int) :
        SearchAction()

    data class MoreResultsLoaded(val species: List<PokemonSpecies>) : SearchAction()

    data class SearchFailed(val error: String) : SearchAction()
}
