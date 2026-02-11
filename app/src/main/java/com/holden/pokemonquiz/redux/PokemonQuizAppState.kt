package com.holden.pokemonquiz.redux

import com.holden.pokemonquiz.redux.detail.DetailAction
import com.holden.pokemonquiz.redux.detail.DetailState
import com.holden.pokemonquiz.redux.search.SearchAction
import com.holden.pokemonquiz.redux.search.SearchState

data class PokemonQuizAppState(
        val searchState: SearchState = SearchState(),
        val detailState: DetailState = DetailState()
)

sealed class PokemonQuizAppAction {
    data class SearchActions(val action: SearchAction) : PokemonQuizAppAction()
    data class DetailActions(val action: DetailAction) : PokemonQuizAppAction()
}
