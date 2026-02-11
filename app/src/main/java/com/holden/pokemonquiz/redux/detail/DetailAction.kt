package com.holden.pokemonquiz.redux.detail

import com.holden.pokemonquiz.domain.model.Pokemon

// MVI 里的 Intent，由此驱动 reducer 。
sealed class DetailAction {
    data class LoadPokemon(val pokemonId: Int) : DetailAction()

    data class PokemonLoaded(val pokemon: Pokemon) : DetailAction()

    data class PokemonLoadFailed(val error: String) : DetailAction()
}
