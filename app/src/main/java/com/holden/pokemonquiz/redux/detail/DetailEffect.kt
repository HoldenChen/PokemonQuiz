package com.holden.pokemonquiz.redux.detail

import com.holden.pokemonquiz.domain.usecase.GetPokemonDetailUseCase
import com.toggl.komposable.architecture.Effect
import kotlinx.coroutines.flow.flow

/**
 * 详情请求接口的副作用
 */
class DetailEffect(private val getPokemonDetailUseCase: GetPokemonDetailUseCase) {
    fun loadPokemon(pokemonId: Int): Effect<DetailAction> = Effect {
        flow {
            try {
                val pokemon = getPokemonDetailUseCase(pokemonId)
                emit(DetailAction.PokemonLoaded(pokemon))
            } catch (e: Exception) {
                emit(DetailAction.PokemonLoadFailed(e.message ?: "Unknown error"))
            }
        }
    }
}
